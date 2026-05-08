package miku.united_as_one.genesis.client.render.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.render.cosmic.PerspectiveModelState;
import miku.united_as_one.genesis.registries.EffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShieldRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private List<BakedQuad> cachedQuads = null;

    public ShieldRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (player.hasEffect(EffectRegistry.WARPED_BARRIER.get()) && player.getAbsorptionAmount() > 0.0F) {
            Minecraft mc = Minecraft.getInstance();

            // 初始化缓存
            if (this.cachedQuads == null) {
                TextureAtlasSprite sprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                        .apply(ResourceLocation.fromNamespaceAndPath("iron_spells_genesis", "item/shield"));
                this.cachedQuads = new ArrayList<>();
                List<BlockElement> unbaked = new ItemModelGenerator().processFrames(0, "layer0", sprite.contents());
                FaceBakery bakery = new FaceBakery();
                for (BlockElement element : unbaked) {
                    for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                        this.cachedQuads.add(bakery.bakeQuad(
                                element.from, element.to, entry.getValue(), sprite, entry.getKey(),
                                new PerspectiveModelState(ImmutableMap.of()), element.rotation, element.shade,
                                ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "dynamic")
                        ));
                    }
                }
            }

            float orbitRadius = 0.9F;
            float orbitSpeed = 5.0F;
            float bobbingSpeed = 0.15F;
            float bobbingAmount = 0.25F;
            float baseAngle = ageInTicks * orbitSpeed;
            float[] angleOffsets = {0F, 120F, -120F};

            for (float angleOffset : angleOffsets) {
                poseStack.pushPose();
                float orbitAngleDeg = baseAngle + angleOffset;
                float bobbingY = (float) Math.sin((ageInTicks * bobbingSpeed) + Math.toRadians(angleOffset)) * bobbingAmount;
                poseStack.translate(0, 0.7F + bobbingY, 0);
                if (player.isCrouching()) {
                    poseStack.translate(0, 0.25F, 0);
                }
                float orbitRad = (float) Math.toRadians(orbitAngleDeg);
                float ox = (float) (orbitRadius * Math.cos(orbitRad));
                float oz = (float) (orbitRadius * Math.sin(orbitRad));
                poseStack.translate(ox, 0, oz);
                poseStack.mulPose(Axis.YP.rotationDegrees(-orbitAngleDeg - 90F));
                poseStack.mulPose(Axis.XP.rotationDegrees(10F));
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
                poseStack.translate(-0.5F, -0.5F, -0.5F);

                VertexConsumer buffer = bufferSource.getBuffer(Sheets.translucentCullBlockSheet());
                mc.getItemRenderer().renderQuadList(poseStack, buffer, this.cachedQuads, ItemStack.EMPTY, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

                poseStack.popPose();
            }
        }
    }
}