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
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition fly = AnimationDefinition.Builder.withLength(1.5F).looping()
            .addAnimation("left_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_spell_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -35.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public WingModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDefinition = mesh.getRoot();

        
        PartDefinition left_spell_wing = rootDefinition.addOrReplaceChild("left_spell_wing",
                CubeListBuilder.create().texOffs(0, 60).addBox(0.0F, -32.0F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.309F, 0.0F));

        PartDefinition right_spell_wing = rootDefinition.addOrReplaceChild("right_spell_wing",
                CubeListBuilder.create().texOffs(0, -16).addBox(0.0F, -32.0F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 1.309F, 0.0F));

        LayerDefinition layerDefinition = LayerDefinition.create(mesh, 128, 128);
        this.root = layerDefinition.bakeRoot();
        this.left_spell_wing = this.root.getChild("left_spell_wing");
        this.right_spell_wing = this.root.getChild("right_spell_wing");
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyAnimationState = new AnimationState();

    public void setupAnimationStates() {
        if (idleAnimationState.isStarted()) {
            idleAnimationState.animateWhen(true, 0);
        }
        if (flyAnimationState.isStarted()) {
            flyAnimationState.animateWhen(true, 0);
        }
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        boolean isFlying = false;

        if (entity instanceof Player player) {
            isFlying = player.getAbilities().flying;
        }

        
        setupAnimationStates();

        if (isFlying) {
            
            idleAnimationState.stop();
            if (!flyAnimationState.isStarted()) {
                flyAnimationState.start((int) ageInTicks);
            }
            animate(flyAnimationState, fly, ageInTicks, 1.5f);
        } else {
            
            flyAnimationState.stop();
            if (!idleAnimationState.isStarted()) {
                idleAnimationState.start((int)ageInTicks);
            }
            animate(idleAnimationState, idle, ageInTicks, 1f);
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