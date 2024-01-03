package net.blay09.mods.farmingforblockheads.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.blay09.mods.farmingforblockheads.registry.PaymentImpl;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class MarketRecipe implements Recipe<Container> {

    private final ResourceLocation preset;
    private final ResourceLocation category;
    private final ItemStack resultItem;
    private final Payment payment;

    public MarketRecipe(ItemStack resultItem, ResourceLocation category, ResourceLocation preset, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Payment> payment) {
        this.preset = preset;
        this.category = category;
        this.resultItem = resultItem;
        this.payment = payment.orElse(null);
    }

    private PaymentImpl getDefaultPayment(ResourceLocation preset) {
        return new PaymentImpl(Ingredient.of(Items.EMERALD), 1); // TODO make configurable per preset
    }

    @Override
    public boolean matches(Container container, Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return resultItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return resultItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.marketRecipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.marketRecipeType;
    }

    public ResourceLocation getPreset() {
        return preset;
    }

    public ResourceLocation getCategory() {
        return category;
    }

    public Payment getPaymentOrDefault() {
        return payment != null ? payment : getDefaultPayment(preset);
    }

    static class Serializer implements RecipeSerializer<MarketRecipe> {

        private static final Codec<ItemStack> RESULT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item")
                        .orElse(BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                        .forGetter(ItemStack::getItemHolder),
                ExtraCodecs.strictOptionalField(ExtraCodecs.POSITIVE_INT, "count", 1).forGetter(ItemStack::getCount),
                CompoundTag.CODEC.optionalFieldOf("tag").forGetter((itemStack) -> Optional.ofNullable(itemStack.getTag()))
        ).apply(instance, ItemStack::new));

        private static final Codec<MarketRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.resultItem),
                ResourceLocation.CODEC.fieldOf("category").forGetter(recipe -> recipe.category),
                ResourceLocation.CODEC.fieldOf("preset").forGetter(recipe -> recipe.preset),
                PaymentImpl.CODEC.optionalFieldOf("payment").forGetter(recipe -> Optional.ofNullable(recipe.payment))
        ).apply(instance, MarketRecipe::new));

        @Override
        public Codec<MarketRecipe> codec() {
            return CODEC;
        }

        @Override
        public MarketRecipe fromNetwork(FriendlyByteBuf buf) {
            final var resultItem = buf.readItem();
            final var category = buf.readResourceLocation();
            final var preset = buf.readResourceLocation();
            final var payment = PaymentImpl.fromNetwork(buf);
            return new MarketRecipe(resultItem, category, preset, Optional.of(payment));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MarketRecipe recipe) {
            buf.writeItem(recipe.resultItem);
            buf.writeResourceLocation(recipe.category);
            buf.writeResourceLocation(recipe.preset);
            PaymentImpl.toNetwork(buf, recipe.getPaymentOrDefault());
        }
    }

}
