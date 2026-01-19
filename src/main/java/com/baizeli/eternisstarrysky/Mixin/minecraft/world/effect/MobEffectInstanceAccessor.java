package com.baizeli.eternisstarrysky.Mixin.minecraft.world.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEffectInstance.class)
public interface MobEffectInstanceAccessor
{
	@Accessor
	MobEffect getEffect();

	@Accessor
	int getDuration();

	@Accessor
	void setDuration(int duration);
}
