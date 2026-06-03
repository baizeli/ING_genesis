package miku.united_as_one.genesis.registries.entity;

import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import miku.united_as_one.genesis.contents.entity.*;
import miku.united_as_one.genesis.contents.entity.LightningBolt;
import miku.united_as_one.genesis.contents.entity.arrow.SpecialArrowEntity;
import miku.united_as_one.genesis.contents.entity.boss.BloodBoss;
import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import miku.united_as_one.genesis.contents.entity.gungnir.GungnirChainLightning;
import miku.united_as_one.genesis.contents.entity.gungnir.GungnirDaggerEntity;
import miku.united_as_one.genesis.contents.entity.laser.DeathLaserEntity;
import miku.united_as_one.genesis.contents.entity.projectile.ThrownIron;
import miku.united_as_one.genesis.contents.entity.spell.eldritch.*;
import miku.united_as_one.genesis.contents.entity.spell.blood_boss.BloodBossFireEruptionAoe;
import miku.united_as_one.genesis.contents.entity.spell.blood_boss.blood_dagger.BloodDaggerEntity;
import miku.united_as_one.genesis.contents.entity.spell.blood_boss.blood_dagger.BloodField;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.*;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.blade_works.*;
import miku.united_as_one.genesis.contents.entity.spell.fire.*;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Genesis.MOD_ID);

    // 通用投射物和短生命周期特效实体
    public static final RegistryObject<EntityType<CustomArrowEntity>> CUSTOM_ARROW = ENTITY_TYPES.register("custom_arrow",
            () -> EntityType.Builder.<CustomArrowEntity>of(CustomArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("custom_arrow"));

    public static final RegistryObject<EntityType<NyanCat>> NYAN_CAT = ENTITY_TYPES.register("nyan_cat",
            () -> EntityType.Builder.<NyanCat>of(NyanCat::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("nyan_cat"));

    // “无限剑制”法术使用的剑阵和魔法阵实体
    public static final RegistryObject<EntityType<MagicCircle>> MAGIC_CIRCLE = ENTITY_TYPES.register("magic_circle",
            () -> EntityType.Builder.<MagicCircle>of(MagicCircle::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(30)
                    .updateInterval(1)
                    .build("magic_circle"));

    public static final RegistryObject<EntityType<SwordEntity>> SWORD_ENTITY = ENTITY_TYPES.register("sword_entity",
            () -> EntityType.Builder.<SwordEntity>of(SwordEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(30)
                    .updateInterval(1)
                    .build("sword_entity"));

    public static final RegistryObject<EntityType<DeadStarDecreeComet>> DEAD_STAR_DECREE_COMET = ENTITY_TYPES.register("dead_star_decree_comet",
            () -> EntityType.Builder.of(DeadStarDecreeComet::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(64)
                    .build("dead_star_decree_comet"));

    public static final RegistryObject<EntityType<DeadStarDecreeComet>> DEAD_STAR_DECREE_LARGE_COMET = ENTITY_TYPES.register("dead_star_decree_large_comet",
            () -> EntityType.Builder.of(DeadStarDecreeComet::new, MobCategory.MISC)
                    .sized(12.0F, 12.0F)
                    .clientTrackingRange(64)
                    .build("dead_star_decree-large_comet"));

    public static final RegistryObject<EntityType<BoxEntity>> BOX_ENTITY = ENTITY_TYPES.register("box_entity",
            () -> EntityType.Builder.<BoxEntity>of(BoxEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(30)
                    .updateInterval(1)
                    .build("box_entity"));

    public static final RegistryObject<EntityType<LightningBolt>> LIGHTNING_BOLT = ENTITY_TYPES.register("lighting_bolt",
            () -> EntityType.Builder.<LightningBolt>of(LightningBolt::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(30)
                    .updateInterval(1)
                    .build("lighting_bolt"));

    // Boss 本体、召唤物和 Boss 技能区域实体
    public static final RegistryObject<EntityType<BloodBoss>> BLOOD_BOSS = ENTITY_TYPES.register("blood_boss",
            () -> EntityType.Builder.of(BloodBoss::new, MobCategory.MONSTER)
                    .sized(1.4875001F, 3.6749997F)
                    .clientTrackingRange(128)
                    .build("blood_boss"));

    public static final RegistryObject<EntityType<HammerMob>> HAMMER_MOB = ENTITY_TYPES.register("hammer_mob",
            () -> EntityType.Builder.of(HammerMob::new, MobCategory.MONSTER)
                    .sized(2.0F, 3.3F)
                    .clientTrackingRange(128)
                    .build("hammer_mob"));

    public static final RegistryObject<EntityType<VoidTentacle>> BLOOD_TENTACLE = ENTITY_TYPES.register("blood_tentacle",
            () -> EntityType.Builder.of(
                            (EntityType<VoidTentacle> type, Level level) -> new VoidTentacle(type, level),
                            MobCategory.MISC
                    )
                    .sized(2.5F, 5.5F)
                    .clientTrackingRange(64)
                    .build("blood_tentacle"));

    public static final RegistryObject<EntityType<BloodBossFireEruptionAoe>> BLOOD_BOSS_FIRE_ERUPTION_AOE = ENTITY_TYPES.register("blood_boss_fire_eruption",
            () -> EntityType.Builder.of(
                    (EntityType<BloodBossFireEruptionAoe> type, Level level) -> new BloodBossFireEruptionAoe(type, level),
                            MobCategory.MISC)
                    .sized(4.0F, 0.8F)
                    .clientTrackingRange(64)
                    .build("blood_boss_fire_eruption"));

    public static final RegistryObject<EntityType<BloodDaggerEntity>> BLOOD_DAGGER_PROJECTILE = ENTITY_TYPES.register("blood_dagger",
            () -> EntityType.Builder.of(
                            (EntityType<BloodDaggerEntity> type, Level level) -> new BloodDaggerEntity(type, level),
                            MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(64)
                    .build("blood_dagger"));

    public static final RegistryObject<EntityType<BloodField>> BLOOD_FIELD = ENTITY_TYPES.register("blood_field",
            () -> EntityType.Builder.of(
                            (EntityType<BloodField> type, Level level) -> new BloodField(type, level),
                            MobCategory.MISC)
                    .sized(4.0F, 1.2F)
                    .clientTrackingRange(64)
                    .build("blood_field"));

    public static final RegistryObject<EntityType<ThrowBloodAndWounds>> THROW_BLOOD_AND_WOUNDS = ENTITY_TYPES.register("throw_blood_and_wounds",
            () -> EntityType.Builder.of(ThrowBloodAndWounds::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("throw_blood_and_wounds"));

    public static final RegistryObject<EntityType<TremorAoeEntity>> TREMOR_AOE_ENTITY = ENTITY_TYPES.register("tremor_aoe_entity",
            () -> EntityType.Builder.<TremorAoeEntity>of(TremorAoeEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .setShouldReceiveVelocityUpdates(false)
                    .build("tremor_aoe_entity"));

    // 独立法术投射物和召唤物
    public static final RegistryObject<EntityType<DeathLaserEntity>> DEATH_LASER = ENTITY_TYPES.register("death_laser",
            () -> EntityType.Builder.of(DeathLaserEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("death_laser"));

    public static final RegistryObject<EntityType<ThrownIron>> THROWN_IRON = ENTITY_TYPES.register("iron",
            () -> EntityType.Builder.<ThrownIron>of(ThrownIron::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("iron"));

    public static final RegistryObject<EntityType<SpecialArrowEntity>> SPECIAL_ARROW = ENTITY_TYPES.register("special_arrow",
            () -> EntityType.Builder.<SpecialArrowEntity>of(SpecialArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("special_arrow"));

    public static final RegistryObject<EntityType<UltimateWhisperArrowEntity>> ULTIMATE_WHISPER_ARROW = ENTITY_TYPES.register("ultimate_whisper_arrow",
            () -> EntityType.Builder.<UltimateWhisperArrowEntity>of(UltimateWhisperArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("ultimate_whisper_arrow"));

    public static final RegistryObject<EntityType<SummonedKeeperEntity>> SUMMONED_KEEPER = ENTITY_TYPES.register("summoned_keeper",
            () -> EntityType.Builder.<SummonedKeeperEntity>of(SummonedKeeperEntity::new, MobCategory.MONSTER)
                    .sized(0.85f, 2.3f)
                    .clientTrackingRange(64)
                    .build("summoned_keeper"));

    public static final RegistryObject<EntityType<SummonedWardenEntity>> SUMMONED_WARDEN = ENTITY_TYPES.register("summoned_warden",
            () -> EntityType.Builder.<SummonedWardenEntity>of(SummonedWardenEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 2.9f)
                    .clientTrackingRange(16)
                    .fireImmune()
                    .build("summoned_warden"));
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder)
    {
        return ENTITY_TYPES.register(name, () -> builder.build(name));
    }
    public static final RegistryObject<EntityType<WardenSpellcaster>> WARDEN_SPELLCASTER =
            register("warden_spellcaster", EntityType.Builder.of(WardenSpellcaster::new, MobCategory.MONSTER)
                    .sized(0.9F, 2.9F).fireImmune());

    public static final RegistryObject<EntityType<GungnirDaggerEntity>> GUNGNIR_DAGGER_PROJECTILE = ENTITY_TYPES.register("gungnir_dagger",
            () -> EntityType.Builder.of(
                            (EntityType<GungnirDaggerEntity> type, Level level) -> new GungnirDaggerEntity(type, level),
                            MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(64)
                    .build("gungnir_dagger"));

    public static final RegistryObject<EntityType<GungnirChainLightning>> GUNGNIR_CHAIN_LIGHTNING_PROJECTILE = ENTITY_TYPES.register("gungnir_chain_lightning",
            () -> EntityType.Builder.of(
                            (EntityType<GungnirChainLightning> type, Level level) -> new GungnirChainLightning(type, level),
                            MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(64)
                    .build("gungnir_chain_lightning"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
