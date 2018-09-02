package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.BlockFertilizedFarmland;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
        setUnlocalizedName(registryName.toString());
        setHasSubtypes(true);
        setCreativeTab(FarmingForBlockheads.creativeTab);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        FertilizerType fertilizerType = FertilizerType.values[MathHelper.clamp(itemStack.getItemDamage(), 0, FertilizerType.values.length)];
        return "item." + registryName.toString() + "_" + fertilizerType.name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (FertilizerType fertilizerType : FertilizerType.values) {
                items.add(new ItemStack(this, 1, fertilizerType.ordinal()));
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        ItemStack heldItem = player.getHeldItem(hand);
        FertilizerType fertilizerType = FertilizerType.values[MathHelper.clamp(heldItem.getItemDamage(), 0, FertilizerType.values.length)];
        if (state.getBlock() == Blocks.FARMLAND) {
            int moisture = state.getValue(BlockFarmland.MOISTURE);
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

            world.setBlockState(pos, newState.withProperty(BlockFarmland.MOISTURE, moisture));
            if (!player.capabilities.isCreativeMode) {
                heldItem.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        } else if (state.getBlock() instanceof BlockFertilizedFarmland) {
            IBlockState newState = ((BlockFertilizedFarmland) state.getBlock()).applyFertilizer(state, fertilizerType);
            if (newState != state) {
                int moisture = state.getValue(BlockFarmland.MOISTURE);
                world.setBlockState(pos, newState.withProperty(BlockFarmland.MOISTURE, moisture));
                if (!player.capabilities.isCreativeMode) {
                    heldItem.shrink(1);
                }

                return EnumActionResult.SUCCESS;
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        FertilizerType fertilizerType = FertilizerType.values[MathHelper.clamp(itemStack.getItemDamage(), 0, FertilizerType.values.length)];
        tooltip.add(I18n.format("tooltip.farmingforblockheads:fertilizer_" + fertilizerType.name().toLowerCase(Locale.ENGLISH)));
    }
}
