package com.baizeli.eternisstarrysky.spell;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class UpgradeOrbTypes {
    public static final ResourceKey<Registry<UpgradeOrbType>> KEY = UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY;

    public static final ResourceKey<UpgradeOrbType> CHAOS_SPELL_POWER = ResourceKey.create(KEY, EternisStarrySky.rl("chaos_power"));
    public static final ResourceKey<UpgradeOrbType> CELESTIAL_SOURCE_SPELL_POWER = ResourceKey.create(KEY, EternisStarrySky.rl("celestial_source_power"));
}