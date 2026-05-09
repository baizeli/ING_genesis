package miku.united_as_one.genesis.handlers.spell.celestial_source;

import miku.united_as_one.genesis.registries.effect.EffectRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class StellarSoulControlEvent {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker// &&
            //event.getSource().getDirectEntity() == attacker
        ) {
            if (attacker.hasEffect(EffectRegistry.STELLAR_SOUL_CONTROL.get())) {
                LivingEntity target = event.getEntity();
                
                // 枯萎
                target.addEffect(new MobEffectInstance(
                    MobEffectRegistry.BLIGHT.get(),
                    80,
                    8,
                    false,
                    false,
                    true
                ));
                
                // 虚弱
                target.addEffect(new MobEffectInstance(
                    MobEffects.WEAKNESS,
                    80,
                    8,
                    false,
                    false,
                    true
                ));
                
                // 缓慢
                target.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    80,
                    3,
                    false,
                    false,
                    true
                ));
            }
        }
    }
}