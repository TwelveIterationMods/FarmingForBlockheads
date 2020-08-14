package net.blay09.mods.farmingforblockheads.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.registries.IForgeRegistry;

public class ModEntities {
    public static EntityType<MerchantEntity> merchant;

    public static void register(IForgeRegistry<EntityType<?>> registry) {
        registry.register(merchant = registerEntity(EntityType.Builder.create(MerchantEntity::new, EntityClassification.MISC).size(0.6f, 1.95f), "merchant"));
        GlobalEntityTypeAttributes.put(merchant, MerchantEntity.createEntityAttributes().create());
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> registerEntity(EntityType.Builder<T> builder, String name) {
        return (EntityType<T>) builder.build(name).setRegistryName(name);
    }
}
