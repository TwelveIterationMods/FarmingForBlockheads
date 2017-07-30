package net.blay09.mods.farmingforblockheads.tile;

import net.blay09.mods.farmingforblockheads.network.MessageChickenNestEffect;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.network.VanillaPacketHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileChickenNest extends TileEntity implements ITickable {

	private static final int TICK_INTERVAL = 20;
	private static final int RANGE = 8;

	private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
		@Override
		public int getSlotLimit(int slot) {
			return 4;
		}

		@Override
		protected void onContentsChanged(int slot) {
			isDirty = true;
			markDirty();
		}
	};

	private int tickTimer;
	private boolean isDirty;

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
				super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) itemHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
		return tagCompound;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			tickTimer++;
			if (tickTimer >= TICK_INTERVAL) {
				stealEgg();
				tickTimer = 0;
			}

			if(isDirty) {
				VanillaPacketHandler.sendTileEntityUpdate(this);
				isDirty = false;
			}
		}
	}

	private void stealEgg() {
		AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() - RANGE, pos.getY() - RANGE, pos.getZ() - RANGE, pos.getX() + RANGE, pos.getY() + RANGE, pos.getZ() + RANGE);
		//noinspection ConstantConditions
		List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, aabb, p -> p != null && p.getItem().getItem() == Items.EGG && p.getItem().getCount() == 1 && p.getThrower() == null);
		if(list.isEmpty()) {
			return;
		}
		EntityItem entityItem = list.get(0);
		ItemStack restStack = entityItem.getItem().copy();
		for(int i = 0; i < itemHandler.getSlots(); i++) {
			restStack = itemHandler.insertItem(i, restStack, false);
			if(restStack.isEmpty()) {
				break;
			}
		}
		if(restStack.isEmpty()) {
			entityItem.setDead();
		} else {
			entityItem.setItem(restStack);
		}
		markDirty();
		NetworkHandler.instance.sendToAllAround(new MessageChickenNestEffect(pos), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
	}

	public int getEggCount() {
		return itemHandler.getStackInSlot(0).getCount();
	}
}
