package com.baizeli.eternisstarrysky.damage;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypes {
    public static final ResourceKey<DamageType> CHAOS_MAGIC = register("chaos_magic");
    public static final ResourceKey<DamageType> CELESTIAL_SOURCE_MAGIC = register("celestial_source_magic");

    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MODID, name));
    }

//    public static void bootstrap(BootstapContext<DamageType> context) {
//        context.register(CHAOS_MAGIC, new DamageType(CHAOS_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
//        context.register(CELESTIAL_SOURCE_MAGIC, new DamageType(CELESTIAL_SOURCE_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
//    }
}
