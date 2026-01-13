package com.baizeli.eternisstarrysky.genesis_core.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;

public class EventUtil {
    public static final Set<UUID> deadList = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void setRemoved(Entity entity, Entity.RemovalReason reason) {
        if (entity.removalReason == null) {
            entity.removalReason = reason;
        }

        if (entity.removalReason.shouldDestroy()) {
            entity.stopRiding();
        }

        entity.getPassengers().forEach(Entity::stopRiding);
        entity.levelCallback.onRemove(reason);
    }

    public static void canUpdate(Entity entity, boolean value) {
        if (deadList.contains(entity.uuid)) {
            entity.canUpdate = false;
        } else {
            entity.canUpdate = value;
        }
    }

    public static boolean canUpdate(Entity entity) {
        return !deadList.contains(entity.uuid) && entity.canUpdate;
    }

    public static float getHealth(LivingEntity entity) {
        if (deadList.contains(entity.uuid)) {
            entity.getEntityData().set(DATA_HEALTH_ID, 0F);
            return 0;
        }

        return entity.getEntityData().get(DATA_HEALTH_ID);
    }

    public static void setHealth(LivingEntity entity, float health) {
        float finalHealth = deadList.contains(entity.uuid) ? 0F : Mth.clamp(health, 0.0F, entity.getMaxHealth());
        entity.entityData.set(DATA_HEALTH_ID, finalHealth);
    }

    public static boolean isDeadOrDying(LivingEntity entity) {
        return deadList.contains(entity.uuid) || entity.getHealth() <= 0.0F;
    }

    public static boolean isAlive(LivingEntity entity) {
        return !deadList.contains(entity.uuid) && !entity.isRemoved() && entity.getHealth() > 0.0F;
    }

    public static float getMaxHealth(LivingEntity entity) {
        return deadList.contains(entity.uuid) ? 0 : (float) entity.getAttributeValue(Attributes.MAX_HEALTH);
    }
}
