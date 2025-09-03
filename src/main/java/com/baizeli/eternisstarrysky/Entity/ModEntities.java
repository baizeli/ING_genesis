package com.baizeli.eternisstarrysky.Entity;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EternisStarrySky.MOD_ID);

    public static final RegistryObject<EntityType<CustomArrowEntity>> CUSTOM_ARROW = ENTITY_TYPES.register("custom_arrow",
            () -> EntityType.Builder.<CustomArrowEntity>of(CustomArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("custom_arrow"));
}