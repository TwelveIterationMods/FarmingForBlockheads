package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.container.MarketContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MarketSelectMessage {

    private final UUID entryId;

    public MarketSelectMessage(UUID entryId) {
        this.entryId = entryId;
    }

    public static void encode(MarketSelectMessage message, PacketBuffer buf) {
        buf.writeUniqueId(message.entryId);
    }

    public static MarketSelectMessage decode(PacketBuffer buf) {
        UUID entryId = buf.readUniqueId();
        return new MarketSelectMessage(entryId);
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
                ((MarketContainer) container).selectMarketEntry(message.entryId);
            }
        });
        context.setPacketHandled(true);
    }

}
