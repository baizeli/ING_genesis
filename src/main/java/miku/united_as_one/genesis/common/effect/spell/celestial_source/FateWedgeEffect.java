package miku.united_as_one.genesis.common.effect.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.effect.*;

public class FateWedgeEffect extends MobEffect {
    public FateWedgeEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + Genesis.MOD_ID + ".fate_wedge";
    }
}