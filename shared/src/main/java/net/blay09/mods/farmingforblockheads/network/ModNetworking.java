package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.resources.ResourceLocation;

public class ModNetworking {
    public static void initialize(BalmNetworking networking) {
        networking.registerClientboundPacket(id("market_categories"), MarketCategoriesMessage.class, MarketCategoriesMessage::encode, MarketCategoriesMessage::decode, MarketCategoriesMessage::handle);
        networking.registerClientboundPacket(id("chicken_nest_effect"), ChickenNestEffectMessage.class, ChickenNestEffectMessage::encode, ChickenNestEffectMessage::decode, ChickenNestEffectMessage::handle);

        networking.registerServerboundPacket(id("market_select"), MarketSelectMessage.class, MarketSelectMessage::encode, MarketSelectMessage::decode, MarketSelectMessage::handle);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }
}
