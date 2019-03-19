package net.blay09.mods.farmingforblockheads.entity;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.sound.ModSounds;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.tile.TileMarket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityMerchant extends EntityCreature implements INpc {

    public enum SpawnAnimationType {
        MAGIC,
        FALLING,
        DIGGING
    }

    private static final Random rand = new Random();

    private BlockPos marketPos;
    private EnumFacing facing;
    private boolean spawnAnimationStarted;
    private boolean spawnDone;
    private SpawnAnimationType spawnAnimation = SpawnAnimationType.MAGIC;

    private BlockPos marketEntityPos;
    private int diggingAnimation;
    private IBlockState diggingBlockState;

    public EntityMerchant(World world) {
        super(ModEntities.merchant, world);
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
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        TileMarket tileMarket = getMarketTileEntity();
        if (tileMarket != null) {
            NetworkHooks.openGui((EntityPlayerMP) player, tileMarket, tileMarket.getPos());
            return true;
        }

        return super.processInteract(player, hand);
    }

    @Override
    public void writeAdditional(NBTTagCompound compound) {
        super.writeAdditional(compound);
        if (marketPos != null) {
            compound.putLong("MarketPos", marketPos.toLong());
        }
        if (facing != null) {
            compound.putByte("Facing", (byte) facing.getIndex());
        }
        compound.putBoolean("SpawnDone", spawnDone);
        compound.putByte("SpawnAnimation", (byte) spawnAnimation.ordinal());
    }

    @Override
    public void readAdditional(NBTTagCompound compound) {
        super.readAdditional(compound);
        if (!hasCustomName()) {
            String merchantName = FarmingForBlockheadsConfig.getRandomMerchantName(rand);
            setCustomName(new TextComponentString(merchantName));
        }
        if (compound.contains("MarketPos")) {
            setMarket(BlockPos.fromLong(compound.getLong("MarketPos")), EnumFacing.byIndex(compound.getByte("Facing")));
        }
        spawnDone = compound.getBoolean("SpawnDone");
        spawnAnimation = SpawnAnimationType.values()[compound.getByte("SpawnAnimation")];
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            if (ticksExisted % 20 == 0) {
                if (!isMarketValid()) {
                    world.setEntityState(this, (byte) 12);
                    remove();
                }
            }
        }

        if (!spawnDone && !spawnAnimationStarted) {
            spawnAnimationStarted = true;
            switch (spawnAnimation) {
                case DIGGING:
                    world.setEntityState(this, (byte) 13);
                    break;
                case FALLING:
                    world.setEntityState(this, (byte) 14);
                    break;
                case MAGIC:
                    world.setEntityState(this, (byte) 15);
                    break;
            }
        }

        if (diggingAnimation > 0) {
            diggingAnimation--;
            for (int i = 0; i < 4; i++) {
                IBlockState diggingState = diggingBlockState != null ? diggingBlockState : Blocks.DIRT.getDefaultState();
                world.addParticle(new BlockParticleData(Particles.BLOCK, diggingState), posX, posY, posZ, Math.random() * 2 - 1, Math.random() * 4, Math.random() * 2 - 1);
                world.addParticle(new BlockParticleData(Particles.FALLING_DUST, diggingState), posX, posY, posZ, (Math.random() - 0.5) * 0.5, Math.random() * 0.5f, (Math.random() - 0.5) * 0.5);
            }

            if (diggingAnimation % 2 == 0) {
                world.playSound(posX, posY, posZ, Blocks.DIRT.getSoundType().getHitSound(), SoundCategory.BLOCKS, 1f, (float) (Math.random() + 0.5), false);
            }
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 12) {
            disappear();
        } else if (id == 13) {
            diggingBlockState = world.getBlockState(getPosition().down());
            diggingAnimation = 60;
        } else if (id == 14) {
            BlockPos pos = world.getHeight(Heightmap.Type.MOTION_BLOCKING, getPosition());
            world.playSound(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, ModSounds.falling, SoundCategory.NEUTRAL, 1f, 1f, false);
        } else if (id == 15) {
            world.playSound(posX + 0.5, posY + 1, posZ + 0.5, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 1f, 1f, false);
            for (int i = 0; i < 50; i++) {
                world.addParticle(Particles.FIREWORK, posX + 0.5, posY + 1, posZ + 0.5, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f);
            }
            world.addParticle(Particles.EXPLOSION, posX + 0.5, posY + 1, posZ + 0.5, 0, 0, 0);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    protected void damageEntity(DamageSource damageSource, float damageAmount) {
        if (!spawnDone && damageSource == DamageSource.FALL) {
            world.playSound(posX, posY, posZ, getHurtSound(damageSource), SoundCategory.NEUTRAL, 1f, 2f, false);
            spawnDone = true;
            return;
        }
        super.damageEntity(damageSource, damageAmount);
    }

    @Override
    public float getEyeHeight() {
        return 1.62f;
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingData, @Nullable NBTTagCompound tagCompound) {
        if (Math.random() < 0.001) {
            setCustomName(new TextComponentString(Math.random() <= 0.5 ? "Pam" : "Blay"));
        } else {
            String merchantName = FarmingForBlockheadsConfig.getRandomMerchantName(rand);
            setCustomName(new TextComponentString(merchantName));
        }

        return super.onInitialSpawn(difficulty, livingData, tagCompound);
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

    @Nullable
    public TileMarket getMarketTileEntity() {
        if (marketPos == null) {
            return null;
        }

        TileEntity tileEntity = world.getTileEntity(marketPos);
        if (tileEntity instanceof TileMarket) {
            return (TileMarket) tileEntity;
        }

        return null;
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
            world.addParticle(Particles.FIREWORK, posX, posY + 1, posZ, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f);
        }
        world.addParticle(Particles.EXPLOSION, posX, posY + 1, posZ, 0, 0, 0);
        remove();
    }

    public void setSpawnAnimation(SpawnAnimationType spawnAnimation) {
        this.spawnAnimation = spawnAnimation;
    }

    public int getDiggingAnimation() {
        return diggingAnimation;
    }
}
