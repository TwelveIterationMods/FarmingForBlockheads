package net.blay09.mods.farmingforblockheads.entity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class MerchantGoal extends Goal {

    private final MerchantEntity entity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double movementSpeed;

    public MerchantGoal(MerchantEntity entity, double movementSpeed) {
        this.entity = entity;
        this.movementSpeed = movementSpeed;
        setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (entity.isAtMarket()) {
            entity.setToFacingAngle();
            return false;
        }

        BlockPos marketPos = entity.getMarketEntityPosition();
        if (marketPos != null && entity.getNavigator().canEntityStandOnPos(marketPos)) {
            this.movePosX = marketPos.getX() + 0.5;
            this.movePosY = marketPos.getY() + 1;
            this.movePosZ = marketPos.getZ() + 0.5;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        entity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, movementSpeed);
    }

}
