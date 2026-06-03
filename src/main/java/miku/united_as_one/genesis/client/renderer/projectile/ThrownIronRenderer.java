package miku.united_as_one.genesis.client.renderer.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import miku.bai_ze_li.genesis.api.render.TrailRenderApi;
import miku.bai_ze_li.genesis.api.render.TrailRenderStyle;
import miku.bai_ze_li.genesis.api.render.shader.GenesisRenderType;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.TrailRender;
import miku.united_as_one.genesis.contents.entity.projectile.ThrownIron;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class ThrownIronRenderer extends EntityRenderer<ThrownIron> {
    private static final ResourceLocation IRON_TEXTURE = new ResourceLocation("minecraft", "textures/item/iron_ingot.png");
    private static final TrailRenderStyle IRON_TRAIL = TrailRenderStyle
            .builder(Genesis.rl("textures/images/trail_stellar.png"), ThrownIronRenderer::trailColor)
            .width(0.24F)
            .alphaMultiplier(0.78F)
            .emissive(true)
            .headless(true)
            .renderTypeProvider((style, texture) -> GenesisRenderType.delayedTrail(texture))
            .build();

    public ThrownIronRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ThrownIron entity, float yaw, float partialTicks, PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (!TrailRender.shouldDeferWorldEffects()) {
            renderTrailOnly(entity, partialTicks, poseStack, bufferSource);
        }

        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.scale(1.1F, 1.1F, 1.1F);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                new ItemStack(Items.IRON_INGOT), ItemDisplayContext.GROUND,
                packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), 0);
        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    public static void renderTrailOnly(ThrownIron entity, float partialTicks, PoseStack poseStack,
                                       MultiBufferSource buffer) {
        TrailRenderApi.renderTrail(entity.getTrailPositions(partialTicks), poseStack, buffer, IRON_TRAIL,
                entity.tickCount + partialTicks, entity.getId());
    }

    private static float[] trailColor(float progress, float time, int entityId) {
        float pulse = 0.92F + 0.08F * Mth.sin(time * 0.24F + progress * Mth.TWO_PI + entityId);
        float shade = Mth.lerp(progress, 0.62F, 1.0F) * pulse;
        return new float[]{
                Mth.clamp(shade, 0.0F, 1.0F),
                Mth.clamp(shade, 0.0F, 1.0F),
                Mth.clamp(shade * 0.96F, 0.0F, 1.0F)
        };
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ThrownIron entity) {
        return IRON_TEXTURE;
    }
}
