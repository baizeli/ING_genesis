package miku.united_as_one.genesis.client.renderer.entity.test;

import com.mojang.blaze3d.vertex.PoseStack;
import miku.united_as_one.genesis.client.model.BaiZeLiModel;
import miku.united_as_one.genesis.common.entity.test.BaiZeLiEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaiZeLiRibbonLayer extends RenderLayer<BaiZeLiEntity, BaiZeLiModel<BaiZeLiEntity>> {

    private final List<BaiZeLiSlashEffect> activeEffects = new ArrayList<>();
    private int lastFlag = 0;

    public BaiZeLiRibbonLayer(RenderLayerParent<BaiZeLiEntity, BaiZeLiModel<BaiZeLiEntity>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light,
                       BaiZeLiEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        int currentFlag = entity.getFlag();

        if (currentFlag == 1 && lastFlag == 0) {
            activeEffects.add(new BaiZeLiSlashEffect(
                    entity.position(),
                    entity.yBodyRot
            ));
        }

        lastFlag = currentFlag;

        Iterator<BaiZeLiSlashEffect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            BaiZeLiSlashEffect effect = iterator.next();
            effect.tick();

            if (effect.isFinished()) {
                iterator.remove();
            } else {
                effect.render(poseStack, buffer, partialTick);
            }
        }
    }
}
