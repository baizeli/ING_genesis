package com.baizeli.eternisstarrysky.effect.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class PerfectEvasionEffect extends MobEffect {
    public PerfectEvasionEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".perfect_evasion";
    }
}