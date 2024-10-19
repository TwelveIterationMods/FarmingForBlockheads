package net.blay09.mods.farmingforblockheads.mixin;

import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GhostSlots.class)
public interface GhostSlotsAccessor {
    @Invoker
    void callSetInput(Slot slot, ContextMap contextMap, SlotDisplay slotDisplay);

    @Invoker
    void callSetResult(Slot slot, ContextMap contextMap, SlotDisplay slotDisplay);
}
