package miku.united_as_one.genesis.init.mixin.minecraft.world.entity;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.items.AvaritiaSword;
import miku.united_as_one.genesis.common.items.curios.rune_plus.NatureRunePlus;
import miku.united_as_one.genesis.init.mixin.minecraft.world.effect.MobEffectInstanceAccessor;
import miku.united_as_one.genesis.init.registry.EffectRegistry;
import miku.united_as_one.genesis.init.registry.SoundRegister;
import miku.united_as_one.genesis.client.renderer.EvasionAnimationRenderer;
import miku.united_as_one.genesis.common.spell.chaos.ReversePlagueSpell;
import miku.united_as_one.genesis.common.event.spell.celestial_source.LifeAndDeathRealmEvent;
import miku.united_as_one.genesis.util.ModCurios;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonCloud;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	
	@Shadow public int deathTime;

	@Shadow public abstract void remove(Entity.RemovalReason p_276115_);

	@Shadow public abstract boolean isAlive();

    @Shadow public abstract int getArmorValue();

	@Unique
	private boolean ava = false;

	@Unique
	private int death = 0;

	@Inject(
		method = "baseTick",
		at = @At(
			value = "INVOKE", 
			target = "Lnet/minecraft/world/entity/LivingEntity;isDeadOrDying()Z"
		)
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
		}
	}

	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity entity = (LivingEntity) (Object) this;
        
		if (entity instanceof ServerPlayer serverPlayer && LifeAndDeathRealmEvent.isPlayerInSacrificeImmunity(serverPlayer)) {
			cir.cancel();
		}

		if (entity.hasEffect(EffectRegistry.PERFECT_EVASION.get())) {
			Random random = new Random();

			if (random.nextInt(100) < 75) {
				entity.level().playSound(
					null, entity.getX(), entity.getY(), entity.getZ(), 
					SoundRegister.EVASION.get(), SoundSource.PLAYERS, 1.0F, 1.0F
				);
					
				if (FMLEnvironment.dist == Dist.CLIENT) {
					EvasionAnimationRenderer.triggerEvasionAnimation(entity);
				}

				cir.cancel();
			}
		}
	}

    @ModifyVariable(method = "hurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float modifyDamageAmount(float amount, DamageSource damageSource) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player player)
        {
            if (isBlockingWithInfinitySword(player))
            {
                return 0.0f;
            }
        }

        return amount;
    }

    private boolean isBlockingWithInfinitySword(Player player) {
        if (!player.isUsingItem()) {
            return false;
        }

        ItemStack usingItem = player.getUseItem();
        return (usingItem.getItem() instanceof AvaritiaSword &&
                usingItem.getUseAnimation() == UseAnim.BLOCK);
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE",
            target = "net/minecraft/world/entity/LivingEntity.actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"),
            cancellable = true)
    private void onActuallyHurt(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player player)
        {
            if (isBlockingWithInfinitySword(player))
            {
                addBlockEffect(player, damageSource);
            }
        }
    }

    private void addBlockEffect(Player player, DamageSource damageSource) {
        Level level = player.level();
        if (!level.isClientSide)
        {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.6F, 2.0F);
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void addEffect(MobEffectInstance effectInstance, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity living = (LivingEntity) (Object) this;



        if (living.level().isClientSide()) {
            return;
        }

        if (!(living.level() instanceof ServerLevel serverLevel)) {
            return;
        }


        Map<LivingEntity, LivingEntity> entityMap = new HashMap<>();
        ReversePlagueSpell.entityMap.forEach(((uuid, uuid1) -> {
            LivingEntity livingEntity = (LivingEntity) serverLevel.getEntity(uuid);
            LivingEntity livingEntity1 = (LivingEntity) serverLevel.getEntity(uuid1);
            if (livingEntity != null && livingEntity1 != null) {
                entityMap.put(livingEntity, livingEntity1);
            }
        }));

        if (entityMap.containsKey(living)) {
            if (entityMap.get(living) != null && entityMap.get(living).getPersistentData().getLong(Genesis.MOD_ID + "remaining_time") >= serverLevel.getGameTime()) {
                if (((MobEffectInstanceAccessor) effectInstance).getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    entityMap.get(living).addEffect(effectInstance);
                }
                cir.cancel();
            } else {
                ReversePlagueSpell.entityMap.remove(living.getUUID());
            }
        }

        if (entityMap.containsValue(living) && living.getPersistentData().getLong(Genesis.MOD_ID + "remaining_time") >= serverLevel.getGameTime() && ((MobEffectInstanceAccessor) effectInstance).getEffect().getCategory() == MobEffectCategory.BENEFICIAL) {
            cir.cancel();
        }
    }

    @Redirect(
            method = "getDamageAfterArmorAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getArmorValue()I"
            )
    )
    public int onDamageArmorAbsorb(LivingEntity instance, DamageSource source) {
        Entity directEntity = source.getDirectEntity();
        Entity owner = source.getEntity();
        int armorValue = getArmorValue();
        if(owner instanceof LivingEntity entity && directEntity instanceof PoisonCloud) {
            if(ModCurios.hasCurios(entity, NatureRunePlus::test)) {
                return 0;
            }
        }
        return armorValue;
    }
	
/* 	@Overwrite
	public float getHealth(){
		 if (InfinitySwordTrue.entities.contains(this)){
			return 0;
		 }
		 return InfinitySwordTrue.getHealth(this);
	} */
}