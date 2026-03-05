package miku.united_as_one.genesis.client.renderer.entity;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.DeathLaserEntity;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class DeathLaserRenderer extends EntityRenderer<DeathLaserEntity> {
    @SuppressWarnings("removal")
    private static final ResourceLocation TEXTURE = new ResourceLocation(Genesis.MOD_ID, "textures/entity/laser_beam.png");

    private boolean clearerView = false;

    public DeathLaserRenderer(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull DeathLaserEntity entity) {
        return TEXTURE;
    }

    public void render(DeathLaserEntity solarBeam, float entityYaw, float delta, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        this.clearerView = (solarBeam.caster instanceof Player && (Minecraft.getInstance()).player == solarBeam.caster && (Minecraft.getInstance()).options.getCameraType() == CameraType.FIRST_PERSON);
        double collidePosX = solarBeam.prevCollidePosX + (solarBeam.collidePosX - solarBeam.prevCollidePosX) * delta;
        double collidePosY = solarBeam.prevCollidePosY + (solarBeam.collidePosY - solarBeam.prevCollidePosY) * delta;
        double collidePosZ = solarBeam.prevCollidePosZ + (solarBeam.collidePosZ - solarBeam.prevCollidePosZ) * delta;
        double posX = solarBeam.xo + (solarBeam.getX() - solarBeam.xo) * delta;
        double posY = solarBeam.yo + (solarBeam.getY() - solarBeam.yo) * delta;
        double posZ = solarBeam.zo + (solarBeam.getZ() - solarBeam.zo) * delta;
        float yaw = solarBeam.prevYaw + (solarBeam.renderYaw - solarBeam.prevYaw) * delta;
        float pitch = solarBeam.prevPitch + (solarBeam.renderPitch - solarBeam.prevPitch) * delta;
        
        /*float length = (float)Math.sqrt(Math.pow(collidePosX - posX, 2d) + Math.pow(collidePosY - posY, 2d) + Math.pow(collidePosZ - posZ, 2d));*/
        int frame = Mth.floor(((solarBeam.appear.getTimer() - 1) + delta) * 2f);
        if (frame < 0) frame = 6;
        RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(getTextureLocation(solarBeam), false, false))
                .setShaderState(RenderStateShard.RENDERTYPE_EYES_SHADER)
                .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                .setCullState(RenderStateShard.NO_CULL)
                .setOverlayState(RenderStateShard.OVERLAY)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .createCompositeState(false);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.create("glow_beam", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$state));
        if (solarBeam.getRenderStart()) renderStart(frame, matrixStackIn, ivertexbuilder, packedLightIn);
        renderBeam(solarBeam.getLaserLength(), 57 * yaw, 57 * pitch, frame, matrixStackIn, ivertexbuilder, packedLightIn);
        if (solarBeam.getRenderEnd()) {
            matrixStackIn.pushPose();
            double endX = posX + solarBeam.getLaserLength() * Math.cos(yaw) * Math.cos(pitch);
            double endZ = posZ + solarBeam.getLaserLength() * Math.sin(yaw) * Math.cos(pitch);
            double endY = posY + solarBeam.getLaserLength() * Math.sin(pitch);
            matrixStackIn.translate(endX - posX, endY - posY, endZ - posZ);
            renderEnd(frame, solarBeam.blockSide, matrixStackIn, ivertexbuilder, packedLightIn);
            matrixStackIn.popPose();
        }
    }

    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0.0F + 0.0625F * frame;
        float minV = 0.0F;
        float maxU = minU + 0.0625F;
        float maxV = minV + 0.5F;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -1.3F, -1.3F, 0.0F, minU, minV, 1.0F, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -1.3F, 1.3F, 0.0F, minU, maxV, 1.0F, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, 1.3F, 1.3F, 0.0F, maxU, maxV, 1.0F, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, 1.3F, -1.3F, 0.0F, maxU, minV, 1.0F, packedLightIn);
    }

    private void renderStart(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        if (this.clearerView) return;
        matrixStackIn.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
    }

    private void renderEnd(int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
        if (side == null) return;
        matrixStackIn.pushPose();
        Quaternionf sideQuat = side.getRotation();
        sideQuat.mul(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.mulPose(sideQuat);
        matrixStackIn.translate(0.0F, 0.0F, -0.01F);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
    }

    private void drawBeam(float length, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0.0F;
        float minV = 0.5F + 0.03125F * frame;
        float maxU = minU + 0.078125F;
        float maxV = minV + 0.03125F;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        float offset = this.clearerView ? -1.0F : 0.0F;
        drawVertex(matrix4f, matrix3f, builder, -1.0F, offset, 0.0F, minU, minV, 1.0F, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -1.0F, length, 0.0F, minU, maxV, 1.0F, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, 1.0F, length, 0.0F, maxU, maxV, 1.0F, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, 1.0F, offset, 0.0F, maxU, minV, 1.0F, packedLightIn);
    }

    private void renderBeam(float length, float yaw, float pitch, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(yaw - 90.0F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-pitch));
        matrixStackIn.pushPose();
        if (!this.clearerView)
            matrixStackIn.mulPose(Axis.YP.rotationDegrees((Minecraft.getInstance()).gameRenderer.getMainCamera().getXRot() + 90.0F));
        drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
        if (!this.clearerView) {
            matrixStackIn.pushPose();
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-(Minecraft.getInstance()).gameRenderer.getMainCamera().getXRot() - 90.0F));
            drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1.0F, 1.0F, 1.0F, alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}