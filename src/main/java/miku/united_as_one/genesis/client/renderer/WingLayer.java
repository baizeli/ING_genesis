package miku.united_as_one.genesis.client.renderer;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.model.WingModel;
import miku.united_as_one.genesis.effect.spell.ModEffect;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static miku.united_as_one.genesis.Genesis.MODID;

public class WingLayer extends RenderLayer<Player, PlayerModel<Player>> {
    private static final ResourceLocation WING_TEXTURE = new ResourceLocation(MODID, "textures/models/armor/spell_wing.png");
    private final WingModel<Player> wingModel;

    public WingLayer(LivingEntityRenderer<Player, PlayerModel<Player>> renderer) {
        super(renderer);
        this.wingModel = new WingModel<>();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       Player player, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        
        if (shouldRenderWings(player)) {
            poseStack.pushPose();

            
            adjustPoseForPlayerModel(poseStack, player);

            
            wingModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            float scale = 0.7F;
            poseStack.scale(scale, scale, scale);

            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(WING_TEXTURE));

            
            wingModel.renderToBuffer(poseStack, vertexConsumer, packedLight,
                    OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            poseStack.popPose();
        }
    }

    private boolean shouldRenderWings(Player player) {
        return player.hasEffect(ModEffect.I_FLY.get());
    }

    private void adjustPoseForPlayerModel(PoseStack poseStack, Player player) {
        
        if (player.isCrouching()) {
            poseStack.translate(0.0D, 0.25D, 0.0D); 
        }

        
        float scale = 1F;
        poseStack.scale(scale, scale, scale);

        
        poseStack.translate(0.0D, -0.1D, 0.1D);
    }

    @Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class WingLayerRegistry {

        @SubscribeEvent
        public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
            
            LivingEntityRenderer<Player, PlayerModel<Player>> defaultRenderer = event.getSkin("default");

            if (defaultRenderer != null) {
                defaultRenderer.addLayer(new WingLayer(defaultRenderer));
            }

            
            LivingEntityRenderer<Player, PlayerModel<Player>> slimRenderer = event.getSkin("slim");

            if (slimRenderer != null) {
                slimRenderer.addLayer(new WingLayer(slimRenderer));
            }
        }
    }
}