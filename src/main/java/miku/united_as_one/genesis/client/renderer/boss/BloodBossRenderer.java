package miku.united_as_one.genesis.client.renderer.boss;

import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.client.model.boss.BloodBossModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BloodBossRenderer extends GeoEntityRenderer<BloodBoss> {
    public BloodBossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloodBossModel());
    }
}