package miku.united_as_one.genesis.client.model.boss;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@SuppressWarnings("removal")
public class BloodBossModel extends AbstractSpellCastingMobModel {
    private static final ResourceLocation MODEL_RESOURCE = new ResourceLocation(Genesis.MOD_ID, "geo/entity/blood_boss.geo.json");
    private static final ResourceLocation TEXTURE_RESOURCE = new ResourceLocation(Genesis.MOD_ID, "textures/entity/blood_boss/stage_1.png");
    private static final ResourceLocation ANIMATION_RESOURCE = new ResourceLocation(Genesis.MOD_ID, "animations/entity/blood_boss.animation.json");

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob abstractSpellCastingMob) {
        return TEXTURE_RESOURCE;
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        return ANIMATION_RESOURCE;
    }



}