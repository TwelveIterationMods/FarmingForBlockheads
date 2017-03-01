package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.client.gui.GuiMarket;
import net.blay09.mods.farmingforblockheads.container.ContainerMarket;
import net.blay09.mods.farmingforblockheads.container.ContainerMarketClient;
import net.blay09.mods.farmingforblockheads.tile.TileMarket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

	public static final int MARKET = 1;

	@Override
	@Nullable
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		if(id == MARKET) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if(tileEntity instanceof TileMarket) {
				return new ContainerMarket(player, pos);
			}
		}
		return null;
	}

	@Override
	@Nullable
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		if(id == MARKET) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if(tileEntity instanceof TileMarket) {
				return new GuiMarket(new ContainerMarketClient(player, pos));
			}
		}
		return null;
	}
}
