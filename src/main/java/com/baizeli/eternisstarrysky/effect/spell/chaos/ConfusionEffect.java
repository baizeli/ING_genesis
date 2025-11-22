package com.baizeli.eternisstarrysky.effect.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.effect.*;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class ConfusionEffect extends MobEffect {
    public ConfusionEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0000);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".confusion";
    }
}