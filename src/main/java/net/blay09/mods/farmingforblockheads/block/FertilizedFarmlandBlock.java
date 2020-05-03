package net.blay09.mods.farmingforblockheads.block;

import com.google.common.collect.Lists;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FertilizedFarmlandBlock extends FarmlandBlock {

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

        String getTraitName();

        TextFormatting getTraitColor();
    }

    public static class FarmlandHealthyTrait implements FarmlandTrait {
        @Override
        public float getDoubleGrowthChance() {
            return FarmingForBlockheadsConfig.COMMON.fertilizerBonusGrowthChance.get().floatValue();
        }

        @Override
        public String getTraitName() {
            return "healthy";
        }

        @Override
        public TextFormatting getTraitColor() {
            return TextFormatting.DARK_RED;
        }
    }

    public static class FarmlandRichTrait implements FarmlandTrait {
        @Override
        public float getBonusCropChance() {
            return FarmingForBlockheadsConfig.COMMON.fertilizerBonusCropChance.get().floatValue();
        }

        @Override
        public String getTraitName() {
            return "rich";
        }

        @Override
        public TextFormatting getTraitColor() {
            return TextFormatting.GREEN;
        }
    }

    public static class FarmlandStableTrait implements FarmlandTrait {
        @Override
        public boolean isStable() {
            return true;
        }

        @Override
        public String getTraitName() {
            return "stable";
        }

        @Override
        public TextFormatting getTraitColor() {
            return TextFormatting.YELLOW;
        }
    }

    private final List<FarmlandTrait> traits;

    public FertilizedFarmlandBlock(FarmlandTrait... traits) {
        super(Block.Properties.create(Material.EARTH).sound(SoundType.GROUND).hardnessAndResistance(0.6f).tickRandomly().harvestTool(ToolType.SHOVEL));
        this.traits = Lists.newArrayList(traits);
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return true;
    }

    @Override
    public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos).get(MOISTURE) > 0;
    }

    public float getDoubleGrowthChance() {
        return (float) traits.stream().mapToDouble(FarmlandTrait::getDoubleGrowthChance).sum();
    }

    public float getBonusCropChance() {
        return (float) traits.stream().mapToDouble(FarmlandTrait::getBonusCropChance).sum();
    }

    public float getRegressionChance() {
        return FarmingForBlockheadsConfig.COMMON.fertilizerRegressionChance.get().floatValue();
    }

    private boolean isStable() {
        return traits.stream().anyMatch(FarmlandTrait::isStable);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (!isStable()) {
            super.onFallenUpon(world, pos, entity, fallDistance);
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int moisture = state.get(MOISTURE);

        if (!hasWater(world, pos) && !world.isRainingAt(pos.up())) {
            if (moisture > 0) {
                world.setBlockState(pos, state.with(MOISTURE, moisture - 1), 2);
            } else if (!hasCropsFFB(world, pos) && traits.stream().noneMatch(FarmlandTrait::isStable)) {
                turnToDirt(state, world, pos);
            }
        } else if (moisture < 7) {
            world.setBlockState(pos, state.with(MOISTURE, 7), 2);
        }
    }

    public Collection<FarmlandTrait> getTraits() {
        return traits;
    }

    /**
     * AT doesn't work on super impl because it's patched?
     */
    private boolean hasCropsFFB(IBlockReader worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos.up());
        return state.getBlock() instanceof IPlantable && canSustainPlant(state, worldIn, pos, Direction.UP, (IPlantable) state.getBlock());
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        for (FarmlandTrait trait : traits) {
            TranslationTextComponent traitComponent = new TranslationTextComponent("tooltip.farmingforblockheads:trait_" + trait.getTraitName());
            traitComponent.getStyle().setColor(trait.getTraitColor());
            tooltip.add(traitComponent);
        }
    }
}
