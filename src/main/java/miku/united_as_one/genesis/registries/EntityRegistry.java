package miku.united_as_one.genesis.registries;

import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import miku.united_as_one.genesis.contents.entity.*;
import miku.united_as_one.genesis.contents.entity.LightningBolt;
import miku.united_as_one.genesis.contents.entity.arrow.BloodArrowEntity;
import miku.united_as_one.genesis.contents.entity.arrow.HolyArrowEntity;
import miku.united_as_one.genesis.contents.entity.arrow.StellarArrowEntity;
import miku.united_as_one.genesis.contents.entity.arrow.ThunderArrowEntity;
import miku.united_as_one.genesis.contents.entity.boss.BloodBoss;
import miku.united_as_one.genesis.contents.entity.laser.DeathLaserEntity;
import miku.united_as_one.genesis.contents.entity.projectile.ThrownIron;
import miku.united_as_one.genesis.contents.entity.spell.eldritch.*;
import miku.united_as_one.genesis.contents.entity.spell.blood_boss.BloodBossFireEruptionAoe;
import miku.united_as_one.genesis.contents.entity.spell.blood_boss.blood_dagger.BloodDaggerEntity;
import miku.united_as_one.genesis.contents.entity.spell.blood_boss.blood_dagger.BloodField;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.*;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.notuse.*;
import miku.united_as_one.genesis.contents.entity.spell.fire.*;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.contents.entity.test.BaiZeLiEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.*;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Genesis.MOD_ID);

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

    public static final RegistryObject<EntityType<BoxEntity>> BOX_ENTIYT = ENTITY_TYPES.register("box_entity",
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

    public static final RegistryObject<EntityType<BloodBoss>> BLOOD_BOSS = ENTITY_TYPES.register("blood_boss",
            () -> EntityType.Builder.of(BloodBoss::new, MobCategory.MONSTER)
                    .sized(1.4875001F, 3.6749997F)
                    .clientTrackingRange(128)
                    .build("blood_boss"));

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

    // 特效箭矢实体注册
    public static final RegistryObject<EntityType<ThunderArrowEntity>> THUNDER_ARROW = ENTITY_TYPES.register("thunder_arrow",
            () -> EntityType.Builder.<ThunderArrowEntity>of(ThunderArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("thunder_arrow"));

    public static final RegistryObject<EntityType<HolyArrowEntity>> HOLY_ARROW = ENTITY_TYPES.register("holy_arrow",
            () -> EntityType.Builder.<HolyArrowEntity>of(HolyArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("holy_arrow"));

    public static final RegistryObject<EntityType<BloodArrowEntity>> BLOOD_ARROW = ENTITY_TYPES.register("blood_arrow",
            () -> EntityType.Builder.<BloodArrowEntity>of(BloodArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("blood_arrow"));

    public static final RegistryObject<EntityType<StellarArrowEntity>> STELLAR_ARROW = ENTITY_TYPES.register("stellar_arrow",
            () -> EntityType.Builder.<StellarArrowEntity>of(StellarArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("stellar_arrow"));

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

    public static final RegistryObject<EntityType<BaiZeLiEntity>> BAI_ZE_LI = ENTITY_TYPES.register("bai_ze_li",
            () -> EntityType.Builder.<BaiZeLiEntity>of(BaiZeLiEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("bai_ze_li"));
}
