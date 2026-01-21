package miku.united_as_one.genesis.client.renderer.boss;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import static miku.united_as_one.genesis.Genesis.MODID;

public class BloodBossGlowLayer extends GeoRenderLayer<AbstractSpellCastingMob> {
    // 发光贴图的资源位置
    private static final ResourceLocation GLOW_TEXTURE =
            new ResourceLocation(MODID, "textures/entity/blood_boss/stage_glow_1.png");
    // 发光渲染类型
    private final RenderType glowRenderType = RenderType.eyes(GLOW_TEXTURE);;

    public BloodBossGlowLayer(GeoRenderer<AbstractSpellCastingMob> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack,
                       AbstractSpellCastingMob animatable,
                       BakedGeoModel bakedModel,
                       RenderType renderType,
                       MultiBufferSource bufferSource,
                       VertexConsumer buffer,
                       float partialTick,
                       int packedLight,
                       int packedOverlay) {

        // 获取发光渲染类型对应的VertexConsumer
        VertexConsumer glowBuffer = bufferSource.getBuffer(this.glowRenderType);
        poseStack.pushPose();
        this.getRenderer().reRender(
                this.getDefaultBakedModel(animatable),  // 获取模型
                poseStack,                               // 姿态矩阵
                bufferSource,                            // 缓冲区源
                animatable,                              // 动画实体
                this.glowRenderType,                     // 使用发光渲染类型
                glowBuffer,                              // 发光顶点消费者
                partialTick,                             // 部分TICK
                15728640,                             // 光照
                packedOverlay,                           // 叠加层
                1.0f, 1.0f, 1.0f, 1.0f                  // RGBA颜色（白色）
        );
        poseStack.popPose();
    }
}