package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.BlockFertilizedFarmland;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemFertilizer extends Item {

    public enum FertilizerType {
        HEALTHY,
        RICH,
        STABLE;

        public IBlockState applyFertilizer(IBlockState state) {
            int moisture = state.get(BlockStateProperties.MOISTURE_0_7);
            Block sourceBlock = state.getBlock();
            List<BlockFertilizedFarmland.FarmlandTrait> traits = new ArrayList<>();
            traits.add(getFarmlandTrait());
            if (sourceBlock instanceof BlockFertilizedFarmland) {
                traits.addAll(((BlockFertilizedFarmland) sourceBlock).getTraits());
            }

            Block targetBlock = getBlockForTraits(traits);
            if (targetBlock == null) {
                return state;
            }

            IBlockState newState = targetBlock.getDefaultState();
            return newState.with(BlockFertilizedFarmland.MOISTURE, moisture);
        }

        private BlockFertilizedFarmland.FarmlandTrait getFarmlandTrait() {
            switch (this) {
                case HEALTHY:
                    return new BlockFertilizedFarmland.FarmlandHealthyTrait();
                case RICH:
                    return new BlockFertilizedFarmland.FarmlandRichTrait();
                case STABLE:
                    return new BlockFertilizedFarmland.FarmlandStableTrait();
            }

            return null;
        }

        @Nullable
        private static Block getBlockForTraits(List<BlockFertilizedFarmland.FarmlandTrait> traits) {
            boolean hasStableTrait = traits.stream().anyMatch(it -> it instanceof BlockFertilizedFarmland.FarmlandStableTrait);
            boolean hasHealthyTrait = traits.stream().anyMatch(it -> it instanceof BlockFertilizedFarmland.FarmlandHealthyTrait);
            boolean hasRichTrait = traits.stream().anyMatch(it -> it instanceof BlockFertilizedFarmland.FarmlandRichTrait);
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

    public ItemFertilizer(FertilizerType fertilizerType) {
        super(new Item.Properties().group(FarmingForBlockheads.itemGroup));
        this.fertilizerType = fertilizerType;
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext useContext) {
        EntityPlayer player = useContext.getPlayer();
        if (player == null) {
            return EnumActionResult.PASS;
        }

        World world = useContext.getWorld();
        BlockPos pos = useContext.getPos();
        ItemStack heldItem = useContext.getItem();
        IBlockState state = world.getBlockState(pos);
        IBlockState newState = fertilizerType.applyFertilizer(state);
        if (newState != state) {
            world.setBlockState(pos, newState);
            if (!player.abilities.isCreativeMode) {
                heldItem.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(useContext);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TextComponentTranslation("tooltip.farmingforblockheads:fertilizer_" + fertilizerType.name().toLowerCase(Locale.ENGLISH)));
    }

}
