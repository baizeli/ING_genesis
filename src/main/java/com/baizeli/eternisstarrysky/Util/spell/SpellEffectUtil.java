package com.baizeli.eternisstarrysky.Util.spell;

import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import net.minecraft.world.entity.LivingEntity;

public class SpellEffectUtil {
    public static boolean isAffectedByChaosEffects(LivingEntity entity) {
        return 
            entity.getEffect(ModEffect.BLOOD_FRENZY.get()) != null || 
            entity.getEffect(ModEffect.BLOOD_WAR.get()) != null || 
            entity.getEffect(ModEffect.CONFUSION.get()) != null || 
            entity.getEffect(ModEffect.SIPHON.get()) != null;
    }
}