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
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFertilizer extends Item {

    private static final int DAMAGE_STABLE = 2;

    public static final String name = "fertilizer";
    public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);

    public ItemFertilizer() {
        setUnlocalizedName(registryName.toString());
        setHasSubtypes(true);
        setCreativeTab(FarmingForBlockheads.creativeTab);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        switch (itemStack.getItemDamage()) {
            case 0:
                return "item." + registryName.toString() + "_healthy";
            case 1:
                return "item." + registryName.toString() + "_rich";
            case DAMAGE_STABLE:
                return "item." + registryName.toString() + "_stable";
        }

        return super.getUnlocalizedName(itemStack);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
            items.add(new ItemStack(this, 1, DAMAGE_STABLE));
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        ItemStack heldItem = player.getHeldItem(hand);
        if (state.getBlock() == Blocks.FARMLAND) {
            int moisture = state.getValue(BlockFarmland.MOISTURE);
            IBlockState newState = state;
            switch (heldItem.getItemDamage()) {
                case 0:
                    newState = ModBlocks.fertilizedFarmlandHealthy.getDefaultState();
                    break;
                case 1:
                    newState = ModBlocks.fertilizedFarmlandRich.getDefaultState();
                    break;
                case DAMAGE_STABLE:
                    newState = ModBlocks.fertilizedFarmlandStable.getDefaultState();
                    break;
            }

            world.setBlockState(pos, newState.withProperty(BlockFarmland.MOISTURE, moisture));
            if (!player.capabilities.isCreativeMode) {
                heldItem.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        } else if (state.getBlock() instanceof BlockFertilizedFarmland && heldItem.getItemDamage() == DAMAGE_STABLE) {
            IBlockState newState = ((BlockFertilizedFarmland) state.getBlock()).getStableBlockState(state);
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
        switch (itemStack.getItemDamage()) {
            case 0:
                tooltip.add(I18n.format("tooltip.farmingforblockheads:fertilizer_healthy"));
                break;
            case 1:
                tooltip.add(I18n.format("tooltip.farmingforblockheads:fertilizer_rich"));
                break;
            case 2:
                tooltip.add(I18n.format("tooltip.farmingforblockheads:fertilizer_stable"));
                break;
        }
    }
}
