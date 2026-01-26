package miku.united_as_one.genesis.registry.spell;

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
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    private static RegistryObject<Attribute> newResistanceAttribute(String id) {
        return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicPercentAttribute("attribute." + Genesis.MOD_ID + "." + id + "_magic_resist", 1.0F, Double.MIN_VALUE, Double.MAX_VALUE)).setSyncable(true));
    }

    private static RegistryObject<Attribute> newPowerAttribute(String id) {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicPercentAttribute("attribute." + Genesis.MOD_ID + "." + id + "_spell_power", 1.0F, Double.MIN_VALUE, Double.MAX_VALUE)).setSyncable(true));
    }
}