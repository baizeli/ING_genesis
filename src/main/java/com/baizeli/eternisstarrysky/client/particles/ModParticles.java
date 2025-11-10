package com.baizeli.eternisstarrysky.client.particles;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EternisStarrySky.MOD_ID);

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
    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientSetup {
        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {

            var particleEngine = Minecraft.getInstance().particleEngine;
            particleEngine.register(
                    ModParticles.STARDUST_TRAIL.get(),
                    StardustTrailParticle.StardustProvider::new
            );


            particleEngine.register(
                    ModParticles.CUBE.get(),
                    CubeParticle.Provider::new
            );
            particleEngine.register(
                    ModParticles.TEST.get(),
                    TestParticle.Provider::new
            );

            particleEngine.register(
                    ModParticles.TESTA.get(),
                    TestAParticle.Provider::new
            );

            particleEngine.register(
                    ModParticles.TESTB.get(),
                    TestBParticle.Provider::new
            );
        }
    }
}