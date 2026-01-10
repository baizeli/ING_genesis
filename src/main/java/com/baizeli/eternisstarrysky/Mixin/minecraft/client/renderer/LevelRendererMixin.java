package com.baizeli.eternisstarrysky.Mixin.minecraft.client.renderer;

import com.baizeli.eternisstarrysky.client.renderer.spell.celestial_source.GlazedFlowerRainRenderer;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow public Minecraft minecraft;

    @Inject(
            method = "renderSnowAndRain",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void renderLevel(LightTexture lightTexture, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (minecraft.level != null) {
            minecraft.level.getEntities().getAll().forEach(entity -> {
                if (entity instanceof LivingEntity living && living.hasEffect(ModEffect.GLAZED_FLOWER_RAIN.get())) {
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
