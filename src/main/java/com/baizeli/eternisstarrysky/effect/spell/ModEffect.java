package com.baizeli.eternisstarrysky.effect.spell;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.celestial_source.FateWedgeEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModEffect {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = 
        DeferredRegister.create(Registries.MOB_EFFECT, EternisStarrySky.MOD_ID);
        
    public static final RegistryObject<MobEffect> FATE_WEDGE = 
        MOB_EFFECTS.register("fate_wedge", FateWedgeEffect::new);
        
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}