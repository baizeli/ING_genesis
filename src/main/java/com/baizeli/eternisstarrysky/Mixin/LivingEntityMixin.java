package com.baizeli.eternisstarrysky.Mixin;

import com.baizeli.eternisstarrysky.Util.AvaritiaKill;
import com.baizeli.eternisstarrysky.Util.AvaritiaLivingEntity;
import com.baizeli.eternisstarrysky.client.renderer.EvasionAnimationRenderer;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import com.baizeli.eternisstarrysky.sound.SoundsRegister;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements AvaritiaKill {
	
	@Shadow public int deathTime;

	@Shadow public abstract void remove(Entity.RemovalReason p_276115_);

	@Shadow public abstract float tickHeadTurn(float p_21260_, float p_21261_);

	@Shadow public abstract boolean isAlive();

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

	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity entity = (LivingEntity) (Object) this;

		if (entity.hasEffect(ModEffect.PERFECT_EVASION.get())) {
			Random random = new Random();

			if (random.nextInt(100) < 75) {
				entity.level().playSound(
					null, entity.getX(), entity.getY(), entity.getZ(), 
					SoundsRegister.EVASION.get(), SoundSource.PLAYERS, 1.0F, 1.0F
				);
					
				if (FMLEnvironment.dist == Dist.CLIENT) {
					EvasionAnimationRenderer.triggerEvasionAnimation(entity);
				}

				cir.cancel();
			}
		}
	}

	/*
	@Overwrite
	public float getHealth(){
		 if (InfinitySwordTrue.entities.contains(this)){
			return 0;
		 }
		 return InfinitySwordTrue.getHealth(this);
	}
	 */

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