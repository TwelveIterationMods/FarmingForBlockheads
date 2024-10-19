package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.balm.api.network.BalmNetworking;

public class ModNetworking {
    public static void initialize(BalmNetworking networking) {
        networking.registerClientboundPacket(MarketCategoriesMessage.TYPE, MarketCategoriesMessage.class, MarketCategoriesMessage::encode, MarketCategoriesMessage::decode, MarketCategoriesMessage::handle);
        networking.registerClientboundPacket(MarketRecipesMessage.TYPE, MarketRecipesMessage.class, MarketRecipesMessage::encode, MarketRecipesMessage::decode, MarketRecipesMessage::handle);
        networking.registerClientboundPacket(ChickenNestEffectMessage.TYPE, ChickenNestEffectMessage.class, ChickenNestEffectMessage::encode, ChickenNestEffectMessage::decode, ChickenNestEffectMessage::handle);
        networking.registerServerboundPacket(MarketPlaceRecipeMessage.TYPE, MarketPlaceRecipeMessage.class, MarketPlaceRecipeMessage.STREAM_CODEC::encode, MarketPlaceRecipeMessage.STREAM_CODEC::decode, MarketPlaceRecipeMessage::handle);
    }
}
