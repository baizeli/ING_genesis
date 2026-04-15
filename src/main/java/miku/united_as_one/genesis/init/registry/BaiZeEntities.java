package miku.united_as_one.genesis.init.registry;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.test.BaiZeLiHiddenLoader;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.*;

public class BaiZeEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Genesis.MOD_ID);

    public static final RegistryObject<EntityType<PathfinderMob>> BAI_ZE =
            ENTITY_TYPES.register("bai_ze_li", () ->
                    EntityType.Builder.<PathfinderMob>of((type, level) -> {
                                try {
                                    Object obj = BaiZeLiHiddenLoader.load()
                                            .getConstructor(EntityType.class, Level.class)
                                            .newInstance(type, level);

                                    return (PathfinderMob) obj;

                                } catch (Throwable e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }, MobCategory.MONSTER)
                            .sized(0.6f, 1.8f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("bai_ze_li")
            );
}