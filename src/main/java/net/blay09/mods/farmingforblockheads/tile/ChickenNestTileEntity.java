package net.blay09.mods.farmingforblockheads.tile;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.network.ChickenNestEffectMessage;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.network.VanillaPacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ChickenNestTileEntity extends TileEntity implements ITickableTileEntity {

    private static final int TICK_INTERVAL = 20;

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (stack.getItem() != Items.EGG) {
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }

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

    public ChickenNestTileEntity() {
        super(ModTileEntities.chickenNest);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            tickTimer++;
            if (tickTimer >= TICK_INTERVAL) {
                stealEgg();
                tickTimer = 0;
            }

            if (isDirty) {
                VanillaPacketHandler.sendTileEntityUpdate(this);
                isDirty = false;
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> itemHandler));
    }

    @Override
    public void read(BlockState state, CompoundNBT tagCompound) {
        super.read(state, tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        read(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tagCompound = new CompoundNBT();
        write(tagCompound);
        return tagCompound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    private void stealEgg() {
        final float range = FarmingForBlockheadsConfig.COMMON.chickenNestRange.get();
        AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range);
        List<ItemEntity> list = world.getEntitiesWithinAABB(ItemEntity.class, aabb, p -> p != null && p.getItem().getItem() == Items.EGG && p.getItem().getCount() == 1 && p.getThrowerId() == null);
        if (list.isEmpty()) {
            return;
        }
        ItemEntity entityItem = list.get(0);
        ItemStack restStack = entityItem.getItem().copy();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            restStack = itemHandler.insertItem(i, restStack, false);
            if (restStack.isEmpty()) {
                break;
            }
        }
        if (restStack.isEmpty()) {
            entityItem.remove();
            NetworkHandler.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), new ChickenNestEffectMessage(pos));
        } else {
            entityItem.setItem(restStack);
        }
        markDirty();
    }

    public int getEggCount() {
        return itemHandler.getStackInSlot(0).getCount();
    }
}
