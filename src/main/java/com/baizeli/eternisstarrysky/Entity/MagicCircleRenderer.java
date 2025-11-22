package com.baizeli.eternisstarrysky.Entity;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MagicCircleRenderer extends EntityRenderer<MagicCircle> {
    private static final ResourceLocation DEFAULT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MODID, "textures/magic_circle.png");

    public MagicCircleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(MagicCircle magicCircle) {
        return DEFAULT_TEXTURE;
    }

    @Override
    public void render(MagicCircle entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        RenderUtils.renderNormalTexturedQuad(poseStack, buffer, DEFAULT_TEXTURE, 50, 50, 0, Axis.YP, 0, 0, 0, LightTexture.FULL_BRIGHT);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public boolean shouldRender(MagicCircle livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected boolean shouldShowName(MagicCircle entity) {
        return false;
    }
}
