package com.baizeli.eternisstarrysky.util.spell;

import net.minecraft.world.entity.*;
import net.minecraft.world.effect.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class SpellEffectUtil {
    public static final TagKey<MobEffect> CHAOS_EFFECT = TagKey.create(
        Registries.MOB_EFFECT, new ResourceLocation("iron_spells_genesis", "spell_effect/chaos")
    );
    
    public static boolean isAffectedByChaosEffect(LivingEntity entity) {
        return entity.getActiveEffects().stream()
            .anyMatch(
                effectInstance ->
                BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effectInstance.getEffect()).is(CHAOS_EFFECT)
            );
    }
}