package com.baizeli.eternisstarrysky.Util.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class FateWedgeUtil {
    private static final Map<UUID, Map<UUID, FateWedgeData>> fateWedgeEffects = new HashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void applyFateWedgeEffect(LivingEntity caster, LivingEntity target, int baseDamageBonus, int durationSeconds) {
        fateWedgeEffects.computeIfAbsent(caster.getUUID(), k -> new HashMap<>())
                .put(target.getUUID(), new FateWedgeData(baseDamageBonus, durationSeconds, target.getHealth()));
        
        scheduler.schedule(() -> removeFateWedgeEffect(caster, target), durationSeconds, TimeUnit.SECONDS);
    }

    public static void removeFateWedgeEffect(LivingEntity caster, LivingEntity target) {
        if (fateWedgeEffects.containsKey(caster.getUUID())) {
            fateWedgeEffects.get(caster.getUUID()).remove(target.getUUID());
            if (fateWedgeEffects.get(caster.getUUID()).isEmpty()) {
                fateWedgeEffects.remove(caster.getUUID());
            }
        }
    }

    public static int getDamageBonus(LivingEntity caster, LivingEntity target) {
        if (fateWedgeEffects.containsKey(caster.getUUID()) && 
            fateWedgeEffects.get(caster.getUUID()).containsKey(target.getUUID())) {
            return fateWedgeEffects.get(caster.getUUID()).get(target.getUUID()).getCurrentBonus(target);
        }
        return 0;
    }
    
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            LivingEntity target = event.getEntity();

            int bonus = getDamageBonus(attacker, target);
            if (bonus > 0) {
                float originalDamage = event.getAmount();
                float bonusDamage = originalDamage * bonus / 100.0f;
                event.setAmount(originalDamage + bonusDamage);
            }
        }
    }

    private static class FateWedgeData {
        private final int baseBonus;
        private final int durationSeconds;
        private final float initialHealth;
        
        public FateWedgeData(int baseBonus, int durationSeconds, float initialHealth) {
            this.baseBonus = baseBonus;
            this.durationSeconds = durationSeconds;
            this.initialHealth = initialHealth;
        }
        
        public int getCurrentBonus(LivingEntity target) {
            float currentHealth = target.getHealth();
            float maxHealth = target.getMaxHealth();

            float lostHealth = initialHealth - currentHealth;
            float lostPercentage = (lostHealth / maxHealth) * 100.0f;

            int bonus = (int) lostPercentage;

            return Math.min(bonus, 50);
        }
    }
}