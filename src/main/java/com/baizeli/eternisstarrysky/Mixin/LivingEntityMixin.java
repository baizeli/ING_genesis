package com.baizeli.eternisstarrysky.Mixin;

import com.baizeli.eternisstarrysky.AvaritiaKill;
import com.baizeli.eternisstarrysky.AvaritiaLivingEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements AvaritiaKill
{
	@Shadow public int deathTime;
	@Unique
	private boolean ava = false;
	@Unique
	private int death = 0;

	@Inject(
		method = "baseTick()V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isDeadOrDying()Z")
	)
	public void baseTick(CallbackInfo ci)
	{
		if (this.ava)
			this.death++;
		else
			this.death = 0;
		if (this.death >= 20)
		{
			this.deathTime = 20;
			AvaritiaLivingEntity.tickDeath((LivingEntity) (Object) this);
		}
	}

	@Override
	public boolean dead()
	{
		return this.ava;
	}

	@Override
	public void dead(boolean dead)
	{
		this.ava = dead;
	}
}
