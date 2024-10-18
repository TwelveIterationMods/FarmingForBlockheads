package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.balm.api.network.BalmNetworking;

public class ModNetworking {
    public static void initialize(BalmNetworking networking) {
        networking.registerClientboundPacket(MarketCategoriesMessage.TYPE, MarketCategoriesMessage.class, MarketCategoriesMessage::encode, MarketCategoriesMessage::decode, MarketCategoriesMessage::handle);
        networking.registerClientboundPacket(MarketRecipesMessage.TYPE, MarketRecipesMessage.class, MarketRecipesMessage::encode, MarketRecipesMessage::decode, MarketRecipesMessage::handle);
        networking.registerClientboundPacket(ChickenNestEffectMessage.TYPE, ChickenNestEffectMessage.class, ChickenNestEffectMessage::encode, ChickenNestEffectMessage::decode, ChickenNestEffectMessage::handle);
        networking.registerServerboundPacket(MarketPutInBasketMessage.TYPE, MarketPutInBasketMessage.class, MarketPutInBasketMessage::encode, MarketPutInBasketMessage::decode, MarketPutInBasketMessage::handle);
    }
}
