package com.baizeli.eternisstarrysky.spell;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.chaos.*;
import com.baizeli.eternisstarrysky.spell.celestial_source.*;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class Spells {
    private static final DeferredRegister<AbstractSpell> SPELLS;

    // 混沌法术卷轴
    public static final RegistryObject<AbstractSpell> WARPED_BLOOD_BURST_SPELL;
    public static final RegistryObject<AbstractSpell> AMENOFUWARI_SPELL;
    public static final RegistryObject<AbstractSpell> BLOOD_WAR_SPELL;
    public static final RegistryObject<AbstractSpell> SIPHON_SPELL;
    public static final RegistryObject<AbstractSpell> BLOOD_RITUAL_SPELL;
    public static final RegistryObject<AbstractSpell> BLOOD_CONTROL_SPELL;

    // 星源法术卷轴
    public static final RegistryObject<AbstractSpell> I_FLY_SPELL;
    public static final RegistryObject<AbstractSpell> FATE_WEDGE_SPELL;
    public static final RegistryObject<AbstractSpell> PERFECT_EVASION_SPELL;
    public static final RegistryObject<AbstractSpell> ABSOLUTE_EQUALITY_SPELL;
    public static final RegistryObject<AbstractSpell> MYRIAD_ARROWS_SPELL;
    public static final RegistryObject<AbstractSpell> LIFE_AND_DEATH_REALM_SPELL;
    public static final RegistryObject<AbstractSpell> STELLAR_SOUL_CONTROL_SPELL;
    public static final RegistryObject<AbstractSpell> UNPARALLELED_SPELL;

    static {
        SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, EternisStarrySky.MOD_ID);

        // 混沌法术卷轴
        WARPED_BLOOD_BURST_SPELL = registerSpell(new WarpedBloodBurst());
        AMENOFUWARI_SPELL = registerSpell(new AmenofuwariSpell());
        BLOOD_WAR_SPELL = registerSpell(new BloodWarSpell());
        SIPHON_SPELL = registerSpell(new SiphonSpell());
        BLOOD_RITUAL_SPELL = registerSpell(new BloodRitualSpell());
        BLOOD_CONTROL_SPELL = registerSpell(new BloodControlSpell());

        // 星源法术卷轴
        I_FLY_SPELL = registerSpell(new IFlySpell());
        FATE_WEDGE_SPELL = registerSpell(new FateWedgeSpell());
        PERFECT_EVASION_SPELL = registerSpell(new PerfectEvasionSpell());
        ABSOLUTE_EQUALITY_SPELL = registerSpell(new AbsoluteEqualitySpell());
        MYRIAD_ARROWS_SPELL = registerSpell(new MyriadArrowsSpell());
        LIFE_AND_DEATH_REALM_SPELL = registerSpell(new LifeAndDeathRealmSpell());
        STELLAR_SOUL_CONTROL_SPELL = registerSpell(new StellarSoulControlSpell());
        UNPARALLELED_SPELL = registerSpell(new UnparalleledSpell());
    }

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
}