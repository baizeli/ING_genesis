package miku.united_as_one.genesis.contents.entity.gungnir;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.render.RenderHelper;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

public class GungnirDaggerModel extends GeoModel<GungnirDaggerEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Genesis.MODID, "textures/entity/fiery_dagger.png");
    public static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "geo/fiery_dagger.geo.json");

    public ResourceLocation getModelResource(GungnirDaggerEntity animatable) {
        return MODEL;
    }

    public ResourceLocation getTextureResource(GungnirDaggerEntity animatable) {
        return TEXTURE;
    }

    public ResourceLocation getAnimationResource(GungnirDaggerEntity animatable) {
        return AbstractSpellCastingMob.animationInstantCast;
    }

    public @Nullable RenderType getRenderType(GungnirDaggerEntity animatable, ResourceLocation texture) {
        return RenderHelper.CustomerRenderType.magic(TEXTURE);
    }
}