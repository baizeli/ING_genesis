package miku.united_as_one.genesis.client;

import com.mojang.blaze3d.shaders.FogShape;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.FluidRegistry;
import net.minecraft.client.Camera;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidClientEvents {

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        Camera camera = event.getCamera();
        FluidState state = camera.getEntity().level().getFluidState(net.minecraft.core.BlockPos.containing(camera.getPosition()));

        if (state.getType() == FluidRegistry.SOURCE_FLUID.get() || state.getType() == FluidRegistry.SOURCE_FLUID.getSource()) {
            event.setRed(33f / 255f); event.setGreen(154f / 255f); event.setBlue(255f / 255f);
        } else if (state.getType() == FluidRegistry.BLACKWATER_FLUID.get() || state.getType() == FluidRegistry.BLACKWATER_FLUID.getSource()) {
            event.setRed(5f / 255f); event.setGreen(5f / 255f); event.setBlue(5f / 255f);
        } else if (state.getType() == FluidRegistry.BLOOD_FLUID.get() || state.getType() == FluidRegistry.BLOOD_FLUID.getSource()) {
            event.setRed(150f / 255f); event.setGreen(0f / 255f); event.setBlue(0f / 255f);
        }
    }

    @SubscribeEvent
    public static void onFogRender(ViewportEvent.RenderFog event) {
        Camera camera = event.getCamera();
        FluidState state = camera.getEntity().level().getFluidState(net.minecraft.core.BlockPos.containing(camera.getPosition()));

        if (state.getType() == FluidRegistry.SOURCE_FLUID.get() || state.getType() == FluidRegistry.SOURCE_FLUID.getSource()) {
            event.setNearPlaneDistance(-8.0F); event.setFarPlaneDistance(15.0F); event.setFogShape(FogShape.CYLINDER); event.setCanceled(true);
        } else if (state.getType() == FluidRegistry.BLACKWATER_FLUID.get() || state.getType() == FluidRegistry.BLACKWATER_FLUID.getSource()) {
            event.setNearPlaneDistance(-8.0F); event.setFarPlaneDistance(3.0F); event.setFogShape(FogShape.SPHERE); event.setCanceled(true);
        } else if (state.getType() == FluidRegistry.BLOOD_FLUID.get() || state.getType() == FluidRegistry.BLOOD_FLUID.getSource()) {
            event.setNearPlaneDistance(-8.0F); event.setFarPlaneDistance(12.0F); event.setFogShape(FogShape.CYLINDER); event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide && event.player.isAlive()) {
            if (event.player.isEyeInFluidType(FluidRegistry.SOURCE_FLUID.get().getFluidType())) {
                spawnFluidParticle(event, ParticleTypes.BUBBLE);
            } else if (event.player.isEyeInFluidType(FluidRegistry.BLACKWATER_FLUID.get().getFluidType())) {
                spawnFluidParticle(event, ParticleTypes.SQUID_INK);
            } else if (event.player.isEyeInFluidType(FluidRegistry.BLOOD_FLUID.get().getFluidType())) {
                spawnFluidParticle(event, ParticleTypes.DAMAGE_INDICATOR);
            }
        }
    }

    private static void spawnFluidParticle(TickEvent.PlayerTickEvent event, net.minecraft.core.particles.ParticleOptions particle) {
        if (event.player.level().random.nextInt(10) == 0) {
            event.player.level().addParticle(particle,
                    event.player.getX() + (event.player.level().random.nextDouble() - 0.5),
                    event.player.getEyeY() + (event.player.level().random.nextDouble() * 0.5),
                    event.player.getZ() + (event.player.level().random.nextDouble() - 0.5),
                    0, 0, 0);
        }
    }
}