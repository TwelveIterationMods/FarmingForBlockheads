package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.util.ResourceLocation;

public class BlockFertilizedFarmlandHealthy extends BlockFertilizedFarmland {

	public static final String name = "fertilized_farmland_healthy";
	public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);

	public BlockFertilizedFarmlandHealthy() {
		setUnlocalizedName(registryName.toString());
	}

	@Override
	public float getDoubleGrowthChance() {
		return 1f;
	}
}
