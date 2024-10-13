package net.blay09.mods.farmingforblockheads.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record SimpleHolder<T>(ResourceLocation id, T value) {
    public SimpleHolder(Map.Entry<ResourceLocation, T> entry) {
        this(entry.getKey(), entry.getValue());
    }
}
