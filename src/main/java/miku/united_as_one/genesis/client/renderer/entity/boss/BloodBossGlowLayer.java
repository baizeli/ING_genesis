package miku.united_as_one.genesis.client.renderer.entity.boss;

import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import net.minecraft.client.renderer.LightTexture;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import static miku.united_as_one.genesis.Genesis.MODID;

public class BloodBossGlowLayer extends GeoRenderLayer<BloodBoss> {
    // 发光贴图的资源位置
    private static final ResourceLocation GLOW_TEXTURE =
            new ResourceLocation(MODID, "textures/entity/blood_boss/stage_glow_1.png");
    private static final ResourceLocation GLOW_TEXTURE2 =
            new ResourceLocation(MODID, "textures/entity/blood_boss/stage_glow_2.png");
    // 发光渲染类型
    private final RenderType glowRenderType =  RenderType.eyes(GLOW_TEXTURE);
    private final RenderType glowRenderType2 =  RenderType.eyes(GLOW_TEXTURE2);

    public BloodBossGlowLayer(GeoRenderer<BloodBoss> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack,
                       BloodBoss animatable,
                       BakedGeoModel bakedModel,
                       RenderType renderType,
                       MultiBufferSource bufferSource,
                       VertexConsumer buffer,
                       float partialTick,
                       int packedLight,
                       int packedOverlay) {
        poseStack.pushPose();
        RenderType rt;
        int bossStageData = animatable.getBossStageData();
        if (bossStageData<=1){
            rt = glowRenderType;
        }else{
            rt = glowRenderType2;
        }



        this.renderer.actuallyRender(
                poseStack, animatable, bakedModel, rt, bufferSource, buffer,
                true, partialTick, LightTexture.FULL_BRIGHT, packedOverlay,
                1.0F, 1.0F, 1.0F, 1.0F
        );
        poseStack.popPose();
    }
}