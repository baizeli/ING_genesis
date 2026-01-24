package miku.united_as_one.genesis.client.renderer.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.model.spell.celestial_source.DeadStarDecreeCometModel;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;

public class DeadStarDecreeCometRenderer extends EntityRenderer<Projectile> {
    private final static ResourceLocation BASE_TEXTURE = ResourceLocation.fromNamespaceAndPath(
        Genesis.MOD_ID, "textures/entity/dead_star_decree_comet.png"
    );
    
    private final DeadStarDecreeCometModel<Projectile> model;
    private final float scale;
    
    public DeadStarDecreeCometRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.model = new DeadStarDecreeCometModel<>(context.bakeLayer(DeadStarDecreeCometModel.LAYER_LOCATION));
        this.scale = scale;
    }

    @Override
    public void render(Projectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        if (scale != 1.0f) {
            poseStack.scale(scale, scale, scale);
        }

        /*poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));*/
        
        // 渲染Geo陨石模型
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        this.model.renderToBuffer(
            poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F
        );
        
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile entity) {
        return BASE_TEXTURE;
    }
}