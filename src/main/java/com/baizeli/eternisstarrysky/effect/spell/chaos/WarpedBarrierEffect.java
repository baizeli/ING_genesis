package com.baizeli.eternisstarrysky.effect.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class WarpedBarrierEffect extends MobEffect {
    public WarpedBarrierEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }

    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".warped_barrier";
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        String key = EternisStarrySky.MOD_ID + ":shield_amount";
        CompoundTag data = livingEntity.getPersistentData();

        if (data.contains(key, CompoundTag.TAG_FLOAT)) {
            livingEntity.setAbsorptionAmount(livingEntity.getAbsorptionAmount() - data.getFloat(key));
            data.remove(key);
        }
        super.removeAttributeModifiers(livingEntity, attributeMap, amplifier);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
