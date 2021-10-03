package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.client.FarmingForBlockheadsClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FarmingForBlockheads.MOD_ID)
public class ForgeFarmingForBlockheads {
    public ForgeFarmingForBlockheads() {
        FarmingForBlockheads.initialize();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> FarmingForBlockheadsClient::initialize);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::processIMC);
    }
}
