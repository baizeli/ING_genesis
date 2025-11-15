package com.baizeli.eternisstarrysky.effect.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.effect.*;

public class StellarSoulControlEffect extends MobEffect {
    public StellarSoulControlEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".stellar_soul_control";
    }
}