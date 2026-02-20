package miku.united_as_one.genesis.common.entity.spells.blood_boss.fiery_dagger;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.render.RenderHelper;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

public class BloodBossFieryDaggerModel extends GeoModel<BloodBossFieryDaggerEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Genesis.MODID, "textures/entity/fiery_dagger.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "geo/fiery_dagger.geo.json");

    public ResourceLocation getModelResource(BloodBossFieryDaggerEntity animatable) {
        return MODEL;
    }

    public ResourceLocation getTextureResource(BloodBossFieryDaggerEntity animatable) {
        return TEXTURE;
    }

    public ResourceLocation getAnimationResource(BloodBossFieryDaggerEntity animatable) {
        return AbstractSpellCastingMob.animationInstantCast;
    }

    public @Nullable RenderType getRenderType(BloodBossFieryDaggerEntity animatable, ResourceLocation texture) {
        return RenderHelper.CustomerRenderType.magic(TEXTURE);
    }
}