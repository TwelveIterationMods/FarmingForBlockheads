package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketRegistryDefaultHandler;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryReloadEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMCHandler {

    private record ImcMarketCategoryRegistration(
            ResourceLocation registryName,
            String tooltipLangKey,
            ItemStack icon,
            int sortIndex
    ) {
    }

    private record ImcMarketEntryRegistration(
            ItemStack outputItem, ItemStack costItem, ResourceLocation categoryId) {
    }

    public static class ImcRegistrations implements IMarketRegistryDefaultHandler {

        private final String id;
        private final List<ImcMarketCategoryRegistration> categories = new ArrayList<>();
        private final List<ImcMarketEntryRegistration> entries = new ArrayList<>();

        public ImcRegistrations(String id) {
            this.id = id;
        }

        @Override
        public void register(@Nullable ItemStack overridePayment, @Nullable Integer overrideCount) {
            categories.forEach(registration -> FarmingForBlockheadsAPI.registerMarketCategory(registration.registryName,
                    registration.tooltipLangKey,
                    registration.icon,
                    registration.sortIndex));
            entries.forEach(registration -> {
                IMarketCategory category = FarmingForBlockheadsAPI.getMarketCategory(registration.categoryId);
                if (category != null) {
                    ItemStack effectiveOutputItem = overrideCount != null ? new ItemStack(registration.outputItem.getItem(), overrideCount) : registration.outputItem;
                    ItemStack effectiveCostItem = overridePayment != null ? overridePayment : registration.costItem;
                    FarmingForBlockheadsAPI.registerMarketEntry(effectiveOutputItem, effectiveCostItem, category);
                } else {
                    FarmingForBlockheads.logger.error("IMC API Error: Market category {} does not exist (from {})", registration.categoryId, id);
                }
            });
        }

        @Override
        public boolean isEnabledByDefault() {
            return true;
        }

        public void addCategory(ImcMarketCategoryRegistration registration) {
            categories.add(registration);
        }

        public void addEntry(ImcMarketEntryRegistration registration) {
            entries.add(registration);
        }
    }

    private static final Map<String, ImcRegistrations> registrations = new HashMap<>();

    private static ImcRegistrations prepareImcRegistrations(String id) {
        // Register event listener if this is the first IMC handler
        if (registrations.isEmpty()) {
            Balm.getEvents().onEvent(MarketRegistryReloadEvent.Pre.class, event -> {
                registrations.values().forEach(it -> {
                    FarmingForBlockheadsAPI.registerMarketDefaultHandler(it.id, it);
                });
            });
        }
        return registrations.computeIfAbsent(id, s -> new ImcRegistrations(id));
    }

    public static void processIMC(InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            String sender = message.senderModId();
            Object obj = message.messageSupplier().get();
            switch (message.method()) {
                case "RegisterMarketCategory" -> {
                    if (obj instanceof CompoundTag tagCompound) {
                        ResourceLocation registryName = new ResourceLocation(tagCompound.getString("RegistryName"));
                        if (registryName.getNamespace().equals(sender)) {
                            String tooltipLangKey = tagCompound.contains("Tooltip",
                                    Tag.TAG_STRING) ? tagCompound.getString("Tooltip") : "gui.farmingforblockheads.market.tooltip_none";
                            ItemStack icon = ItemStack.of(tagCompound.getCompound("Icon"));
                            int sortIndex = tagCompound.getInt("SortIndex");

                            prepareImcRegistrations(sender + "_imc")
                                    .addCategory(new ImcMarketCategoryRegistration(registryName, tooltipLangKey, icon, sortIndex));
                        } else {
                            FarmingForBlockheads.logger.error("IMC API Error: Market category must be prefixed by your mod id (from {})", sender);
                        }
                    } else {
                        FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry expects NBT (from {})", sender);
                    }
                }
                case "RegisterMarketEntry" -> {
                    if (obj instanceof CompoundTag tagCompound) {
                        if (!tagCompound.contains("OutputItem", Tag.TAG_COMPOUND)) {
                            FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry requires OutputItem tag (from {})", sender);
                        } else if (!tagCompound.contains("CostItem", Tag.TAG_COMPOUND)) {
                            FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry requires CostItem tag (from {})", sender);
                        } else {
                            ItemStack outputItem = ItemStack.of(tagCompound.getCompound("OutputItem"));
                            ItemStack costItem = ItemStack.of(tagCompound.getCompound("CostItem"));
                            ResourceLocation categoryId = tagCompound.contains("Category", Tag.TAG_STRING) ? new ResourceLocation(tagCompound.getString(
                                    "Category")) : FarmingForBlockheadsAPI.MARKET_CATEGORY_OTHER;

                            prepareImcRegistrations(sender + "_imc").addEntry(new ImcMarketEntryRegistration(outputItem, costItem, categoryId));
                        }
                    } else {
                        FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry expects NBT (from {})", sender);
                    }
                }
            }
        });
    }

}
