package miku.united_as_one.genesis.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class BloodTentacleEmissiveLayer2 extends GeoRenderLayer<VoidTentacle> {
    public static final ResourceLocation TEXTURE =  new ResourceLocation(Genesis.MOD_ID, "textures/entity/blood_tentacle/blood_tentacle_emissive2.png");

    public BloodTentacleEmissiveLayer2(GeoEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public void render(PoseStack poseStack, VoidTentacle animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        renderType = RenderType.eyes(TEXTURE);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
        poseStack.pushPose();
        float f = Mth.sin((float)(((double)((float)animatable.tickCount + partialTick) + (animatable.getX() + animatable.getZ()) * 500.0) * 0.15000000596046448)) * 0.5F + 0.5F;
        this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, 15728880, OverlayTexture.NO_OVERLAY, f, f, f, 1.0F);
        poseStack.popPose();
    }
}
