package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.FertilizedFarmlandBlock;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FertilizerItem extends Item {

    public enum FertilizerType {
        HEALTHY,
        RICH,
        STABLE;

        public BlockState applyFertilizer(BlockState state) {
            Block sourceBlock = state.getBlock();
            if (sourceBlock != Blocks.FARMLAND && !(sourceBlock instanceof FertilizedFarmlandBlock)) {
                return state;
            }

            int moisture = state.get(BlockStateProperties.MOISTURE_0_7);
            List<FertilizedFarmlandBlock.FarmlandTrait> traits = new ArrayList<>();
            traits.add(getFarmlandTrait());
            if (sourceBlock instanceof FertilizedFarmlandBlock) {
                traits.addAll(((FertilizedFarmlandBlock) sourceBlock).getTraits());
            }

            Block targetBlock = getBlockForTraits(traits);
            if (targetBlock == null) {
                return state;
            }

            BlockState newState = targetBlock.getDefaultState();
            return newState.with(FertilizedFarmlandBlock.MOISTURE, moisture);
        }

        private FertilizedFarmlandBlock.FarmlandTrait getFarmlandTrait() {
            switch (this) {
                case HEALTHY:
                    return new FertilizedFarmlandBlock.FarmlandHealthyTrait();
                case RICH:
                    return new FertilizedFarmlandBlock.FarmlandRichTrait();
                case STABLE:
                    return new FertilizedFarmlandBlock.FarmlandStableTrait();
            }

            return null;
        }

        @Nullable
        private static Block getBlockForTraits(List<FertilizedFarmlandBlock.FarmlandTrait> traits) {
            boolean hasStableTrait = traits.stream().anyMatch(it -> it instanceof FertilizedFarmlandBlock.FarmlandStableTrait);
            boolean hasHealthyTrait = traits.stream().anyMatch(it -> it instanceof FertilizedFarmlandBlock.FarmlandHealthyTrait);
            boolean hasRichTrait = traits.stream().anyMatch(it -> it instanceof FertilizedFarmlandBlock.FarmlandRichTrait);
            if (hasStableTrait && !hasRichTrait && !hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandStable;
            }

            if (!hasStableTrait && hasRichTrait && !hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandRich;
            }

            if (!hasStableTrait && !hasRichTrait && hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandHealthy;
            }

            if (hasStableTrait && hasRichTrait && !hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandRichStable;
            }

            if (hasStableTrait && !hasRichTrait && hasHealthyTrait) {
                return ModBlocks.fertilizedFarmlandHealthyStable;
            }

            return null;
        }
    }

    private final FertilizerType fertilizerType;

    public FertilizerItem(FertilizerType fertilizerType) {
        super(new Item.Properties().group(FarmingForBlockheads.itemGroup));
        this.fertilizerType = fertilizerType;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext useContext) {
        PlayerEntity player = useContext.getPlayer();
        if (player == null) {
            return ActionResultType.PASS;
        }

        World world = useContext.getWorld();
        BlockPos pos = useContext.getPos();
        ItemStack heldItem = useContext.getItem();
        BlockState state = world.getBlockState(pos);
        BlockState newState = fertilizerType.applyFertilizer(state);
        if (newState != state) {
            world.setBlockState(pos, newState);
            if (!player.abilities.isCreativeMode) {
                heldItem.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }

        return super.onItemUse(useContext);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TranslationTextComponent("tooltip.farmingforblockheads:fertilizer_" + fertilizerType.name().toLowerCase(Locale.ENGLISH)));
    }

}
