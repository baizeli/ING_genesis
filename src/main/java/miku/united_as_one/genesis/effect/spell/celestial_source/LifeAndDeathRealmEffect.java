package miku.united_as_one.genesis.effect.spell.celestial_source;

import miku.united_as_one.genesis.EternisStarrySky;
import net.minecraft.world.effect.*;

public class LifeAndDeathRealmEffect extends MobEffect {
    public LifeAndDeathRealmEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".life_and_death_realm";
    }
}