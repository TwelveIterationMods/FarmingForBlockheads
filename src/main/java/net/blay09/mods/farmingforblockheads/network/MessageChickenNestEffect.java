package net.blay09.mods.farmingforblockheads.network;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Particles;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageChickenNestEffect {
    private final BlockPos pos;

    public MessageChickenNestEffect(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(MessageChickenNestEffect message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
    }

    public static MessageChickenNestEffect decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        return new MessageChickenNestEffect(pos);
    }

    public static void handle(MessageChickenNestEffect message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                World world = Minecraft.getInstance().world;
                world.addParticle(Particles.EXPLOSION, message.pos.getX() + 0.5f, message.pos.getY() + 0.5f, message.pos.getZ() + 0.5f, 0, 0, 0);
            });
        });
    }
}
