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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
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
        Vector3d origin = context.get(LootParameters.field_237457_g_);
        BlockState plant = context.get(LootParameters.BLOCK_STATE);
        if (origin == null || plant == null || !(plant.getBlock() instanceof IGrowable)) {
            return generatedLoot;
        }

        BlockPos pos = new BlockPos(origin);
        BlockPos posBelow = pos.down();
        if (!world.getChunkProvider().isChunkLoaded(new ChunkPos(pos))) {
            return generatedLoot;
        }

        BlockState farmland = world.getBlockState(posBelow);
        if (farmland.getBlock() instanceof FertilizedFarmlandBlock) {
            if (Math.random() <= ((FertilizedFarmlandBlock) farmland.getBlock()).getBonusCropChance()) {
                generatedLoot.stream().filter(p -> !isProbablySeed(p)).findAny().ifPresent(c -> {
                    generatedLoot.add(c.copy());
                    world.playEvent(2005, pos, 0);
                    FarmlandHandler.rollRegression(world, posBelow, farmland);
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

        @Override
        public JsonObject write(RichFarmlandLootModifier instance) {
            return new JsonObject();
        }
    }
}
