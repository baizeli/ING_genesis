package miku.united_as_one.genesis.common.spell;

import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class UpgradeOrbTypes {
    public static final ResourceKey<Registry<UpgradeOrbType>> KEY = UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY;

    //混沌法强+1
    public static final ResourceKey<UpgradeOrbType> CHAOS_SPELL_POWER = ResourceKey.create(
        KEY, Genesis.rl("chaos_power")
    );

    //烈焰穿透+1
    public static final ResourceKey<UpgradeOrbType> FIRE_SPELL_PENETRATION = ResourceKey.create(
        KEY, Genesis.rl("fire_spell_penetration")
    );

    //星源法强+1
    public static final ResourceKey<UpgradeOrbType> CELESTIAL_SOURCE_SPELL_POWER = ResourceKey.create(
        KEY, Genesis.rl("celestial_source_power")
    );
    //忘了
    public static final ResourceKey<UpgradeOrbType> CULINARY_SPELL_POWER = ResourceKey.create(
        KEY, Genesis.rl("culinary_power")
    );

    //远古巫术+1
    public static final ResourceKey<UpgradeOrbType> ELDRITCH_SPELL_POWER = ResourceKey.create(
        KEY, Genesis.rl("eldritch_power")
    );

    //神圣穿透+1
    public static final ResourceKey<UpgradeOrbType> HOLY_SPELL_PENETRATION = ResourceKey.create(
        KEY,Genesis.rl("holy_spell_penetration")
    );

    //傻逼冰霜+1
    public static final ResourceKey<UpgradeOrbType> IEC_SPELL_PENETRATION = ResourceKey.create(
        KEY,Genesis.rl("ice_spell_penetration")
    );

    //猩红穿透+1
    public static final ResourceKey<UpgradeOrbType> BLOOD_SPELL_PENETRATION = ResourceKey.create(
        KEY,Genesis.rl("blood_spell_penetration")
    );

    //末影穿透+1
    public static final ResourceKey<UpgradeOrbType> ENDER_SPELL_PENETRATION = ResourceKey.create(
            KEY,Genesis.rl("ender_spell_penetration")
    );

    //雷霆穿透+1
    public static final ResourceKey<UpgradeOrbType> THUNDER_SPELL_PENETRATION = ResourceKey.create(
            KEY,Genesis.rl("thunder_spell_penetration")
    );

    //自然穿透+1
    public static final ResourceKey<UpgradeOrbType> NATURE_SPELL_PENETRATION = ResourceKey.create(
            KEY,Genesis.rl("nature_spell_penetration")
    );

    //邪术穿透+1
    public static final ResourceKey<UpgradeOrbType> ELDRITCH_SPELL_PENETRATION = ResourceKey.create(
            KEY,Genesis.rl("eldritch_spell_penetration")
    );

    //混沌穿透
    public static final ResourceKey<UpgradeOrbType> CHAOS_SPELL_PENETRATION = ResourceKey.create(
            KEY,Genesis.rl("chaos_spell_penetration")
    );

    //星源穿透
    public static final ResourceKey<UpgradeOrbType> CELESTIAL_SOURCE_SPELL_PENETRATION = ResourceKey.create(
            KEY,Genesis.rl("celestial_source_spell_penetration")
    );
}