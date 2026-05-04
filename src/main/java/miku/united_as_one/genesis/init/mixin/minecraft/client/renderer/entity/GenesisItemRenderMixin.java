package miku.united_as_one.genesis.init.mixin.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import miku.united_as_one.genesis.client.render.luminous.GenesisEffect;
import miku.united_as_one.genesis.client.render.luminous.GenesisRegistry;
import miku.united_as_one.genesis.client.render.luminous.GenesisOutlineRenderer;

@Mixin(value = ItemRenderer.class, priority = 950)
public class GenesisItemRenderMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void genesis$captureHead(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack ps, MultiBufferSource bs, int light, int overlay, BakedModel model, CallbackInfo ci) {
        GenesisEffect effect = GenesisRegistry.getTargetEffect(stack.getItem());
        if (effect != null) {
            if (bs instanceof MultiBufferSource.BufferSource source) source.endBatch();

            if (context == ItemDisplayContext.GUI) {
                GenesisOutlineRenderer.startGuiCapture(stack, effect);
            } else {
                GenesisOutlineRenderer.startWorldCapture(stack, effect);
            }
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void genesis$captureReturn(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack ps, MultiBufferSource bs, int light, int overlay, BakedModel model, CallbackInfo ci) {
        GenesisEffect effect = GenesisRegistry.getTargetEffect(stack.getItem());
        if (effect != null) {
            if (bs instanceof MultiBufferSource.BufferSource source) source.endBatch();

            if (context == ItemDisplayContext.GUI) {
                GenesisOutlineRenderer.flushGuiCapture();
            } else {
                GenesisOutlineRenderer.stopWorldCapture();
            }
        }
    }
}