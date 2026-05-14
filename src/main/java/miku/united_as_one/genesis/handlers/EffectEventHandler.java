package miku.united_as_one.genesis.handlers;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.effect.EffectRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectEventHandler {

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        MobEffectInstance effect = event.getEntity().getEffect(EffectRegistry.CHAOS_RESISTANCE.get());
        if (effect != null) {
            int amplifier = effect.getAmplifier();
            float multiplier = 1.3F + (amplifier * 0.1F);
            event.setAmount(event.getAmount() * multiplier);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.hasEffect(EffectRegistry.MANA_SHIELD.get())) {
                float damage = event.getAmount();
                if (damage > 0) {
                    MagicData magicData = MagicData.getPlayerMagicData(player);
                    float currentMana = magicData.getMana();
                    float requiredMana = damage * 4.0F;

                    if (currentMana > 0) {
                        float consumedMana = Math.min(currentMana, requiredMana);
                        magicData.setMana(currentMana - consumedMana);

                        float reductionRatio = consumedMana / requiredMana;
                        float newDamage = damage - (damage * 0.5F * reductionRatio);
                        event.setAmount(newDamage);
                    }
                }
            }
        }
    }
}