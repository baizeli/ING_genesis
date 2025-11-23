package com.baizeli.eternisstarrysky.event.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class SiphonEvent {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (attacker.hasEffect(ModEffect.SIPHON.get())) {
                // 获取buff等级
                int amplifier = attacker.getEffect(ModEffect.SIPHON.get()).getAmplifier();
                
                // 计算转换的血量
                float healAmount = event.getAmount() * 0.06f * (amplifier + 1);
                
                // 治疗攻击者
                attacker.heal(healAmount);
            }
        }
    }
}