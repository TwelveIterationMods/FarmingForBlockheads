package net.blay09.mods.farmingforblockheads.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.farmingforblockheads.FarmlandHandler;
import net.blay09.mods.farmingforblockheads.block.FertilizedFarmlandBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class RichFarmlandLootModifier extends LootModifier {

    public RichFarmlandLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        World world = context.getWorld();
        BlockPos pos = context.get(LootParameters.POSITION);
        BlockState plant = context.get(LootParameters.BLOCK_STATE);
        if (pos == null || plant == null) {
            return generatedLoot;
        }

        BlockState farmland = world.getBlockState(pos.down());
        if (farmland.getBlock() instanceof FertilizedFarmlandBlock && plant.getBlock() instanceof IGrowable) {
            if (Math.random() <= ((FertilizedFarmlandBlock) farmland.getBlock()).getBonusCropChance()) {
                generatedLoot.stream().filter(p -> !isProbablySeed(p)).findAny().ifPresent(c -> {
                    generatedLoot.add(c.copy());
                    world.playEvent(2005, pos, 0);
                    FarmlandHandler.rollRegression(world, pos, farmland);
                });
            }
        }

        return generatedLoot;
    }


    private static boolean isProbablySeed(ItemStack itemStack) {
        ResourceLocation registryName = itemStack.getItem().getRegistryName();
        return registryName != null && registryName.getPath().contains("seed");
    }

    public static class Serializer extends GlobalLootModifierSerializer<RichFarmlandLootModifier> {
        @Override
        public RichFarmlandLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new RichFarmlandLootModifier(conditionsIn);
        }
    }
}
