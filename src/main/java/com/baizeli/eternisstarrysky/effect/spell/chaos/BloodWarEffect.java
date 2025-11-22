package com.baizeli.eternisstarrysky.effect.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BloodWarEffect extends MobEffect {
    public BloodWarEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".blood_war";
    }
}