package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.BlockFertilizedFarmland;
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
import java.util.List;
import java.util.Locale;

public class ItemFertilizer extends Item {

    public enum FertilizerType {
        HEALTHY,
        RICH,
        STABLE;

        public IBlockState applyFertilizer(IBlockState state) {
            int moisture = state.get(BlockStateProperties.MOISTURE_0_7);
            IBlockState newState = state;
            // TODO
            return newState.with(BlockFertilizedFarmland.MOISTURE, moisture);
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
