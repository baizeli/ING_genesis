package com.baizeli.eternisstarrysky.entity;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.render.MathUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SwordManCsdyModel extends GeoModel<SwordManCsdy> {
    private final ResourceLocation model = new ResourceLocation(EternisStarrySky.MODID, "geo/sword_man_csdy.geo.json");
    private final ResourceLocation texture = new ResourceLocation(EternisStarrySky.MODID, "textures/entity/sword_man_csdy.png");
    private final ResourceLocation animations = new ResourceLocation(EternisStarrySky.MODID, "animations/sword_man_csdy.animation.json");
    @Override
    public ResourceLocation getModelResource(SwordManCsdy swordManCsdy) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(SwordManCsdy swordManCsdy) {
        return texture;
    }



    @Override
    public void setCustomAnimations(SwordManCsdy animatable, long instanceId, AnimationState<SwordManCsdy> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        CoreGeoBone swordLocate1 = this.getAnimationProcessor().getBone("BladeLower2");
        CoreGeoBone swordLocate2 = this.getAnimationProcessor().getBone("BladeTip2");
        Vec3 vec31 = MathUtils.getWorldPosFromModel(animatable, animatable.yBodyRot, (GeoBone) swordLocate1);
        Vec3 vec32 = MathUtils.getWorldPosFromModel(animatable, animatable.yBodyRot, (GeoBone) swordLocate2);
        animatable.updateTrail(vec31, vec32);

        CoreGeoBone swordLocate3 = this.getAnimationProcessor().getBone("BladeLower");
        CoreGeoBone swordLocate4 = this.getAnimationProcessor().getBone("BladeTip");
        Vec3 vec33 = MathUtils.getWorldPosFromModel(animatable, animatable.yBodyRot, (GeoBone) swordLocate3);
        Vec3 vec34 = MathUtils.getWorldPosFromModel(animatable, animatable.yBodyRot, (GeoBone) swordLocate4);
        animatable.updateTrail2(vec33, vec34);
    }

    @Override
    public ResourceLocation getAnimationResource(SwordManCsdy swordManCsdy) {
        return animations;
    }
}