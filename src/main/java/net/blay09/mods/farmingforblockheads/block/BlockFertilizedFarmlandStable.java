package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockFertilizedFarmlandStable extends BlockFertilizedFarmland {

	public static final String name = "fertilized_farmland_stable";
	public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);

	public BlockFertilizedFarmlandStable() {
		setUnlocalizedName(registryName.toString());
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
	}

}
