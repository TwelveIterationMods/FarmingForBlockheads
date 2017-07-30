package net.blay09.mods.farmingforblockheads.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageChickenNestEffect implements IMessage {
	private BlockPos pos;

	public MessageChickenNestEffect() {
	}

	public MessageChickenNestEffect(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
	}

	public BlockPos getPos() {
		return pos;
	}
}
