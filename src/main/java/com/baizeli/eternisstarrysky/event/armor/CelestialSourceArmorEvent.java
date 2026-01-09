package com.baizeli.eternisstarrysky.event.armor;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.util.ArmorSetUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class CelestialSourceArmorEvent {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        if (!ArmorSetUtil.hasFullCelestialSourceSet(entity)) {
            return;
        }

        // 单次伤害不超过70%最大生命值
        float maxHealth = entity.getMaxHealth();
        float damage = event.getAmount();
        float maxDamage = maxHealth * 0.7f;
        
        if (damage > maxDamage) {
            event.setAmount(maxDamage);
        }
    }
/*
    @SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();

        if (!ArmorSetUtil.hasFullCelestialSourceSet(entity)) {
            return;
        }

        MobEffect effect = event.getEffectInstance().getEffect();

        // 免疫负面
        if (!effect.isBeneficial()) {
            event.setResult(Event.Result.DENY);
        }
    }*/
}