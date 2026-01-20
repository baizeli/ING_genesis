package miku.united_as_one.genesis.render.entity;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.effect.spell.ModEffect;
import miku.united_as_one.genesis.render.cosmic.PerspectiveModelState;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShieldRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public ShieldRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (player.hasEffect(ModEffect.WARPED_BARRIER.get()) && player.getAbsorptionAmount() > 0.0F) {
            Minecraft mc = Minecraft.getInstance();

            // 获取sprite
            TextureAtlasSprite sprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                    .apply(ResourceLocation.fromNamespaceAndPath("iron_spells_genesis", "item/shield"));

            // 准备quads
            LinkedList<BakedQuad> quads = new LinkedList<>();
            List<BlockElement> unbaked = new ItemModelGenerator().processFrames(0, "layer0", sprite.contents());

            for (BlockElement element : unbaked) {
                for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                    quads.add(new FaceBakery().bakeQuad(
                            element.from, element.to, entry.getValue(), sprite, entry.getKey(),
                            new PerspectiveModelState(ImmutableMap.of()), element.rotation, element.shade,
                            ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "dynamic")
                    ));
                }
            }

            // 旋转动画参数
            float angle = (System.currentTimeMillis() % 360000L) / 1000F * 90F;
            float orbitRadius = 0.5F;

            float[] angleOffsets = {0F, 120F, -120F};

            for (float angleOffset : angleOffsets) {
                poseStack.pushPose();

                float orbitAngleDeg = angle * 0.75F + angleOffset;
                float orbitRad = (float) Math.toRadians(orbitAngleDeg);
                float ox = (float) (orbitRadius * Math.cos(orbitRad));
                float oz = (float) (orbitRadius * Math.sin(orbitRad));
                poseStack.translate(ox, 0, oz);

                poseStack.mulPose(Axis.YP.rotationDegrees(-orbitAngleDeg));
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));

                // 渲染盾牌
                VertexConsumer buffer = bufferSource.getBuffer(Sheets.translucentCullBlockSheet());
                mc.getItemRenderer().renderQuadList(poseStack, buffer, quads, ItemStack.EMPTY, packedLight, OverlayTexture.NO_OVERLAY);

                poseStack.popPose();
            }
        }
    }
}