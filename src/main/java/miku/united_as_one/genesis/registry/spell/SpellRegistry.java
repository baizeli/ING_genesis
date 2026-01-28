package miku.united_as_one.genesis.registry.spell;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.spell.celestial_source.*;
import miku.united_as_one.genesis.spell.chaos.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;

public class SpellRegistry {
/*    private static final DeferredRegister<AbstractSpell> SPELLS;

    // 混沌法术卷轴
    public static final RegistryObject<AbstractSpell> WARPED_BLOOD_BURST_SPELL;
    public static final RegistryObject<AbstractSpell> WARPED_BARRIER_SPELL;
    public static final RegistryObject<AbstractSpell> AMENOFUWARI_SPELL;
    public static final RegistryObject<AbstractSpell> REVERSE_PLAGUE_SPELL;
    public static final RegistryObject<AbstractSpell> BLOOD_WAR_SPELL;
    public static final RegistryObject<AbstractSpell> SIPHON_SPELL;
    public static final RegistryObject<AbstractSpell> BLOOD_RITUAL_SPELL;
    public static final RegistryObject<AbstractSpell> BLOOD_CONTROL_SPELL;
    public static final RegistryObject<AbstractSpell> BLOOD_FRENZY_SPELL;
    public static final RegistryObject<AbstractSpell> CONFUSION_SPELL;
    public static final RegistryObject<AbstractSpell> FINAL_WHISPER_SPELL;

    // 星源法术卷轴
    public static final RegistryObject<AbstractSpell> I_FLY_SPELL;
    public static final RegistryObject<AbstractSpell> FATE_WEDGE_SPELL;
    public static final RegistryObject<AbstractSpell> PERFECT_EVASION_SPELL;
    public static final RegistryObject<AbstractSpell> ABSOLUTE_EQUALITY_SPELL;
    public static final RegistryObject<AbstractSpell> MYRIAD_ARROWS_SPELL;
    public static final RegistryObject<AbstractSpell> LIFE_AND_DEATH_REALM_SPELL;
    public static final RegistryObject<AbstractSpell> STELLAR_SOUL_CONTROL_SPELL;
    public static final RegistryObject<AbstractSpell> UNLIMITED_BLADE_WORKS_SPELL;
    public static final RegistryObject<AbstractSpell> UNPARALLELED_SPELL;
    public static final RegistryObject<AbstractSpell> GLAZED_FLOWER_RAIN_SPELL;
    public static final RegistryObject<AbstractSpell> DEAD_STAR_DECREE_SPELL;
    public static final RegistryObject<AbstractSpell> SUMMON_PIG_SWARM_SPELL;
    public static final RegistryObject<AbstractSpell> NYAN_CAT_JET_SPELL;

    static {
        SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, Genesis.MOD_ID);

        // 混沌法术卷轴
        WARPED_BLOOD_BURST_SPELL = registerSpell(new WarpedBloodBurstSpell());
        WARPED_BARRIER_SPELL = registerSpell(new WarpedBarrierSpell());
        AMENOFUWARI_SPELL = registerSpell(new AmenofuwariSpell());
        REVERSE_PLAGUE_SPELL = registerSpell(new ReversePlagueSpell());
        BLOOD_WAR_SPELL = registerSpell(new BloodWarSpell());
        SIPHON_SPELL = registerSpell(new SiphonSpell());
        BLOOD_RITUAL_SPELL = registerSpell(new BloodRitualSpell());
        BLOOD_CONTROL_SPELL = registerSpell(new BloodControlSpell());
        BLOOD_FRENZY_SPELL = registerSpell(new BloodFrenzySpell());
        CONFUSION_SPELL = registerSpell(new ConfusionSpell());

        // 星源法术卷轴
        I_FLY_SPELL = registerSpell(new IFlySpell());
        FATE_WEDGE_SPELL = registerSpell(new FateWedgeSpell());
        PERFECT_EVASION_SPELL = registerSpell(new PerfectEvasionSpell());
        ABSOLUTE_EQUALITY_SPELL = registerSpell(new AbsoluteEqualitySpell());
        MYRIAD_ARROWS_SPELL = registerSpell(new MyriadArrowsSpell());
        LIFE_AND_DEATH_REALM_SPELL = registerSpell(new LifeAndDeathRealmSpell());
        STELLAR_SOUL_CONTROL_SPELL = registerSpell(new StellarSoulControlSpell());
        UNLIMITED_BLADE_WORKS_SPELL = registerSpell(new UnlimitedBladeWorksSpell());
        UNPARALLELED_SPELL = registerSpell(new UnparalleledSpell());
        GLAZED_FLOWER_RAIN_SPELL = registerSpell(new GlazedFlowerRainSpell());
        DEAD_STAR_DECREE_SPELL = registerSpell(new DeadStarDecreeSpell());
        SUMMON_PIG_SWARM_SPELL = registerSpell(new SummonPigSwarmSpell());
        FINAL_WHISPER_SPELL = registerSpell(new FinalWhisper());
        NYAN_CAT_JET_SPELL = registerSpell(new NyanCatJetSpell());
    }

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }*/
}