package miku.united_as_one.genesis.mixin.minecraft.client.gui;

import miku.united_as_one.genesis.client.render.luminous.GenesisOutlineRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class, priority = 950)
public class GenesisGuiMixin {

    @Inject(method = "render", at = @At("RETURN"))
    private void genesis$flushHudGuiOutline(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        guiGraphics.flush();
        GenesisOutlineRenderer.flushGuiPass();
    }
}
