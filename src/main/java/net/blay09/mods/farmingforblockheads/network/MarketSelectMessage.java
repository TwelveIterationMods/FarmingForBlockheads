package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.container.MarketContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MarketSelectMessage {

    private final ItemStack outputItem;

    public MarketSelectMessage(ItemStack outputItem) {
        this.outputItem = outputItem;
    }

    public static void encode(MarketSelectMessage message, PacketBuffer buf) {
        buf.writeItemStack(message.outputItem);
    }

    public static MarketSelectMessage decode(PacketBuffer buf) {
        ItemStack outputItem = buf.readItemStack();
        return new MarketSelectMessage(outputItem);
    }

    public static void handle(MarketSelectMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            PlayerEntity player = context.getSender();
            if (player == null) {
                return;
            }

            Container container = player.openContainer;
            if (container instanceof MarketContainer) {
                ((MarketContainer) container).selectMarketEntry(message.outputItem);
            }
        });
    }

}
