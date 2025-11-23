package com.baizeli.eternisstarrysky.effect.spell;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.celestial_source.*;
import com.baizeli.eternisstarrysky.effect.spell.chaos.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class ModEffect {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = 
        DeferredRegister.create(Registries.MOB_EFFECT, EternisStarrySky.MOD_ID);
        
    public static final RegistryObject<MobEffect> FATE_WEDGE = 
        MOB_EFFECTS.register("fate_wedge", FateWedgeEffect::new);
        
    public static final RegistryObject<MobEffect> PERFECT_EVASION = 
        MOB_EFFECTS.register("perfect_evasion", PerfectEvasionEffect::new);
        
    public static final RegistryObject<MobEffect> I_FLY = 
        MOB_EFFECTS.register("i_fly", IFlyEffect::new);
        
    public static final RegistryObject<MobEffect> LIFE_AND_DEATH_REALM = 
        MOB_EFFECTS.register("life_and_death_realm", LifeAndDeathRealmEffect::new);
        
    public static final RegistryObject<MobEffect> STELLAR_SOUL_CONTROL = 
        MOB_EFFECTS.register("stellar_soul_control", StellarSoulControlEffect::new);
        
    public static final RegistryObject<MobEffect> UNPARALLELED = 
        MOB_EFFECTS.register("unparalleled", UnparalleledEffect::new);
        
    public static final RegistryObject<MobEffect> GLAZED_FLOWER_RAIN = 
        MOB_EFFECTS.register("glazed_flower_rain", GlazedFlowerRainEffect::new);
        
    public static final RegistryObject<MobEffect> BLOOD_WAR = 
        MOB_EFFECTS.register("blood_war", BloodWarEffect::new);
        
    public static final RegistryObject<MobEffect> SIPHON = 
        MOB_EFFECTS.register("siphon", SiphonEffect::new);
        
    public static final RegistryObject<MobEffect> BLOOD_FRENZY = 
        MOB_EFFECTS.register("blood_frenzy", BloodFrenzyEffect::new);

    public static final RegistryObject<MobEffect> CONFUSION = 
        MOB_EFFECTS.register("confusion", ConfusionEffect::new);
        
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}