package miku.united_as_one.genesis.mixin.minecraft.client.renderer.entity;

import miku.united_as_one.genesis.client.render.cosmic.CosmicBakeModel;
import com.mojang.blaze3d.vertex.PoseStack;
import miku.united_as_one.genesis.registries.ItemRegistry;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void onRenderItem(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack mStack, MultiBufferSource buffers, int packedLight, int packedOverlay, BakedModel modelIn, CallbackInfo ci) {
        if (modelIn instanceof CosmicBakeModel iItemRenderer) {
            ci.cancel();
            mStack.pushPose();
            final CosmicBakeModel renderer = (CosmicBakeModel) ForgeHooksClient.handleCameraTransforms(mStack, iItemRenderer, context, leftHand);
            mStack.translate(-0.5D, -0.5D, -0.5D);
            renderer.renderItem(stack, context, mStack, buffers, packedLight, packedOverlay);
            mStack.popPose();
        }
    }

    @ModifyVariable(
            method = "render",
            at = @At("HEAD"),
            index = 6,
            argsOnly = true
    )
    private int makeFullBright(int value, ItemStack stack) {
        return stack.is(ItemRegistry.GUNGNIR.get()) ? LightTexture.FULL_BRIGHT : value;
    }
}