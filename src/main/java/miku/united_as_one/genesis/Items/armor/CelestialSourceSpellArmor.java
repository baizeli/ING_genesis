package miku.united_as_one.genesis.Items.armor;

import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.TooltipParticleHandler.ITooltipParticleItem;
import miku.united_as_one.genesis.TooltipParticleHandler.PTID;
import miku.united_as_one.genesis.TooltipParticleHandler.TooltipParticleSystem;
import miku.united_as_one.genesis.util.ArmorSetUtil;
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

public class CelestialSourceSpellArmor extends ExtendedArmorItem implements ITooltipParticleItem {
    public CelestialSourceSpellArmor(IronsExtendedArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }
    
    Minecraft mc = Minecraft.getInstance();

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {

        // 星源法术套
        CelestialSourceSpellArmorRenderer renderer = new CelestialSourceSpellArmorRenderer(
            new GeoModel<CelestialSourceSpellArmor>() {
                @Override
                public ResourceLocation getModelResource(CelestialSourceSpellArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        EternisStarrySky.MOD_ID, "geo/armor/celestial_source_spell_armor.geo.json"
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
                        EternisStarrySky.MOD_ID, "animations/armor/celestial_source_spell_armor.animation.json"
                    );
                }
            }
        );

        // 星源法术环[渲染层]
        renderer.addRenderLayer(new GeoRenderLayer<CelestialSourceSpellArmor>(renderer) {
            private final GeoModel<CelestialSourceSpellArmor> ringModel = new GeoModel<CelestialSourceSpellArmor>() {
                @Override
                public ResourceLocation getModelResource(CelestialSourceSpellArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        EternisStarrySky.MOD_ID, "geo/armor/celestial_source_spell_ring.geo.json"
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
                        EternisStarrySky.MOD_ID, "animations/armor/celestial_source_spell_armor.animation.json"
                    );
                }
            };

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

                    this.renderer.actuallyRender(
                        poseStack, animatable, ringBakedModel, ringRenderType, bufferSource, ringBuffer,
                        true, partialTick, LightTexture.FULL_BRIGHT, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F
                    );
                }
            }
        });

        // 星源法术披风[渲染层]
        renderer.addRenderLayer(new GeoRenderLayer<CelestialSourceSpellArmor>(renderer) {
            private final GeoModel<CelestialSourceSpellArmor> capeModel = new GeoModel<CelestialSourceSpellArmor>() {
                @Override
                public ResourceLocation getModelResource(CelestialSourceSpellArmor object) {
                    return ResourceLocation.fromNamespaceAndPath(
                        EternisStarrySky.MOD_ID, "geo/armor/celestial_source_spell_cape.geo.json"
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
                        EternisStarrySky.MOD_ID, "animations/armor/celestial_source_spell_armor.animation.json"
                    );
                }
            };

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
        });

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

    @Override
    public TooltipParticleSystem.ParticleConfig getParticleConfig() {
        Minecraft mc = Minecraft.getInstance();
        double mouseX = mc.mouseHandler.xpos();
        double mouseY = mc.mouseHandler.ypos();

        // 转换为整数坐标
        int mousePosX = (int) mouseX;
        int mousePosY = (int) mouseY;
        return new TooltipParticleSystem.ParticleConfig()
                // 纹理使用
                .setTextures(
                        PTID.Star_0,
                        PTID.Star_1,
                        PTID.Star_2,
                        PTID.Star_3,
                        PTID.Star_4,
                        PTID.Star_5,
                        PTID.Star_6,
                        PTID.Star_7,
                        PTID.Star_8,
                        PTID.Star_9
                )
                .setParticleCount(1, 3) // 生成数量多少到多少
                .setMaxTotalParticles(400) // 最大粒子数量
                // 大小
                .setSize(4.0f, 12.0f) // 基础大小
                .setRandomSize(false) // 随机大小变化
                // 生命周期
                .setLife(3.0f, 4.0f) // 存活时间3-4秒
                // 速度
                .setSpeed(70.0f, 100.0f) // 基础速度
                // 颜色
                .setColors(0xFFffb800, 0xFFcd7231, 0xFFffeb00, 0xFFe0ae2d) // 基础颜色（彩虹模式下会被覆盖）
                .setRainbowColors(true, 2.0f) // 开启彩虹渐变
                .setColorTransitionSpeed(1.5f) // 颜色过渡速度
                // 物理
                .setGravity(false, 40.0f) // 关闭重力（RAIN模式自带下落效果）
                .setWind(true, 0.0f, 110.0f) // 强风效果
                .setAirResistance(0.1f) // 轻微空气阻力
                .setBounciness(10.0f) // 弹性系数
                // 旋转
                .setRotation(true, 0.0f, 0.01f) // 旋转速度
                .setInitialRotation(0.0f, 360.0f) // 随机初始旋转角度
                // 运动类型
                .setMotionType(TooltipParticleSystem.MotionType.RAIN) // 从屏幕上方下落
                .setMotionProperties(20.0f, 1.5f) // 运动幅度和频率（对RAIN模式影响较小）
                .setCenter((float) mousePosX / 3, (float) mousePosY / 3) // 运动中心点（对RAIN模式影响较小）
                .setRadius(200.0f) // 运动半径（对RAIN模式影响较小）
                // 淡入淡出
                .setFadeIn(true, 0.05f) // 淡入，持续0.05秒
                .setFadeOut(true, 0.25f) // 淡出，持续0.25秒
                // 层次感
                .setDepthLayers(true, 12, 0.08f) // 12层深度，每层变暗8%
                // 贝塞尔曲线
                .setSizeCurve(TooltipParticleSystem.BezierCurveType.STAR_EXPAND) // 星星膨胀曲线
                .setAlphaCurve(TooltipParticleSystem.BezierCurveType.NONE) // 透明度曲线
                .setSpeedCurve(TooltipParticleSystem.BezierCurveType.NONE) // 速度曲线
                .setRotationCurve(TooltipParticleSystem.BezierCurveType.NONE); // 旋转曲线
    }
}