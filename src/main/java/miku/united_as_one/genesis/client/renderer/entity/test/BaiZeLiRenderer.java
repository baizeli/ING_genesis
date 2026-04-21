package miku.united_as_one.genesis.client.renderer.entity.test;

import com.mojang.blaze3d.vertex.PoseStack;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.model.BaiZeLiModel;
import miku.united_as_one.genesis.common.entity.test.BaiZeLiEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BaiZeLiRenderer extends MobRenderer<BaiZeLiEntity, BaiZeLiModel<BaiZeLiEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Genesis.MOD_ID, "textures/entity/bai_ze_li.png");

    public BaiZeLiRenderer(EntityRendererProvider.Context context) {
        super(context,
                new BaiZeLiModel<>(context.bakeLayer(BaiZeLiModel.LAYER_LOCATION)),
                0.5f
        );
        this.addLayer(new BaiZeLiItemLayer(this, context.getItemInHandRenderer()));
        this.addLayer(new BaiZeLiRibbonLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BaiZeLiEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(BaiZeLiEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}
