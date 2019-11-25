package net.blay09.mods.farmingforblockheads.network;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.container.MarketClientContainer;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class MarketListMessage {

    private final ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap;

    public MarketListMessage(ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap) {
        this.entryMap = entryMap;
    }

    public static MarketListMessage decode(PacketBuffer buf) {
        ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap = ArrayListMultimap.create();
        int categoryCount = buf.readByte();
        for (int i = 0; i < categoryCount; i++) {
            ResourceLocation categoryId = buf.readResourceLocation();
            IMarketCategory category = FarmingForBlockheadsAPI.getMarketCategory(categoryId);
            if (category == null) {
                category = FarmingForBlockheadsAPI.getMarketCategoryOther();
            }
            int entryCount = buf.readShort();
            for (int j = 0; j < entryCount; j++) {
                entryMap.put(category, readEntry(buf, category));
            }
        }

        return new MarketListMessage(entryMap);
    }

    public static void encode(MarketListMessage message, PacketBuffer buf) {
        buf.writeByte(message.entryMap.keySet().size());
        for (IMarketCategory category : message.entryMap.keySet()) {
            buf.writeResourceLocation(category.getRegistryName());
            List<IMarketEntry> entries = message.entryMap.get(category);
            buf.writeShort(entries.size());
            for (IMarketEntry entry : entries) {
                writeEntry(entry, buf);
            }
        }
    }

    public static void handle(MarketListMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                Container container = Minecraft.getInstance().player.openContainer;
                if (container instanceof MarketClientContainer) {
                    ((MarketClientContainer) container).setEntryList(message.entryMap.values());
                }
            });
        });
        context.setPacketHandled(true);
    }

    private static MarketEntry readEntry(PacketBuffer buf, IMarketCategory category) {
        ItemStack outputItem = buf.readItemStack();
        ItemStack costItem = buf.readItemStack();
        return new MarketEntry(outputItem, costItem, category);
    }

    private static void writeEntry(IMarketEntry entry, PacketBuffer buf) {
        buf.writeItemStack(entry.getOutputItem());
        buf.writeItemStack(entry.getCostItem());
    }

}
