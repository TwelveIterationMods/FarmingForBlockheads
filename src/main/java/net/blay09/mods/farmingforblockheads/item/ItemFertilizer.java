package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.BlockFertilizedFarmland;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemFertilizer extends Item {

    public enum FertilizerType {
        HEALTHY,
        RICH,
        STABLE;

        private static FertilizerType[] values = values();
    }

    public static final String name = "fertilizer";
    public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);

    public ItemFertilizer() {
        super(new Item.Properties().group(FarmingForBlockheads.itemGroup));
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
        FertilizerType fertilizerType = FertilizerType.values[MathHelper.clamp(heldItem.getItemDamage(), 0, FertilizerType.values.length)];
        if (state.getBlock() == Blocks.FARMLAND) {
            int moisture = state.get(BlockFarmland.MOISTURE);
            IBlockState newState = state;
            switch (fertilizerType) {
                case HEALTHY:
                    newState = ModBlocks.fertilizedFarmlandHealthy.getDefaultState();
                    break;
                case RICH:
                    newState = ModBlocks.fertilizedFarmlandRich.getDefaultState();
                    break;
                case STABLE:
                    newState = ModBlocks.fertilizedFarmlandStable.getDefaultState();
                    break;
            }

            world.setBlockState(pos, newState.with(BlockFarmland.MOISTURE, moisture));
            if (!player.abilities.isCreativeMode) {
                heldItem.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        } else if (state.getBlock() instanceof BlockFertilizedFarmland) {
            IBlockState newState = ((BlockFertilizedFarmland) state.getBlock()).applyFertilizer(state, fertilizerType);
            if (newState != state) {
                int moisture = state.get(BlockFarmland.MOISTURE);
                world.setBlockState(pos, newState.with(BlockFarmland.MOISTURE, moisture));
                if (!player.abilities.isCreativeMode) {
                    heldItem.shrink(1);
                }

                return EnumActionResult.SUCCESS;
            }
        }

        return super.onItemUse(useContext);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        FertilizerType fertilizerType = FertilizerType.values[MathHelper.clamp(itemStack.getItemDamage(), 0, FertilizerType.values.length)];
        tooltip.add(new TextComponentTranslation("tooltip.farmingforblockheads:fertilizer_" + fertilizerType.name().toLowerCase(Locale.ENGLISH)));
    }

}
