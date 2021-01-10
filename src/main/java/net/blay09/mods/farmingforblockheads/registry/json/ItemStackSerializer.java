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
        if (json.isJsonPrimitive()) {
            Item item = JSONUtils.getItem(json, "item");
            return new ItemStack(item);
        } else {
            JsonObject jsonObject = json.getAsJsonObject();
            Item item = JSONUtils.getItem(jsonObject, "item");
            int count = JSONUtils.getInt(jsonObject, "count", 1);
            ItemStack itemStack = new ItemStack(item, count);

            // Allow JSON to be specified as either String or Object. String JSON can be used to support Mojang's special JSON format with type identifiers.
            String jsonString = null;
            if (JSONUtils.isString(jsonObject, "nbt")) {
                jsonString = JSONUtils.getString(jsonObject, "nbt");
            } else {
                JsonObject nbtJson = JSONUtils.getJsonObject(jsonObject, "nbt", new JsonObject());
                if (nbtJson.size() > 0) {
                    jsonString = nbtJson.toString();
                }
            }
            if (jsonString != null) {
                try {
                    CompoundNBT tagFromJson = JsonToNBT.getTagFromJson(jsonString);
                    itemStack.setTag(tagFromJson);
                } catch (CommandSyntaxException e) {
                    FarmingForBlockheads.logger.error("Failed to parse nbt data for item stack {}x {}: ", item, count, e);
                }
            }

            return itemStack;
        }
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        throw new UnsupportedOperationException("Serialization of ItemStack to JSON is not implemented");
    }
}
