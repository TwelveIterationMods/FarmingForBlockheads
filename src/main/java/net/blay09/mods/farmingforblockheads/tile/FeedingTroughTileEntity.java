package net.blay09.mods.farmingforblockheads.tile;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.network.ChickenNestEffectMessage;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.network.VanillaPacketHandler;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedingTroughTileEntity extends TileEntity implements ITickableTileEntity {

    private static final int TICK_INTERVAL = 20 * 5;

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            isDirty = true;
            markDirty();
        }
    };

    private int tickTimer;
    private boolean isDirty;

    public FeedingTroughTileEntity() {
        super(ModTileEntities.feedingTrough);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            tickTimer++;
            if (tickTimer >= TICK_INTERVAL) {
                teehee();
                tickTimer = 0;
            }

            if (isDirty) {
                VanillaPacketHandler.sendTileEntityUpdate(this);
                isDirty = false;
            }
        }
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
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
        read(pkt.getNbtCompound());
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> itemHandler));
    }

    private void teehee() {
        ItemStack itemStack = itemHandler.getStackInSlot(0);
        if (itemStack.isEmpty() || itemStack.getCount() < 2) {
            return;
        }

        final float range = FarmingForBlockheadsConfig.COMMON.feedingTroughRange.get();
        AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range);
        List<AnimalEntity> entities = world.getEntitiesWithinAABB(AnimalEntity.class, aabb);
        if (entities.isEmpty()) {
            return;
        }

        ArrayListMultimap<Class<? extends AnimalEntity>, AnimalEntity> map = ArrayListMultimap.create();
        for (AnimalEntity animal : entities) {
            map.put(animal.getClass(), animal);
        }

        List<Class<? extends AnimalEntity>> keys = new ArrayList<>(map.keySet());
        Collections.shuffle(keys);

        for (Class<? extends AnimalEntity> key : keys) {
            List<AnimalEntity> list = map.get(key);
            if (list.size() < FarmingForBlockheadsConfig.COMMON.feedingTroughMaxAnimals.get()) {
                if (list.stream().filter(p -> p.getGrowingAge() == 0).count() >= 2) {
                    for (AnimalEntity entity : list) {
                        if (entity.getGrowingAge() == 0 && !entity.isInLove() && !entity.isChild() && entity.isBreedingItem(itemStack)) {
                            entity.setInLove(null);
                            itemHandler.extractItem(0, 1, false);
                            markDirty();
                            NetworkHandler.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), new ChickenNestEffectMessage(pos));
                            break;
                        }
                    }
                }
            }
        }
    }

    public ItemStack getContentStack() {
        return itemHandler.getStackInSlot(0);
    }
}
