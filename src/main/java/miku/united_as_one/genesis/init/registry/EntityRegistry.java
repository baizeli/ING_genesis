package miku.united_as_one.genesis.init.registry;

import io.redspace.ironsspellbooks.entity.spells.FireEruptionAoe;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import miku.united_as_one.genesis.common.entity.*;
import miku.united_as_one.genesis.common.entity.LightningBolt;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.spells.blood_boss.BloodBossFireEruptionAoe;
import miku.united_as_one.genesis.common.entity.spells.blood_boss.blood_dagger.BloodDaggerEntity;
import miku.united_as_one.genesis.common.entity.spells.blood_boss.blood_dagger.BloodField;
import miku.united_as_one.genesis.common.entity.spells.celestial_source.*;
import miku.united_as_one.genesis.common.entity.spells.celestial_source.notuse.*;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.resources.ResourceLocation;
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

    public static final RegistryObject<EntityType<EntitySolarBeam>> SOLAR_BEAM = ENTITY_TYPES.register("solar_beam",
            () -> EntityType.Builder.of(EntitySolarBeam::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("solar_beam"));
}