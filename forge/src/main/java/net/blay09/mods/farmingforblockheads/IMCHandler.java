package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

public class IMCHandler {

    public static void processIMC(InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            String sender = message.senderModId();
            Object obj = message.messageSupplier().get();
            switch (message.method()) {
                case "RegisterMarketCategory":
                    if (obj instanceof CompoundTag tagCompound) {
                        ResourceLocation registryName = new ResourceLocation(tagCompound.getString("RegistryName"));
                        if (registryName.getNamespace().equals(sender)) {
                            String tooltipLangKey = tagCompound.contains("Tooltip", Tag.TAG_STRING) ? tagCompound.getString("Tooltip") : "gui.farmingforblockheads:market.tooltip_none";
                            ItemStack icon = ItemStack.of(tagCompound.getCompound("Icon"));
                            int sortIndex = tagCompound.getInt("SortIndex");
                            FarmingForBlockheadsAPI.registerMarketCategory(registryName, tooltipLangKey, icon, sortIndex);
                        } else {
                            FarmingForBlockheads.logger.error("IMC API Error: Market category must be prefixed by your mod id (from {})", sender);
                        }
                    } else {
                        FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry expects NBT (from {})", sender);
                    }
                    break;
                case "RegisterMarketEntry":
                    if (obj instanceof CompoundTag tagCompound) {
                        if (!tagCompound.contains("OutputItem", Tag.TAG_COMPOUND)) {
                            FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry requires OutputItem tag (from {})", sender);
                        } else if (!tagCompound.contains("CostItem", Tag.TAG_COMPOUND)) {
                            FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry requires CostItem tag (from {})", sender);
                        } else {
                            ItemStack outputItem = ItemStack.of(tagCompound.getCompound("OutputItem"));
                            ItemStack costItem = ItemStack.of(tagCompound.getCompound("CostItem"));
                            ResourceLocation categoryId = tagCompound.contains("Category", Tag.TAG_STRING) ? new ResourceLocation(tagCompound.getString("Category")) : FarmingForBlockheadsAPI.MARKET_CATEGORY_OTHER;
                            IMarketCategory category = FarmingForBlockheadsAPI.getMarketCategory(categoryId);
                            if (category != null) {
                                FarmingForBlockheadsAPI.registerMarketEntry(outputItem, costItem, category);
                            } else {
                                FarmingForBlockheads.logger.error("IMC API Error: Market category {} does not exist (from {})", categoryId, sender);
                            }
                        }
                    } else {
                        FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry expects NBT (from {})", sender);
                    }
                    break;
            }
        });
    }

}
