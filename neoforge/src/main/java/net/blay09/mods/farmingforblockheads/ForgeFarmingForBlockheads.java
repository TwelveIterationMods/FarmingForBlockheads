package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.farmingforblockheads.client.FarmingForBlockheadsClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FarmingForBlockheads.MOD_ID)
public class ForgeFarmingForBlockheads {
    public ForgeFarmingForBlockheads() {
        Balm.initialize(FarmingForBlockheads.MOD_ID, FarmingForBlockheads::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initialize(FarmingForBlockheads.MOD_ID, FarmingForBlockheadsClient::initialize));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::processIMC);
    }
}
