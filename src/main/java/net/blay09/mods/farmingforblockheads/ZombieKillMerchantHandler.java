package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.entity.EntityMerchant;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ZombieKillMerchantHandler {
    @SubscribeEvent
    public void onZombieSpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityZombie) {
            EntityZombie entityZombie = (EntityZombie) event.getEntity();
            entityZombie.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(entityZombie, EntityMerchant.class, false));
        }
    }
}
