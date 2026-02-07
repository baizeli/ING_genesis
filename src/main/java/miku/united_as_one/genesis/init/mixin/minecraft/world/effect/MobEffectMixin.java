package miku.united_as_one.genesis.init.mixin.minecraft.world.effect;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.spell.chaos.ReversePlagueSpell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(MobEffect.class)
public class MobEffectMixin {
    @Shadow @Final private MobEffectCategory category;

    // 处理瞬间型药水
    @Inject(method = "applyInstantenousEffect", at = @At("HEAD"), cancellable = true)
    private void applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity livingEntity, int amplifier, double health, CallbackInfo ci) {
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            Map<LivingEntity, LivingEntity> entityMap = new HashMap<>();
            ReversePlagueSpell.entityMap.forEach(((uuid, uuid1) -> {
                LivingEntity living = (LivingEntity) serverLevel.getEntity(uuid);
                LivingEntity living1 = (LivingEntity) serverLevel.getEntity(uuid1);
                if (living != null && living1 != null) {
                    entityMap.put(living, living1);
                }
            }));

            if (entityMap.containsKey(livingEntity) && entityMap.get(livingEntity) != null) {
                if (entityMap.get(livingEntity) != null && entityMap.get(livingEntity).getPersistentData().getLong(Genesis.MOD_ID + "remaining_time") >= serverLevel.getGameTime()) {
                    if (this.category == MobEffectCategory.HARMFUL) {
                        ((MobEffect) (Object) this).applyInstantenousEffect(source, indirectSource, entityMap.get(livingEntity), amplifier, health);
                    }
                    ci.cancel();
                } else {
                    ReversePlagueSpell.entityMap.remove(livingEntity.getUUID());
                }
            }

            if (entityMap.containsValue(livingEntity) && livingEntity.getPersistentData().getLong(Genesis.MOD_ID + "remaining_time") >= serverLevel.getGameTime() && this.category == MobEffectCategory.BENEFICIAL) {
                ci.cancel();
            }
        }
    }
}
