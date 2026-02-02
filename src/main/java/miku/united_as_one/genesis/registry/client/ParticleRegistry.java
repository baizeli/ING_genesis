package miku.united_as_one.genesis.registry.client;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.particles.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.*;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Genesis.MOD_ID);

    public static final RegistryObject<SimpleParticleType> STARDUST_TRAIL =
            PARTICLE_TYPES.register("stardust_trail",
                    () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CUBE =
            PARTICLE_TYPES.register("cube",
                    () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TEST =
            PARTICLE_TYPES.register("test",
                    () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TESTA =
            PARTICLE_TYPES.register("testa",
                    () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TESTB =
            PARTICLE_TYPES.register("testb",
                    () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> MAGIC_CIRCLE =
            PARTICLE_TYPES.register("magic_circle",
                    () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CRESCENT_BLADE =
            PARTICLE_TYPES.register("crescent_blade",
                    () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_DRIP_HANG =
            PARTICLE_TYPES.register("blood_drip_hang",
                    () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> BLOOD_DRIP_FALL =
            PARTICLE_TYPES.register("blood_drip_fall",
                    () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_DRIP_LAND =
            PARTICLE_TYPES.register("blood_drip_land",
                    () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_DRIP_TWIST =
            PARTICLE_TYPES.register("blood_drip_twist",
                    () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientSetup {
        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {

            var particleEngine = Minecraft.getInstance().particleEngine;
            particleEngine.register(
                    ParticleRegistry.STARDUST_TRAIL.get(),
                    StardustTrailParticle.StardustProvider::new
            );


            particleEngine.register(
                    ParticleRegistry.CUBE.get(),
                    CubeParticle.Provider::new
            );
            particleEngine.register(
                    ParticleRegistry.TEST.get(),
                    TestParticle.Provider::new
            );

            particleEngine.register(
                    ParticleRegistry.TESTA.get(),
                    TestAParticle.Provider::new
            );

            particleEngine.register(
                    ParticleRegistry.TESTB.get(),
                    TestBParticle.Provider::new
            );

            particleEngine.register(
                    ParticleRegistry.MAGIC_CIRCLE.get(),
                    MagicCircleParticle.Provider::new
            );

            particleEngine.register(
                    ParticleRegistry.CRESCENT_BLADE.get(),
                    CrescentBladeParticle.Provider::new
            );

            particleEngine.register(
                    ParticleRegistry.BLOOD_DRIP_HANG.get(),
                    BloodDripParticle.HangProvider::new
            );

            particleEngine.register(
                    ParticleRegistry.BLOOD_DRIP_FALL.get(),
                    BloodDripParticle.FallProvider::new
            );
            particleEngine.register(
                    ParticleRegistry.BLOOD_DRIP_LAND.get(),
                    BloodDripParticle.LandProvider::new
            );

            particleEngine.register(
                    ParticleRegistry.BLOOD_DRIP_TWIST.get(),
                    CrescentBladeParticle.BloodDripTwistProvider::new
            );

        }
    }
}