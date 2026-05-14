package miku.united_as_one.genesis.handlers.spell.celestial_source;

import miku.united_as_one.genesis.registries.effect.EffectRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.Tags;

@Mod.EventBusSubscriber
public class LifeAndDeathRealmEvent {
    private static boolean playerHasBeenSacrificed = false;
    private static long sacrificeImmunityEndTime = 0;

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        event.getEffectInstance();
        if (event.getEffectInstance().getEffect() == EffectRegistry.LIFE_AND_DEATH_REALM.get() && 
            event.getEntity() instanceof ServerPlayer
        ) {
            playerHasBeenSacrificed = false;
            sacrificeImmunityEndTime = 0;
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() != null && 
            event.getEffectInstance().getEffect() == EffectRegistry.LIFE_AND_DEATH_REALM.get() &&
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
            sacrificeImmunityEndTime = 0;
        }
    }
    
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.hasEffect(EffectRegistry.LIFE_AND_DEATH_REALM.get())) {
                if (event.getSource().getEntity() instanceof LivingEntity attacker) {
                    if (event.getAmount() >= player.getHealth()) {
                        // 只能替死一次
                        if (playerHasBeenSacrificed) {
                            return;
                        }
                        
                        // 取消伤害
                        event.setCanceled(true);

                        if (!attacker.getType().is(Tags.EntityTypes.BOSSES)) {
                            // 虚空伤害最大值/设置生命值0
                            attacker.hurt(player.level().damageSources().fellOutOfWorld(), Float.MAX_VALUE);
                            attacker.setHealth(0.0F);
                        }

                        // 替死完将生命值回满/向后一个力/音效
                        player.setHealth(player.getMaxHealth());
                        player.knockback(5.0F, attacker.getX() - player.getX(), attacker.getZ() - player.getZ());
                        player.level().playSound(
                            null, player.getX(), player.getY(), player.getZ(), 
                            SoundEvents.TOTEM_USE, player.getSoundSource(), 1.0F, 1.0F
                        );
                        
                        // 标记施法者已被替死/移除效果
                        playerHasBeenSacrificed = true;
                        player.removeEffect(EffectRegistry.LIFE_AND_DEATH_REALM.get());
                        sacrificeImmunityEndTime = player.level().getGameTime() + (5 * 20);
                    }
                }
            }
        }
    }

    public static boolean isPlayerInSacrificeImmunity(ServerPlayer player) {
        return playerHasBeenSacrificed && player.level().getGameTime() < sacrificeImmunityEndTime;
    }
}