package net.blay09.mods.farmingforblockheads.tile;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.network.MessageChickenNestEffect;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.network.VanillaPacketHandler;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
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

public class TileFeedingTrough extends TileEntity implements ITickable {

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

    public TileFeedingTrough() {
        super(ModTiles.feedingTrough);
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
    public void read(NBTTagCompound tagCompound) {
        super.read(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tagCompound) {
        super.write(tagCompound);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        read(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        write(tagCompound);
        return tagCompound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> itemHandler));
    }

    private void teehee() {
        ItemStack itemStack = itemHandler.getStackInSlot(0);
        if (itemStack.isEmpty() || itemStack.getCount() < 2) {
            return;
        }

        final float range = FarmingForBlockheadsConfig.general.feedingTroughRange;
        AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range);
        List<EntityAnimal> entities = world.getEntitiesWithinAABB(EntityAnimal.class, aabb);
        if (entities.isEmpty()) {
            return;
        }

        ArrayListMultimap<Class<? extends EntityAnimal>, EntityAnimal> map = ArrayListMultimap.create();
        for (EntityAnimal animal : entities) {
            map.put(animal.getClass(), animal);
        }

        List<Class<? extends EntityAnimal>> keys = new ArrayList<>(map.keySet());
        Collections.shuffle(keys);

        for (Class<? extends EntityAnimal> key : keys) {
            List<EntityAnimal> list = map.get(key);
            if (list.size() < FarmingForBlockheadsConfig.general.feedingTroughMaxAnimals) {
                if (list.stream().filter(p -> p.getGrowingAge() == 0).count() >= 2) {
                    for (EntityAnimal entity : list) {
                        if (entity.getGrowingAge() == 0 && !entity.isInLove() && !entity.isChild() && entity.isBreedingItem(itemStack)) {
                            entity.setInLove(null);
                            itemHandler.extractItem(0, 1, false);
                            markDirty();
                            NetworkHandler.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos)), new MessageChickenNestEffect(pos));
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
