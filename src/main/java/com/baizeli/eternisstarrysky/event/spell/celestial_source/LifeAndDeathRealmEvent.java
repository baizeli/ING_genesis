package com.baizeli.eternisstarrysky.event.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID)
public class LifeAndDeathRealmEvent {

    // 施法者是否被替死的标记
    private static boolean playerHasBeenSacrificed = false;

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() != null && 
            event.getEffectInstance().getEffect() == ModEffect.LIFE_AND_DEATH_REALM.get() &&
            event.getEntity() instanceof ServerPlayer player
        ) {
            // 如果buff期间内被替死了以后buff结束就不会将自己杀了
            if (!playerHasBeenSacrificed) {
                // buff结束后自己把自己杀了
                if (!player.isDeadOrDying()) {
                    // 设置生命值0/虚空伤害最大值
                    player.setHealth(0.0F);
                    player.hurt(player.level().damageSources().fellOutOfWorld(), Float.MAX_VALUE);
                }
            }
            
            // 重置一下标记
            playerHasBeenSacrificed = false;
        }
    }
    
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.hasEffect(ModEffect.LIFE_AND_DEATH_REALM.get())) {
                if (event.getSource().getEntity() instanceof LivingEntity attacker) {
                    if (event.getAmount() >= player.getHealth()) {
                        // 只能替死一次
                        if (playerHasBeenSacrificed) {
                            return;
                        }
                        
                        // 取消伤害
                        event.setCanceled(true);

                        // 你打的我你自己承担/虚空伤害最大值/设置生命值0
                        attacker.hurt(player.level().damageSources().fellOutOfWorld(), Float.MAX_VALUE);
                        attacker.setHealth(0.0F);
                        
                        // 替死完将生命值回满
                        player.setHealth(player.getMaxHealth());
                        
                        // 标记施法者已被替死
                        playerHasBeenSacrificed = true;
                    }
                }
            }
        }
    }
}