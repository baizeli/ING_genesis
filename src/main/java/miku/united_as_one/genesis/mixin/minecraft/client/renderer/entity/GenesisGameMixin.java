package miku.united_as_one.genesis.mixin.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import miku.united_as_one.genesis.client.render.luminous.GenesisOutlineRenderer;

@Mixin(value = GameRenderer.class, priority = 950)
public class GenesisGameMixin {

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void genesis$beginWorldFrame(CallbackInfo ci) {
        GenesisOutlineRenderer.beginWorldPass();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;F)V", shift = At.Shift.BEFORE))
    private void genesis$drawWorldPass(CallbackInfo ci) {
        GenesisOutlineRenderer.flushWorldPass();
    }
}