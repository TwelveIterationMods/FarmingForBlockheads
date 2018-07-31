package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.block.BlockFertilizedFarmland;
import net.blay09.mods.farmingforblockheads.item.ModItems;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FarmlandHandler {

	@SubscribeEvent
	public void onGrowEvent(BlockEvent.CropGrowEvent.Post event) {
		IBlockState plant = event.getWorld().getBlockState(event.getPos());
		if(plant.getBlock() instanceof IGrowable) {
			IGrowable growable = (IGrowable) plant.getBlock();
			IBlockState farmland = event.getWorld().getBlockState(event.getPos().down());
			if (farmland.getBlock() instanceof BlockFertilizedFarmland) {
				if (Math.random() <= ((BlockFertilizedFarmland) farmland.getBlock()).getDoubleGrowthChance()) {
					if(growable.canGrow(event.getWorld(), event.getPos(), plant, event.getWorld().isRemote)) {
						growable.grow(event.getWorld(), event.getWorld().rand, event.getPos(), plant);
						event.getWorld().playEvent(2005, event.getPos(), 0);
						rollRegression(event.getWorld(), event.getPos(), farmland);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onHarvest(BlockEvent.HarvestDropsEvent event) {
		IBlockState plant = event.getState();
		IBlockState farmland = event.getWorld().getBlockState(event.getPos().down());
		if (farmland.getBlock() instanceof BlockFertilizedFarmland && plant.getBlock() instanceof IGrowable) {
			if(Math.random() <= ((BlockFertilizedFarmland) farmland.getBlock()).getBonusCropChance()) {
				event.getDrops().stream().filter(p -> !isProbablySeed(p)).findAny().ifPresent(c -> {
					event.getDrops().add(c.copy());
					event.getWorld().playEvent(2005, event.getPos(), 0);
					rollRegression(event.getWorld(), event.getPos(), farmland);
				});
			}
		}
	}

	@SubscribeEvent
	public void onFertilize(PlayerInteractEvent.RightClickBlock event) {
		if(!event.getItemStack().isEmpty() && event.getItemStack().getItem() == ModItems.fertilizer) {
			IBlockState farmland = event.getWorld().getBlockState(event.getPos().down());
			if (farmland.getBlock() == Blocks.FARMLAND) {
				if (ModItems.fertilizer.onItemUse(event.getEntityPlayer(), event.getWorld(), event.getPos().down(), event.getHand(), EnumFacing.UP, (float) event.getHitVec().x, (float) event.getHitVec().y, (float) event.getHitVec().z) == EnumActionResult.SUCCESS) {
					event.setCanceled(true);
				}
			}
		}
	}

	private void rollRegression(World world, BlockPos pos, IBlockState farmland) {
		if(farmland.getBlock() instanceof BlockFertilizedFarmland) {
			if(Math.random() <= ((BlockFertilizedFarmland) farmland.getBlock()).getRegressionChance()) {
				world.setBlockState(pos, Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, farmland.getValue(BlockFarmland.MOISTURE)));
			}
		}
	}

	private boolean isProbablySeed(ItemStack itemStack) {
		ResourceLocation registryName = itemStack.getItem().getRegistryName();
		return registryName != null && registryName.getResourcePath().contains("seed");
	}
}
