package net.blay09.mods.farmingforblockheads.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;

public record PaymentImpl(Ingredient ingredient, int count, Optional<Component> tooltip) implements Payment {
    public static final Codec<Payment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(Payment::ingredient),
            Codec.INT.fieldOf("count").orElse(1).forGetter(Payment::count),
            ComponentSerialization.CODEC.optionalFieldOf("tooltip").forGetter(Payment::tooltip)
    ).apply(instance, PaymentImpl::new));

    public static Payment fromNetwork(RegistryFriendlyByteBuf buf) {
        final var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        final var count = buf.readVarInt();
        final var tooltip = ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC.decode(buf);
        return new PaymentImpl(ingredient, count, tooltip);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buf, Payment payment) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, payment.ingredient());
        buf.writeVarInt(payment.count());
        ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC.encode(buf, payment.tooltip());
    }
}
