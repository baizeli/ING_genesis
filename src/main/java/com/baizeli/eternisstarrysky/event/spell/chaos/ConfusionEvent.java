package com.baizeli.eternisstarrysky.event.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.chaos.ConfusionEffect;
import net.minecraft.world.entity.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class ConfusionEvent {
    private static final Map<UUID, Long> aiDisabledEntities = new HashMap<>();
    
    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() != null && event.getEffectInstance().getEffect() instanceof ConfusionEffect) {
            LivingEntity entity = event.getEntity();
            if (!entity.level().isClientSide && !(entity instanceof Player) && entity instanceof Mob mob) {
                mob.setNoAi(true);

                aiDisabledEntities.put(entity.getUUID(), System.currentTimeMillis() + 5000);
            }
        }
    }
    
    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (!event.level.isClientSide() && event.phase == TickEvent.Phase.END) {
            long currentTime = System.currentTimeMillis();
            
            aiDisabledEntities.entrySet().removeIf(entry -> {
                UUID entityUUID = entry.getKey();
                Long restoreTime = entry.getValue();
                
                if (currentTime >= restoreTime) {
                    if (event.level instanceof ServerLevel serverLevel) {
                        Entity entity = serverLevel.getEntity(entityUUID);

                        if (entity instanceof Mob mob) {
                            mob.setNoAi(false);
                        }
                    }

                    return true;
                }

                return false;
            });
        }
    }
    
    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        aiDisabledEntities.remove(event.getEntity().getUUID());
    }
}