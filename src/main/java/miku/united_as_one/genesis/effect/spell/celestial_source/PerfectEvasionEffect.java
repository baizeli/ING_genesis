package miku.united_as_one.genesis.effect.spell.celestial_source;

import miku.united_as_one.genesis.EternisStarrySky;
import net.minecraft.world.effect.*;

public class PerfectEvasionEffect extends MobEffect {
    public PerfectEvasionEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".perfect_evasion";
    }
}