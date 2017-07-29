package net.blay09.mods.farmingforblockheads.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageMarketSelect implements IMessage {

	private ItemStack outputItem = ItemStack.EMPTY;

	public MessageMarketSelect() {
	}

	public MessageMarketSelect(ItemStack outputItem) {
		this.outputItem = outputItem;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		outputItem = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeItemStack(buf, outputItem);
	}

	public ItemStack getOutputItem() {
		return outputItem;
	}

}
