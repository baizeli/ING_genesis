package miku.united_as_one.genesis.client.render.entity.arrow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import miku.united_as_one.genesis.Genesis;
import miku.bai_ze_li.genesis.api.render.TrailHelp;
import miku.bai_ze_li.genesis.api.render.TrailRenderApi;
import miku.united_as_one.genesis.contents.entity.arrow.ArrowRenderDefinition;
import miku.united_as_one.genesis.contents.entity.arrow.SpecialArrowEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SpecialArrowRenderer extends EntityRenderer<SpecialArrowEntity> {
    private static final ResourceLocation ARROW_TEXTURE = Genesis.rl("textures/entity/feg.png");
    private static final float ARROW_SPRITE_SCALE = 7.0F;

    public SpecialArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SpecialArrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        ArrowRenderDefinition definition = entity.getRenderDefinition();
        TrailRenderApi.renderTrail(entity.getTrailPositions(), poseStack, buffer, definition.trailStyle(),
                entity.tickCount + partialTicks, entity.getId());

        if (!entity.isDying()) {
            renderArrowSprite(entity, definition, partialTicks, poseStack, buffer);
        }
    }

    private void renderArrowSprite(SpecialArrowEntity entity, ArrowRenderDefinition definition, float partialTicks,
                                   PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();

        Vec3 cameraPos = this.entityRenderDispatcher.camera.getPosition();
        Vec3 arrowPos = entity.position();
        Vec3 direction = cameraPos.subtract(arrowPos);
        if (direction.lengthSqr() < 0.0001D) {
            direction = new Vec3(0.0D, 0.0D, 1.0D);
        } else {
            direction = direction.normalize();
        }

        poseStack.mulPose(Axis.YP.rotation((float) Math.atan2(direction.x, direction.z)));
        poseStack.mulPose(Axis.XP.rotation((float) -Math.asin(direction.y)));

        float selfRotation = entity.tickCount + partialTicks;
        float rotationSpeed = 8.0F + (entity.getId() % 13) * 0.923F;
        poseStack.mulPose(Axis.ZP.rotationDegrees(selfRotation * rotationSpeed));
        poseStack.scale(ARROW_SPRITE_SCALE, ARROW_SPRITE_SCALE, ARROW_SPRITE_SCALE);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(entity)));
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        float size = 0.5F;
        float brightness = calculateTwinkleBrightness(entity, partialTicks, cameraPos.distanceTo(arrowPos));
        float[] color = applyBrightness(definition.bodyColor(entity.getId()), brightness);

        TrailHelp.addVertexWithColor(consumer, matrix, normal, new Vec3(-size, -size, 0.0D), color, 1.0F, 0.0F, 1.0F, LightTexture.FULL_BRIGHT);
        TrailHelp.addVertexWithColor(consumer, matrix, normal, new Vec3(size, -size, 0.0D), color, 1.0F, 1.0F, 1.0F, LightTexture.FULL_BRIGHT);
        TrailHelp.addVertexWithColor(consumer, matrix, normal, new Vec3(size, size, 0.0D), color, 1.0F, 1.0F, 0.0F, LightTexture.FULL_BRIGHT);
        TrailHelp.addVertexWithColor(consumer, matrix, normal, new Vec3(-size, size, 0.0D), color, 1.0F, 0.0F, 0.0F, LightTexture.FULL_BRIGHT);

        poseStack.popPose();
    }

    private float calculateTwinkleBrightness(SpecialArrowEntity entity, float partialTicks, double distanceToPlayer) {
        if (distanceToPlayer <= 10.0D) {
            return 1.0F;
        }

        float twinklePeriod = 5.0F + (entity.getId() % 8) * 0.875F;
        float twinkleTime = (entity.tickCount + partialTicks) / 20.0F;
        float cyclePosition = (twinkleTime % twinklePeriod) / twinklePeriod;
        if (cyclePosition >= 0.4F) {
            return 1.0F;
        }

        float riseSpeed = cyclePosition / 0.4F;
        float twinkleIntensity = Mth.sin(riseSpeed * Mth.PI);
        return 0.7F + twinkleIntensity * 0.3F;
    }

    private float[] applyBrightness(float[] color, float brightness) {
        return new float[]{
                Mth.clamp(color[0] * brightness, 0.0F, 1.0F),
                Mth.clamp(color[1] * brightness, 0.0F, 1.0F),
                Mth.clamp(color[2] * brightness, 0.0F, 1.0F)
        };
    }

    @Override
    public ResourceLocation getTextureLocation(SpecialArrowEntity entity) {
        return ARROW_TEXTURE;
    }
}
