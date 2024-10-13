package net.blay09.mods.farmingforblockheads.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.farmingforblockheads.api.MarketPreset;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.blay09.mods.farmingforblockheads.registry.PaymentImpl;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class MarketRecipe implements Recipe<RecipeInput> {

    private final String defaults;
    private final ResourceLocation category;
    private final ItemStack resultItem;
    private final Payment payment;

    public MarketRecipe(ItemStack resultItem, String defaults, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<ResourceLocation> category, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Payment> payment) {
        this.defaults = defaults;
        this.category = category.orElse(null);
        this.resultItem = resultItem;
        this.payment = payment.orElse(null);
    }

    @Override
    public boolean matches(RecipeInput container, Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput container, HolderLookup.Provider provider) {
        return resultItem.copy();
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return ModRecipes.marketRecipeSerializer;
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return ModRecipes.marketRecipeType;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CAMPFIRE;
    }

    public Optional<Payment> getPayment() {
        return Optional.ofNullable(payment);
    }

    public Optional<ResourceLocation> getCategory() {
        return Optional.ofNullable(category);
    }

    static class Serializer implements RecipeSerializer<MarketRecipe> {

        private static final MapCodec<ItemStack> RESULT_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item")
                        .orElse(BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                        .forGetter(ItemStack::getItemHolder),
                ExtraCodecs.POSITIVE_INT.fieldOf("count").orElse(1).forGetter(ItemStack::getCount),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStack::getComponentsPatch)
        ).apply(instance, ItemStack::new));

        private static final MapCodec<MarketRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.resultItem),
                ExtraCodecs.NON_EMPTY_STRING.fieldOf("defaults").forGetter(recipe -> recipe.defaults),
                ResourceLocation.CODEC.optionalFieldOf("category").forGetter(MarketRecipe::getCategory),
                PaymentImpl.CODEC.optionalFieldOf("payment").forGetter(MarketRecipe::getPayment)
        ).apply(instance, MarketRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, MarketRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<MarketRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MarketRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static MarketRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            final var resultItem = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            final var defaults = buf.readUtf();
            final var category = buf.readResourceLocation();
            final var payment = PaymentImpl.fromNetwork(buf);
            return new MarketRecipe(resultItem, defaults, Optional.of(category), Optional.of(payment));
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, MarketRecipe recipe) {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.resultItem);
            buf.writeUtf(recipe.defaults);
            buf.writeResourceLocation(recipe.category); // TODO resolve default
            PaymentImpl.toNetwork(buf, recipe.payment); // TODO resolve default
        }
    }

}
