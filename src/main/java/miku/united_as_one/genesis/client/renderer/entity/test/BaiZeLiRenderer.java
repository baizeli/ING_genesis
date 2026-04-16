package miku.united_as_one.genesis.client.renderer.entity.test;

import com.mojang.blaze3d.vertex.PoseStack;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.test.BaiZeLiEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob; // 引入父类

public class BaiZeLiRenderer extends MobRenderer<PathfinderMob, HumanoidModel<PathfinderMob>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Genesis.MOD_ID, "textures/entity/bai_ze_li.png");

    public BaiZeLiRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }
    @Override
    public ResourceLocation getTextureLocation(PathfinderMob entity) {
        return TEXTURE;
    }
    @Override
    public void render(PathfinderMob entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}
