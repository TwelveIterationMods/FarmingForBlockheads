package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.farmingforblockheads.client.gui.screen.MarketScreen;
import net.blay09.mods.farmingforblockheads.container.ModContainers;
import net.minecraft.client.gui.ScreenManager;

public class ModScreens {
    public static void register() {
        ScreenManager.registerFactory(ModContainers.market, MarketScreen::new);
    }
}
