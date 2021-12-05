package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.UUID;

public class MarketSelectMessage {

    private final UUID entryId;

    public MarketSelectMessage(UUID entryId) {
        this.entryId = entryId;
    }

    public static void encode(MarketSelectMessage message, FriendlyByteBuf buf) {
        buf.writeUUID(message.entryId);
    }

    public static MarketSelectMessage decode(FriendlyByteBuf buf) {
        UUID entryId = buf.readUUID();
        return new MarketSelectMessage(entryId);
    }

    public static void handle(ServerPlayer player, MarketSelectMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof MarketMenu marketMenu) {
            marketMenu.selectMarketEntry(message.entryId);
        }
    }

}
