package net.blay09.mods.farmingforblockheads.loot;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.IForgeRegistry;

public class ModLootModifiers {
    public static void register(IForgeRegistry<GlobalLootModifierSerializer<?>> registry) {
        registry.register(
                new RichFarmlandLootModifier.Serializer().setRegistryName(new ResourceLocation(FarmingForBlockheads.MOD_ID, "rich_farmland"))
        );
    }
}
