package miku.united_as_one.genesis.client.render.entity;

import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import miku.united_as_one.genesis.client.model.BloodTentacleModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BloodTentacleRenderer extends GeoLivingEntityRenderer<VoidTentacle> {
    public BloodTentacleRenderer(EntityRendererProvider.Context context) {
        super(context, new BloodTentacleModel());
        this.addRenderLayer(new BloodTentacleEmissiveLayer(this));
        this.addRenderLayer(new BloodTentacleEmissiveLayer2(this));
        this.shadowRadius = 1.0F;
    }
}
