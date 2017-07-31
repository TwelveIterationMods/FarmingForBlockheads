package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class IMCHandler {

	public static void handleIMCMessage(FMLInterModComms.IMCEvent event) {
		for(FMLInterModComms.IMCMessage message : event.getMessages()) {
			switch(message.key) {
				case "RegisterMarketCategory":
					if(message.getMessageType() == NBTTagCompound.class) {
						NBTTagCompound tagCompound = message.getNBTValue();
						ResourceLocation registryName = new ResourceLocation(tagCompound.getString("RegistryName"));
						if(registryName.getResourceDomain().equals(message.getSender())) {
							String tooltipLangKey = tagCompound.hasKey("Tooltip", Constants.NBT.TAG_STRING) ? tagCompound.getString("Tooltip") : "gui.farmingforblockheads:market.tooltip_none";
							ResourceLocation texturePath = new ResourceLocation(tagCompound.getString("Texture"));
							int textureX = tagCompound.getInteger("TextureX");
							int textureY = tagCompound.getInteger("TextureY");
							FarmingForBlockheadsAPI.registerMarketCategory(registryName, tooltipLangKey, texturePath, textureX, textureY);
						} else {
							FarmingForBlockheads.logger.error("IMC API Error: Market category must be prefixed by your mod id (from {})", message.getSender());
						}
					} else {
						FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketCategory expects NBT (from {})", message.getSender());
					}
					break;
				case "RegisterMarketEntry":
					if(message.getMessageType() == NBTTagCompound.class) {
						NBTTagCompound tagCompound = message.getNBTValue();
						if(!tagCompound.hasKey("OutputItem", Constants.NBT.TAG_COMPOUND)) {
							FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry requires OutputItem tag (from {})", message.getSender());
						} else if(!tagCompound.hasKey("CostItem", Constants.NBT.TAG_COMPOUND)) {
							FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry requires CostItem tag (from {})", message.getSender());
						} else {
							ItemStack outputItem = new ItemStack(tagCompound.getCompoundTag("OutputItem"));
							ItemStack costItem = new ItemStack(tagCompound.getCompoundTag("CostItem"));
							ResourceLocation categoryId = tagCompound.hasKey("Category", Constants.NBT.TAG_STRING) ? new ResourceLocation(tagCompound.getString("Category")) : FarmingForBlockheadsAPI.MARKET_CATEGORY_OTHER;
							IMarketCategory category = FarmingForBlockheadsAPI.getMarketCategory(categoryId);
							if(category != null) {
								FarmingForBlockheadsAPI.registerMarketEntry(outputItem, costItem, category);
							} else {
								FarmingForBlockheads.logger.error("IMC API Error: Market category {} does not exist (from {})", categoryId, message.getSender());
							}
						}
					} else {
						FarmingForBlockheads.logger.error("IMC API Error: RegisterMarketEntry expects NBT (from {})", message.getSender());
					}
					break;
			}
		}
	}

}
