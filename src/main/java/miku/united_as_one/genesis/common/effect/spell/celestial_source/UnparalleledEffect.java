package miku.united_as_one.genesis.common.effect.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class UnparalleledEffect extends MobEffect {
    public UnparalleledEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF00FF);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + Genesis.MOD_ID + ".unparalleled";
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.getPersistentData().putBoolean("isUnparalleledActive", true);
        }
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    
    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.getPersistentData().putBoolean("isUnparalleledActive", false);
        }
    }
}