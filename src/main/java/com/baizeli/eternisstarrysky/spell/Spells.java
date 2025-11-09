package com.baizeli.eternisstarrysky.spell;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.chaos.ChaosAreaSpell;
import com.baizeli.eternisstarrysky.spell.chaos.AmenofuwariSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class Spells {
    private static final DeferredRegister<AbstractSpell> SPELLS;
    public static final RegistryObject<AbstractSpell> CHAOS_AREA_SPELL;
    public static final RegistryObject<AbstractSpell> AMENOFUWARI_SPELL;

    static {
        SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, EternisStarrySky.MOD_ID);
        CHAOS_AREA_SPELL = registerSpell(new ChaosAreaSpell());
        AMENOFUWARI_SPELL = registerSpell(new AmenofuwariSpell());
    }

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
}