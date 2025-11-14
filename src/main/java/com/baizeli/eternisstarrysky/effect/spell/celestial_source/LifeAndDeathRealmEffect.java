package com.baizeli.eternisstarrysky.effect.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class LifeAndDeathRealmEffect extends MobEffect {
    public LifeAndDeathRealmEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".life_and_death_realm";
    }
}