package com.baizeli.eternisstarrysky.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class AfterImageRenderer {
    public static void renderAfterImages(PoseStack poseStack, MultiBufferSource bufferSource, Camera camera, float partialTick) {
        if (playerRenderer == null) {
            Minecraft mc = Minecraft.getInstance();
            EntityRendererProvider.Context context = new EntityRendererProvider.Context(
                mc.getEntityRenderDispatcher(),
                mc.getItemRenderer(),
                mc.getBlockRenderer(),
                mc.gameRenderer.itemInHandRenderer,
                mc.getResourceManager(),
                mc.getEntityModels(),
                mc.font
            );
            playerRenderer = new PlayerRenderer(context, false);
        }

        Vec3 cameraPos = camera.getPosition();

        for (AfterImageData afterImage : AfterImageManager.getAfterImages()) {
            renderSingleAfterImage(poseStack, bufferSource, afterImage, cameraPos, partialTick);
        }
    }

    private static PlayerRenderer playerRenderer;

    private static void renderSingleAfterImage(PoseStack poseStack, MultiBufferSource bufferSource, AfterImageData afterImage, Vec3 cameraPos, float partialTick) {
        poseStack.pushPose();
        Vec3 originalPos = afterImage.getPosition();
        float yRot = afterImage.getYRot();
        Vec3 backwardPos = calculateBackwardPosition(originalPos, yRot, 1.0D);
        poseStack.translate(
            backwardPos.x - cameraPos.x,
            backwardPos.y - cameraPos.y,
            backwardPos.z - cameraPos.z
        );

        poseStack.mulPose(Axis.YP.rotationDegrees(-yRot + 180.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.5010000467300415D, 0.0D);
        renderTransparentPlayerModel(poseStack, bufferSource, afterImage);
        poseStack.popPose();
    }

    private static void renderTransparentPlayerModel(PoseStack poseStack, MultiBufferSource bufferSource, AfterImageData afterImage) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) return;
        Player originalPlayer = level.getPlayerByUUID(UUID.fromString(afterImage.getPlayerUUID()));
        if (originalPlayer instanceof AbstractClientPlayer) {
            AbstractClientPlayer clientPlayer = (AbstractClientPlayer) originalPlayer;
            PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(clientPlayer);
            PlayerModel<AbstractClientPlayer> model = (PlayerModel<AbstractClientPlayer>) playerRenderer.getModel();
            afterImage.applyPoseToModel(model);
            ResourceLocation skinTexture = afterImage.getSkinTexture();
            RenderType renderType = RenderType.entityTranslucent(skinTexture);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            model.leftArmPose = afterImage.getLeftArmPose();
            model.rightArmPose = afterImage.getRightArmPose();
            float alpha = afterImage.getAlpha();
            model.renderToBuffer(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);

            renderPlayerSkin(poseStack, bufferSource, model, afterImage, alpha);
            renderPlayerArmor(poseStack, bufferSource, model, afterImage, alpha);
            renderHeldItems(poseStack, bufferSource, model, afterImage, alpha);
            renderPlayerLayers(poseStack, bufferSource, model, afterImage.getSkinTexture(), alpha, 15728880, OverlayTexture.NO_OVERLAY);
        }
    }

    private static Vec3 calculateBackwardPosition(Vec3 originalPos, float yRotationDegrees, double distance) {
        double yRotRad = Math.toRadians(yRotationDegrees);

        double facingX = -Math.sin(yRotRad);
        double facingZ = Math.cos(yRotRad);

        double backwardX = originalPos.x - facingX * distance;
        double backwardZ = originalPos.z - facingZ * distance;
        return new Vec3(backwardX, originalPos.y, backwardZ);
    }

    private static void renderPlayerSkin(PoseStack poseStack, MultiBufferSource bufferSource, PlayerModel<AbstractClientPlayer> model, AfterImageData afterImage, float alpha) {
        ResourceLocation skinTexture = afterImage.getSkinTexture();
        RenderType renderType = RenderType.entityTranslucent(skinTexture);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        model.renderToBuffer(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
    }

    private static void renderPlayerLayers(PoseStack poseStack, MultiBufferSource bufferSource, PlayerModel<AbstractClientPlayer> model, ResourceLocation skinTexture, float alpha, int packedLight, int packedOverlay) {
        RenderType hatRenderType = RenderType.entityTranslucent(skinTexture);
        VertexConsumer hatConsumer = bufferSource.getBuffer(hatRenderType);
        model.hat.render(poseStack, hatConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha * 0.8F);
        model.jacket.render(poseStack, hatConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha * 0.8F);
        model.leftSleeve.render(poseStack, hatConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha * 0.8F);
        model.rightSleeve.render(poseStack, hatConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha * 0.8F);
        model.leftPants.render(poseStack, hatConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha * 0.8F);
        model.rightPants.render(poseStack, hatConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, alpha * 0.8F);
    }

    private static void renderPlayerArmor(PoseStack poseStack, MultiBufferSource bufferSource, PlayerModel<AbstractClientPlayer> model, AfterImageData afterImage, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        EntityModelSet entityModels = mc.getEntityModels();

        renderArmorPiece(poseStack, bufferSource, entityModels, model, afterImage.getHelmet(), EquipmentSlot.HEAD, alpha);
        renderArmorPiece(poseStack, bufferSource, entityModels, model, afterImage.getChestplate(), EquipmentSlot.CHEST, alpha);
        renderArmorPiece(poseStack, bufferSource, entityModels, model, afterImage.getLeggings(), EquipmentSlot.LEGS, alpha);
        renderArmorPiece(poseStack, bufferSource, entityModels, model, afterImage.getBoots(), EquipmentSlot.FEET, alpha);
    }

    private static ResourceLocation getArmorTextureSimple(ArmorMaterial material, EquipmentSlot slot, ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) item;
            Minecraft mc = Minecraft.getInstance();
            AbstractClientPlayer localPlayer = mc.player;

            if (localPlayer != null) {
                String type = (slot == EquipmentSlot.LEGS) ? "layer_2" : "layer_1";
                String customTexture = armorItem.getArmorTexture(stack, localPlayer, slot, type);

                if (customTexture != null && !customTexture.isEmpty()) {
                    return new ResourceLocation(customTexture);
                }
            }
        }

        String materialName = material.getName();
        String layer = (slot == EquipmentSlot.LEGS) ? "layer_2" : "layer_1";
        return new ResourceLocation("textures/models/armor/" + materialName + "_" + layer + ".png");
    }

    private static void renderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, EntityModelSet entityModels, PlayerModel<AbstractClientPlayer> playerModel, ItemStack armorStack, EquipmentSlot slot, float alpha) {
        if (armorStack.isEmpty()) return;
        Item item = armorStack.getItem();
        if (!(item instanceof ArmorItem)) {
            return;
        }
        ArmorItem armorItem = (ArmorItem) item;
        ArmorMaterial armorMaterial = armorItem.getMaterial();
        ResourceLocation armorTexture = getArmorTextureSimple(armorMaterial, slot, armorStack);

        HumanoidModel<LivingEntity> armorModel = getArmorModel(entityModels, slot, armorStack);
        if (armorModel == null) return;
        copyHumanoidProps(playerModel, armorModel);
        armorModel.crouching = false;

        switch (slot) {
            case HEAD:
                armorModel.head.visible = true;
                break;
            case CHEST:
                armorModel.body.visible = true;
                armorModel.rightArm.visible = true;
                armorModel.leftArm.visible = true;
                break;
            case LEGS:
                armorModel.body.visible = true;
                armorModel.rightLeg.visible = true;
                armorModel.leftLeg.visible = true;
                break;
            case FEET:
                armorModel.rightLeg.visible = true;
                armorModel.leftLeg.visible = true;
                break;
        }

        RenderType renderType = RenderType.entityTranslucent(armorTexture);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

        armorModel.renderToBuffer(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha * 0.9F);
    }

    public static void copyHumanoidProps(HumanoidModel<?> from, HumanoidModel<?> to) {
        if (from instanceof HumanoidModel && to instanceof HumanoidModel) {
            HumanoidModel<LivingEntity> fromModel = (HumanoidModel<LivingEntity>) from;
            HumanoidModel<LivingEntity> toModel = (HumanoidModel<LivingEntity>) to;
            fromModel.copyPropertiesTo(toModel);
        }
        
        to.leftArmPose = from.leftArmPose;
        to.rightArmPose = from.rightArmPose;
        to.crouching = from.crouching;
        to.head.copyFrom(from.head);
        to.hat.copyFrom(from.hat);
        to.body.copyFrom(from.body);
        to.rightArm.copyFrom(from.rightArm);
        to.leftArm.copyFrom(from.leftArm);
        to.rightLeg.copyFrom(from.rightLeg);
        to.leftLeg.copyFrom(from.leftLeg);
    }

    private static HumanoidModel<LivingEntity> getArmorModel(EntityModelSet entityModels, EquipmentSlot slot, ItemStack armorStack) {
        boolean innerArmor = (slot == EquipmentSlot.LEGS);

        if (innerArmor) {
            return new HumanoidModel<>(entityModels.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        } else {
            return new HumanoidModel<>(entityModels.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
        }
    }

    private static void renderHeldItems(PoseStack poseStack, MultiBufferSource bufferSource, PlayerModel<AbstractClientPlayer> model, AfterImageData afterImage, float alpha) {
        poseStack.pushPose();

        if (!afterImage.getMainHandItem().isEmpty()) {
            poseStack.pushPose();

            model.rightArm.translateAndRotate(poseStack);
            poseStack.translate(-0.0625D, 0.4375D, 0.0625D);

            adjustItemForArmPose(poseStack, afterImage.getRightArmPose(), true);

            renderItemWithAlpha(poseStack, bufferSource, afterImage.getMainHandItem(), alpha);

            poseStack.popPose();
        }

        if (!afterImage.getOffHandItem().isEmpty()) {
            poseStack.pushPose();

            model.leftArm.translateAndRotate(poseStack);
            poseStack.translate(0.0625D, 0.4375D, 0.0625D);

            adjustItemForArmPose(poseStack, afterImage.getLeftArmPose(), false);

            renderItemWithAlpha(poseStack, bufferSource, afterImage.getOffHandItem(), alpha);

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static void adjustItemForArmPose(PoseStack poseStack, HumanoidModel.ArmPose armPose, boolean rightHand) {
        switch (armPose) {
            case EMPTY:
                break;
            case ITEM:
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                if (rightHand) {
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                }
                break;
            case BLOCK:
                poseStack.translate(rightHand ? -0.25D : 0.25D, 0.1875D, 0.0D);
                poseStack.mulPose(Axis.XP.rotationDegrees(-20.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(rightHand ? -60.0F : 60.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rightHand ? -20.0F : 20.0F));
                break;
            case BOW_AND_ARROW:
                poseStack.mulPose(Axis.YP.rotationDegrees(rightHand ? -60.0F : 60.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rightHand ? -90.0F : 90.0F));
                break;
            case CROSSBOW_CHARGE:
            case CROSSBOW_HOLD:
                poseStack.mulPose(Axis.YP.rotationDegrees(rightHand ? -60.0F : 60.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                break;
            case SPYGLASS:
                poseStack.translate(rightHand ? -0.125D : 0.125D, 0.125D, 0.0D);
                poseStack.mulPose(Axis.XP.rotationDegrees(-45.0F));
                break;
        }
    }

    private static void renderItemWithAlpha(PoseStack poseStack, MultiBufferSource bufferSource, ItemStack itemStack, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        MultiBufferSource.BufferSource transparentBufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, 15728880, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, mc.level, 0);

        transparentBufferSource.endBatch();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}