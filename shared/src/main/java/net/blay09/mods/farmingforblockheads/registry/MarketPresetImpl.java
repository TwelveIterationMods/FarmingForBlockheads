package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.MarketPreset;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.minecraft.resources.ResourceLocation;

public record MarketPresetImpl(ResourceLocation id, Payment payment) implements MarketPreset {
}
