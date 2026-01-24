package miku.united_as_one.genesis.client.renderer.boss;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import miku.united_as_one.genesis.client.model.boss.BloodBossModel;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BloodBossRenderer extends GeoEntityRenderer<BloodBoss> {

    public BloodBossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloodBossModel());
        this.shadowRadius = 0.65F;
        this.addRenderLayer(new BloodBossGlowLayer(this));
    }

    @Override
    public void preRender(PoseStack poseStack, BloodBoss animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender,
                partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        float scale = 1.75F;
        poseStack.scale(scale, scale, scale);
    }
}