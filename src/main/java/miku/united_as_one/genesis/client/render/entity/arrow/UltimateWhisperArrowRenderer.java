package miku.united_as_one.genesis.client.render.entity.arrow;

import com.mojang.blaze3d.vertex.PoseStack;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.TrailRender;
import miku.bai_ze_li.genesis.api.render.shader.GenesisRenderType;
import miku.bai_ze_li.genesis.api.text.GenesisColor;
import miku.bai_ze_li.genesis.api.render.TrailHelp;
import miku.bai_ze_li.genesis.api.render.TrailRenderApi;
import miku.bai_ze_li.genesis.api.render.TrailRenderStyle;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.UltimateWhisperArrowEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class UltimateWhisperArrowRenderer extends ArrowRenderer<UltimateWhisperArrowEntity> {
    private static final ResourceLocation ARROW_TEXTURE = Genesis.rl("textures/entity/jiba.png");
    private static final float ARROW_SCALE = 1.18F;
    private static final TrailRenderStyle RAINBOW_TRAIL = TrailRenderStyle
            .builder(Genesis.rl("textures/images/trail_stellar.png"), UltimateWhisperArrowRenderer::arrowColor)
            .width(0.24F)
            .alphaMultiplier(0.78F)
            .emissive(true)
            .headless(true)
            .renderTypeProvider((style, texture) -> GenesisRenderType.delayedTrail(texture))
            .build();

    public UltimateWhisperArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(UltimateWhisperArrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        if (!TrailRender.shouldDeferWorldEffects()) {
            renderTrailOnly(entity, partialTicks, poseStack, buffer);
        }
        poseStack.pushPose();
        poseStack.scale(ARROW_SCALE, ARROW_SCALE, ARROW_SCALE);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    public static void renderTrailOnly(UltimateWhisperArrowEntity entity, float partialTicks, PoseStack poseStack,
                                       MultiBufferSource buffer) {
        TrailRenderApi.renderTrail(entity.getTrailPositions(partialTicks), poseStack, buffer, RAINBOW_TRAIL,
                entity.tickCount + partialTicks, entity.getId());
    }

    @Override
    public ResourceLocation getTextureLocation(UltimateWhisperArrowEntity entity) {
        return ARROW_TEXTURE;
    }

    private static float[] arrowColor(float progress, float time, int entityId) {
        float gradient = wrap01(progress * 0.78F - time * 0.018F + entityId * 0.137F);
        float pulse = 0.92F + 0.08F * Mth.sin(time * 0.24F + progress * Mth.TWO_PI + entityId);
        float[] color = TrailHelp.interpolateGradientColor(gradient, GenesisColor.RAINBOW);
        return new float[]{
                Mth.clamp(color[0] * pulse, 0.0F, 1.0F),
                Mth.clamp(color[1] * pulse, 0.0F, 1.0F),
                Mth.clamp(color[2] * pulse, 0.0F, 1.0F)
        };
    }

    private static float wrap01(float value) {
        return value - Mth.floor(value);
    }
}
