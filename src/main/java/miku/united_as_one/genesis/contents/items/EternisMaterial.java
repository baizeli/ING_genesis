package miku.united_as_one.genesis.contents.items;

import miku.bai_ze_li.genesis.api.annotation.GenesisAnnotations;
import miku.bai_ze_li.genesis.api.annotation.GenesisTextEffect.Preset;
import miku.bai_ze_li.genesis.api.annotation.GenesisTooltipTextEffect;
import miku.bai_ze_li.genesis.api.item.GenesisPurpleTooltipParticleItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public final class EternisMaterial extends GenesisPurpleTooltipParticleItem
{

    private final int type;

    public EternisMaterial(Properties p_41383_, int type)
    {
        super(p_41383_);
        this.type = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        if (type == 0) {
            GenesisAnnotations.addTooltipEffects(VioletGalaxyIngotText.class, tooltip);
        }
    }

    @GenesisTooltipTextEffect(
            preset = Preset.RAINBOW_GRADIENT,
            translation = "item.genesis_magic.violet_galaxy_ingot1"
    )
    private static final class VioletGalaxyIngotText {
        private VioletGalaxyIngotText() {
        }
    }
}
