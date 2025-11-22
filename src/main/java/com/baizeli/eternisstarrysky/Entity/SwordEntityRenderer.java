package com.baizeli.eternisstarrysky.Entity;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;

public class SwordEntityRenderer extends EntityRenderer<SwordEntity> {

    public SwordEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SwordEntity swordEntity) {
        return ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MODID, "nop");
    }

    @Override
    public void render(SwordEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemStack stack = entity.getStoredSword();
        if (stack != null && !stack.isEmpty()) {
            ItemEntity fakeItem = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack);
            RenderUtils.renderFakeItem(fakeItem, (ItemEntityRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(fakeItem), poseStack, buffer, partialTick, packedLight);
        }
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public boolean shouldRender(SwordEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected boolean shouldShowName(SwordEntity entity) {
        return false;
    }
}
