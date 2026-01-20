package miku.united_as_one.genesis.effect.spell.chaos;

import miku.united_as_one.genesis.EternisStarrySky;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class ConfusionEffect extends MobEffect {
    public ConfusionEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0000);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".confusion";
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            RandomSource random = entity.level().random;

            double motionX = (random.nextDouble() - 0.5) * 0.5;
            double motionY = entity.getDeltaMovement().y;
            double motionZ = (random.nextDouble() - 0.5) * 0.5;
            
            entity.setDeltaMovement(motionX, motionY, motionZ);
        }
        
        super.applyEffectTick(entity, amplifier);
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}