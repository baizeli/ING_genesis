package miku.united_as_one.genesis.effect.spell.chaos;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID)
public class SiphonEffect extends MobEffect {
    public SiphonEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + Genesis.MOD_ID + ".siphon";
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
}