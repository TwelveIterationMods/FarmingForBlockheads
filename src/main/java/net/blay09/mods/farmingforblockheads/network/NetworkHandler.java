package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    public static SimpleChannel channel;

    public static void init() {
        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(FarmingForBlockheads.MOD_ID, "network"), () -> "1.0", it -> true, it -> true);
        channel.registerMessage(0, MessageMarketList.class, MessageMarketList::encode, MessageMarketList::decode, MessageMarketList::handle);
        channel.registerMessage(1, MessageMarketSelect.class, MessageMarketSelect::encode, MessageMarketSelect::decode, MessageMarketSelect::handle);
        channel.registerMessage(2, MessageChickenNestEffect.class, MessageChickenNestEffect::encode, MessageChickenNestEffect::decode, MessageChickenNestEffect::handle);
    }

}
