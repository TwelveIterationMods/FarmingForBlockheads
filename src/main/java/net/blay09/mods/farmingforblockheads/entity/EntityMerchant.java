package net.blay09.mods.farmingforblockheads.entity;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.network.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityMerchant extends EntityCreature implements INpc {

	public enum SpawnAnimationType {
		MAGIC,
		FALLING,
		DIGGING
	}

	private static final Random rand = new Random();
	public static final String[] NAMES = new String[]{
			"Swap-O-Matic",
			"Emerald Muncher",
			"Back Alley Dealer",
			"Weathered Salesperson"
	};

	private BlockPos marketPos;
	private EnumFacing facing;
	private boolean spawnDone;
	private SpawnAnimationType spawnAnimation = SpawnAnimationType.MAGIC;

	private BlockPos marketEntityPos;
	private int diggingAnimation;
	private IBlockState diggingBlockState;

	public EntityMerchant(World world) {
		super(world);
		setSize(0.6f, 1.95f);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityZombie.class, 8f, 0.6, 0.6));
		tasks.addTask(5, new EntityAIMerchant(this, 0.6));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (isMarketValid()) {
			player.openGui(FarmingForBlockheads.MOD_ID, GuiHandler.MARKET, world, marketPos.getX(), marketPos.getY(), marketPos.getZ());
			return true;
		}
		return super.processInteract(player, hand);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if (marketPos != null) {
			compound.setLong("MarketPos", marketPos.toLong());
		}
		if(facing != null) {
			compound.setByte("Facing", (byte) facing.getIndex());
		}
		compound.setBoolean("SpawnDone", spawnDone);
		compound.setByte("SpawnAnimation", (byte) spawnAnimation.ordinal());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (!hasCustomName()) {
			setCustomNameTag(NAMES[rand.nextInt(NAMES.length)]);
		}
		if (compound.hasKey("MarketPos")) {
			setMarket(BlockPos.fromLong(compound.getLong("MarketPos")), EnumFacing.getFront(compound.getByte("Facing")));
		}
		spawnDone = compound.getBoolean("SpawnDone");
		spawnAnimation = SpawnAnimationType.values()[compound.getByte("SpawnAnimation")];
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_VILLAGER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VILLAGER_DEATH;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(!world.isRemote) {
			if (ticksExisted % 20 == 0) {
				if (!isMarketValid()) {
					world.setEntityState(this, (byte) 12);
					setDead();
				}
			}
		}

		if(!spawnDone && spawnAnimation == SpawnAnimationType.DIGGING) {
			world.setEntityState(this, (byte) 13);
			spawnDone = true;
		}
		if(diggingAnimation > 0) {
			diggingAnimation--;
			for(int i = 0; i < 4; i++) {
				int stateId = Block.getStateId(diggingBlockState != null ? diggingBlockState : Blocks.DIRT.getDefaultState());
				world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX, posY, posZ, Math.random() * 2 - 1, Math.random() * 4, Math.random() * 2 - 1, stateId);
				world.spawnParticle(EnumParticleTypes.BLOCK_DUST, posX, posY, posZ, (Math.random() - 0.5) * 0.5, Math.random() * 0.5f, (Math.random() - 0.5) * 0.5, stateId);
			}
			if(diggingAnimation % 2 == 0) {
				world.playSound(posX, posY, posZ, Blocks.DIRT.getSoundType().getHitSound(), SoundCategory.BLOCKS, 1f, (float) (Math.random() + 0.5), false);
			}
		}
	}

	@Override
	public void handleStatusUpdate(byte id) {
		if(id == 12) {
			disappear();
			return;
		} else if(id == 13) {
			diggingBlockState = world.getBlockState(getPosition().down());
			diggingAnimation = 60;
			return;
		}
		super.handleStatusUpdate(id);
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if (!spawnDone && damageSrc == DamageSource.FALL) {
			world.playSound(posX, posY, posZ, getHurtSound(), SoundCategory.NEUTRAL, 1f, 2f, false);
			spawnDone = true;
			return;
		}
		super.damageEntity(damageSrc, damageAmount);
	}

	@Override
	public float getEyeHeight() {
		return 1.62f;
	}

	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingData) {
		if(Math.random() < 0.001) {
			setCustomNameTag(Math.random() <= 0.5 ? "Pam" : "Blay");
		} else {
			setCustomNameTag(NAMES[rand.nextInt(NAMES.length)]);
		}
		return super.onInitialSpawn(difficulty, livingData);
	}



	@Override
	public boolean canBeLeashedTo(EntityPlayer player) {
		return false;
	}

	public void setMarket(BlockPos marketPos, EnumFacing facing) {
		this.marketPos = marketPos;
		this.marketEntityPos = marketPos.offset(facing.getOpposite());
		this.facing = facing;
	}

	@Nullable
	public BlockPos getMarketEntityPosition() {
		return marketEntityPos;
	}

	public boolean isAtMarket() {
		return marketEntityPos != null && getDistanceSq(marketEntityPos.offset(facing.getOpposite())) <= 1;
	}

	private boolean isMarketValid() {
		return marketPos != null && world.getBlockState(marketPos).getBlock() == ModBlocks.market;
	}

	public void setToFacingAngle() {
		float facingAngle = facing.getHorizontalAngle();
		setRotation(facingAngle, 0f);
		setRotationYawHead(facingAngle);
		setRenderYawOffset(facingAngle);
	}

	public void disappear() {
		world.playSound(posX, posY, posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 1f, 1f, false);
		for (int i = 0; i < 50; i++) {
			world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, posX, posY + 1, posZ, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f);
		}
		world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY + 1, posZ, 0, 0, 0);
		setDead();
	}

	public void setSpawnAnimation(SpawnAnimationType spawnAnimation) {
		this.spawnAnimation = spawnAnimation;
	}

	public int getDiggingAnimation() {
		return diggingAnimation;
	}
}