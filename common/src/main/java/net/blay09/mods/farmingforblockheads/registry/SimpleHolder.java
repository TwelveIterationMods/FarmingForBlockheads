package net.blay09.mods.farmingforblockheads.registry;

import net.minecraft.resources.ResourceLocation;

public record SimpleHolder<T>(ResourceLocation id, T value) {
}
