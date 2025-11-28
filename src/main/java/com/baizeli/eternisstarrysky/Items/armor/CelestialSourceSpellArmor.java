package com.baizeli.eternisstarrysky.Items.armor;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.item.armor.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.*;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CelestialSourceSpellArmor extends ExtendedArmorItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    public CelestialSourceSpellArmor(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new CelestialSourceSpellArmorModel());
    }
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
            new AnimationController<>(
                this, "controller", 20, this::predicate
            )
        );
    }
    
    private PlayState predicate(AnimationState<CelestialSourceSpellArmor> animationState) {
        animationState.getController().setAnimation(
            RawAnimation.begin().thenLoop("celestial_source_spell.animation")
        );
        return PlayState.CONTINUE;
    }
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static class CelestialSourceSpellArmorModel extends GeoModel<CelestialSourceSpellArmor> {
        @Override
        public ResourceLocation getModelResource(CelestialSourceSpellArmor object) {
            return ResourceLocation.fromNamespaceAndPath(
                EternisStarrySky.MOD_ID, "geo/celestial_source_spell_armor.geo.json"
            );
        }

        @Override
        public ResourceLocation getTextureResource(CelestialSourceSpellArmor object) {
            return ResourceLocation.fromNamespaceAndPath(
                EternisStarrySky.MOD_ID, "textures/models/armor/celestial_source_spell.png"
            );
        }

        @Override
        public ResourceLocation getAnimationResource(CelestialSourceSpellArmor animatable) {
            return ResourceLocation.fromNamespaceAndPath(
                EternisStarrySky.MOD_ID, "animations/celestial_source_spell.animation.json"
            );
        }
    }
}