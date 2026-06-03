package miku.united_as_one.genesis.contents.effect.spell.celestial_source;

import miku.bai_ze_li.genesis.api.nbt.GenesisPersistentData;
import miku.bai_ze_li.genesis.api.nbt.PersistentDataKey;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class UnparalleledEffect extends MobEffect {
    public static final PersistentDataKey ACTIVE = PersistentDataKey.of(Genesis.MOD_ID, "unparalleled_active");

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
        if (entity instanceof Player player) {
            GenesisPersistentData.putBoolean(player, ACTIVE, true);
        }
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    
    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) {
            GenesisPersistentData.putBoolean(player, ACTIVE, false);
        }
    }
}
