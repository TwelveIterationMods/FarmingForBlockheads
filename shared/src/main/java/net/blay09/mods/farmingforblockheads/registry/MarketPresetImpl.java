package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.MarketPreset;
import net.blay09.mods.farmingforblockheads.api.Payment;

public record MarketPresetImpl(Payment payment, boolean optional) implements MarketPreset {
}
