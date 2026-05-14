package miku.united_as_one.genesis.client.renderer.entity.boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.model.entity.boss.HammerMobModel;
import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HammerMobGlowLayer extends RenderLayer<HammerMob, HammerMobModel<HammerMob>> {
    private static final ResourceLocation GLOW_TEXTURE = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "textures/entity/hammer_mob/glow.png");

    public HammerMobGlowLayer(RenderLayerParent<HammerMob, HammerMobModel<HammerMob>> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, MultiBufferSource buffer, int packedLight, @NotNull HammerMob entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.eyes(GLOW_TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}