package net.blay09.mods.farmingforblockheads.registry;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record SimpleHolder<T>(ResourceLocation id, T value) {
    public SimpleHolder(Map.Entry<ResourceLocation, T> entry) {
        this(entry.getKey(), entry.getValue());
    }

    public static <T> SimpleHolder<T> of(ResourceLocation id, @Nullable T marketCategory) {
        if (marketCategory == null) {
            return null;
        }
        return new SimpleHolder<>(id, marketCategory);
    }
}
