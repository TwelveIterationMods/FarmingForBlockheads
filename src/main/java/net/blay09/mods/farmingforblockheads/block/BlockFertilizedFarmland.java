package net.blay09.mods.farmingforblockheads.block;

import com.google.common.collect.Lists;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class BlockFertilizedFarmland extends BlockFarmland {

    public interface FarmlandTrait {
        default float getDoubleGrowthChance() {
            return 0f;
        }

        default float getBonusCropChance() {
            return 0f;
        }

        default boolean isStable() {
            return false;
        }
    }

    public static class FarmlandHealthyTrait implements FarmlandTrait {
        @Override
        public float getDoubleGrowthChance() {
            return FarmingForBlockheadsConfig.COMMON.fertilizerBonusGrowthChance.get();
        }
    }

    public static class FarmlandRichTrait implements FarmlandTrait {
        @Override
        public float getBonusCropChance() {
            return FarmingForBlockheadsConfig.COMMON.fertilizerBonusCropChance.get();
        }
    }

    public static class FarmlandStableTrait implements FarmlandTrait {
        @Override
        public boolean isStable() {
            return true;
        }
    }

    private final List<FarmlandTrait> traits;

    public BlockFertilizedFarmland(FarmlandTrait... traits) {
        super(Block.Properties.create(Material.GROUND).sound(SoundType.GROUND).hardnessAndResistance(1f));
        this.traits = Lists.newArrayList(traits);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing facing, IPlantable plantable) {
        return true;
    }

    @Override
    public boolean isFertile(IBlockState state, IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos).get(MOISTURE) > 0;
    }

    public float getDoubleGrowthChance() {
        return (float) traits.stream().mapToDouble(FarmlandTrait::getDoubleGrowthChance).sum();
    }

    public float getBonusCropChance() {
        return (float) traits.stream().mapToDouble(FarmlandTrait::getBonusCropChance).sum();
    }

    public float getRegressionChance() {
        return FarmingForBlockheadsConfig.COMMON.fertilizerRegressionChance.get();
    }

    public boolean isStable() {
        return traits.stream().anyMatch(FarmlandTrait::isStable);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (!isStable()) {
            super.onFallenUpon(world, pos, entity, fallDistance);
        }
    }

    @Override
    public void tick(IBlockState state, World world, BlockPos pos, Random random) {
        int moisture = state.get(MOISTURE);

        if (!hasWater(world, pos) && !world.isRainingAt(pos.up())) {
            if (moisture > 0) {
                world.setBlockState(pos, state.with(MOISTURE, moisture - 1), 2);
            } else if (!hasCrops(world, pos) && traits.stream().noneMatch(FarmlandTrait::isStable)) {
                turnToDirt(state, world, pos);
            }
        } else if (moisture < 7) {
            world.setBlockState(pos, state.with(MOISTURE, 7), 2);
        }
    }

    public Collection<FarmlandTrait> getTraits() {
        return traits;
    }
}
