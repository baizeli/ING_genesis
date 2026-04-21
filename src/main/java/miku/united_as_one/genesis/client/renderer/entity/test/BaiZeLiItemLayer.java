package miku.united_as_one.genesis.client.renderer.entity.test;

import com.mojang.blaze3d.vertex.PoseStack;
import miku.united_as_one.genesis.client.model.BaiZeLiModel;
import miku.united_as_one.genesis.common.entity.test.BaiZeLiEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BaiZeLiItemLayer extends RenderLayer<BaiZeLiEntity, BaiZeLiModel<BaiZeLiEntity>> {

    private final ItemInHandRenderer itemRenderer;

    public BaiZeLiItemLayer(RenderLayerParent<BaiZeLiEntity, BaiZeLiModel<BaiZeLiEntity>> parent,
                            ItemInHandRenderer renderer) {
        super(parent);
        this.itemRenderer = renderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light,
                       BaiZeLiEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        ItemStack stack = entity.getMainHandItem();
        if (stack.isEmpty()) return;

        poseStack.pushPose();
        this.getParentModel().translateToHand(HumanoidArm.RIGHT, poseStack);

        itemRenderer.renderItem(
                entity,
                stack,
                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                false,
                poseStack,
                buffer,
                light
        );

        poseStack.popPose();
    }
}