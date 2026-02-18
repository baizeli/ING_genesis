package miku.united_as_one.genesis.init.registry.spell;

import miku.united_as_one.genesis.Genesis;
import io.redspace.ironsspellbooks.api.attribute.MagicPercentAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class SpellAttributesRegistry {
    private static final DeferredRegister<Attribute> ATTRIBUTES;
    public static final RegistryObject<Attribute> CHAOS_MAGIC_RESIST;
    public static final RegistryObject<Attribute> CHAOS_SPELL_POWER;
    public static final RegistryObject<Attribute> CELESTIAL_SOURCE_MAGIC_RESIST;
    public static final RegistryObject<Attribute> CELESTIAL_SOURCE_SPELL_POWER;
    public static final RegistryObject<Attribute> CULINARY_MAGIC_RESIST;
    public static final RegistryObject<Attribute> CULINARY_SPELL_POWER;
    public static final RegistryObject<Attribute> FLAME_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> HOLY_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> FROST_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> SCARLET_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> ENDER_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> THUNDER_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> NATURE_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> WARLOCK_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> CHAOS_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> CELESTIAL_SOURCE_SPELL_PENETRATION;
    public static final RegistryObject<Attribute> MAX_MANA_PERCENT;
    public static final RegistryObject<Attribute> SPELL_DAMAGE_PERCENT;

    static {
        ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Genesis.MOD_ID);
        // 混沌法术强度/抗性
        CHAOS_MAGIC_RESIST = SpellAttributesRegistry.newResistanceAttribute("chaos");
        CHAOS_SPELL_POWER = SpellAttributesRegistry.newPowerAttribute("chaos");

        // 星源法术强度/抗性
        CELESTIAL_SOURCE_SPELL_POWER = SpellAttributesRegistry.newPowerAttribute("celestial_source");
        CELESTIAL_SOURCE_MAGIC_RESIST = SpellAttributesRegistry.newResistanceAttribute("celestial_source");
        
        // 美食法术强度/抗性
        CULINARY_SPELL_POWER = SpellAttributesRegistry.newPowerAttribute("culinary");
        CULINARY_MAGIC_RESIST = SpellAttributesRegistry.newResistanceAttribute("culinary");
        
        // 烈焰法术穿透
        FLAME_SPELL_PENETRATION = ATTRIBUTES.register("flame_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".flame_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));
        
        // 神圣法术穿透
        HOLY_SPELL_PENETRATION = ATTRIBUTES.register("holy_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".holy_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));
        
        // 冰霜法术穿透
        FROST_SPELL_PENETRATION = ATTRIBUTES.register("frost_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".frost_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));
        
        // 猩红法术穿透
        SCARLET_SPELL_PENETRATION = ATTRIBUTES.register("scarlet_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".scarlet_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));
        
        // 末影法术穿透
        ENDER_SPELL_PENETRATION = ATTRIBUTES.register("ender_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".ender_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));
        
        // 雷霆法术穿透
        THUNDER_SPELL_PENETRATION = ATTRIBUTES.register("thunder_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".thunder_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));
        
        // 自然法术穿透
        NATURE_SPELL_PENETRATION = ATTRIBUTES.register("nature_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".nature_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));
        
        // 邪术法术穿透
        WARLOCK_SPELL_PENETRATION = ATTRIBUTES.register("warlock_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".warlock_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));
        
        // 混沌法术穿透
        CHAOS_SPELL_PENETRATION = ATTRIBUTES.register("chaos_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".chaos_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));
        
        // 星源法术穿透
        CELESTIAL_SOURCE_SPELL_PENETRATION = ATTRIBUTES.register("celestial_source_spell_penetration", () -> (new MagicPercentAttribute(
            "attribute." + Genesis.MOD_ID + ".celestial_source_spell_penetration",
            1.0, 0, 1
        )).setSyncable(true));

        // 法术伤害和法力值
        MAX_MANA_PERCENT = ATTRIBUTES.register("max_mana_percent", () -> (new MagicPercentAttribute(
                "attribute." + Genesis.MOD_ID + ".max_mana_percent",
                1.0, -Double.MAX_VALUE, Double.MAX_VALUE
        )).setSyncable(true));
        SPELL_DAMAGE_PERCENT = ATTRIBUTES.register("spell_damage_percent", () -> (new MagicPercentAttribute(
                "attribute." + Genesis.MOD_ID + ".spell_damage_percent",
                1.0, -Double.MAX_VALUE, Double.MAX_VALUE
        )).setSyncable(true));

        // 法术伤害和法力值
        MAX_MANA_PERCENT = ATTRIBUTES.register("max_mana_percent", () -> (new MagicPercentAttribute(
                "attribute." + Genesis.MOD_ID + ".max_mana_percent",
                1.0, -Double.MAX_VALUE, Double.MAX_VALUE
        )).setSyncable(true));
        SPELL_DAMAGE_PERCENT = ATTRIBUTES.register("spell_damage_percent", () -> (new MagicPercentAttribute(
                "attribute." + Genesis.MOD_ID + ".spell_damage_percent",
                1.0, -Double.MAX_VALUE, Double.MAX_VALUE
        )).setSyncable(true));
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static DeferredRegister<Attribute> getAttributes() {
        return ATTRIBUTES;
    }

    private static RegistryObject<Attribute> newResistanceAttribute(String id) {
        return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicPercentAttribute("attribute." + Genesis.MOD_ID + "." + id + "_magic_resist", 1.0F, -Double.MAX_VALUE, Double.MAX_VALUE)).setSyncable(true));
    }

    private static RegistryObject<Attribute> newPowerAttribute(String id) {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicPercentAttribute("attribute." + Genesis.MOD_ID + "." + id + "_spell_power", 1.0F, -Double.MAX_VALUE, Double.MAX_VALUE)).setSyncable(true));
    }
}