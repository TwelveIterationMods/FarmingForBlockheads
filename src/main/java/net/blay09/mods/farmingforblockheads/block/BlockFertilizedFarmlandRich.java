package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFertilizedFarmlandRich extends BlockFertilizedFarmland {

	public static final String name = "fertilized_farmland_rich";
	public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);

	public BlockFertilizedFarmlandRich() {
		setUnlocalizedName(registryName.toString());
	}

	@Override
	public float getBonusCropChance() {
		return 1f;
	}
}
