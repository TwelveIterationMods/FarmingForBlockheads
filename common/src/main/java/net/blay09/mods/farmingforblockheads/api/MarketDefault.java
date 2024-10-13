package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public interface MarketDefault {
	Optional<ResourceLocation> category();
	Optional<Payment> payment();
}
