package miku.united_as_one.genesis.effect.spell.chaos;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.effect.*;

public class BloodWarEffect extends MobEffect {
    public BloodWarEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + Genesis.MOD_ID + ".blood_war";
    }
}