package net.blay09.mods.farmingforblockheads.tile;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.farmingforblockheads.ModConfig;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public void update() {
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
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    private void teehee() {
        ItemStack itemStack = itemHandler.getStackInSlot(0);
        if (itemStack.isEmpty() || itemStack.getCount() < 2) {
            return;
        }

        final float range = ModConfig.general.feedingTroughRange;
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
            if (list.size() < ModConfig.general.feedingTroughMaxAnimals) {
                if (list.stream().filter(p -> p.getGrowingAge() == 0).count() >= 2) {
                    for (EntityAnimal entity : list) {
                        if (entity.getGrowingAge() == 0 && !entity.isInLove() && !entity.isChild() && entity.isBreedingItem(itemStack)) {
                            entity.setInLove(null);
                            itemHandler.extractItem(0, 1, false);
                            markDirty();
                            NetworkHandler.instance.sendToAllAround(new MessageChickenNestEffect(pos), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
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
