package miku.united_as_one.genesis.util;

import miku.united_as_one.genesis.render.cosmic.AvaritiaShaders;
import miku.united_as_one.genesis.Genesis;
import com.mojang.blaze3d.pipeline.RenderTarget;
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
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static com.mojang.math.Axis.*;

public class RenderUtils {
    private static final ResourceLocation TEX = new ResourceLocation(Genesis.MODID, "textures/misc/white.png");
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "item/mask/background");

    public static RenderType createTexturedQuadType(ResourceLocation texture) {
        return RenderType.create("textured_quad_no_cull",
                DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
                VertexFormat.Mode.TRIANGLES,
                256,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .createCompositeState(false));
    }

    public static RenderType cosmicBackground(ResourceLocation tex) {
        return RenderType.create("", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
                .setTextureState(AvaritiaShaders.RenderStateShardAccess.COSMIC_TEXTURE_ISOLATED)
                .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(RenderType.LIGHTMAP)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderType.NO_CULL)
                .createCompositeState(true));
    }

    public static RenderType cosmicTriangles() {
        return RenderType.create("", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.TRIANGLES, 256, RenderType.CompositeState.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
                .setTextureState(AvaritiaShaders.RenderStateShardAccess.COSMIC_TEXTURE_ISOLATED)
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

    public static void renderCosmicBackground(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture, float width, float height, double x, double y, int light, int useType) {
        if (texture == null) {
            texture = BACKGROUND;
        }
        RenderTarget mainTarget = Minecraft.instance.mainRenderTarget;

        float yaw = 0.0F;
        float pitch = 0.0F;
        float screenWidth = (float)mainTarget.width;
        float screenHeight = (float)mainTarget.height;
        float time = (System.currentTimeMillis() - AvaritiaShaders.renderTime) / 1000.0F;
        float opacity = (float) (0.7F + 0.3F * MathUtils.sin(time * 2.5F));

        AvaritiaShaders.useType.set(useType);
        AvaritiaShaders.cosmicTime.set(time);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(50F);
        AvaritiaShaders.cosmicOpacity.set(opacity);
        AvaritiaShaders.cosmicColor.set(new Vector4f(0.1F, 0.1F, 0.1F, 1.33F));
        AvaritiaShaders.cosmicScreenSize.set(screenWidth, screenHeight);
        AvaritiaShaders.cosmicIs2D.set(1);

        for (int i = 0; i < 10; ++i)
        {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "item/misc/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }

        AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);

        float u0 = sprite.getU0();  // 左边界
        float u1 = sprite.getU1();  // 右边界
        float v0 = sprite.getV0();  // 上边界
        float v1 = sprite.getV1();  // 下边界

        VertexConsumer consumer = buffer.getBuffer(cosmicBackground(InventoryMenu.BLOCK_ATLAS));

        renderTextured(poseStack, consumer, width, height, x, y, light, u0, u1, v0, v1);

        if (buffer instanceof MultiBufferSource.BufferSource bufferSource) {
            bufferSource.endLastBatch();
        }
    }

    private static void renderTextured(PoseStack poseStack, VertexConsumer consumer, float width, float height,
                                       double x, double y, int light,
                                       float u0, float u1, float v0, float v1) {
        poseStack.pushPose();
        poseStack.translate(x, y, 0);

        float w = width / 2f;
        float h = height / 2f;

        Matrix4f matrix = poseStack.last().pose();

        consumer.vertex(matrix, -w, -h, 0).color(255, 255, 255, 255).uv(u0, v1).uv2(light).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix,  w, -h, 0).color(255, 255, 255, 255).uv(u1, v1).uv2(light).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix,  w,  h, 0).color(255, 255, 255, 255).uv(u1, v0).uv2(light).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix, -w,  h, 0).color(255, 255, 255, 255).uv(u0, v0).uv2(light).normal(0, 0, 1).endVertex();

        poseStack.popPose();
    }

    public static void renderCosmicEllipsoid(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture,
                                             float radiusX, float radiusY, float radiusZ,
                                             double x, double y, double z, int light, int useType) {
        if (texture == null) {
            texture = BACKGROUND;
        }

        RenderTarget mainTarget = Minecraft.instance.mainRenderTarget;
        float time = (System.currentTimeMillis() - AvaritiaShaders.renderTime) / 1000.0F;
        float opacity = (float) (0.7F + 0.3F * MathUtils.sin(time * 2.5F));

        // 设置Cosmic着色器参数
        AvaritiaShaders.useType.set(useType);
        AvaritiaShaders.cosmicTime.set(time);
        AvaritiaShaders.cosmicYaw.set(0.0F);
        AvaritiaShaders.cosmicPitch.set(0.0F);
        AvaritiaShaders.cosmicExternalScale.set(AvaritiaShaders.inventoryRender ? 50F : 1F);
        AvaritiaShaders.cosmicOpacity.set(opacity);
        AvaritiaShaders.cosmicColor.set(new Vector4f(0.1F, 0.1F, 0.1F, 1.33F));
        AvaritiaShaders.cosmicScreenSize.set((float)mainTarget.width, (float)mainTarget.height);
        AvaritiaShaders.cosmicIs2D.set(AvaritiaShaders.inventoryRender ? 1 : 0); // 设置为3D模式

        for (int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "item/misc/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }
        AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);

        float u0 = sprite.getU0();  // 左边界
        float u1 = sprite.getU1();  // 右边界
        float v0 = sprite.getV0();  // 上边界
        float v1 = sprite.getV1();  // 下边界

        // 渲染椭圆体
        VertexConsumer consumer = buffer.getBuffer(cosmicTriangles());
        poseStack.pushPose();
        poseStack.translate(x, y, z);

        renderEllipsoid0(poseStack, consumer, radiusX, radiusY, radiusZ, light, u0, u1, v1, v0);

        poseStack.popPose();

        if (buffer instanceof MultiBufferSource.BufferSource bufferSource) {
            bufferSource.endLastBatch();
        }
    }

    private static void renderEllipsoid0(PoseStack poseStack, VertexConsumer consumer,
                                         float radiusX, float radiusY, float radiusZ, int light,
                                         float u0, float u1, float v0, float v1) {
        Matrix4f mat = poseStack.last().pose();
        Matrix3f norm = poseStack.last().normal();

        int stacks = 40;   // 纬度细分
        int slices = 60;   // 经度细分

        for (int i = 0; i < stacks; i++) {
            float theta0 = (float) (Math.PI * i / stacks);
            float theta1 = (float) (Math.PI * (i + 1) / stacks);

            float cosTheta0 = Mth.cos(theta0);
            float cosTheta1 = Mth.cos(theta1);
            float sinTheta0 = Mth.sin(theta0);
            float sinTheta1 = Mth.sin(theta1);

            float y0 = cosTheta0 * radiusY;
            float y1 = cosTheta1 * radiusY;

            // 计算V坐标（纬度方向）
            float v00 = v0 + (v1 - v0) * (float)i / stacks;
            float v11 = v0 + (v1 - v0) * (float)(i + 1) / stacks;

            for (int j = 0; j < slices; j++) {
                float phi0 = (float) (2 * Math.PI * j / slices);
                float phi1 = (float) (2 * Math.PI * (j + 1) / slices);

                // 计算U坐标（经度方向）
                float u00 = u0 + (u1 - u0) * (float)j / slices;
                float u11 = u0 + (u1 - u0) * (float)(j + 1) / slices;

                float cosPhi0 = Mth.cos(phi0);
                float sinPhi0 = Mth.sin(phi0);
                float cosPhi1 = Mth.cos(phi1);
                float sinPhi1 = Mth.sin(phi1);

                float x0 = cosPhi0 * sinTheta0 * radiusX;
                float z0 = sinPhi0 * sinTheta0 * radiusZ;
                float x1 = cosPhi1 * sinTheta0 * radiusX;
                float z1 = sinPhi1 * sinTheta0 * radiusZ;

                float x2 = cosPhi0 * sinTheta1 * radiusX;
                float z2 = sinPhi0 * sinTheta1 * radiusZ;
                float x3 = cosPhi1 * sinTheta1 * radiusX;
                float z3 = sinPhi1 * sinTheta1 * radiusZ;

                // 法线（针对椭圆体的正确法线计算）
                float nx0 = cosPhi0 * sinTheta0 / radiusX;
                float ny0 = cosTheta0 / radiusY;
                float nz0 = sinPhi0 * sinTheta0 / radiusZ;
                float len0 = Mth.sqrt(nx0 * nx0 + ny0 * ny0 + nz0 * nz0);
                nx0 /= len0; ny0 /= len0; nz0 /= len0;

                float nx1 = cosPhi1 * sinTheta0 / radiusX;
                float ny1 = cosTheta0 / radiusY;
                float nz1 = sinPhi1 * sinTheta0 / radiusZ;
                float len1 = Mth.sqrt(nx1 * nx1 + ny1 * ny1 + nz1 * nz1);
                nx1 /= len1; ny1 /= len1; nz1 /= len1;

                float nx2 = cosPhi0 * sinTheta1 / radiusX;
                float ny2 = cosTheta1 / radiusY;
                float nz2 = sinPhi0 * sinTheta1 / radiusZ;
                float len2 = Mth.sqrt(nx2 * nx2 + ny2 * ny2 + nz2 * nz2);
                nx2 /= len2; ny2 /= len2; nz2 /= len2;

                float nx3 = cosPhi1 * sinTheta1 / radiusX;
                float ny3 = cosTheta1 / radiusY;
                float nz3 = sinPhi1 * sinTheta1 / radiusZ;
                float len3 = Mth.sqrt(nx3 * nx3 + ny3 * ny3 + nz3 * nz3);
                nx3 /= len3; ny3 /= len3; nz3 /= len3;

                // 第一个三角形
                putVertexEllipsoid(mat, norm, consumer, x0, y0, z0, nx0, ny0, nz0, u00, v00, light);
                putVertexEllipsoid(mat, norm, consumer, x1, y0, z1, nx1, ny1, nz1, u11, v00, light);
                putVertexEllipsoid(mat, norm, consumer, x2, y1, z2, nx2, ny2, nz2, u00, v11, light);

// 第二个三角形
                putVertexEllipsoid(mat, norm, consumer, x1, y0, z1, nx1, ny1, nz1, u11, v00, light);
                putVertexEllipsoid(mat, norm, consumer, x3, y1, z3, nx3, ny3, nz3, u11, v11, light);
                putVertexEllipsoid(mat, norm, consumer, x2, y1, z2, nx2, ny2, nz2, u00, v11, light);
            }
        }
    }

    private static void putVertexEllipsoid(Matrix4f mat, Matrix3f norm, VertexConsumer c,
                                           float x, float y, float z, float nx, float ny, float nz,
                                           float u, float v, int light) {
        c.vertex(mat, x, y, z).color(255, 255, 255, 255).uv(u, v).uv2(light).normal(norm, nx, ny, nz).endVertex();
    }

    private static void normalize(float[] n) {
        float len = Mth.sqrt(n[0] * n[0] + n[1] * n[1] + n[2] * n[2]);
        n[0] /= len; n[1] /= len; n[2] /= len;
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

    public static void renderFakeItem(ItemEntity item, ItemEntityRenderer renderer, PoseStack pose, MultiBufferSource buffers, float partialTick, int packedLight) {
        pose.pushPose();
        pose.scale(4F,4F, 4F);
        try {
            // 一个方块放大4倍正好是放置在世界中的大小
            ItemStack stack = item.getItem();
            int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
            renderer.random.setSeed(seed);

            BakedModel model = renderer.itemRenderer.getModel(stack, item.level(), null, item.getId());
            boolean gui3d = model.isGui3d();
            int amount = renderer.getRenderAmount(stack);

            if (!gui3d) {
                float dx = -0f * (amount - 1) * 0.5f;
                float dz = -0.09375f * (amount - 1) * 0.5f;
                pose.translate(dx, 0, dz);
            }

            for (int i = 0; i < amount; i++) {
                pose.pushPose();
                if (i > 0) {
                    if (gui3d) {
                        float sx = (renderer.random.nextFloat() * 2 - 1) * 0.15f;
                        float sy = (renderer.random.nextFloat() * 2 - 1) * 0.15f;
                        float sz = (renderer.random.nextFloat() * 2 - 1) * 0.15f;
                        if (renderer.shouldSpreadItems()) pose.translate(sx, sy, sz);
                    } else {
                        float sx = (renderer.random.nextFloat() * 2 - 1) * 0.15f * 0.5f;
                        float sy = (renderer.random.nextFloat() * 2 - 1) * 0.15f * 0.5f;
                        if (renderer.shouldSpreadItems()) pose.translate(sx, sy, 0);
                    }
                }

                renderer.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, pose, buffers, packedLight, OverlayTexture.NO_OVERLAY, model);

                pose.popPose();
                if (!gui3d) pose.translate(0, 0, 0.09375);
            }
        } finally {
            pose.popPose();
        }
    }

    public static <T extends LivingEntity> void fixRot(PoseStack poseStack, LivingEntity entity, float partialTick) {
        poseStack.mulPose(Axis.YP.rotationDegrees(-Mth.lerp(partialTick, entity.yBodyRotO, entity.yBodyRot)));
    }

    public static float[] getRainbowColor(float rangeSeconds) {
        float time = (float)(System.currentTimeMillis() % (long)(rangeSeconds * 1000.0F)) / (rangeSeconds * 1000.0F);
        float hue = time * 360.0F;
        return hsvToRgb(hue, 1.0F, 1.0F);
    }

    private static float[] hsvToRgb(float h, float s, float v) {
        float c = v * s;
        float x = c * (1.0F - Math.abs(h / 60.0F % 2.0F - 1.0F));
        float m = v - c;
        float r;
        float g;
        float b;
        if (h < 60.0F) {
            r = c;
            g = x;
            b = 0.0F;
        } else if (h < 120.0F) {
            r = x;
            g = c;
            b = 0.0F;
        } else if (h < 180.0F) {
            r = 0.0F;
            g = c;
            b = x;
        } else if (h < 240.0F) {
            r = 0.0F;
            g = x;
            b = c;
        } else if (h < 300.0F) {
            r = x;
            g = 0.0F;
            b = c;
        } else {
            r = c;
            g = 0.0F;
            b = x;
        }

        return new float[]{r + m, g + m, b + m};
    }

    public static void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity entity, float partialTick) {
        float height = 10;
        float angle = (System.currentTimeMillis() % 360000L) / 1000F * 90F;
        poseStack.pushPose();
        fixRot(poseStack, entity, partialTick);
        poseStack.translate(0, -5.0F, 0);
        float halfH = height / 2.0F;
        float r = 1.0F, g = 1.0F, b = 1.0F, a = 0.3F;
        var color = getRainbowColor(3);
        r = color[0];
        g = color[1];
        b = color[2];
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEX, true));
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(angle));
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
            poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
            drawCylinderSide(poseStack, vc, 10 + (i * 0.3F), halfH + (i * 0.04F), 16, r, g, b, a, packedLight);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private static void drawCylinderSide(PoseStack poseStack, VertexConsumer vc, float range, float halfH, int seg, float r, float g, float b, float a, int packedLight) {
        PoseStack.Pose m = poseStack.last();
        for (int i = 0; i < seg; i++) {
            float a0 = (float) (2 * Math.PI * (i / (float) seg));
            float a1 = (float) (2 * Math.PI * ((i + 1F) / seg));
            float x0 = Mth.cos(a0) * range;
            float z0 = Mth.sin(a0) * range;
            float x1 = Mth.cos(a1) * range;
            float z1 = Mth.sin(a1) * range;
            float U = halfH;
            float D = -halfH;
            vc.vertex(m.pose(), x0, U, z0).color(r, g, b, a).uv(0, 0).overlayCoords(0, 10).uv2(packedLight).normal(m.normal(), x0, 0, z0).endVertex();
            vc.vertex(m.pose(), x0, D, z0).color(r, g, b, a).uv(0, 1).overlayCoords(0, 10).uv2(packedLight).normal(m.normal(), x0, 0, z0).endVertex();
            vc.vertex(m.pose(), x1, D, z1).color(r, g, b, a).uv(1, 1).overlayCoords(0, 10).uv2(packedLight).normal(m.normal(), x1, 0, z1).endVertex();
            vc.vertex(m.pose(), x1, U, z1).color(r, g, b, a).uv(1, 0).overlayCoords(0, 10).uv2(packedLight).normal(m.normal(), x1, 0, z1).endVertex();
        }
    }
}