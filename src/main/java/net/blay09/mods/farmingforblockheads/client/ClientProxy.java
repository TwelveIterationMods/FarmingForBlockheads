package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.farmingforblockheads.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {
    @Override
    public void playChickenNestEffect(BlockPos pos) {
        World world = Minecraft.getInstance().world;
        world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0);
    }
}
