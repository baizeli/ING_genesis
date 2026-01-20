package miku.united_as_one.genesis.spell;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.damage.DamageTypes;
import miku.united_as_one.genesis.sound.SoundsRegister;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class SpellSchool {
    private static final DeferredRegister<SchoolType> SCHOOLS;
    public static final ResourceLocation CHAOS_RESOURCE;
    public static final ResourceLocation CELESTIAL_SOURCE_RESOURCE;
    public static final ResourceLocation CULINARY_RESOURCE;

    public static final RegistryObject<SchoolType> CHAOS;
    public static final RegistryObject<SchoolType> CELESTIAL_SOURCE;
    public static final RegistryObject<SchoolType> CULINARY;

    public static final TagKey<Item> CHAOS_FOCUS = ItemTags.create(
        ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "chaos_focus")
    );
    public static final TagKey<Item> CELESTIAL_SOURCE_FOCUS = ItemTags.create(
        ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "celestial_source_focus")
    );
    public static final TagKey<Item> CULINARY_FOCUS = ItemTags.create(
        ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "culinary_focus")
    );

    static {
        SCHOOLS =  DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, Genesis.MOD_ID);
        CHAOS_RESOURCE = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "chaos");
        CELESTIAL_SOURCE_RESOURCE = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "celestial_source");
        CULINARY_RESOURCE = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "culinary");

        // 混沌流派
        CHAOS = registerSchool(
            new SchoolType(
                CHAOS_RESOURCE, 
                CHAOS_FOCUS, 
                Component.translatable(
                    "school." + Genesis.MOD_ID + ".chaos"
                ).withStyle(ChatFormatting.DARK_RED), 
                SpellAttributes.CHAOS_SPELL_POWER,
                SpellAttributes.CHAOS_MAGIC_RESIST,
                SoundsRegister.CHAOS_CAST,
                DamageTypes.CHAOS_MAGIC,
                true,
                false
            )
        );

        // 星源流派
        CELESTIAL_SOURCE = registerSchool(
            new SchoolType(
                CELESTIAL_SOURCE_RESOURCE, 
                CELESTIAL_SOURCE_FOCUS, 
                Component.translatable(
                    "school." + Genesis.MOD_ID + ".celestial_source"
                ).withStyle(ChatFormatting.DARK_AQUA), 
                SpellAttributes.CELESTIAL_SOURCE_SPELL_POWER,
                SpellAttributes.CELESTIAL_SOURCE_MAGIC_RESIST,
                SoundsRegister.CELESTIAL_SOURCE_CAST, 
                DamageTypes.CELESTIAL_SOURCE_MAGIC,
                true,
                false
            )
        );

        // 美食流派
        CULINARY = registerSchool(
            new SchoolType(
                CULINARY_RESOURCE, 
                CULINARY_FOCUS, 
                Component.translatable(
                    "school." + Genesis.MOD_ID + ".culinary"
                ).withStyle(ChatFormatting.GOLD), 
                SpellAttributes.CULINARY_SPELL_POWER,
                SpellAttributes.CULINARY_MAGIC_RESIST,
                SoundsRegister.CULINARY_CAST, 
                ISSDamageTypes.FIRE_MAGIC,
                true,
                false
            )
        );
    }

    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
    }

    private static RegistryObject<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }
}