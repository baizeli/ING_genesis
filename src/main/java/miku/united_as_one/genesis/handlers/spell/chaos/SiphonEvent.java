package miku.united_as_one.genesis.handlers.spell.chaos;

import miku.united_as_one.genesis.registries.effect.EffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SiphonEvent {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (attacker.hasEffect(EffectRegistry.SIPHON.get())) {
                attacker.heal(event.getAmount() * 0.06f * (attacker.getEffect(EffectRegistry.SIPHON.get()).getAmplifier() + 1));
            }
        }
    }
}