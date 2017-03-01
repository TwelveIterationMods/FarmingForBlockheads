package net.blay09.mods.farmingforblockheads.entity;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIMerchant extends EntityAIBase {

	private final EntityMerchant entity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private final double movementSpeed;

	public EntityAIMerchant(EntityMerchant entity, double movementSpeed) {
		this.entity = entity;
		this.movementSpeed = movementSpeed;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (entity.isAtMarket()) {
			entity.setToFacingAngle();
			return false;
		}

		BlockPos marketPos = entity.getMarketEntityPosition();
		if(marketPos != null && entity.getNavigator().canEntityStandOnPos(marketPos)) {
			this.movePosX = marketPos.getX() + 0.5;
			this.movePosY = marketPos.getY() + 1;
			this.movePosZ = marketPos.getZ() + 0.5;
			return true;
		}
		return false;
	}

	@Override
	public boolean continueExecuting() {
		return !entity.getNavigator().noPath();
	}

	@Override
	public void startExecuting() {
		entity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, movementSpeed);
	}

}
