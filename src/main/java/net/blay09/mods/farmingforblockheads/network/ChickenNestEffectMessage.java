package net.blay09.mods.farmingforblockheads.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ChickenNestEffectMessage {
    private final BlockPos pos;

    public ChickenNestEffectMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(ChickenNestEffectMessage message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
    }

    public static ChickenNestEffectMessage decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        return new ChickenNestEffectMessage(pos);
    }

    public static void handle(ChickenNestEffectMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                World world = Minecraft.getInstance().world;
                world.addParticle(ParticleTypes.EXPLOSION, message.pos.getX() + 0.5f, message.pos.getY() + 0.5f, message.pos.getZ() + 0.5f, 0, 0, 0);
            });
        });
        context.setPacketHandled(true);
    }
}
