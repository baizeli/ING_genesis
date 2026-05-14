package miku.united_as_one.genesis.contents.effect.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.effect.*;

public class LifeAndDeathRealmEffect extends MobEffect {
    public LifeAndDeathRealmEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + Genesis.MOD_ID + ".life_and_death_realm";
    }
}