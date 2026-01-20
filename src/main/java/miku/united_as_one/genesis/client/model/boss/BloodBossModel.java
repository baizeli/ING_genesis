package miku.united_as_one.genesis.client.model.boss;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@SuppressWarnings("removal")
public class BloodBossModel extends GeoModel<BloodBoss> {

    @Override
    public ResourceLocation getModelResource(BloodBoss animatable) {
        return new ResourceLocation(Genesis.MOD_ID, "geo/entity/blood_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BloodBoss animatable) {
        return new ResourceLocation(Genesis.MOD_ID, "textures/entity/blood_boss/stage_1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BloodBoss animatable) {
        return new ResourceLocation(Genesis.MOD_ID, "animations/entity/blood_boss.animation.json");
    }
}