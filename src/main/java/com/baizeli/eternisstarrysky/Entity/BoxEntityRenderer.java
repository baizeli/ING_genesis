package com.baizeli.eternisstarrysky.Entity;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Util.RenderUtils;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BoxEntityRenderer extends EntityRenderer<BoxEntity> {

    public BoxEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(BoxEntity magicCircle) {
        return ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MODID, "<null>");
    }

    @Override
    public void render(BoxEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {long time = System.currentTimeMillis();
        float hue = (time % 5000L) / 5000.0F;
        int rgb = java.awt.Color.HSBtoRGB(hue, 1.0F, 1.0F);
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;

        float angle = (System.currentTimeMillis() % 360000L) / 1000F * 90F;
        RenderUtils.renderWireCube(poseStack, buffer, entity.halfSize, angle, Axis.YP, r, g, b, 0, 0, 0);
        RenderUtils.renderWireCube(poseStack, buffer, entity.halfSize, angle, Axis.XP, r, g, b, 0, 0, 0);
        RenderUtils.renderWireCube(poseStack, buffer, entity.halfSize, angle, Axis.ZP, r, g, b, 0, 0, 0);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public boolean shouldRender(BoxEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected boolean shouldShowName(BoxEntity entity) {
        return false;
    }
}
