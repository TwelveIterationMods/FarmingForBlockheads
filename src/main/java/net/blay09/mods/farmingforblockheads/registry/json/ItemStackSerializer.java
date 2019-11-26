package net.blay09.mods.farmingforblockheads.registry.json;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) json;
        Item item = JSONUtils.getItem(jsonObject, "item");
        int count = JSONUtils.getInt(jsonObject, "count", 1);
        ItemStack itemStack = new ItemStack(item, count);
        JsonObject nbtJson = JSONUtils.getJsonObject(jsonObject, "nbt", new JsonObject());
        if (nbtJson.size() > 0) {
            try {
                CompoundNBT tagFromJson = JsonToNBT.getTagFromJson(jsonObject.toString());
                itemStack.setTag(tagFromJson);
            } catch (CommandSyntaxException e) {
                FarmingForBlockheads.logger.error("Failed to parse nbt data for itemstack {}x {}: ", item, count, e);
            }
        }

        return itemStack;
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        throw new UnsupportedOperationException("Serialization of ItemStack to JSON is not implemented");
    }
}
