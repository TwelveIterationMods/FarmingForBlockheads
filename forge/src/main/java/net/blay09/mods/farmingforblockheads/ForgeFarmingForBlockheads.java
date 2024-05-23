package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.farmingforblockheads.client.FarmingForBlockheadsClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(FarmingForBlockheads.MOD_ID)
public class ForgeFarmingForBlockheads {
    public ForgeFarmingForBlockheads() {
        Balm.initialize(FarmingForBlockheads.MOD_ID, EmptyLoadContext.INSTANCE, FarmingForBlockheads::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initialize(FarmingForBlockheads.MOD_ID, EmptyLoadContext.INSTANCE, FarmingForBlockheadsClient::initialize));
    }
}
