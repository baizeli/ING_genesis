package com.baizeli.eternisstarrysky.Mixin;


import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.baizeli.eternisstarrysky.client.TrailRender.renderTrail;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {


    @Inject(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V",
            shift = At.Shift.AFTER))
    private void afterIrisRender(float partialTicks, long finishTimeNano, boolean renderLevel, CallbackInfo ci) {
        renderTrail(partialTicks,finishTimeNano,renderLevel);
    }



}