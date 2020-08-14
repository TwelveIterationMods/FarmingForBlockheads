package net.blay09.mods.farmingforblockheads.client;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.farmingforblockheads.CommonProxy;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.container.MarketClientContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {
    @Override
    public void playChickenNestEffect(BlockPos pos) {
        World world = Minecraft.getInstance().world;
        world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0);
    }

    @Override
    public void receivedMarketList(ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap) {
        Container container = Minecraft.getInstance().player.openContainer;
        if (container instanceof MarketClientContainer) {
            ((MarketClientContainer) container).setCategoryList(entryMap.keySet());
            ((MarketClientContainer) container).setEntryList(entryMap.values());
        }
    }
}
