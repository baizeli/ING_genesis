package miku.united_as_one.genesis.util.spell;

import miku.united_as_one.genesis.EternisStarrySky;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public class SpellEffectUtil {
    public static final TagKey<MobEffect> CHAOS_EFFECT = TagKey.create(
        Registries.MOB_EFFECT, new ResourceLocation(EternisStarrySky.MOD_ID, "spell_effect/chaos")
    );
    
    public static boolean isAffectedByChaosEffect(LivingEntity entity) {
        return entity.getActiveEffects().stream()
            .anyMatch(
                effectInstance ->
                BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effectInstance.getEffect()).is(CHAOS_EFFECT)
            );
    }
}