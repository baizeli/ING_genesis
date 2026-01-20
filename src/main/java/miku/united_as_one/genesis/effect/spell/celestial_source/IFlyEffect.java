package miku.united_as_one.genesis.effect.spell.celestial_source;

import miku.united_as_one.genesis.EternisStarrySky;
import net.minecraft.world.effect.*;

public class IFlyEffect extends MobEffect {
    public IFlyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FF00);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".i_fly";
    }
}