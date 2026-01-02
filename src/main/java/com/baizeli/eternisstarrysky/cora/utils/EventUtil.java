package com.baizeli.eternisstarrysky.cora.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;

public class EventUtil {
    public static final Set<UUID> deadList = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static float getHealth(Entity entity) {
        if (deadList.contains(entity.uuid)) {
            entity.getEntityData().set(DATA_HEALTH_ID, 0F);
            return 0;
        }

        if (entity instanceof LivingEntity) {
            return entity.getEntityData().get(DATA_HEALTH_ID);
        }

        return 0;
    }

    public static float getMaxHealth(Entity entity) {
        if (deadList.contains(entity.uuid)) {
            entity.getEntityData().set(DATA_HEALTH_ID, 0F);
            return 0;
        }

        if (entity instanceof LivingEntity living) {
            return (float) living.getAttributeValue(Attributes.MAX_HEALTH);
        }

        return 0;
    }
}
