package com.baizeli.eternisstarrysky.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class WingModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart left_spell_wing;
    private final ModelPart right_spell_wing;

    public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(3.0F).looping()
            .addAnimation("left_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -16.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition fly = AnimationDefinition.Builder.withLength(1.5F).looping()
            .addAnimation("left_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 60.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -60.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
    public static final AnimationDefinition run = AnimationDefinition.Builder.withLength(3.0F).looping()
            .addAnimation("left_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 50.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 66.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 50.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -50.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -66.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, -50.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
    public static final AnimationDefinition idle2 = AnimationDefinition.Builder.withLength(3.0F).looping()
            .addAnimation("left_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-71.5757F, 9.1708F, 12.2423F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-60.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-71.5757F, 9.1708F, 12.2423F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_spell_wing", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-10.0F, 20.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.posVec(-10.0F, 20.0F, 5.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-71.5757F, -9.1708F, -12.2423F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(-60.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(-71.5757F, -9.1708F, -12.2423F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_spell_wing", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(10.0F, 20.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.posVec(10.0F, 20.0F, 5.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F,  KeyframeAnimations.posVec(0.0F, -10F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
    
    private static final float TRANSITION_DURATION = 5F;
    
    
    private AnimationState activeAnimationState = null;
    
    
    private AnimationState previousAnimationState = null;
    private AnimationDefinition previousAnimationDefinition = null;
    private float transitionProgress = 1.0F; 
    
    
    private final ModelPart tempRoot;

    public WingModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDefinition = mesh.getRoot();

        
        PartDefinition left_spell_wing = rootDefinition.addOrReplaceChild("left_spell_wing",
                CubeListBuilder.create().texOffs(0, -77).addBox(0.0F, -20.0F, 0.0F, 0.0F, 43.0F, 77.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.309F, 0.0F));
        PartDefinition right_spell_wing = rootDefinition.addOrReplaceChild("right_spell_wing",
                CubeListBuilder.create().texOffs(0, -29).addBox(0.0F, -20.0F, 0.0F, 0.0F, 43.0F, 77.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.309F, 0.0F));

        LayerDefinition layerDefinition = LayerDefinition.create(mesh, 154, 128);
        this.root = layerDefinition.bakeRoot();
        this.tempRoot = layerDefinition.bakeRoot(); 
        this.left_spell_wing = this.root.getChild("left_spell_wing");
        this.right_spell_wing = this.root.getChild("right_spell_wing");
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyAnimationState = new AnimationState();
    public final AnimationState runAnimationState = new AnimationState();


    boolean isFlying= false;
    boolean isRunning= false;
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        isFlying = false;
        isRunning = false;

        if (entity instanceof Player player) {
            isFlying = player.getAbilities().flying;
            isRunning = player.isSprinting() && !isFlying;
        }

        
        AnimationState targetState = null;
        AnimationDefinition targetDefinition = null;

        if (isFlying) {
            targetState = flyAnimationState;
            targetDefinition = fly;
        } else if (isRunning) {
            targetState = runAnimationState;
            targetDefinition = run;
        } else {
            targetState = idleAnimationState;
            targetDefinition = idle2;
        }

        
        if (activeAnimationState != targetState) {
            previousAnimationState = activeAnimationState;
            previousAnimationDefinition = getCurrentAnimationDefinition();
            activeAnimationState = targetState;
            transitionProgress = 0.0F;

            
            if (!targetState.isStarted()) {
                targetState.start((int) ageInTicks);
            }
        }

        
        if (transitionProgress < 1.0F) {
            transitionProgress += 1.0F / (TRANSITION_DURATION * 20.0F);
            transitionProgress = Math.min(transitionProgress, 1.0F);

            if (previousAnimationState != null && previousAnimationDefinition != null) {
                applyTransitionPose(transitionProgress, previousAnimationState, previousAnimationDefinition,
                        targetState, targetDefinition, ageInTicks);
            } else {
                animate(targetState, targetDefinition, ageInTicks, 1.0F);
            }
        } else {
            animate(targetState, targetDefinition, ageInTicks, 1.0F);
        }
    }
    
    private AnimationDefinition getCurrentAnimationDefinition() {
        if (activeAnimationState == idleAnimationState) {
            return idle2;
        } else if (activeAnimationState == flyAnimationState) {
            return fly;
        } else if (activeAnimationState == runAnimationState) {
            return run;
        }
        return idle2;
    }
    
    
    private void applyTransitionPose(float progress, AnimationState previousState, AnimationDefinition previousDefinition,
                                   AnimationState targetState, AnimationDefinition targetDefinition, float ageInTicks) {
        
        this.tempRoot.getAllParts().forEach(ModelPart::resetPose);
        
        
        if (previousState != null && previousDefinition != null) {
            animate(previousState, previousDefinition, ageInTicks, 1.0F);
            copyModelPose(this.root, this.tempRoot);
        }
        
        
        this.root().getAllParts().forEach(ModelPart::resetPose);
        
        
        if (targetState != null && targetDefinition != null) {
            animate(targetState, targetDefinition, ageInTicks, 1.0F);
        }
        
        
        if (previousState != null && previousDefinition != null) {
            interpolatePoses(this.tempRoot, this.root, progress);
        }
    }
    
    
    private void copyModelPose(ModelPart source, ModelPart target) {
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
        target.x = source.x;
        target.y = source.y;
        target.z = source.z;
        
        
        copyWingPose(source, target, "left_spell_wing");
        copyWingPose(source, target, "right_spell_wing");
    }
    
    
    private void copyWingPose(ModelPart sourceRoot, ModelPart targetRoot, String wingName) {
        
        if (sourceRoot.hasChild(wingName) && targetRoot.hasChild(wingName)) {
            ModelPart sourceWing = sourceRoot.getChild(wingName);
            ModelPart targetWing = targetRoot.getChild(wingName);
            
            targetWing.xRot = sourceWing.xRot;
            targetWing.yRot = sourceWing.yRot;
            targetWing.zRot = sourceWing.zRot;
            targetWing.x = sourceWing.x;
            targetWing.y = sourceWing.y;
            targetWing.z = sourceWing.z;
        }
    }
    
    
    private void interpolatePoses(ModelPart from, ModelPart to, float progress) {
        
        to.xRot = from.xRot + (to.xRot - from.xRot) * progress;
        to.yRot = from.yRot + (to.yRot - from.yRot) * progress;
        to.zRot = from.zRot + (to.zRot - from.zRot) * progress;
        to.x = from.x + (to.x - from.x) * progress;
        to.y = from.y + (to.y - from.y) * progress;
        to.z = from.z + (to.z - from.z) * progress;
        
        
        interpolateWingPose(from, to, "left_spell_wing", progress);
        interpolateWingPose(from, to, "right_spell_wing", progress);
    }
    
    
    private void interpolateWingPose(ModelPart fromRoot, ModelPart toRoot, String wingName, float progress) {
        
        if (fromRoot.hasChild(wingName) && toRoot.hasChild(wingName)) {
            ModelPart fromWing = fromRoot.getChild(wingName);
            ModelPart toWing = toRoot.getChild(wingName);
            
            toWing.xRot = fromWing.xRot + (toWing.xRot - fromWing.xRot) * progress;
            toWing.yRot = fromWing.yRot + (toWing.yRot - fromWing.yRot) * progress;
            toWing.zRot = fromWing.zRot + (toWing.zRot - fromWing.zRot) * progress;
            toWing.x = fromWing.x + (toWing.x - fromWing.x) * progress;
            toWing.y = fromWing.y + (toWing.y - fromWing.y) * progress;
            toWing.z = fromWing.z + (toWing.z - fromWing.z) * progress;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}