package miku.united_as_one.genesis.client.model.boss;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@SuppressWarnings("removal")
public class BloodBossModel extends AbstractSpellCastingMobModel {


    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return new ResourceLocation(Genesis.MOD_ID, "geo/entity/blood_boss.geo.json");
    }


    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob abstractSpellCastingMob) {
        return new ResourceLocation(Genesis.MOD_ID, "textures/entity/blood_boss/stage_1.png");
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId,
                                    AnimationState<AbstractSpellCastingMob> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
    }
}