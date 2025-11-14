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
public class SiphonEffect extends MobEffect {
    public SiphonEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".siphon";
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // 每秒扣除施法者血量
        if (entity.level().getGameTime() % 20 == 0) {
            float maxHealth = entity.getMaxHealth();
            float damage = maxHealth * 0.02f;

            // 虚空伤害
            entity.hurt(entity.damageSources().genericKill(), damage);
        }
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (attacker.hasEffect(ModEffect.SIPHON.get())) {
                // 获取效果等级
                int amplifier = attacker.getEffect(ModEffect.SIPHON.get()).getAmplifier();
                
                // 计算转换的血量
                float healAmount = event.getAmount() * 0.06f * (amplifier + 1);
                
                // 治疗攻击者
                attacker.heal(healAmount);
            }
        }
    }
}