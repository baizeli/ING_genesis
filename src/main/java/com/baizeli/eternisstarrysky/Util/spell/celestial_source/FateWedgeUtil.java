package com.baizeli.eternisstarrysky.util.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class FateWedgeUtil {
    private static final Map<LivingEntity, LivingEntity> targetToCasterMap = new HashMap<>();
    private static final Map<LivingEntity, Map<LivingEntity, Float>> initialHealthMap = new HashMap<>();

    public static void setInitialHealth(LivingEntity caster, LivingEntity target) {
        targetToCasterMap.put(target, caster);
        initialHealthMap.computeIfAbsent(caster, k -> new HashMap<>()).put(target, target.getHealth());
    }

    public static int getDamageBonus(LivingEntity caster, LivingEntity target) {
        if (initialHealthMap.containsKey(caster) &&
            initialHealthMap.get(caster).containsKey(target)) {

            float initialHealth = initialHealthMap.get(caster).get(target);
            float currentHealth = target.getHealth();
            float maxHealth = target.getMaxHealth();

            float lostHealth = initialHealth - currentHealth;
            float lostPercentage = (lostHealth / maxHealth) * 100.0f;

            return Math.min((int) lostPercentage, 50);
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

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity deadEntity = event.getEntity();

        if (targetToCasterMap.containsKey(deadEntity)) {
            LivingEntity caster = targetToCasterMap.get(deadEntity);

            if (caster instanceof Player && !caster.isDeadOrDying()) {
                caster.removeEffect(ModEffect.FATE_WEDGE.get());
            }

            targetToCasterMap.remove(deadEntity);
            for (Map<LivingEntity, Float> targetMap : initialHealthMap.values()) {
                targetMap.remove(deadEntity);
            }
        }

        List<LivingEntity> targetsToRemove = new ArrayList<>();
        for (Map.Entry<LivingEntity, LivingEntity> entry : targetToCasterMap.entrySet()) {
            LivingEntity caster = entry.getValue();
            if (caster.equals(deadEntity)) {
                targetsToRemove.add(entry.getKey());
            }
        }

        for (LivingEntity target : targetsToRemove) {
            if (target != null && !target.level().isClientSide()) {
                target.removeEffect(MobEffects.GLOWING);
            }
            targetToCasterMap.remove(target);
        }

        if (initialHealthMap.containsKey(deadEntity)) {
            initialHealthMap.remove(deadEntity);
        }
    }
}