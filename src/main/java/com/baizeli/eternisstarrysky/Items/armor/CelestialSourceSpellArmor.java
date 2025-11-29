package com.baizeli.eternisstarrysky.Items.armor;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Util.ArmorSetUtil;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.item.armor.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import software.bernie.geckolib.core.animatable.instance.*;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.*;
import software.bernie.geckolib.model.*;
import software.bernie.geckolib.renderer.*;
import software.bernie.geckolib.renderer.layer.*;
import software.bernie.geckolib.util.*;
import software.bernie.geckolib.cache.object.*;

public class CelestialSourceSpellArmor extends ExtendedArmorItem {
    /*private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);*/
    
    public CelestialSourceSpellArmor(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {

        // 星源法术套
        CelestialSourceSpellArmorRenderer renderer = new CelestialSourceSpellArmorRenderer(new CelestialSourceSpellArmorModel());

        // 星源法术环这里使用渲染层
        renderer.addRenderLayer(new CelestialSourceSpellRingLayer(renderer));

        return renderer;
    }
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
            new AnimationController(
                this, "controller", 0, this::predicate
            )
        );
    }
    
    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(
            RawAnimation.begin().thenLoop("celestial_source_spell_ring")
        );
        return PlayState.CONTINUE;
    }

    /*@Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }*/

    public static class CelestialSourceSpellRingLayer extends GeoRenderLayer<CelestialSourceSpellArmor> {
        private final CelestialSourceSpellRingModel ringModel;
        
        public CelestialSourceSpellRingLayer(GeoArmorRenderer<CelestialSourceSpellArmor> renderer) {
            super(renderer);
            this.ringModel = new CelestialSourceSpellRingModel();
        }
        
        @Override
        public void render(
            PoseStack poseStack, CelestialSourceSpellArmor animatable, BakedGeoModel bakedModel, RenderType renderType, 
            MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay
        ) {
            Entity entity = ((GeoArmorRenderer<?>) this.renderer).getCurrentEntity();
            EquipmentSlot currentSlot = ((GeoArmorRenderer<?>) this.renderer).getCurrentSlot();

            if (entity instanceof LivingEntity livingEntity && 
                ArmorSetUtil.hasFullCelestialSourceSet(livingEntity) && currentSlot == EquipmentSlot.CHEST
            ) {
                AnimationState<CelestialSourceSpellArmor> animationState = new AnimationState<>(
                    animatable, 0, 0, partialTick, false
                );
                long instanceId = this.renderer.getInstanceId(animatable);

                this.ringModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
                this.ringModel.handleAnimations(animatable, instanceId, animationState);

                ResourceLocation texture = this.ringModel.getTextureResource(animatable);
                RenderType ringRenderType = RenderType.entityCutoutNoCull(texture);
                VertexConsumer ringBuffer = bufferSource.getBuffer(ringRenderType);

                BakedGeoModel ringBakedModel = this.ringModel.getBakedModel(this.ringModel.getModelResource(animatable));

                this.renderer.actuallyRender(
                    poseStack, animatable, ringBakedModel, ringRenderType, bufferSource, ringBuffer, 
                    true, partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F
                );
            }
        }
    }

    public static class CelestialSourceSpellArmorRenderer extends GenericCustomArmorRenderer<CelestialSourceSpellArmor> {
        public CelestialSourceSpellArmorRenderer(GeoModel<CelestialSourceSpellArmor> model) {
            super(model);
        }

        @Override
        public void prepForRender(Entity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> baseModel) {
            super.prepForRender(entity, stack, slot, baseModel);
        }
    }

    // 星源法术环相关...
    public static class CelestialSourceSpellRingModel extends GeoModel<CelestialSourceSpellArmor> {
        @Override
        public ResourceLocation getModelResource(CelestialSourceSpellArmor object) {
            return ResourceLocation.fromNamespaceAndPath(
                EternisStarrySky.MOD_ID, "geo/celestial_source_spell_ring.geo.json"
            );
        }

        @Override
        public ResourceLocation getTextureResource(CelestialSourceSpellArmor object) {
            return ResourceLocation.fromNamespaceAndPath(
                EternisStarrySky.MOD_ID, "textures/models/armor/celestial_source_spell_ring.png"
            );
        }

        @Override
        public ResourceLocation getAnimationResource(CelestialSourceSpellArmor animatable) {
            return ResourceLocation.fromNamespaceAndPath(
                EternisStarrySky.MOD_ID, "animations/celestial_source_spell_ring.animation.json"
            );
        }
    }

    // 星源法术套相关...
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
                EternisStarrySky.MOD_ID, "animations/celestial_source_spell_ring.animation.json"
            );
        }
    }
}