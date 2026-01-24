package miku.united_as_one.genesis.entity_temp;

import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.entity.spells.celestial_source.BoxEntity;
import miku.united_as_one.genesis.entity.spells.celestial_source.DeadStarDecreeComet;
import miku.united_as_one.genesis.entity.spells.celestial_source.notuse.MagicCircle;
import miku.united_as_one.genesis.entity.spells.celestial_source.notuse.SwordEntity;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
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
                    .clientTrackingRange(64)
                    .build("blood_boss"));
}