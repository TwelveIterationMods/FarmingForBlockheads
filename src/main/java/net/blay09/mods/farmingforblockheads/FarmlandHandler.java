package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.block.FertilizedFarmlandBlock;
import net.blay09.mods.farmingforblockheads.item.FertilizerItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
        BlockState plant = world.getBlockState(event.getPos());
        if (plant.getBlock() instanceof IGrowable) {
            IGrowable growable = (IGrowable) plant.getBlock();
            BlockState farmland = world.getBlockState(event.getPos().down());
            if (farmland.getBlock() instanceof FertilizedFarmlandBlock) {
                if (Math.random() <= ((FertilizedFarmlandBlock) farmland.getBlock()).getDoubleGrowthChance()) {
                    if (growable.canGrow(world, pos, plant, world.isRemote())) {
                        growable.grow(((ServerWorld) world), world.getRandom(), event.getPos(), plant);
                        world.playEvent(2005, pos, 0);
                        rollRegression(world, pos, farmland);
                    }
                }
            }
        }
    }

    public static void rollRegression(World world, BlockPos pos, BlockState farmland) {
        if (farmland.getBlock() instanceof FertilizedFarmlandBlock) {
            if (Math.random() <= ((FertilizedFarmlandBlock) farmland.getBlock()).getRegressionChance()) {
                world.setBlockState(pos, Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, farmland.get(FarmlandBlock.MOISTURE)));
            }
        }
    }
}
