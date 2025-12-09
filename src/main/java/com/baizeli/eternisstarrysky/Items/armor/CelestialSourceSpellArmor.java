package com.baizeli.eternisstarrysky.Items.armor;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Util.ArmorSetUtil;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.item.armor.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.*;
import software.bernie.geckolib.model.*;
import software.bernie.geckolib.renderer.*;
import software.bernie.geckolib.renderer.layer.*;
import software.bernie.geckolib.cache.object.*;

public class CelestialSourceSpellArmor extends ExtendedArmorItem {
    public CelestialSourceSpellArmor(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    Minecraft mc = Minecraft.getInstance();

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {

        // 星源法术套
        CelestialSourceSpellArmorRenderer renderer = new CelestialSourceSpellArmorRenderer(new CelestialSourceSpellArmorModel());

        // 星源法术环这里使用渲染层
        renderer.addRenderLayer(new CelestialSourceSpellRingLayer(renderer));
        
        // 星源法术披风这里使用渲染层
        renderer.addRenderLayer(new CelestialSourceSpellCapeLayer(renderer));

        return renderer;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
            new AnimationController<>(
                this, "celestial_source_spell_ring", 0, this::celestial_source_spell_ring
            ),
            new AnimationController<>(
                this, "celestial_source_spell_cape_standby", 0, this::celestial_source_spell_cape_standby
            ),
            new AnimationController<>(
                this, "celestial_source_spell_cape_running", 0, this::celestial_source_spell_cape_running
            ),
            new AnimationController<>(
                this, "celestial_source_spell_cape_shift", 0, this::celestial_source_spell_cape_shift
            )
        );
    }

    private PlayState celestial_source_spell_ring(AnimationState<CelestialSourceSpellArmor> animationState) {
        animationState.getController().setAnimation(
            RawAnimation.begin().thenLoop("celestial_source_spell_ring.animation")
        );
        
        return PlayState.CONTINUE;
    }
    
    private PlayState celestial_source_spell_cape_standby(AnimationState<CelestialSourceSpellArmor> animationState) {
        animationState.getController().setAnimation(
            RawAnimation.begin().thenLoop("celestial_source_spell_cape_standby.animation")
        );

        return PlayState.CONTINUE;
    }
    
    private PlayState celestial_source_spell_cape_running(AnimationState<CelestialSourceSpellArmor> animationState) {
        if (mc.player.isSprinting()) {
            animationState.getController().setAnimation(
                RawAnimation.begin().thenLoop("celestial_source_spell_cape_running.animation")
            );
            
            return PlayState.CONTINUE;
        }
        
        return PlayState.STOP;
    }

    private PlayState celestial_source_spell_cape_shift(AnimationState<CelestialSourceSpellArmor> animationState) {
        if (mc.player.isShiftKeyDown()) {
            /*animationState.getController().forceAnimationReset();*/
            animationState.getController().setAnimation(
                RawAnimation.begin().thenLoop("celestial_source_spell_cape_shift.animation_3")
            );
            /*animationState.getController().setAnimation(
                RawAnimation.begin().thenPlay("celestial_source_spell_cape_shift.animation_1")
            );*/
            
            return PlayState.CONTINUE;
        }

        /*if (animationState.getController().hasAnimationFinished()) {
            return PlayState.STOP;
        }*/
        
        return PlayState.STOP;
    }

    // 星源法术环渲染层...
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

                RenderType ringRenderType = RenderType.entityTranslucentEmissive(this.ringModel.getTextureResource(animatable));
                VertexConsumer ringBuffer = bufferSource.getBuffer(ringRenderType);

                BakedGeoModel ringBakedModel = this.ringModel.getBakedModel(this.ringModel.getModelResource(animatable));

                /*long time = System.currentTimeMillis() / 100;
                float red = (float) (Math.sin(time * 0.05) + 1) / 2;
                float green = (float) (Math.sin(time * 0.05 + 2) + 1) / 2;
                float blue = (float) (Math.sin(time * 0.05 + 4) + 1) / 2;*/
                
                this.renderer.actuallyRender(
                    poseStack, animatable, ringBakedModel, ringRenderType, bufferSource, ringBuffer, 
                    true, partialTick, LightTexture.FULL_BRIGHT, packedOverlay, /*red, green, blue*/1.0F, 1.0F, 1.0F, 1.0F
                );
            }
        }
    }

    // 星源法术披风渲染层...
    public static class CelestialSourceSpellCapeLayer extends GeoRenderLayer<CelestialSourceSpellArmor> {
        private final CelestialSourceSpellCapeModel capeModel;

        public CelestialSourceSpellCapeLayer(GeoArmorRenderer<CelestialSourceSpellArmor> renderer) {
            super(renderer);
            this.capeModel = new CelestialSourceSpellCapeModel();
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

                this.capeModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
                this.capeModel.handleAnimations(animatable, instanceId, animationState);

                RenderType capeRenderType = RenderType.entityCutoutNoCull(this.capeModel.getTextureResource(animatable));
                VertexConsumer capeBuffer = bufferSource.getBuffer(capeRenderType);

                BakedGeoModel capeBakedModel = this.capeModel.getBakedModel(this.capeModel.getModelResource(animatable));

                this.renderer.actuallyRender(
                    poseStack, animatable, capeBakedModel, capeRenderType, bufferSource, capeBuffer,
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

        /*@Override
        public void actuallyRender(
            PoseStack poseStack, CelestialSourceSpellArmor animatable,
            BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource,
            VertexConsumer buffer, boolean isReRender, float partialTick,
            int packedLight, int packedOverlay, float red, float green, float blue, float alpha
        ) {
            super.actuallyRender(
                poseStack, animatable, model, renderType, bufferSource, buffer, isReRender,
                partialTick, LightTexture.FULL_BRIGHT, packedOverlay, red, green, blue, alpha
            );
        }*/
    }

    // 星源法术套相关...
    public static class CelestialSourceSpellArmorModel extends GeoModel<CelestialSourceSpellArmor> {
        @Override
        public ResourceLocation getModelResource(CelestialSourceSpellArmor object) {
            return ResourceLocation.fromNamespaceAndPath(
                EternisStarrySky.MOD_ID, "geo/celestial_source_spell.geo.json"
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
                EternisStarrySky.MOD_ID, "animations/celestial_source_spell.animation.json"
            );
        }
    }

    // 星源法术披风相关...
    public static class CelestialSourceSpellCapeModel extends GeoModel<CelestialSourceSpellArmor> {
        @Override
        public ResourceLocation getModelResource(CelestialSourceSpellArmor object) {
            return ResourceLocation.fromNamespaceAndPath(
                EternisStarrySky.MOD_ID, "geo/celestial_source_spell_cape.geo.json"
            );
        }

        @Override
        public ResourceLocation getTextureResource(CelestialSourceSpellArmor object) {
            return ResourceLocation.fromNamespaceAndPath(
                EternisStarrySky.MOD_ID, "textures/models/armor/celestial_source_spell_cape.png"
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