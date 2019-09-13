package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    public static SimpleChannel channel;

    private static final String version = "1.0";

    public static void init() {
        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(FarmingForBlockheads.MOD_ID, "network"), () -> version, it -> it.equals(version), it -> it.equals(version));
        channel.registerMessage(0, MarketListMessage.class, MarketListMessage::encode, MarketListMessage::decode, MarketListMessage::handle);
        channel.registerMessage(1, MarketSelectMessage.class, MarketSelectMessage::encode, MarketSelectMessage::decode, MarketSelectMessage::handle);
        channel.registerMessage(2, ChickenNestEffectMessage.class, ChickenNestEffectMessage::encode, ChickenNestEffectMessage::decode, ChickenNestEffectMessage::handle);
    }

    public static void sendTo(Object message, PlayerEntity player) {
        channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }
}
