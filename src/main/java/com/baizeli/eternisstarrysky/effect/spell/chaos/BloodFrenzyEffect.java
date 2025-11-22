package com.baizeli.eternisstarrysky.effect.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class BloodFrenzyEffect extends MobEffect {
    public BloodFrenzyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".blood_frenzy";
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // 一个简单的回血.
        if (entity.level().getGameTime() % 20 == 0) {
            entity.heal(1.0f * (amplifier + 1));
        }
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        // 攻击的伤害增加.
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (attacker.hasEffect(ModEffect.BLOOD_FRENZY.get())) {
                event.setAmount(event.getAmount() * 2.0f);
            }
        }

        // 受伤的减免.
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity victim = (LivingEntity) event.getEntity();
            if (victim.hasEffect(ModEffect.BLOOD_FRENZY.get())) {
                event.setAmount(event.getAmount() * 0.5f);
            }
        }
    }
}