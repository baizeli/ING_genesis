package com.baizeli.eternisstarrysky.util;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.cosmicRender.AvaritiaShaders;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static com.mojang.math.Axis.*;

public class RenderUtils {
    public static final ResourceLocation cosmic = new ResourceLocation(EternisStarrySky.MODID, "textures/shader/cosmictexture.png");
    private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
    private static final FaceBakery FACE_BAKERY = new FaceBakery();

    public static RenderType createTexturedQuadType(ResourceLocation texture) {
        return RenderType.create("textured_quad_no_cull",
                DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
                VertexFormat.Mode.QUADS,
                256,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .createCompositeState(false));
    }

    public static RenderType maskType(ResourceLocation tex) {
        return RenderType.create("", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
                .setTextureState(new RenderStateShard.TextureStateShard(tex, false, false))
                .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(RenderType.LIGHTMAP)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderType.NO_CULL)
                .createCompositeState(true));
    }

    public static RenderType END_PORTAL(ResourceLocation resourceLocation) {
        return RenderType.create("2", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
                RenderType.CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(new RenderType.ShaderStateShard(GameRenderer::getRendertypeEndPortalShader))
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setTextureState(RenderType.MultiTextureStateShard.builder()
                                .add(resourceLocation, false, false)
                                .add(resourceLocation, false, false)
                                .build())
                        .createCompositeState(true));
    }

    public static void renderItemRings(PoseStack poseStack, MultiBufferSource buffer, float angle, Axis axis, int r, int g, int b, double x, double y, double z) {
        VertexConsumer line = buffer.getBuffer(RenderType.LINES);

        poseStack.pushPose();
        poseStack.translate(x, y, z);

        if (axis == YP) {
            poseStack.mulPose(XP.rotation((float) Math.toRadians(angle)));
            renderCircleWire(poseStack.last().pose(), line, YP, r, g, b);
        } else if (axis == XP) {
            poseStack.mulPose(XP.rotationDegrees(90));
            poseStack.mulPose(YP.rotation((float) Math.toRadians(angle)));
            renderCircleWire(poseStack.last().pose(), line, XP, r, g, b);
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(ZP.rotationDegrees(angle));
            renderCircleWire(poseStack.last().pose(), line, ZP, r, g, b);
        }

        poseStack.popPose();
    }

    public static void renderCircleWire(Matrix4f mat, VertexConsumer vc, Axis axis, int r, int g, int b) {
        int segments = 10000;
        float radius = 0.5F;
        float rf = r / 255f;
        float gf = g / 255f;
        float bf = b / 255f;

        for (int i = 0; i < segments; i++) {
            float angle1 = i * 2 * (float) Math.PI / segments;
            float angle2 = (i + 1) * 2 * (float) Math.PI / segments;

            float x1 = radius * Mth.cos(angle1);
            float y1 = radius * Mth.sin(angle1);
            float x2 = radius * Mth.cos(angle2);
            float y2 = radius * Mth.sin(angle2);

            if (axis == YP) {
                vc.vertex(mat, x1, 0, y1).color(rf, gf, bf, 1f).normal(1, 0, 0).endVertex();
                vc.vertex(mat, x2, 0, y2).color(rf, gf, bf, 1f).normal(0, 1, 0).endVertex();
            } else if (axis == XP) {
                vc.vertex(mat, 0, x1, y1).color(rf, gf, bf, 1f).normal(1, 0, 0).endVertex();
                vc.vertex(mat, 0, x2, y2).color(rf, gf, bf, 1f).normal(0, 1, 0).endVertex();
            } else if (axis == ZP) {
                vc.vertex(mat, x1, y1, 0).color(rf, gf, bf, 1f).normal(0, 0, 1).endVertex();
                vc.vertex(mat, x2, y2, 0).color(rf, gf, bf, 1f).normal(0, 0, 1).endVertex();
            }
        }
    }

    public static void renderWireCube(PoseStack poseStack, MultiBufferSource bufferIn, float halfSize, float angleDeg, Axis axis, int r, int g, int b, double x, double y, double z) {
        AABB box = new AABB(-halfSize, -halfSize, -halfSize, halfSize,  halfSize,  halfSize);
        VertexConsumer consumer = bufferIn.getBuffer(RenderType.LINES);

//        poseStack.last().pose().identity();
//        poseStack.last().normal().identity();

        poseStack.pushPose();
        poseStack.translate(x, y, z);

        if (axis == Axis.YP) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(Axis.YP.rotationDegrees(angleDeg));
        } else if (axis == Axis.XP) {
            poseStack.mulPose(Axis.XP.rotationDegrees(angleDeg));
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(ZP.rotationDegrees(angleDeg));
        }

        float rf = r / 255f;
        float gf = g / 255f;
        float bf = b / 255f;

        renderLineBox(poseStack, consumer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, rf, gf, bf, 0.8f, rf, gf, bf);

        poseStack.popPose();
    }

    public static void renderLineBox(PoseStack poseStack, VertexConsumer consumer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float redMin, float greenMin, float blueMin, float alpha, float redMax, float greenMax, float blueMax) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        float f = (float)minX;
        float f1 = (float)minY;
        float f2 = (float)minZ;
        float f3 = (float)maxX;
        float f4 = (float)maxY;
        float f5 = (float)maxZ;
        consumer.vertex(matrix4f, f, f1, f2).color(redMin, greenMax, blueMax, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f3, f1, f2).color(redMin, greenMax, blueMax, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f, f1, f2).color(redMax, greenMin, blueMax, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f, f4, f2).color(redMax, greenMin, blueMax, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f, f1, f2).color(redMax, greenMax, blueMin, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, f, f1, f5).color(redMax, greenMax, blueMin, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, f3, f1, f2).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f3, f4, f2).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f3, f4, f2).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f, f4, f2).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f, f4, f2).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, f, f4, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, f, f4, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f, f1, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f, f1, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f3, f1, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f3, f1, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        consumer.vertex(matrix4f, f3, f1, f2).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        consumer.vertex(matrix4f, f, f4, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f3, f4, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f3, f1, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f3, f4, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, f3, f4, f2).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, f3, f4, f5).color(redMin, greenMin, blueMin, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    }

    public static void renderWireTriangle(PoseStack poseStack, VertexConsumer consumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float rf, float gf, float bf, float alpha) {
        Matrix4f mat4 = poseStack.last().pose();
        Matrix3f mat3 = poseStack.last().normal();

        consumer.vertex(mat4, x1, y1, z1).color(rf, gf, bf, alpha).normal(mat3, 0, 0, 0).endVertex();
        consumer.vertex(mat4, x2, y2, z2).color(rf, gf, bf, alpha).normal(mat3, 0, 0, 0).endVertex();
        consumer.vertex(mat4, x2, y2, z2).color(rf, gf, bf, alpha).normal(mat3, 0, 0, 0).endVertex();
        consumer.vertex(mat4, x3, y3, z3).color(rf, gf, bf, alpha).normal(mat3, 0, 0, 0).endVertex();
        consumer.vertex(mat4, x3, y3, z3).color(rf, gf, bf, alpha).normal(mat3, 0, 0, 0).endVertex();
        consumer.vertex(mat4, x1, y1, z1).color(rf, gf, bf, alpha).normal(mat3, 0, 0, 0).endVertex();
    }

    public static void renderWireTriangle(PoseStack poseStack, MultiBufferSource buffer, float angleDeg, Axis axis, int r, int g, int b, double x, double y, double z) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.LINES);
        poseStack.pushPose();
        poseStack.translate(x, y, z);

        if (axis == Axis.YP) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(Axis.YP.rotationDegrees(angleDeg));
        } else if (axis == Axis.XP) {
            poseStack.mulPose(Axis.XP.rotationDegrees(angleDeg));
        } else {
            poseStack.mulPose(ZP.rotationDegrees(angleDeg));
        }

        float rf = r / 255f, gf = g / 255f, bf = b / 255f;
        float h = 0.866f;
        float x1 = 0, y1 = h * 2 / 3, z1 = 0;
        float x2 = -0.5f, y2 = -h / 3, z2 = 0;
        float x3 = 0.5f, y3 = -h / 3, z3 = 0;

        renderWireTriangle(poseStack, consumer, x1, y1, z1, x2, y2, z2, x3, y3, z3, rf, gf, bf, 0.8f);
        poseStack.popPose();
    }

    public static void renderWireStar6(PoseStack poseStack, MultiBufferSource buffer, float radius, float angleDeg, Axis axis, int r, int g, int b, float alpha, double x, double y, double z) {
        VertexConsumer vc = buffer.getBuffer(RenderType.LINES);

        poseStack.pushPose();
        poseStack.translate(x, y, z);

        if (axis == Axis.YP) {
            //poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(Axis.YP.rotationDegrees(angleDeg));
        } else if (axis == Axis.XP) {
            poseStack.mulPose(Axis.XP.rotationDegrees(angleDeg));
        } else { // ZP
            //poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(ZP.rotationDegrees(angleDeg));
        }

        float rf = r / 255f, gf = g / 255f, bf = b / 255f;
        float h = 0.866f * radius;
        float x1 = 0, y1 = h * 2 / 3, z1 = 0;
        float x2 = -0.5f * radius, y2 = -h / 3, z2 = 0;
        float x3 = 0.5f * radius, y3 = -h / 3, z3 = 0;

        renderWireTriangle(poseStack, vc, x1, y1, z1, x2, y2, z2, x3, y3, z3, rf, gf, bf, alpha);
        poseStack.mulPose(axis.rotationDegrees(180));
        renderWireTriangle(poseStack, vc, x1, y1, z1, x2, y2, z2, x3, y3, z3, rf, gf, bf, alpha);

        poseStack.popPose();
    }

    public static void renderWireStar6Orbit(PoseStack poseStack, MultiBufferSource buffer, float radius, float selfAngleDeg, Axis axis, int r, int g, int b, float alpha, double orbitRadius, float orbitAngleDeg, double x, double y, double z) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);

        float orbitRad = (float) Math.toRadians(orbitAngleDeg);
        float ox = (float) (orbitRadius * Math.cos(orbitRad));
        float oz = (float) (orbitRadius * Math.sin(orbitRad));
        poseStack.translate(ox, 0, oz);
        poseStack.mulPose(Axis.YP.rotationDegrees(-orbitAngleDeg));
        if (axis != ZP) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
        }
        renderWireStar6(poseStack, buffer, radius, selfAngleDeg, Axis.ZP, r, g, b, alpha, 0, 0, 0);

        poseStack.popPose();
    }

    public static void renderSkyRenderTexturedQuad(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture, float width, float height, float angleDeg, Axis axis, double x, double y, double z, int light) {
        AvaritiaShaders.cosmicOpacity.set(2f);
        if (AvaritiaShaders.inventoryRender) {
            AvaritiaShaders.cosmicExternalScale.set(25f);
        } else {
            AvaritiaShaders.cosmicExternalScale.set(1f);
        }

        final Minecraft mc = Minecraft.getInstance();
        float yaw = 0.0f;
        float pitch = 0.0f;
        float scale = 1f;

        float time = (System.currentTimeMillis() - AvaritiaShaders.renderTime) / 1000.0F;

        float uOffset = time * 0.05F % 1.0F;
        float vOffset = time * 0.03F % 1.0F;

        float opacity = (float) (0.7F + 0.3F * MathUtils.sin(time * 2.5F));

        AvaritiaShaders.cosmicTime.set(time);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(scale);
        AvaritiaShaders.cosmicOpacity.set(opacity);

        float[] uvs = new float[40];

        for (int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(EternisStarrySky.MODID, "misc/cosmic_" + i));
            uvs[i * 4] = sprite.getU0() + uOffset;
            uvs[i * 4 + 1] = sprite.getV0() + vOffset;
            uvs[i * 4 + 2] = sprite.getU1() + uOffset;
            uvs[i * 4 + 3] = sprite.getV1() + vOffset;
        }

        if (AvaritiaShaders.cosmicUVs != null) {
            AvaritiaShaders.cosmicUVs.set(uvs);
        }

        VertexConsumer consumer = new Material(InventoryMenu.BLOCK_ATLAS, texture).buffer(buffer, RenderUtils::maskType);

        renderTextured(poseStack, consumer, width, height, angleDeg, axis, x, y, z, light);
    }

    public static void renderNormalTexturedQuad(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture, float width, float height, float angleDeg, Axis axis, double x, double y, double z, int light) {
        VertexConsumer consumer = buffer.getBuffer(createTexturedQuadType(texture));
        renderTextured(poseStack, consumer, width, height, angleDeg, axis, x, y, z, light);
    }

    private static void renderTextured(PoseStack poseStack, VertexConsumer consumer, float width, float height, float angleDeg, Axis axis, double x, double y, double z, int light) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);

        if (axis == Axis.YP) {
            poseStack.mulPose(Axis.YP.rotationDegrees(angleDeg));
        } else if (axis == Axis.XP) {
            poseStack.mulPose(Axis.XP.rotationDegrees(angleDeg));
        } else {
            poseStack.mulPose(Axis.ZP.rotationDegrees(angleDeg));
        }

        float w = width / 2f;
        float h = height / 2f;

        Matrix4f matrix = poseStack.last().pose();
        consumer.vertex(matrix, -w, -h, 0).color(255, 255, 255, 255).uv(0, 1).uv2(light).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix,  w, -h, 0).color(255, 255, 255, 255).uv(1, 1).uv2(light).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix,  w,  h, 0).color(255, 255, 255, 255).uv(1, 0).uv2(light).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix, -w,  h, 0).color(255, 255, 255, 255).uv(0, 0).uv2(light).normal(0, 0, 1).endVertex();

        poseStack.popPose();
    }

    public static void renderSolidSphere(PoseStack poseStack, MultiBufferSource buffer, float radius, int r, int g, int b, int light, double x, double y, double z) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.solid());
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        renderSolidSphere0(poseStack, consumer, radius, r / 255f, g / 255f, b / 255f, 1f, light);
        poseStack.scale(-1, 1, 1);
        renderSolidSphere0(poseStack, consumer, radius, r / 255f, g / 255f, b / 255f, 1f, light);
        poseStack.popPose();
        if (buffer instanceof MultiBufferSource.BufferSource src && src.lastState.isPresent()) {
            src.endBatch(RenderType.solid());
        }
    }

    private static void renderSolidSphere0(PoseStack poseStack, VertexConsumer consumer, float radius, float rf, float gf, float bf, float alpha, int light) {
        Matrix4f mat = poseStack.last().pose();
        Matrix3f norm = poseStack.last().normal();

        int stacks = 80;   // 纬度细分
        int slices = 120;   // 经度细分

        for (int i = 0; i < stacks; i++) {
            float theta0 = (float) (Math.PI * i / stacks);
            float theta1 = (float) (Math.PI * (i + 1) / stacks);

            float y0  =  Mth.cos(theta0) * radius;
            float y1  =  Mth.cos(theta1) * radius;
            float sin0 = Mth.sin(theta0);
            float sin1 = Mth.sin(theta1);

            for (int j = 0; j < slices; j++) {
                float phi0 = (float) (2 * Math.PI * j / slices);
                float phi1 = (float) (2 * Math.PI * (j + 1) / slices);

                float x0 = Mth.cos(phi0) * sin0 * radius;
                float z0 = Mth.sin(phi0) * sin0 * radius;
                float x1 = Mth.cos(phi1) * sin0 * radius;
                float z1 = Mth.sin(phi1) * sin0 * radius;

                float x2 = Mth.cos(phi0) * sin1 * radius;
                float z2 = Mth.sin(phi0) * sin1 * radius;
                float x3 = Mth.cos(phi1) * sin1 * radius;
                float z3 = Mth.sin(phi1) * sin1 * radius;

                putVertex(mat, norm, consumer, x0, y0, z0, rf, gf, bf, alpha, light);
                putVertex(mat, norm, consumer, x1, y0, z1, rf, gf, bf, alpha, light);
                putVertex(mat, norm, consumer, x2, y1, z2, rf, gf, bf, alpha, light);

                putVertex(mat, norm, consumer, x1, y0, z1, rf, gf, bf, alpha, light);
                putVertex(mat, norm, consumer, x3, y1, z3, rf, gf, bf, alpha, light);
                putVertex(mat, norm, consumer, x2, y1, z2, rf, gf, bf, alpha, light);
            }
        }
    }

    private static void putVertex(Matrix4f mat, Matrix3f norm, VertexConsumer c, float x, float y, float z, float r, float g, float b, float a, int light) {
        float len = Mth.sqrt(x * x + y * y + z * z);
        float nx = x / len;
        float ny = y / len;
        float nz = z / len;

        c.vertex(mat, x, y, z).color(r, g, b, a).uv(0, 0).uv2(light).normal(norm, nx, ny, nz).endVertex();
    }

    public static void drawRenderTypeRect(float posX, float posY, float width, float height, RenderType renderType, Matrix4f matrix4f) {
        VertexConsumer vertexconsumer = Minecraft.getInstance().renderBuffers.bufferSource().getBuffer(renderType);

        posX -= 0.05F;
        posY -= 0.05F;
        float x2 = posX + width;
        float y2 = posY + height;
        x2 += 0.05F;
        y2 += 0.05F;
        vertexconsumer.vertex(matrix4f, posX, posY, (float) 0).color(0, 0, 0, 0).uv(0.0F, 1.0F).uv2(0, 0).normal(0, 0, 0).endVertex();
        vertexconsumer.vertex(matrix4f, posX, y2, (float) 0).color(0, 0, 0, 0).uv(0.0F, 1.0F).uv2(0, 0).normal(0, 0, 0).endVertex();
        vertexconsumer.vertex(matrix4f, x2, y2, (float) 0).color(0, 0, 0, 0).uv(0.0F, 1.0F).uv2(0, 0).normal(0, 0, 0).endVertex();
        vertexconsumer.vertex(matrix4f, x2, posY, (float) 0).color(0, 0, 0, 0).uv(0.0F, 1.0F).uv2(0, 0).normal(0, 0, 0).endVertex();
    }
}
