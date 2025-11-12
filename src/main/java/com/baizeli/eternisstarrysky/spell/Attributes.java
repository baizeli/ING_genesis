package com.baizeli.eternisstarrysky.spell;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import io.redspace.ironsspellbooks.api.attribute.MagicPercentAttribute;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class Attributes {
    private static final DeferredRegister<Attribute> ATTRIBUTES;
    public static final RegistryObject<Attribute> CHAOS_MAGIC_RESIST;
    public static final RegistryObject<Attribute> CHAOS_SPELL_POWER;
    public static final RegistryObject<Attribute> CELESTIAL_SOURCE_MAGIC_RESIST;
    public static final RegistryObject<Attribute> CELESTIAL_SOURCE_SPELL_POWER;
    public static final RegistryObject<Attribute> CULINARY_MAGIC_RESIST;
    public static final RegistryObject<Attribute> CULINARY_SPELL_POWER;

    static {
        ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, EternisStarrySky.MOD_ID);
        // 混沌法术强度/抗性
        CHAOS_MAGIC_RESIST = Attributes.newResistanceAttribute("chaos");
        CHAOS_SPELL_POWER = Attributes.newPowerAttribute("chaos");

        // 星源法术强度/抗性
        CELESTIAL_SOURCE_SPELL_POWER = Attributes.newPowerAttribute("celestial_source");
        CELESTIAL_SOURCE_MAGIC_RESIST = Attributes.newResistanceAttribute("celestial_source");
        
        // 美食法术强度/抗性
        CULINARY_SPELL_POWER = Attributes.newPowerAttribute("culinary");
        CULINARY_MAGIC_RESIST = Attributes.newResistanceAttribute("culinary");
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    private static RegistryObject<Attribute> newResistanceAttribute(String id) {
        return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicPercentAttribute("attribute." + EternisStarrySky.MOD_ID + "." + id + "_magic_resist", (double)1.0F, Double.MIN_VALUE, Double.MAX_VALUE)).setSyncable(true));
    }

    private static RegistryObject<Attribute> newPowerAttribute(String id) {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicPercentAttribute("attribute." + EternisStarrySky.MOD_ID + "." + id + "_spell_power", 1.0F, Double.MIN_VALUE, Double.MAX_VALUE)).setSyncable(true));
    }
}