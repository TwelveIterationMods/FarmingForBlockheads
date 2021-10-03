package net.blay09.mods.farmingforblockheads.loot;

import com.google.gson.JsonObject;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.farmingforblockheads.FarmlandHandler;
import net.blay09.mods.farmingforblockheads.block.FertilizedFarmlandBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class RichFarmlandLootModifier extends LootModifier {

    public RichFarmlandLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Level level = context.getLevel();
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        BlockState plant = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (origin == null || plant == null || !(plant.getBlock() instanceof IGrowable)) {
            return generatedLoot;
        }

        BlockPos pos = new BlockPos(origin);
        // Other mods might trigger loot tables during world gen, which results in a deadlock when reading the
        // block state below as the world in the context is the server world, not the world gen region
        ChunkPos chunkPos = new ChunkPos(pos);
        if (!level.getChunkSource().hasChunk(chunkPos.x, chunkPos.z)) {
            return generatedLoot;
        }

        BlockPos posBelow = pos.below();
        BlockState farmland = level.getBlockState(posBelow);
        if (farmland.getBlock() instanceof FertilizedFarmlandBlock) {
            if (Math.random() <= ((FertilizedFarmlandBlock) farmland.getBlock()).getBonusCropChance()) {
                generatedLoot.stream().filter(p -> !isProbablySeed(p)).findAny().ifPresent(c -> {
                    generatedLoot.add(c.copy());
                    level.levelEvent(2005, pos, 0);
                    FarmlandHandler.rollRegression(level, posBelow, farmland);
                });
            }
        }

        return generatedLoot;
    }


    private static boolean isProbablySeed(ItemStack itemStack) {
        ResourceLocation registryName = Balm.getRegistries().getKey(itemStack.getItem());
        return registryName != null && registryName.getPath().contains("seed");
    }

    public static class Serializer extends GlobalLootModifierSerializer<RichFarmlandLootModifier> {
        @Override
        public RichFarmlandLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            return new RichFarmlandLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(RichFarmlandLootModifier instance) {
            return new JsonObject();
        }
    }
}
