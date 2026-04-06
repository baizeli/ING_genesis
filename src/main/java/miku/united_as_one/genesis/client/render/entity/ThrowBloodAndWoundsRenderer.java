package miku.united_as_one.genesis.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import miku.united_as_one.genesis.client.model.ThrowBloodAndWoundsModel;
import miku.united_as_one.genesis.common.entity.ThrowBloodAndWounds; 
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrowBloodAndWoundsRenderer extends EntityRenderer<ThrowBloodAndWounds> {

    private final ThrowBloodAndWoundsModel<ThrowBloodAndWounds> model;

    public ThrowBloodAndWoundsRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.model = new ThrowBloodAndWoundsModel<>(context.bakeLayer(ThrowBloodAndWoundsModel.LAYER_LOCATION));
    }


    @Override
    public ResourceLocation getTextureLocation(ThrowBloodAndWounds entity) {

        return ThrowBloodAndWoundsModel.TEXTURE;
    }


    @Override
    public void render(ThrowBloodAndWounds entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 1. 处理水平旋转 (Yaw) - 原版箭需要减去 90 度来对齐 X 轴
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));

        // 2. 处理垂直旋转 (Pitch)
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch-90F));

        poseStack.translate(0, -1.5, 0);

        this.model.setupAnim(entity, 0.0F, 0.0F, partialTicks, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}