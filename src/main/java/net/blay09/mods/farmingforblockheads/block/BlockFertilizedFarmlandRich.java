package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.item.ItemFertilizer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

/**
 * TODO in 1.13, move these properties to BlockStates and find a better way to make the textures match
 * Using properties would require model changes right now, since they also have Vanilla's wet property.
 */
public class BlockFertilizedFarmlandRich extends BlockFertilizedFarmland {
    public static final String name = "fertilized_farmland_rich";
    public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);

    public BlockFertilizedFarmlandRich(boolean isStable) {
        super(isStable);
        setUnlocalizedName(registryName.toString());
    }

    @Override
    public float getBonusCropChance() {
        return FarmingForBlockheadsConfig.general.fertilizerBonusCropChance;
    }

    @Override
    public IBlockState applyFertilizer(IBlockState state, ItemFertilizer.FertilizerType fertilizerType) {
        if (!isStable) {
            return ModBlocks.fertilizedFarmlandRichStable.getDefaultState();
        }

        return state;
    }
}
