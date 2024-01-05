package net.blay09.mods.farmingforblockheads.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;

public record PaymentImpl(Ingredient ingredient, int count, Optional<Component> tooltip) implements Payment {
    public static final Codec<Payment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(Payment::ingredient),
            Codec.INT.fieldOf("count").forGetter(Payment::count),
            ComponentSerialization.CODEC.optionalFieldOf("tooltip").forGetter(Payment::tooltip)
    ).apply(instance, PaymentImpl::new));

    public static Payment fromNetwork(FriendlyByteBuf buf) {
        final var ingredient = Ingredient.fromNetwork(buf);
        final var count = buf.readVarInt();
        final var tooltip = Optional.ofNullable(buf.readBoolean() ? buf.readComponent() : null);
        return new PaymentImpl(ingredient, count, tooltip);
    }

    public static void toNetwork(FriendlyByteBuf buf, Payment payment) {
        payment.ingredient().toNetwork(buf);
        buf.writeVarInt(payment.count());
        buf.writeBoolean(payment.tooltip().isPresent());
        payment.tooltip().ifPresent(buf::writeComponent);
    }
}
