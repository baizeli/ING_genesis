package miku.united_as_one.genesis.mixin.ironsspellbooks.render;

import io.redspace.ironsspellbooks.render.ChargeSpellLayer;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import com.mojang.blaze3d.vertex.PoseStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Redirect;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;


import java.util.Optional;

@Mixin(value = ChargeSpellLayer.Geo.class, remap = false)
public abstract class MixinChargeSpellLayerGeo {

    @Redirect(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lio/redspace/ironsspellbooks/entity/mobs/abstract_spell_casting_mob/AbstractSpellCastingMob;Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/VertexConsumer;FII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;getBone(Ljava/lang/String;)Ljava/util/Optional;"
            )
    )
    private Optional<GeoBone> redirectGetBone(BakedGeoModel instance, String boneName,
                                              PoseStack poseStack,
                                              AbstractSpellCastingMob entity,
                                              BakedGeoModel bakedModel) {
        if (entity instanceof BloodBoss) {
            if (boneName.equals("bipedHandRight")) {
                return instance.getBone("sword");
            } else if (boneName.equals("right_arm")) {
                return instance.getBone("armorRightArm2");
            }
        }
        return instance.getBone(boneName);
    }
}