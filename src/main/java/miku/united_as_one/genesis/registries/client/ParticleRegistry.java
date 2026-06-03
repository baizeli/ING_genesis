package miku.united_as_one.genesis.registries.client;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.particles.*;
import miku.bai_ze_li.genesis.api.render.particle.CrescentBladeParticle;
import miku.bai_ze_li.genesis.api.render.particle.CubeParticle;
import miku.bai_ze_li.genesis.api.render.particle.GlowCubeParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Genesis.MOD_ID);

    public static final RegistryObject<SimpleParticleType> STARDUST_TRAIL =
            PARTICLE_TYPES.register("stardust_trail", () -> new SimpleParticleType(false));

    // 开发和预留粒子：当前可能只在调试入口触发，后续效果迭代仍会复用。
    public static final RegistryObject<SimpleParticleType> CUBE =
            PARTICLE_TYPES.register("cube", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> GLOW_CUBE =
            PARTICLE_TYPES.register("glow_cube", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TEST =
            PARTICLE_TYPES.register("test", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TESTA =
            PARTICLE_TYPES.register("testa", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TESTB =
            PARTICLE_TYPES.register("testb", () -> new SimpleParticleType(false));

    // 星源法术视觉粒子。
    public static final RegistryObject<SimpleParticleType> MAGIC_CIRCLE =
            PARTICLE_TYPES.register("magic_circle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CRESCENT_BLADE =
            PARTICLE_TYPES.register("crescent_blade", () -> new SimpleParticleType(false));

    // 血系 Boss 和血滴视觉粒子。
    public static final RegistryObject<SimpleParticleType> BLOOD_DRIP_HANG =
            PARTICLE_TYPES.register("blood_drip_hang", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_DRIP_FALL =
            PARTICLE_TYPES.register("blood_drip_fall", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_DRIP_LAND =
            PARTICLE_TYPES.register("blood_drip_land", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_DRIP_TWIST =
            PARTICLE_TYPES.register("blood_drip_twist", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSetup {
        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            var particleEngine = Minecraft.getInstance().particleEngine;
            CrescentBladeParticle.setBloodDripHangParticle(BLOOD_DRIP_HANG::get);

            particleEngine.register(STARDUST_TRAIL.get(), StardustTrailParticle.StardustProvider::new);
            particleEngine.register(CUBE.get(), CubeParticle.Provider::new);
            particleEngine.register(GLOW_CUBE.get(), GlowCubeParticle.Provider::new);
            particleEngine.register(TEST.get(), TestParticle.Provider::new);
            particleEngine.register(TESTA.get(), TestAParticle.Provider::new);
            particleEngine.register(TESTB.get(), TestBParticle.Provider::new);
            particleEngine.register(MAGIC_CIRCLE.get(), MagicCircleParticle.Provider::new);
            particleEngine.register(CRESCENT_BLADE.get(), CrescentBladeParticle.Provider::new);
            particleEngine.register(BLOOD_DRIP_HANG.get(), BloodDripParticle.HangProvider::new);
            particleEngine.register(BLOOD_DRIP_FALL.get(), BloodDripParticle.FallProvider::new);
            particleEngine.register(BLOOD_DRIP_LAND.get(), BloodDripParticle.LandProvider::new);
            particleEngine.register(BLOOD_DRIP_TWIST.get(), CrescentBladeParticle.BloodDripTwistProvider::new);
        }
    }
}
