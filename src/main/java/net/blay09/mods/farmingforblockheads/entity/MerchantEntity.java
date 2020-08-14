package net.blay09.mods.farmingforblockheads.entity;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.sound.ModSounds;
import net.blay09.mods.farmingforblockheads.tile.MarketTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class MerchantEntity extends CreatureEntity {

    public enum SpawnAnimationType {
        MAGIC,
        FALLING,
        DIGGING
    }

    private static final Random rand = new Random();

    private BlockPos marketPos;
    private Direction facing;
    private boolean spawnAnimationStarted;
    private boolean spawnDone;
    private SpawnAnimationType spawnAnimation = SpawnAnimationType.MAGIC;

    private BlockPos marketEntityPos;
    private int diggingAnimation;
    private BlockState diggingBlockState;

    public MerchantEntity(EntityType<MerchantEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, ZombieEntity.class, 8f, 0.6, 0.6));
        goalSelector.addGoal(5, new MerchantGoal(this, 0.6));
    }

    public static AttributeModifierMap.MutableAttribute createEntityAttributes() {
        return ZombieEntity.func_234342_eQ_().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5);
    }

    @Override // processInteract
    protected ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        MarketTileEntity tileMarket = getMarketTileEntity();
        if (tileMarket != null) {
            NetworkHooks.openGui((ServerPlayerEntity) player, tileMarket, tileMarket.getPos());
            return ActionResultType.func_233537_a_(this.world.isRemote);
        }

        return super.func_230254_b_(player, hand);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
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
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (!hasCustomName()) {
            String merchantName = FarmingForBlockheadsConfig.getRandomMerchantName(rand);
            setCustomName(new StringTextComponent(merchantName));
        }
        if (compound.contains("MarketPos")) {
            setMarket(BlockPos.fromLong(compound.getLong("MarketPos")), Direction.byIndex(compound.getByte("Facing")));
        }
        spawnDone = compound.getBoolean("SpawnDone");
        spawnAnimation = SpawnAnimationType.values()[compound.getByte("SpawnAnimation")];
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean preventDespawn() {
        return true;
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

            double posX = getPosX();
            double posY = getPosY();
            double posZ = getPosZ();

            for (int i = 0; i < 4; i++) {
                BlockState diggingState = diggingBlockState != null ? diggingBlockState : Blocks.DIRT.getDefaultState();
                world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, diggingState), posX, posY, posZ, Math.random() * 2 - 1, Math.random() * 4, Math.random() * 2 - 1);
                world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, diggingState), posX, posY, posZ, (Math.random() - 0.5) * 0.5, Math.random() * 0.5f, (Math.random() - 0.5) * 0.5);
            }

            if (diggingAnimation % 2 == 0) {
                world.playSound(posX, posY, posZ, SoundType.GROUND.getHitSound(), SoundCategory.BLOCKS, 1f, (float) (Math.random() + 0.5), false);
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
            double posX = getPosX();
            double posY = getPosY();
            double posZ = getPosZ();

            world.playSound(posX + 0.5, posY + 1, posZ + 0.5, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 1f, 1f, false);
            for (int i = 0; i < 50; i++) {
                world.addParticle(ParticleTypes.FIREWORK, posX + 0.5, posY + 1, posZ + 0.5, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f);
            }
            world.addParticle(ParticleTypes.EXPLOSION, posX + 0.5, posY + 1, posZ + 0.5, 0, 0, 0);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    protected void damageEntity(DamageSource damageSource, float damageAmount) {
        if (!spawnDone && damageSource == DamageSource.FALL) {
            double posX = getPosX();
            double posY = getPosY();
            double posZ = getPosZ();
            world.playSound(posX, posY, posZ, getHurtSound(damageSource), SoundCategory.NEUTRAL, 1f, 2f, false);
            spawnDone = true;
            return;
        }
        super.damageEntity(damageSource, damageAmount);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62f;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
        if (Math.random() < 0.001) {
            setCustomName(new StringTextComponent(Math.random() <= 0.5 ? "Pam" : "Blay"));
        } else {
            String merchantName = FarmingForBlockheadsConfig.getRandomMerchantName(rand);
            setCustomName(new StringTextComponent(merchantName));
        }

        return super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    public void setMarket(BlockPos marketPos, Direction facing) {
        this.marketPos = marketPos;
        this.marketEntityPos = marketPos.offset(facing.getOpposite());
        this.facing = facing;
    }

    @Nullable
    public BlockPos getMarketEntityPosition() {
        return marketEntityPos;
    }

    public boolean isAtMarket() {
        return marketEntityPos != null && getDistanceSq(Vector3d.copyCentered(marketEntityPos.offset(facing.getOpposite()))) <= 1;
    }

    @Nullable
    private MarketTileEntity getMarketTileEntity() {
        if (marketPos == null) {
            return null;
        }

        TileEntity tileEntity = world.getTileEntity(marketPos);
        if (tileEntity instanceof MarketTileEntity) {
            return (MarketTileEntity) tileEntity;
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

    private void disappear() {
        double posX = getPosX();
        double posY = getPosY();
        double posZ = getPosZ();

        world.playSound(posX, posY, posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 1f, 1f, false);
        for (int i = 0; i < 50; i++) {
            world.addParticle(ParticleTypes.FIREWORK, posX, posY + 1, posZ, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f);
        }
        world.addParticle(ParticleTypes.EXPLOSION, posX, posY + 1, posZ, 0, 0, 0);
        remove();
    }

    public void setSpawnAnimation(SpawnAnimationType spawnAnimation) {
        this.spawnAnimation = spawnAnimation;
    }

    public int getDiggingAnimation() {
        return diggingAnimation;
    }
}
