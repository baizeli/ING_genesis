package com.baizeli.eternisstarrysky.Mixin.minecraft.client.renderer;

import com.baizeli.eternisstarrysky.Util.spell.SpellEffectUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.*;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.*;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    
    @Inject(
        method = "setupColor", 
        at = @At(
            value = "INVOKE", 
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", 
            shift = At.Shift.BEFORE
        )
    )
    private static void onSetupColor(
        Camera activeRenderInfo, float partialTicks,
        ClientLevel level, int renderDistanceChunks,
        float bossColorModifier, CallbackInfo ci
    ) {
        Entity entity = activeRenderInfo.getEntity();

        if (entity instanceof LivingEntity livingEntity) {
            if (SpellEffectUtil.isAffectedByChaosEffects(livingEntity)) {
                try {
                    Field fogRedField = FogRenderer.class.getDeclaredField("fogRed");
                    Field fogGreenField = FogRenderer.class.getDeclaredField("fogGreen");
                    Field fogBlueField = FogRenderer.class.getDeclaredField("fogBlue");
                    
                    fogRedField.setAccessible(true);
                    fogGreenField.setAccessible(true);
                    fogBlueField.setAccessible(true);
                    
                    fogRedField.set(null, 1.0f);
                    fogGreenField.set(null, 0.2f);
                    fogBlueField.set(null, 0.2f);
                } catch (Exception e) {}
            }
        }
    }
    
    @Redirect(
        method = "setupColor",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V"
        )
    )
    private static void redirectClearColor(float red, float green, float blue, float alpha, Camera activeRenderInfo, float partialTicks, ClientLevel level, int renderDistanceChunks, float bossColorModifier) {
        Entity entity = activeRenderInfo.getEntity();

        if (entity instanceof LivingEntity livingEntity) {
            if (SpellEffectUtil.isAffectedByChaosEffects(livingEntity)) {
                RenderSystem.clearColor(1.0f, 0.2f, 0.2f, 0.0f);
                return;
            }
        }

        RenderSystem.clearColor(red, green, blue, alpha);
    }
}