package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.MarketDefault;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record MarketDefaultImpl(Optional<ResourceLocation> category, Optional<Payment> payment) implements MarketDefault {
}
