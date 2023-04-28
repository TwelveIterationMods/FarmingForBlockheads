package net.blay09.mods.farmingforblockheads.compat;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;
import net.blay09.mods.farmingforblockheads.client.gui.screen.MarketScreen;

public class EmiIntegration implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addExclusionArea(MarketScreen.class, (screen, consumer) ->
                screen.getFilterButtons().forEach(b ->
                        consumer.accept(new Bounds(b.getX(), b.getY(), b.getWidth(), b.getHeight()))));
    }
}
