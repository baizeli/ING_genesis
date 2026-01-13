package com.baizeli.eternisstarrysky.render.entity;

import com.baizeli.eternisstarrysky.render.ModRenderType;
import com.baizeli.eternisstarrysky.render.ModShaders;
import com.baizeli.eternisstarrysky.render.cosmic.AvaritiaShaders;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Items.ModItems;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;

public class HaloRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public HaloRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    public static boolean isEquippedAndVisible(@Nullable LivingEntity entity, Item item) {
        return entity != null && CuriosApi.getCuriosInventory(entity)
                .map(inv -> inv.findFirstCurio(item)
                        .map(result -> result.slotContext().visible())
                        .orElse(false))
                .orElse(false);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (isEquippedAndVisible(player, ModItems.GENESIS_CURSE.get())) {
            poseStack.pushPose();

            ModelPart head = this.getParentModel().getHead();
            poseStack.translate(head.x / 16.0F, head.y / 16.0F, head.z / 16.0F);

            setHaloShader(ageInTicks);
            poseStack.translate(0.0F, -0.35F, 0.5F);

            float time = ageInTicks / 40F;
            float baseRotation = (time * 2f) % (2 * (float) Math.PI);

            float haloThickness = 0.3f; // 厚度
            float haloSize = 2.4f; // 大小
            float backgroundZ = 0f;

            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, backgroundZ);
            poseStack.mulPose(Axis.ZP.rotation(baseRotation));
            renderThickSixPointedStar(poseStack, bufferSource, haloSize * 1.1f, haloThickness * 1.1f, false);
            renderThickSixPointedStar(poseStack, bufferSource, haloSize * 0.9f, haloThickness * 0.9f, false);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, backgroundZ);
            poseStack.mulPose(Axis.ZP.rotation(baseRotation + (float)Math.toRadians(180)));
            renderThickSixPointedStar(poseStack, bufferSource, haloSize * 1.07f, haloThickness * 1.07f, false);
            renderThickSixPointedStar(poseStack, bufferSource, haloSize * 0.93f, haloThickness * 0.93f, false);
            poseStack.popPose();

            float foregroundZ = 0.01f;

            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, foregroundZ);
            poseStack.mulPose(Axis.ZP.rotation(baseRotation));
            renderThickSixPointedStar(poseStack, bufferSource, haloSize, haloThickness, true);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, foregroundZ);
            poseStack.mulPose(Axis.ZP.rotation(baseRotation + (float)Math.toRadians(180)));
            renderThickSixPointedStar(poseStack, bufferSource, haloSize, haloThickness, true);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, foregroundZ);
            poseStack.mulPose(Axis.ZP.rotation(baseRotation));
            renderThickSixPointedStar(poseStack, bufferSource, haloSize, haloThickness, true);
            poseStack.popPose();

            float foregroundZ1 = -0.01f;

            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, foregroundZ1);
            poseStack.mulPose(Axis.ZP.rotation(baseRotation));
            renderThickSixPointedStar(poseStack, bufferSource, haloSize, haloThickness, true);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, foregroundZ1);
            poseStack.mulPose(Axis.ZP.rotation(baseRotation + (float)Math.toRadians(180)));
            renderThickSixPointedStar(poseStack, bufferSource, haloSize, haloThickness, true);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, foregroundZ1);
            poseStack.mulPose(Axis.ZP.rotation(baseRotation));
            renderThickSixPointedStar(poseStack, bufferSource, haloSize, haloThickness, true);
            poseStack.popPose();

            poseStack.popPose();
        }
    }

    public static void setHaloShader(float pk) {
        ModShaders.setTime(ModShaders.getHaloShader(), pk / 10F);
        ModShaders.setScreenSize(ModShaders.getHaloShader());

        Minecraft mc = Minecraft.getInstance();
        RenderTarget mainTarget = Minecraft.instance.mainRenderTarget;

        float yaw = 0.0F;
        float pitch = 0.0F;
        float screenWidth = (float)mainTarget.width;
        float screenHeight = (float)mainTarget.height;

        if (mc.player != null) {
            yaw = (float) (mc.player.getYRot() * 2.0F * Math.PI / 360.0);
            pitch = -(float) (mc.player.getXRot() * 2.0F * Math.PI / 360.0);
        }

        AvaritiaShaders.useType.set(10);
        AvaritiaShaders.cosmicTime.set((System.currentTimeMillis() - AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(AvaritiaShaders.inventoryRender ? 30F : 0.6F);
        AvaritiaShaders.cosmicOpacity.set(1.0F);
        AvaritiaShaders.cosmicIs2D.set(AvaritiaShaders.inventoryRender ? 1 : 0);
        AvaritiaShaders.cosmicScreenSize.set(screenWidth, screenHeight);

        // 准备纹理UV
        for (int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "item/misc/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }
        AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
    }

    public static void renderThickSixPointedStar(PoseStack poseStack, MultiBufferSource buffer, float size, float thickness, boolean type) {
        float outerRadius = size / 2.0f;
        float innerRadius = Math.max(outerRadius - thickness, outerRadius * 0.25f);
        float midRadius = outerRadius * 0.4f; // 六芒星内凹点的半径

        VertexConsumer consumer;
        if (type) consumer = buffer.getBuffer(ModRenderType.halo);
        else consumer = buffer.getBuffer(ModRenderType.cosmic_world);

        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        int packedLight = 15728880;
        int packedOverlay = 0;

        float[] outerVerticesX = new float[12];
        float[] outerVerticesY = new float[12];

        for (int i = 0; i < 12; i++) {
            float angle = (float) (i * Math.PI / 6.0);
            if (i % 2 == 0) {
                outerVerticesX[i] = (float) Math.cos(angle) * outerRadius;
                outerVerticesY[i] = (float) Math.sin(angle) * outerRadius;
            } else {
                outerVerticesX[i] = (float) Math.cos(angle) * midRadius;
                outerVerticesY[i] = (float) Math.sin(angle) * midRadius;
            }
        }

        float[] innerVerticesX = new float[6];
        float[] innerVerticesY = new float[6];

        for (int i = 0; i < 6; i++) {
            float angle = (float) (i * Math.PI / 3.0); // 每60度一个点
            innerVerticesX[i] = (float) Math.cos(angle) * innerRadius;
            innerVerticesY[i] = (float) Math.sin(angle) * innerRadius;
        }

        float nx = 0.0f, ny = 0.0f, nz = 1.0f;

        for (int i = 0; i < 12; i++) {
            int next = (i + 1) % 12;

            int innerIndex = findClosestInnerVertex(outerVerticesX[i], outerVerticesY[i], innerVerticesX, innerVerticesY);
            int innerNext = findClosestInnerVertex(outerVerticesX[next], outerVerticesY[next], innerVerticesX, innerVerticesY);

            // 绘制俩三角
            addHaloVertex(consumer, pose, normal, outerVerticesX[i], outerVerticesY[i], 0.0f, packedLight, packedOverlay, nx, ny, nz);
            addHaloVertex(consumer, pose, normal, outerVerticesX[next], outerVerticesY[next], 0.0f, packedLight, packedOverlay, nx, ny, nz);
            addHaloVertex(consumer, pose, normal, innerVerticesX[innerIndex], innerVerticesY[innerIndex], 0.0f, packedLight, packedOverlay, nx, ny, nz);

            addHaloVertex(consumer, pose, normal, outerVerticesX[next], outerVerticesY[next], 0.0f, packedLight, packedOverlay, nx, ny, nz);
            addHaloVertex(consumer, pose, normal, innerVerticesX[innerNext], innerVerticesY[innerNext], 0.0f, packedLight, packedOverlay, nx, ny, nz);
            addHaloVertex(consumer, pose, normal, innerVerticesX[innerIndex], innerVerticesY[innerIndex], 0.0f, packedLight, packedOverlay, nx, ny, nz);
        }

        if (buffer instanceof MultiBufferSource.BufferSource bs) bs.endBatch();
    }

    private static int findClosestInnerVertex(float x, float y, float[] innerX, float[] innerY) {
        int closest = 0;
        float minDistance = Float.MAX_VALUE;

        for (int i = 0; i < innerX.length; i++) {
            float dx = x - innerX[i];
            float dy = y - innerY[i];
            float distance = dx * dx + dy * dy;

            if (distance < minDistance) {
                minDistance = distance;
                closest = i;
            }
        }

        return closest;
    }

    private static void addHaloVertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, float x, float y, float z, int packedLight, int packedOverlay, float nx, float ny, float nz) {
        consumer.vertex(pose, x, y, z).color(255, 255, 255, 255).normal(normal, nx, ny, nz).endVertex();
    }
}