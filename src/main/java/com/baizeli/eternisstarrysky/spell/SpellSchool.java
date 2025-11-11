package com.baizeli.eternisstarrysky.spell;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.SoundsRegister;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SpellSchool {
    private static final DeferredRegister<SchoolType> SCHOOLS;
    public static final ResourceLocation CHAOS_RESOURCE;
    public static final ResourceLocation CELESTIAL_SOURCE_RESOURCE;
    public static final RegistryObject<SchoolType> CHAOS;
    public static final RegistryObject<SchoolType> CELESTIAL_SOURCE;
    public static final TagKey<Item> CHAOS_FOCUS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "chaos_focus"));
    public static final TagKey<Item> CELESTIAL_SOURCE_FOCUS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "celestial_source_focus"));

    static {
        SCHOOLS =  DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, EternisStarrySky.MOD_ID);
        CHAOS_RESOURCE = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "chaos");
        CELESTIAL_SOURCE_RESOURCE = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "celestial_source");
        CHAOS = registerSchool(new SchoolType(CHAOS_RESOURCE, CHAOS_FOCUS, Component.translatable("school." + EternisStarrySky.MOD_ID + ".chaos").withStyle(ChatFormatting.DARK_RED), Attributes.CHAOS_SPELL_POWER, Attributes.CHAOS_MAGIC_RESIST, SoundsRegister.CHAOS_CAST, ISSDamageTypes.FIRE_MAGIC));
        CELESTIAL_SOURCE = registerSchool(new SchoolType(CELESTIAL_SOURCE_RESOURCE, CELESTIAL_SOURCE_FOCUS, Component.translatable("school." + EternisStarrySky.MOD_ID + ".celestial_source").withStyle(ChatFormatting.DARK_AQUA), Attributes.CELESTIAL_SOURCE_SPELL_POWER, Attributes.CELESTIAL_SOURCE_MAGIC_RESIST, SoundsRegister.CELESTIAL_SOURCE_CAST, ISSDamageTypes.FIRE_MAGIC));
    }

    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
    }

    private static RegistryObject<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }
}