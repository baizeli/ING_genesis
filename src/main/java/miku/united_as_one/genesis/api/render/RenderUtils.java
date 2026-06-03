package miku.united_as_one.genesis.api.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import miku.bai_ze_li.genesis.GenesisLib;
import miku.united_as_one.genesis.Genesis;
import miku.bai_ze_li.genesis.api.math.GenesisFastMath;
import miku.bai_ze_li.genesis.api.render.cosmic.AvaritiaShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public final class RenderUtils {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "item/mask/background");
    private static final RenderType COSMIC_BACKGROUND_RENDER_TYPE = RenderType.create("genesis_cosmic_background", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
            .setTextureState(AvaritiaShaders.RenderStateShardAccess.COSMIC_TEXTURE_ISOLATED)
            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setLightmapState(RenderType.LIGHTMAP)
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .setCullState(RenderType.NO_CULL)
            .createCompositeState(true));
    private static final RenderType COSMIC_TRIANGLES_RENDER_TYPE = RenderType.create("genesis_cosmic_triangles", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.TRIANGLES, 256, RenderType.CompositeState.builder()
            .setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
            .setTextureState(AvaritiaShaders.RenderStateShardAccess.COSMIC_TEXTURE_ISOLATED)
            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setLightmapState(RenderType.LIGHTMAP)
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .setCullState(RenderType.NO_CULL)
            .createCompositeState(true));

    private RenderUtils() {
    }

    public static RenderType cosmicBackground(ResourceLocation texture) {
        return COSMIC_BACKGROUND_RENDER_TYPE;
    }

    public static RenderType cosmicTriangles() {
        return COSMIC_TRIANGLES_RENDER_TYPE;
    }

    public static void renderCosmicBackground(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture, float width, float height, double x, double y, int light, int useType) {
        if (AvaritiaShaders.cosmicShader == null || AvaritiaShaders.useType == null) {
            return;
        }
        if (texture == null) {
            texture = BACKGROUND;
        }

        RenderTarget mainTarget = Minecraft.getInstance().getMainRenderTarget();
        float time = (AvaritiaShaders.renderTime + AvaritiaShaders.renderFrame) / 20.0F;
        float opacity = (float) (0.7F + 0.3F * GenesisFastMath.sin(time * 2.5F));

        AvaritiaShaders.useType.set(useType);
        AvaritiaShaders.cosmicTime.set(time);
        AvaritiaShaders.cosmicYaw.set(0.0F);
        AvaritiaShaders.cosmicPitch.set(0.0F);
        AvaritiaShaders.cosmicExternalScale.set(50.0F);
        AvaritiaShaders.cosmicOpacity.set(opacity);
        AvaritiaShaders.cosmicColor.set(new Vector4f(0.1F, 0.1F, 0.1F, 1.33F));
        AvaritiaShaders.cosmicScreenSize.set(mainTarget.width, mainTarget.height);
        AvaritiaShaders.cosmicIs2D.set(1);
        updateCosmicUvs();

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        VertexConsumer consumer = buffer.getBuffer(cosmicBackground(InventoryMenu.BLOCK_ATLAS));
        renderTextured(poseStack, consumer, width, height, x, y, light, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());

        if (buffer instanceof MultiBufferSource.BufferSource bufferSource) {
            bufferSource.endLastBatch();
        }
    }

    public static void renderCosmicEllipsoid(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture, float radiusX, float radiusY, float radiusZ, double x, double y, double z, int light, int useType) {
        if (AvaritiaShaders.cosmicShader == null || AvaritiaShaders.useType == null) {
            return;
        }
        if (texture == null) {
            texture = BACKGROUND;
        }

        RenderTarget mainTarget = Minecraft.getInstance().getMainRenderTarget();
        float time = (AvaritiaShaders.renderTime + AvaritiaShaders.renderFrame) / 20.0F;
        float opacity = (float) (0.7F + 0.3F * GenesisFastMath.sin(time * 2.5F));

        AvaritiaShaders.useType.set(useType);
        AvaritiaShaders.cosmicTime.set(time);
        AvaritiaShaders.cosmicYaw.set(0.0F);
        AvaritiaShaders.cosmicPitch.set(0.0F);
        AvaritiaShaders.cosmicExternalScale.set(AvaritiaShaders.inventoryRender ? 50.0F : 1.0F);
        AvaritiaShaders.cosmicOpacity.set(opacity);
        AvaritiaShaders.cosmicColor.set(new Vector4f(0.1F, 0.1F, 0.1F, 1.33F));
        AvaritiaShaders.cosmicScreenSize.set(mainTarget.width, mainTarget.height);
        AvaritiaShaders.cosmicIs2D.set(AvaritiaShaders.inventoryRender ? 1 : 0);
        updateCosmicUvs();

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        VertexConsumer consumer = buffer.getBuffer(cosmicTriangles());
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        renderEllipsoid(poseStack, consumer, radiusX, radiusY, radiusZ, light, sprite.getU0(), sprite.getU1(), sprite.getV1(), sprite.getV0());
        poseStack.popPose();

        if (buffer instanceof MultiBufferSource.BufferSource bufferSource) {
            bufferSource.endLastBatch();
        }
    }

    private static void updateCosmicUvs() {
        for (int i = 0; i < 10; i++) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocation.fromNamespaceAndPath(GenesisLib.MODID, "item/misc/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }
        AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
    }

    private static void renderTextured(PoseStack poseStack, VertexConsumer consumer, float width, float height, double x, double y, int light, float u0, float u1, float v0, float v1) {
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        float w = width / 2.0F;
        float h = height / 2.0F;
        Matrix4f matrix = poseStack.last().pose();
        consumer.vertex(matrix, -w, -h, 0).color(255, 255, 255, 255).uv(u0, v1).uv2(light).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix, w, -h, 0).color(255, 255, 255, 255).uv(u1, v1).uv2(light).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix, w, h, 0).color(255, 255, 255, 255).uv(u1, v0).uv2(light).normal(0, 0, 1).endVertex();
        consumer.vertex(matrix, -w, h, 0).color(255, 255, 255, 255).uv(u0, v0).uv2(light).normal(0, 0, 1).endVertex();
        poseStack.popPose();
    }

    private static void renderEllipsoid(PoseStack poseStack, VertexConsumer consumer, float radiusX, float radiusY, float radiusZ, int light, float u0, float u1, float v0, float v1) {
        Matrix4f mat = poseStack.last().pose();
        Matrix3f norm = poseStack.last().normal();
        int stacks = 40;
        int slices = 60;

        for (int i = 0; i < stacks; i++) {
            float theta0 = (float) (Math.PI * i / stacks);
            float theta1 = (float) (Math.PI * (i + 1) / stacks);
            float cosTheta0 = Mth.cos(theta0);
            float cosTheta1 = Mth.cos(theta1);
            float sinTheta0 = Mth.sin(theta0);
            float sinTheta1 = Mth.sin(theta1);
            float y0 = cosTheta0 * radiusY;
            float y1 = cosTheta1 * radiusY;
            float v00 = v0 + (v1 - v0) * i / stacks;
            float v11 = v0 + (v1 - v0) * (i + 1) / stacks;

            for (int j = 0; j < slices; j++) {
                float phi0 = (float) (2.0F * Math.PI * j / slices);
                float phi1 = (float) (2.0F * Math.PI * (j + 1) / slices);
                float u00 = u0 + (u1 - u0) * j / slices;
                float u11 = u0 + (u1 - u0) * (j + 1) / slices;

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

                putEllipsoidVertex(mat, norm, consumer, x0, y0, z0, cosPhi0 * sinTheta0 / radiusX, cosTheta0 / radiusY, sinPhi0 * sinTheta0 / radiusZ, u00, v00, light);
                putEllipsoidVertex(mat, norm, consumer, x1, y0, z1, cosPhi1 * sinTheta0 / radiusX, cosTheta0 / radiusY, sinPhi1 * sinTheta0 / radiusZ, u11, v00, light);
                putEllipsoidVertex(mat, norm, consumer, x2, y1, z2, cosPhi0 * sinTheta1 / radiusX, cosTheta1 / radiusY, sinPhi0 * sinTheta1 / radiusZ, u00, v11, light);
                putEllipsoidVertex(mat, norm, consumer, x1, y0, z1, cosPhi1 * sinTheta0 / radiusX, cosTheta0 / radiusY, sinPhi1 * sinTheta0 / radiusZ, u11, v00, light);
                putEllipsoidVertex(mat, norm, consumer, x3, y1, z3, cosPhi1 * sinTheta1 / radiusX, cosTheta1 / radiusY, sinPhi1 * sinTheta1 / radiusZ, u11, v11, light);
                putEllipsoidVertex(mat, norm, consumer, x2, y1, z2, cosPhi0 * sinTheta1 / radiusX, cosTheta1 / radiusY, sinPhi0 * sinTheta1 / radiusZ, u00, v11, light);
            }
        }
    }

    private static void putEllipsoidVertex(Matrix4f mat, Matrix3f norm, VertexConsumer consumer, float x, float y, float z, float nx, float ny, float nz, float u, float v, int light) {
        float len = Mth.sqrt(nx * nx + ny * ny + nz * nz);
        consumer.vertex(mat, x, y, z).color(255, 255, 255, 255).uv(u, v).uv2(light).normal(norm, nx / len, ny / len, nz / len).endVertex();
    }

    public static void renderFakeItem(ItemEntity item, ItemEntityRenderer renderer, PoseStack pose, MultiBufferSource buffers, float partialTick, int packedLight) {
        pose.pushPose();
        pose.scale(4.0F, 4.0F, 4.0F);
        try {
            ItemStack stack = item.getItem();
            int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
            renderer.random.setSeed(seed);

            BakedModel model = renderer.itemRenderer.getModel(stack, item.level(), null, item.getId());
            boolean gui3d = model.isGui3d();
            int amount = renderer.getRenderAmount(stack);

            if (!gui3d) {
                float dz = -0.09375F * (amount - 1) * 0.5F;
                pose.translate(0, 0, dz);
            }

            for (int i = 0; i < amount; i++) {
                pose.pushPose();
                if (i > 0) {
                    if (gui3d) {
                        float sx = (renderer.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float sy = (renderer.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float sz = (renderer.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        if (renderer.shouldSpreadItems()) {
                            pose.translate(sx, sy, sz);
                        }
                    } else {
                        float sx = (renderer.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        float sy = (renderer.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        if (renderer.shouldSpreadItems()) {
                            pose.translate(sx, sy, 0);
                        }
                    }
                }

                renderer.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, pose, buffers, packedLight, OverlayTexture.NO_OVERLAY, model);
                pose.popPose();
                if (!gui3d) {
                    pose.translate(0, 0, 0.09375);
                }
            }
        } finally {
            pose.popPose();
        }
    }
}
