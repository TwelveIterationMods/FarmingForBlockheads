package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.block.BlockFertilizedFarmland;
import net.blay09.mods.farmingforblockheads.item.ItemFertilizer;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FarmingForBlockheads.MOD_ID)
public class FarmlandHandler {

    @SubscribeEvent
    public static void onGrowEvent(BlockEvent.CropGrowEvent.Post event) {
        World world = (World) event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState plant = world.getBlockState(event.getPos());
        if (plant.getBlock() instanceof IGrowable) {
            IGrowable growable = (IGrowable) plant.getBlock();
            IBlockState farmland = world.getBlockState(event.getPos().down());
            if (farmland.getBlock() instanceof BlockFertilizedFarmland) {
                if (Math.random() <= ((BlockFertilizedFarmland) farmland.getBlock()).getDoubleGrowthChance()) {
                    if (growable.canGrow(world, pos, plant, world.isRemote())) {
                        growable.grow(world, world.getRandom(), event.getPos(), plant);
                        world.playEvent(2005, pos, 0);
                        rollRegression(world, pos, farmland);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHarvest(BlockEvent.HarvestDropsEvent event) {
        World world = (World) event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState plant = event.getState();
        IBlockState farmland = event.getWorld().getBlockState(event.getPos().down());
        if (farmland.getBlock() instanceof BlockFertilizedFarmland && plant.getBlock() instanceof IGrowable) {
            if (Math.random() <= ((BlockFertilizedFarmland) farmland.getBlock()).getBonusCropChance()) {
                event.getDrops().stream().filter(p -> !isProbablySeed(p)).findAny().ifPresent(c -> {
                    event.getDrops().add(c.copy());
                    world.playEvent(2005, pos, 0);
                    rollRegression(world, pos, farmland);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onFertilize(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ItemFertilizer) {
            IBlockState farmland = event.getWorld().getBlockState(event.getPos().down());
            if (farmland.getBlock() == Blocks.FARMLAND) {
                ItemUseContext useContext = new ItemUseContext(event.getEntityPlayer(), event.getItemStack(), event.getPos().down(), EnumFacing.UP, (float) event.getHitVec().x, (float) event.getHitVec().y, (float) event.getHitVec().z);
                if (event.getItemStack().getItem().onItemUse(useContext) == EnumActionResult.SUCCESS) {
                    event.setCanceled(true);
                }
            }
        }
    }

    private static void rollRegression(World world, BlockPos pos, IBlockState farmland) {
        if (farmland.getBlock() instanceof BlockFertilizedFarmland) {
            if (Math.random() <= ((BlockFertilizedFarmland) farmland.getBlock()).getRegressionChance()) {
                world.setBlockState(pos, Blocks.FARMLAND.getDefaultState().with(BlockFarmland.MOISTURE, farmland.get(BlockFarmland.MOISTURE)));
            }
        }
    }

    private static boolean isProbablySeed(ItemStack itemStack) {
        ResourceLocation registryName = itemStack.getItem().getRegistryName();
        return registryName != null && registryName.getPath().contains("seed");
    }
}
