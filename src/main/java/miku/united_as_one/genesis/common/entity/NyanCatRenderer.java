package miku.united_as_one.genesis.common.entity;

import miku.united_as_one.genesis.client.render.ModShaders;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.config.IrisConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static miku.united_as_one.genesis.client.TrailRender.IRIS_Setup;


public class NyanCatRenderer extends EntityRenderer<NyanCat> {
    private final CatModel<Cat> model;

    private static final ResourceLocation DEFAULT_TEXTURE =
            new ResourceLocation("textures/entity/cat/all_black.png");

    public NyanCatRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CatModel<>(context.bakeLayer(ModelLayers.CAT));
    }

    @Override
    public void render(@NotNull NyanCat entity, float entityYaw, float partialTick,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if (IRIS_Setup){
            IrisConfig irisConfig = Iris.getIrisConfig();

            if (!irisConfig.areShadersEnabled()){
                renderTrail(entity, partialTick, poseStack, Minecraft.getInstance().renderBuffers.bufferSource() , packedLight);
            }
        }else {
            renderTrail(entity, partialTick, poseStack, Minecraft.getInstance().renderBuffers.bufferSource() , packedLight);
        }

        poseStack.pushPose();
        Vector3f direction = new Vector3f(
                (float) (entity.getX() - entity.xOld),
                (float) (entity.getY() - entity.yOld),
                (float) (entity.getZ() - entity.zOld)
        );
        Vector3f normalizedDir = new Vector3f(direction).normalize();
        Vector3f right = new Vector3f(0, 1, 0).cross(normalizedDir).normalize();
        Vector3f up = new Vector3f(normalizedDir).cross(right).normalize();

        Matrix4f rotationMatrix = new Matrix4f();
        rotationMatrix.set(
                right.x, right.y, right.z, 0,
                up.x, up.y, up.z, 0,
                normalizedDir.x, normalizedDir.y, normalizedDir.z, 0,
                0, 0, 0, 1
        );

        poseStack.mulPoseMatrix(rotationMatrix);

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.translate(0, -1.25F, 0);

        this.model.renderToBuffer(poseStack,
                buffer.getBuffer(net.minecraft.client.renderer.RenderType.entityCutout(this.getTextureLocation(entity))),
                packedLight, OverlayTexture.NO_OVERLAY,
                1.0f, 1.0f, 1.0f, 1.0f);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private static final RenderType SHADER_RENDER_TYPE = RenderType.create(
            "shader_test_a",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(ModShaders::getRainbowShader))
                    .setCullState(RenderStateShard.NO_CULL)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST) // 添加深度测试
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE) // 可选：禁用深度写入
                    .createCompositeState(false)

    );
    
    public void renderTrail(NyanCat entity, float partialTick, PoseStack poseStack,
                            MultiBufferSource.BufferSource buffer, int packedLight) {
        if (entity.trailPointer < 0) {
            return;
        }

        VertexConsumer vertexconsumer = buffer.getBuffer(SHADER_RENDER_TYPE);
        
        double x = Mth.lerp(partialTick, entity.xOld, entity.getX());
        double y = Mth.lerp(partialTick, entity.yOld, entity.getY());
        double z = Mth.lerp(partialTick, entity.zOld, entity.getZ());

        Vec3 currentPos = new Vec3(x, y, z);

        poseStack.pushPose();
        poseStack.translate(-x, -y, -z);

        int samples = 0;

        int sampleSize = 8;
        Vec3 drawFrom = currentPos;

        while (samples < sampleSize) {
            Vec3 sample = getTrailPosition(entity, samples + 2, partialTick);
            if (sample == null) {
                samples++;
                continue;
            }
            
            float u1 = samples / (float) sampleSize;
            float u2 = u1 + 1 / (float) sampleSize;
            
            float cameraRot = getCameraRot(entity, partialTick);
            Vec3 topAngleVec = (new Vec3(0.0, getTrailHeight() / 1.0D, 0.0)).zRot(cameraRot);
            Vec3 bottomAngleVec = (new Vec3(0.0, getTrailHeight() / -1.0D, 0.0)).zRot(cameraRot);

            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();

            float alpha = getTrailAlpha(samples);
            float red = 1.0f, green = 1.0f, blue = 1.0f;

            vertexconsumer.vertex(matrix4f, (float)drawFrom.x + (float)bottomAngleVec.x,
                            (float)drawFrom.y + (float)bottomAngleVec.y,
                            (float)drawFrom.z + (float)bottomAngleVec.z)
                    .color(red, green, blue, alpha)
                    .uv(u1, 1F)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(packedLight)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();
                    
            vertexconsumer.vertex(matrix4f, (float)sample.x + (float)bottomAngleVec.x,
                            (float)sample.y + (float)bottomAngleVec.y,
                            (float)sample.z + (float)bottomAngleVec.z)
                    .color(red, green, blue, alpha)
                    .uv(u2, 1F)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(packedLight)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();
                    
            vertexconsumer.vertex(matrix4f, (float)sample.x + (float)topAngleVec.x,
                            (float)sample.y + (float)topAngleVec.y,
                            (float)sample.z + (float)topAngleVec.z)
                    .color(red, green, blue, alpha)
                    .uv(u2, 0)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(packedLight)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();
                    
            vertexconsumer.vertex(matrix4f, (float)drawFrom.x + (float)topAngleVec.x,
                            (float)drawFrom.y + (float)topAngleVec.y,
                            (float)drawFrom.z + (float)topAngleVec.z)
                    .color(red, green, blue, alpha)
                    .uv(u1, 0)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(packedLight)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F)
                    .endVertex();

            samples++;
            drawFrom = sample;
        }
        buffer.endBatch();
        poseStack.popPose();
    }

    
    private float getCameraRot(NyanCat entity, float partialTick) {
        // 获取摄像机
        net.minecraft.client.Camera camera = net.minecraft.client.Minecraft.getInstance().gameRenderer.getMainCamera();
        
        // 获取实体位置
        double entityX = Mth.lerp(partialTick, entity.xOld, entity.getX());
        double entityY = Mth.lerp(partialTick, entity.yOld, entity.getY());
        double entityZ = Mth.lerp(partialTick, entity.zOld, entity.getZ());
        
        // 计算从实体到摄像机的向量
        Vec3 entityToCamera = camera.getPosition().subtract(entityX, entityY, entityZ);
        
        // 计算该向量在XZ平面上的角度（绕Y轴旋转的角度）
        return (float) Mth.atan2(entityToCamera.z, entityToCamera.x);
    }
    
    private float getTrailHeight() {
        return 0.6F; 
    }

    
    private Vec3 getTrailPosition(NyanCat entity, int pointer, float partialTick) {
        if (entity.trailPointer < 0) {
            return null;
        }

        int i = (entity.trailPointer - pointer) & 63;
        int j = (entity.trailPointer - pointer - 1) & 63;

        Vec3 prevPos = entity.trailPositions[j];
        Vec3 currentPos = entity.trailPositions[i];

        if (prevPos == null || currentPos == null) {
            return null;
        }

        Vec3 delta = currentPos.subtract(prevPos);
        return prevPos.add(delta.scale(partialTick));
    }
    
    private float getTrailAlpha(int sampleIndex) {
        float progress = (float) sampleIndex / 8.0f; 
        
        float alpha = 1.0f - progress * progress; 
        return alpha * 0.8f; 
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull NyanCat nyanCat) {
        return DEFAULT_TEXTURE;
    }
}