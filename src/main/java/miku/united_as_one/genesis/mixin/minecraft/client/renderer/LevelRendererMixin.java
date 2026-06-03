package miku.united_as_one.genesis.mixin.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import miku.united_as_one.genesis.client.TrailRender;
import miku.united_as_one.genesis.client.render.luminous.GenesisOutlineRenderer;
import miku.united_as_one.genesis.client.renderer.entity.spell.celestial_source.GlazedFlowerRainRenderer;
import miku.united_as_one.genesis.registries.effect.EffectRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow public Minecraft minecraft;

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void genesis$captureDeferredWorldRenderContext(PoseStack poseStack, float partialTick, long finishTimeNano,
                                                           boolean renderBlockOutline, Camera camera,
                                                           GameRenderer gameRenderer, LightTexture lightTexture,
                                                           Matrix4f projectionMatrix, CallbackInfo ci) {
        TrailRender.captureLevelRenderContext(poseStack, partialTick, camera, projectionMatrix);
        GenesisOutlineRenderer.captureLevelRenderContext(poseStack, partialTick, camera, projectionMatrix);
    }

    @Inject(
            method = "renderSnowAndRain",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void renderLevel(LightTexture lightTexture, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (minecraft.level != null) {
            minecraft.level.getEntities().getAll().forEach(entity -> {
                if (entity instanceof LivingEntity living && living.hasEffect(EffectRegistry.GLAZED_FLOWER_RAIN.get())) {
                    GlazedFlowerRainRenderer renderer = new GlazedFlowerRainRenderer();
                    renderer.renderSnowAndRain(
                            lightTexture,
                            partialTick,
                            entity.position().x,
                            entity.position().y,
                            entity.position().z,
                            camX,
                            camY,
                            camZ
                    );
                    renderer.tickRain(living);
                    ci.cancel();
                }
            });
        }
    }
}
