package miku.united_as_one.genesis.client.model.boss;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.render.MathUtils;
import miku.united_as_one.genesis.contents.entity.boss.BloodBoss;
import miku.bai_ze_li.genesis.api.entity.TrailComponent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@SuppressWarnings("removal")
public class BloodBossModel extends GeoModel<BloodBoss> {
    private static final ResourceLocation MODEL_RESOURCE = new ResourceLocation(Genesis.MOD_ID, "geo/entity/blood_boss.geo.json");
    private static final ResourceLocation TEXTURE_RESOURCE = new ResourceLocation(Genesis.MOD_ID, "textures/entity/blood_boss/stage_1.png");
    private static final ResourceLocation TEXTURE_RESOURCE2 = new ResourceLocation(Genesis.MOD_ID, "textures/entity/blood_boss/stage_2.png");
    private static final ResourceLocation ANIMATION_RESOURCE = new ResourceLocation(Genesis.MOD_ID, "animations/entity/blood_boss.animation.json");

    CoreGeoBone startBone = this.getAnimationProcessor().getBone("weapon_handle");
    CoreGeoBone endBone = this.getAnimationProcessor().getBone("weapon_tip");


    @Override
    public ResourceLocation getModelResource(BloodBoss object) {
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(BloodBoss abstractSpellCastingMob) {

        int bossStageData = abstractSpellCastingMob.getBossStageData();
        if (bossStageData<=1){
            return TEXTURE_RESOURCE;
        }else{
            return TEXTURE_RESOURCE2;
        }


    }

    @Override
    public ResourceLocation getAnimationResource(BloodBoss animatable) {
        return ANIMATION_RESOURCE;
    }

    @Override
    public RenderType getRenderType(BloodBoss animatable, ResourceLocation texture) {
        return RenderType.entityCutout(texture);
    }

    @Override
    public void setCustomAnimations(BloodBoss animatable, long instanceId, AnimationState<BloodBoss> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone startBone = this.getAnimationProcessor().getBone("weapon_handle");
        CoreGeoBone endBone = this.getAnimationProcessor().getBone("weapon_tip");
        if (startBone != null && endBone != null) {
            Vec3 worldStart = MathUtils.getWorldPosFromModel(animatable, animatable.yBodyRot, (GeoBone) startBone);
            Vec3 worldEnd = MathUtils.getWorldPosFromModel(animatable, animatable.yBodyRot, (GeoBone) endBone);

                TrailComponent trailComponent = animatable.getTrailComponent();
                if (trailComponent.hasTrail()){
                    trailComponent.updateTrail(worldStart, worldEnd);
                }

        }

    }
}
