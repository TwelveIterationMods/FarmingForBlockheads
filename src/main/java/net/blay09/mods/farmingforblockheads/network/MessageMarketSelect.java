package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.container.ContainerMarket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageMarketSelect {

    private final ItemStack outputItem;

    public MessageMarketSelect(ItemStack outputItem) {
        this.outputItem = outputItem;
    }

    public static void encode(MessageMarketSelect message, PacketBuffer buf) {
        buf.writeItemStack(message.outputItem);
    }

    public static MessageMarketSelect decode(PacketBuffer buf) {
        ItemStack outputItem = buf.readItemStack();
        return new MessageMarketSelect(outputItem);
    }

    public static void handle(MessageMarketSelect message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            EntityPlayer player = context.getSender();
            if (player == null) {
                return;
            }

            Container container = player.openContainer;
            if (container instanceof ContainerMarket) {
                ((ContainerMarket) container).selectMarketEntry(message.outputItem);
            }
        });
    }

}
