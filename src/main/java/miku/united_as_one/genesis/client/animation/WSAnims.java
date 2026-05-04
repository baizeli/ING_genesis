
package miku.united_as_one.genesis.client.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class WSAnims
{
	public static final AnimationDefinition start = AnimationDefinition.Builder.withLength(6.625F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.9167F, KeyframeAnimations.degreeVec(35.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.degreeVec(25.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.4583F, KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.7917F, KeyframeAnimations.degreeVec(70.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -63.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -56.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.1667F, KeyframeAnimations.posVec(0.0F, -27.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.posVec(0.0F, -14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.9167F, KeyframeAnimations.posVec(0.0F, -11.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.posVec(0.0F, -14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.4583F, KeyframeAnimations.posVec(0.0F, -6.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5417F, KeyframeAnimations.posVec(0.0F, -4.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.posVec(0.0F, -6.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, -3.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.7917F, KeyframeAnimations.posVec(-3.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(0.74F, -40.38F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-67.5F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-67.5F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-47.5F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-67.5F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-67.5F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(-67.5F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.degreeVec(-52.5F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.9167F, KeyframeAnimations.degreeVec(-67.5F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.625F, KeyframeAnimations.degreeVec(-17.5F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.degreeVec(-70.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0417F, KeyframeAnimations.degreeVec(-70.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.125F, KeyframeAnimations.degreeVec(-80.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.625F, KeyframeAnimations.degreeVec(-70.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.degreeVec(-77.5F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(-8.0F, -11.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.47F, -0.95F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.47F, -0.95F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.9167F, KeyframeAnimations.posVec(0.0F, 1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.625F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0417F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-152.5F, 2.5F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-180.0F, 12.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-90.0F, 12.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(-90.0F, 12.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0833F, KeyframeAnimations.degreeVec(-95.0F, 12.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.25F, KeyframeAnimations.degreeVec(-83.93F, 3.93F, 5.71F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.degreeVec(-67.5F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.degreeVec(-67.5F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.4583F, KeyframeAnimations.degreeVec(-55.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5417F, KeyframeAnimations.degreeVec(-60.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.degreeVec(-55.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.degreeVec(-67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.5417F, KeyframeAnimations.degreeVec(-50.45F, 0.0F, 2.69F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.0833F, KeyframeAnimations.degreeVec(-62.72F, 0.0F, 4.3F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, -21.0F, 9.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.posVec(2.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.7917F, KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.posVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0833F, KeyframeAnimations.posVec(2.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.375F, KeyframeAnimations.posVec(2.0F, 1.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.posVec(2.0F, 3.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.posVec(2.0F, 3.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.4583F, KeyframeAnimations.posVec(2.67F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5417F, KeyframeAnimations.posVec(2.67F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.posVec(2.67F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.posVec(0.67F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-167.5F, -17.5F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-167.5F, -17.5F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-175.0F, -17.5F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-190.0F, -17.5F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-90.0F, -5.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-90.0F, -17.5F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(-90.0F, -17.5F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-81.29F, -10.64F, -14.21F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.1667F, KeyframeAnimations.degreeVec(-83.5F, -5.5F, -15.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.degreeVec(-62.5F, -7.5F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.9167F, KeyframeAnimations.degreeVec(-58.75F, -3.75F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.degreeVec(-55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.4583F, KeyframeAnimations.degreeVec(-52.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5417F, KeyframeAnimations.degreeVec(-52.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.degreeVec(-72.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.degreeVec(-52.5F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.5417F, KeyframeAnimations.degreeVec(-57.5F, 4.54F, 2.99F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.0833F, KeyframeAnimations.degreeVec(-70.99F, 5.77F, 1.78F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -8.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, -8.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2083F, KeyframeAnimations.posVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.posVec(-4.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.posVec(-4.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0417F, KeyframeAnimations.posVec(-3.23F, 5.7F, 4.97F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.1667F, KeyframeAnimations.posVec(-1.49F, 2.22F, 5.25F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.75F, KeyframeAnimations.posVec(-1.14F, 1.71F, 1.86F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.9167F, KeyframeAnimations.posVec(-1.49F, 1.21F, 3.86F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.posVec(-1.49F, 1.71F, 4.86F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.4583F, KeyframeAnimations.posVec(-1.0F, 1.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5417F, KeyframeAnimations.posVec(0.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.posVec(0.0F, 1.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.posVec(-2.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.degreeVec(20.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8333F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -63.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -56.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.375F, KeyframeAnimations.posVec(0.0F, -22.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.posVec(-4.0F, 2.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.posVec(-4.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.posVec(-4.0F, 0.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8333F, KeyframeAnimations.posVec(-2.0F, 2.0F, -3.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.degreeVec(20.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8333F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -63.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -56.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.375F, KeyframeAnimations.posVec(0.0F, -22.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.posVec(-4.0F, 2.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.posVec(-4.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.6667F, KeyframeAnimations.posVec(-4.0F, 0.0F, -9.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8333F, KeyframeAnimations.posVec(-2.0F, 2.0F, -3.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(6.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(3.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(1.43F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 1.43F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-1.43F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -1.43F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(1.43F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(3.44F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 3.44F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-3.44F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -3.44F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(3.44F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(2.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.1F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.86F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-20.1F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.86F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(20.1F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(24.1F, 0.0F, 8.6F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-24.1F, 0.0F, -8.6F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-22.9F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(22.9F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.9F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(22.9F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(-22.9F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(28.6F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-28.6F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(28.6F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-28.6F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(28.6F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(-28.6F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition leave = AnimationDefinition.Builder.withLength(4.5F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(4.1344F, 0.9474F, 1.2694F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(54.4541F, -13.5394F, -18.1418F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(59.4644F, -10.8885F, 35.7954F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(82.2826F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.625F, KeyframeAnimations.degreeVec(53.2361F, 10.0472F, -29.7293F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.2083F, KeyframeAnimations.degreeVec(-17.7174F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5417F, KeyframeAnimations.degreeVec(112.2826F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(116.0689F, 5.1158F, -24.5012F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.8333F, KeyframeAnimations.degreeVec(121.5624F, -4.1725F, 19.5774F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0417F, KeyframeAnimations.degreeVec(138.5689F, 5.1158F, -24.5012F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.25F, KeyframeAnimations.degreeVec(144.0624F, -4.1725F, 19.5774F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.375F, KeyframeAnimations.degreeVec(147.2826F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.625F, KeyframeAnimations.degreeVec(147.2826F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.875F, KeyframeAnimations.degreeVec(134.3622F, 8.8111F, -8.9017F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0417F, KeyframeAnimations.degreeVec(132.0597F, -8.3593F, 9.7051F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.25F, KeyframeAnimations.degreeVec(134.3622F, 8.8111F, -8.9017F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5F, KeyframeAnimations.degreeVec(147.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -16.4845F, -6.5784F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.posVec(0.0F, -16.4845F, -6.5784F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, -16.97F, -7.11F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, -13.97F, -7.11F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0833F, KeyframeAnimations.posVec(0.0F, -11.4845F, -0.5784F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5417F, KeyframeAnimations.posVec(0.0F, -16.4845F, -6.5784F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.posVec(0.0F, -20.27F, -5.42F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.375F, KeyframeAnimations.posVec(0.0F, -21.4845F, -5.5784F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0417F, KeyframeAnimations.posVec(0.0F, -22.4845F, -5.5784F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.375F, KeyframeAnimations.posVec(0.0F, -40.0F, -8.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5F, KeyframeAnimations.posVec(0.0F, -40.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, -22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5417F, KeyframeAnimations.degreeVec(0.0F, 67.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5F, KeyframeAnimations.degreeVec(0.0F, 67.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-101.8036F, -21.2959F, 30.6148F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-101.8036F, -21.2959F, 30.6148F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(48.7585F, -17.6194F, 9.9865F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(48.7585F, -17.6194F, 9.9865F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(-101.8036F, -21.2959F, 30.6148F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(-89.0499F, -4.1966F, -1.4785F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.2083F, KeyframeAnimations.degreeVec(-158.3073F, 3.7152F, -1.5235F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5417F, KeyframeAnimations.degreeVec(-89.0499F, -4.1966F, -1.4785F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.375F, KeyframeAnimations.degreeVec(-120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5F, KeyframeAnimations.degreeVec(-120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7917F, KeyframeAnimations.posVec(2.22F, 0.0F, 0.86F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(3.12F, 0.0F, 4.29F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.2083F, KeyframeAnimations.posVec(1.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-63.8929F, -0.5201F, 2.0949F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-63.8929F, -0.5201F, 2.0949F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-62.8786F, 15.1506F, 9.9744F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-86.9364F, 17.4503F, 4.0528F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(-86.9364F, 17.4503F, 4.0528F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(63.0984F, 8.8357F, -8.7128F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.875F, KeyframeAnimations.degreeVec(35.5984F, 8.8357F, -8.7128F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.2083F, KeyframeAnimations.degreeVec(-153.2747F, -0.0295F, 3.5235F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5417F, KeyframeAnimations.degreeVec(-87.0775F, -0.0262F, 3.132F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.375F, KeyframeAnimations.degreeVec(-120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5F, KeyframeAnimations.degreeVec(-120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(-0.28F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.posVec(-1.51F, 4.35F, 4.33F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9167F, KeyframeAnimations.posVec(-0.6F, 3.61F, 4.63F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.posVec(-0.6F, 3.61F, 0.63F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.posVec(-2.85F, -0.1F, 3.33F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.2083F, KeyframeAnimations.posVec(-1.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(113.27F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(113.27F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5833F, KeyframeAnimations.degreeVec(182.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.degreeVec(120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.degreeVec(182.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2917F, KeyframeAnimations.degreeVec(120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -13.98F, -2.37F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.posVec(0.0F, -13.98F, -2.37F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5833F, KeyframeAnimations.posVec(0.0F, -7.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.posVec(0.0F, -9.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.posVec(0.0F, -16.71F, -3.69F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2917F, KeyframeAnimations.posVec(0.0F, -28.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5F, KeyframeAnimations.posVec(0.0F, -28.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(114.98F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(114.98F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5833F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.degreeVec(172.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2917F, KeyframeAnimations.degreeVec(197.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -14.01F, -2.35F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.posVec(0.0F, -14.01F, -2.35F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.5833F, KeyframeAnimations.posVec(0.0F, -5.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8333F, KeyframeAnimations.posVec(0.0F, -7.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0833F, KeyframeAnimations.posVec(0.0F, -15.5F, -3.76F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2917F, KeyframeAnimations.posVec(0.0F, -28.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5F, KeyframeAnimations.posVec(0.0F, -28.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition roar = AnimationDefinition.Builder.withLength(4.2083F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5833F, KeyframeAnimations.degreeVec(32.5F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.degreeVec(38.33F, 0.0F, 2.99F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0833F, KeyframeAnimations.degreeVec(40.97F, 0.0F, -4.3F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.375F, KeyframeAnimations.degreeVec(44.41F, 0.0F, 6.29F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, -1.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, -3.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -3.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 26.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.4583F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 26.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.8333F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, -2.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -2.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_tendril", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.85F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.85F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.85F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_tendril", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.85F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.85F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.85F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-120.0F, 0.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-77.5F, 3.75F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(67.5F, -32.5F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.4583F, KeyframeAnimations.degreeVec(37.5F, -32.5F, 25.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(27.6F, -17.1F, 32.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 3.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, 4.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.4583F, KeyframeAnimations.posVec(0.0F, 4.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-125.0F, 0.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(-76.25F, -17.5F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(62.5F, 42.5F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.4583F, KeyframeAnimations.degreeVec(37.5F, 27.5F, -27.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(25.0F, 18.4F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, -3.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, -4.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.4583F, KeyframeAnimations.posVec(0.0F, -4.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition smell = AnimationDefinition.Builder.withLength(3.3333F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(17.5F, 32.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(0.0F, 32.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.2083F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7917F, KeyframeAnimations.degreeVec(10.0F, -30.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 40.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-22.5F, 40.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-35.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.2917F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7917F, KeyframeAnimations.degreeVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.9583F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition combat = AnimationDefinition.Builder.withLength(0.3333F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -1.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-30.1749F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-120.3612F, 40.7895F, -20.941F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-90.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.posVec(4.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-120.3612F, -40.7895F, 20.941F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-61.1632F, 42.8588F, 11.5242F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0417F, KeyframeAnimations.posVec(-4.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition skill = AnimationDefinition.Builder.withLength(3.0F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0833F, KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.625F, KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.4583F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.625F, KeyframeAnimations.posVec(0.0F, -4.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_ribcage", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, 125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_ribcage", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, -125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, -125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.9167F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7083F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-42.2866F, -32.6981F, -5.0083F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-29.8376F, -35.3963F, -45.2809F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-29.8376F, -35.3963F, -45.2809F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-72.2866F, -32.6981F, -5.0083F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.degreeVec(35.2644F, -30.0F, 35.2644F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.9167F, KeyframeAnimations.degreeVec(73.7548F, -13.0931F, 19.2052F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(73.7548F, -13.0931F, 19.2052F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(58.2071F, -21.1064F, 28.7261F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.posVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.posVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-33.8069F, 32.3106F, 6.88F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-17.8783F, 34.6212F, 49.0243F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-17.8783F, 34.6212F, 49.0243F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-51.3069F, 32.3106F, 6.88F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.degreeVec(35.2644F, 30.0F, -35.2644F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.9167F, KeyframeAnimations.degreeVec(73.7548F, 13.0931F, -19.2052F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(73.7548F, 13.0931F, -19.2052F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(58.2071F, 21.1064F, -28.7261F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.8333F, KeyframeAnimations.posVec(-3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.posVec(-3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition spell1 = AnimationDefinition.Builder.withLength(2.7917F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-29.7873F, -3.742F, -6.5045F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-22.2873F, -3.742F, -6.5045F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-22.5673F, -0.8771F, 0.4332F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-30.0673F, -0.8771F, 0.4332F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-29.9537F, 2.8781F, 6.9275F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-22.4537F, 2.8781F, 6.9275F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-22.628F, -0.0005F, -0.001F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.9167F, KeyframeAnimations.degreeVec(-29.7873F, -3.742F, -6.5045F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-22.2873F, -3.742F, -6.5045F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-22.5673F, -0.8771F, 0.4332F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(-27.5673F, -0.8771F, 0.4332F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-15.0673F, -0.8771F, 0.4332F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(19.9327F, -0.8771F, 0.4332F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5417F, KeyframeAnimations.degreeVec(29.9327F, -0.8771F, 0.4332F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -1.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -2.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-3.44F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.4167F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-35.3335F, -13.4716F, -13.1914F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-34.8161F, -14.9084F, -15.3018F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-77.3161F, -14.9084F, -15.3018F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-131.3042F, -9.1018F, -20.1574F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-145.743F, -6.3063F, -24.3411F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-147.7621F, -2.3281F, -30.7185F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-162.7621F, -2.3281F, -30.7185F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-162.7389F, 2.1432F, -45.0402F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-147.7389F, 2.1432F, -45.0402F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-148.1527F, -5.8505F, -32.3306F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-163.1527F, -5.8505F, -32.3306F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-162.2444F, -1.366F, -46.6769F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-147.2444F, -1.366F, -46.6769F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-148.4459F, -9.3824F, -33.9317F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-162.7621F, -2.3281F, -30.7185F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.9167F, KeyframeAnimations.degreeVec(-162.7389F, 2.1432F, -45.0402F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-147.7389F, 2.1432F, -45.0402F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-148.1527F, -5.8505F, -32.3306F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(-157.1783F, -17.1604F, -12.4404F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-122.1783F, -17.1604F, -12.4404F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(-33.8713F, -1.0873F, 13.3123F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5417F, KeyframeAnimations.degreeVec(3.5203F, -1.3992F, 18.3037F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-35.3335F, 13.4716F, 13.1914F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-34.8161F, 14.9084F, 15.3018F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-74.8161F, 14.9084F, 15.3018F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-133.681F, 9.532F, 20.6545F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-140.4497F, 4.7891F, 26.5119F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-142.6862F, 0.2524F, 32.4916F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-127.6862F, 0.2524F, 32.4916F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-128.693F, 12.0681F, 23.1801F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-146.193F, 12.0681F, 23.1801F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-144.496F, 3.5045F, 35.6226F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-129.496F, 3.5045F, 35.6226F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-131.083F, 14.9747F, 25.8114F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-146.083F, 14.9747F, 25.8114F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-143.7618F, 6.3196F, 38.2911F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-127.6862F, 0.2524F, 32.4916F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.9167F, KeyframeAnimations.degreeVec(-128.693F, 12.0681F, 23.1801F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-146.193F, 12.0681F, 23.1801F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-144.496F, 3.5045F, 35.6226F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(-154.1182F, 18.7769F, 12.2301F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.4167F, KeyframeAnimations.degreeVec(-121.6182F, 18.7769F, 12.2301F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(-36.269F, 0.274F, -14.9989F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5417F, KeyframeAnimations.degreeVec(1.1847F, 0.4323F, -22.4974F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition spell2 = AnimationDefinition.Builder.withLength(0.5F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.7765F, -2.8978F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-94.936F, -7.4355F, -0.9845F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-87.436F, -7.4355F, -0.9845F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-87.3687F, -14.9278F, -1.3307F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-94.8687F, -14.9278F, -1.3307F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-94.7442F, -7.4541F, -1.9709F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.9829F, 0.2611F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("group2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(77.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("group2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.6498F, 4.3564F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-104.8305F, 11.8519F, 1.9975F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-104.5074F, -0.2438F, 5.1734F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-117.0074F, -0.2438F, 5.1734F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-117.5426F, 10.8758F, -0.5707F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-105.0426F, 10.8758F, -0.5707F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition spell3 = AnimationDefinition.Builder.withLength(0.5F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-2.5F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-5.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(10.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(12.5F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(7.4205F, -12.5212F, 0.3342F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(7.5283F, -4.9571F, -0.6543F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(10.0283F, -4.9571F, -0.6543F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-10.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-4.8942F, 12.5376F, 0.4446F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-81.8739F, 4.3066F, 7.917F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-94.3739F, 4.3066F, 7.917F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-19.3739F, 4.3066F, 7.917F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("group2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("group2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.9537F, 0.3007F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -2.7024F, 2.9491F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -0.7681F, 2.4324F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-9.9907F, 0.434F, 2.4621F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-25.3609F, 9.4883F, -1.8035F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(10.0262F, -8.551F, -9.1202F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(12.5262F, -8.551F, -9.1202F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0262F, -8.551F, -9.1202F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();
}