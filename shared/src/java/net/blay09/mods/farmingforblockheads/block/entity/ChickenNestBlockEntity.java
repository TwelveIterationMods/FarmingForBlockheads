package net.blay09.mods.farmingforblockheads.block.entity;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.network.ChickenNestEffectMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ChickenNestBlockEntity extends BalmBlockEntity implements BalmContainerProvider {

    private static final int TICK_INTERVAL = 20;

    private final DefaultContainer container = new DefaultContainer(1) {

        @Override
        public boolean canPlaceItem(int i, ItemStack itemStack) {
            return itemStack.getItem() == Items.EGG;
        }

        @Override
        public void setChanged() {
            isDirty = true;
            ChickenNestBlockEntity.this.setChanged();
        }

        @Override
        public int getMaxStackSize() {
            return 4;
        }
    };

    private int tickTimer;
    private boolean isDirty;

    public ChickenNestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.chickenNest.get(), pos, state);
    }

    public void tick() { // TODO
        if (!level.isClientSide) {
            tickTimer++;
            if (tickTimer >= TICK_INTERVAL) {
                stealEgg();
                tickTimer = 0;
            }

            if (isDirty) {
                balmSync();
                isDirty = false;
            }
        }
    }

    /*@Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> itemHandler));
    }*/

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        container.deserialize(tagCompound.getCompound("ItemHandler"));
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        tagCompound.put("ItemHandler", container.serialize());
        return tagCompound;
    }

    private void stealEgg() {
        final float range = FarmingForBlockheadsConfig.getActive().chickenNestRange;
        AABB aabb = new AABB(worldPosition.getX() - range, worldPosition.getY() - range, worldPosition.getZ() - range, worldPosition.getX() + range, worldPosition.getY() + range, worldPosition.getZ() + range);
        List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, aabb, p -> p != null && p.getItem().getItem() == Items.EGG && p.getItem().getCount() == 1 && p.getThrower() == null);
        if (list.isEmpty()) {
            return;
        }
        ItemEntity entityItem = list.get(0);
        ItemStack restStack = entityItem.getItem().copy();
        for (int i = 0; i < container.getContainerSize(); i++) {
            restStack = ContainerUtils.insertItem(container, i, restStack, false);
            if (restStack.isEmpty()) {
                break;
            }
        }
        if (restStack.isEmpty()) {
            entityItem.remove(Entity.RemovalReason.DISCARDED);
            Balm.getNetworking().sendToTracking(((ServerLevel) level), worldPosition, new ChickenNestEffectMessage(worldPosition));
        } else {
            entityItem.setItem(restStack);
        }
        setChanged();
    }

    public int getEggCount() {
        return container.getItem(0).getCount();
    }

    @Override
    public Container getContainer() {
        return container;
    }
}
