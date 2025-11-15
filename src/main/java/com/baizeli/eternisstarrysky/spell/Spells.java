package com.baizeli.eternisstarrysky.spell;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.celestial_source.FateWedgeSpell;
import com.baizeli.eternisstarrysky.spell.celestial_source.IFlySpell;
import com.baizeli.eternisstarrysky.spell.chaos.AmenofuwariSpell;
import com.baizeli.eternisstarrysky.spell.chaos.ReversePlagueSpell;
import com.baizeli.eternisstarrysky.spell.chaos.WarpedBarrierSpell;
import com.baizeli.eternisstarrysky.spell.chaos.WarpedBloodBurstSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class Spells {
    private static final DeferredRegister<AbstractSpell> SPELLS;
    public static final RegistryObject<AbstractSpell> WARPED_BLOOD_BURST_SPELL;
    public static final RegistryObject<AbstractSpell> WARPED_BARRIER_SPELL;
    public static final RegistryObject<AbstractSpell> AMENOFUWARI_SPELL;
    public static final RegistryObject<AbstractSpell> REVERSE_PLAGUE_SPELL;
    public static final RegistryObject<AbstractSpell> I_FLY_SPELL;
    public static final RegistryObject<AbstractSpell> FATE_WEDGE_SPELL;

    static {
        SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, EternisStarrySky.MOD_ID);
        WARPED_BLOOD_BURST_SPELL = registerSpell(new WarpedBloodBurstSpell());
        WARPED_BARRIER_SPELL = registerSpell(new WarpedBarrierSpell());
        AMENOFUWARI_SPELL = registerSpell(new AmenofuwariSpell());
        REVERSE_PLAGUE_SPELL = registerSpell(new ReversePlagueSpell());
        I_FLY_SPELL = registerSpell(new IFlySpell());
        FATE_WEDGE_SPELL = registerSpell(new FateWedgeSpell());
    }

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
}