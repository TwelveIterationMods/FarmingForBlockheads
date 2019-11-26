package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
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
        context.enqueueWork(() -> FarmingForBlockheads.proxy.playChickenNestEffect(message.pos));
        context.setPacketHandled(true);
    }

}
