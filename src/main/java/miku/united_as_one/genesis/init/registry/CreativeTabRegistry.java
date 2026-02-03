package miku.united_as_one.genesis.init.registry;

import io.redspace.ironsspellbooks.api.item.IScroll;
import io.redspace.ironsspellbooks.api.spells.*;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.common.spell.celestial_source.*;
import miku.united_as_one.genesis.common.common.spell.chaos.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.SCROLL;

@SuppressWarnings("removal")
public class CreativeTabRegistry {
    private static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, Genesis.MOD_ID);
    
    public static final ResourceKey<CreativeModeTab> IRON_SPELLS_GENESIS_BLOCK = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB, new ResourceLocation(Genesis.MOD_ID, "block"));

    public static final ResourceKey<CreativeModeTab> IRON_SPELLS_GENESIS_MATERIAL = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB, new ResourceLocation(Genesis.MOD_ID, "material"));

    public static final ResourceKey<CreativeModeTab> IRON_SPELLS_GENESIS_EQUIPMENT = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB, new ResourceLocation(Genesis.MOD_ID, "equipment"));

    public static final ResourceKey<CreativeModeTab> IRON_SPELLS_GENESIS_SPELL_SCROLL = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB, new ResourceLocation(Genesis.MOD_ID, "spell_scroll"));

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

        // iron的法术创世纪：方块
        Genesis.L2_REGISTRATE
            .buildModCreativeTab("block", "itemGroup." + Genesis.MOD_ID, builder -> builder
                .icon(() -> ItemRegistry.CELESTIAL_SOURCE_BLOCK_ITEM.get().getDefaultInstance())
            );
            
        // iron的法术创世纪：材料
        Genesis.L2_REGISTRATE
            .buildModCreativeTab("material", "itemGroup." + Genesis.MOD_ID, builder -> builder
                .icon(() -> ItemRegistry.CREATE_STAR.get().getDefaultInstance())
            );

        // iron的法术创世纪：装备
        Genesis.L2_REGISTRATE
            .buildModCreativeTab("equipment", "itemGroup." + Genesis.MOD_ID, builder -> builder
                .icon(() -> ItemRegistry.MITHRIL_SWORD.get().getDefaultInstance())
            );

        // iron的法术创世纪：法术卷轴
        Genesis.L2_REGISTRATE
            .buildModCreativeTab("spell_scroll", "itemGroup." + Genesis.MOD_ID, builder -> builder
                .icon(() -> createScrollWithSpell(AMENOFUWARI_SPELL.get(), 2009))
                .displayItems((params, output) -> {
                    // 混沌法术卷轴
                    AbstractSpell[] chaosSpells = {
                        WARPED_BLOOD_BURST_SPELL.get(),
                        WARPED_BARRIER_SPELL.get(),
                        AMENOFUWARI_SPELL.get(),
                        REVERSE_PLAGUE_SPELL.get(),
                        BLOOD_WAR_SPELL.get(),
                        SIPHON_SPELL.get(),
                        BLOOD_RITUAL_SPELL.get(),
                        BLOOD_CONTROL_SPELL.get(),
                        BLOOD_FRENZY_SPELL.get(),
                        CONFUSION_SPELL.get(),
                        FINAL_WHISPER_SPELL.get()
                    };

                    // 星源法术卷轴
                    AbstractSpell[] celestialSpells = {
                        I_FLY_SPELL.get(),
                        FATE_WEDGE_SPELL.get(),
                        PERFECT_EVASION_SPELL.get(),
                        ABSOLUTE_EQUALITY_SPELL.get(),
                        MYRIAD_ARROWS_SPELL.get(),
                        LIFE_AND_DEATH_REALM_SPELL.get(),
                        STELLAR_SOUL_CONTROL_SPELL.get(),
                        UNLIMITED_BLADE_WORKS_SPELL.get(),
                        UNPARALLELED_SPELL.get(),
                        GLAZED_FLOWER_RAIN_SPELL.get(),
                        DEAD_STAR_DECREE_SPELL.get(),
                        SUMMON_PIG_SWARM_SPELL.get(),
                        NYAN_CAT_JET_SPELL.get()
                    };

                    // 混沌法术卷轴
                    for (AbstractSpell spell : chaosSpells) {
                        for (int level = spell.getMinLevel(); level <= spell.getMaxLevel(); level++) {
                            output.accept(createScrollWithSpell(spell, level));
                        }
                    }
                    
                    // 星源法术卷轴
                    for (AbstractSpell spell : celestialSpells) {
                        for (int level = spell.getMinLevel(); level <= spell.getMaxLevel(); level++) {
                            output.accept(createScrollWithSpell(spell, level));
                        }
                    }
                })
            );

        Genesis.L2_REGISTRATE.defaultCreativeTab(CreativeModeTabs.SEARCH);
    }

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    private static ItemStack createScrollWithSpell(AbstractSpell spell, int level) {
        ItemStack scrollStack = new ItemStack(SCROLL.get());
        if (scrollStack.getItem() instanceof IScroll) {
            ISpellContainerMutable spellContainer = ISpellContainer.create(1, false, false).mutableCopy();
            spellContainer.addSpellAtIndex(spell, level, 0, true);
            ISpellContainer.set(scrollStack, spellContainer.toImmutable());
        }
        return scrollStack;
    }
}