package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

public class IMCHandler {

    public static void handleIMCMessage(InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            String sender = message.getModId(); // TODO sender
            Object obj = message.getMessageSupplier().get();
            switch (message.getMethod()) {
                case "RegisterMarketCategory":
                    if (obj instanceof CompoundNBT) {
                        CompoundNBT tagCompound = (CompoundNBT) obj;
                        ResourceLocation registryName = new ResourceLocation(tagCompound.getString("RegistryName"));
                        if (registryName.getNamespace().equals(sender)) {
                            String tooltipLangKey = tagCompound.contains("Tooltip", Constants.NBT.TAG_STRING) ? tagCompound.getString("Tooltip") : "gui.farmingforblockheads:market.tooltip_none";
                            ResourceLocation texturePath = new ResourceLocation(tagCompound.getString("Texture"));
                            int textureX = tagCompound.getInt("TextureX");
                            int textureY = tagCompound.getInt("TextureY");
                            int sortIndex = tagCompound.getInt("SortIndex");
                            FarmingForBlockheadsAPI.registerMarketCategory(registryName, tooltipLangKey, texturePath, textureX, textureY, sortIndex);
                        } else {
                            FarmingForBlockheads.logger.error("IMC API Error: Market category must be prefixed by your mod id (from {})", sender);
                        }
                    } else {
                        FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry expects NBT (from {})", sender);
                    }
                    break;
                case "RegisterMarketEntry":
                    if (obj instanceof CompoundNBT) {
                        CompoundNBT tagCompound = (CompoundNBT) obj;
                        if (!tagCompound.contains("OutputItem", Constants.NBT.TAG_COMPOUND)) {
                            FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry requires OutputItem tag (from {})", sender);
                        } else if (!tagCompound.contains("CostItem", Constants.NBT.TAG_COMPOUND)) {
                            FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry requires CostItem tag (from {})", sender);
                        } else {
                            ItemStack outputItem = ItemStack.read(tagCompound.getCompound("OutputItem"));
                            ItemStack costItem = ItemStack.read(tagCompound.getCompound("CostItem"));
                            ResourceLocation categoryId = tagCompound.contains("Category", Constants.NBT.TAG_STRING) ? new ResourceLocation(tagCompound.getString("Category")) : FarmingForBlockheadsAPI.MARKET_CATEGORY_OTHER;
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
