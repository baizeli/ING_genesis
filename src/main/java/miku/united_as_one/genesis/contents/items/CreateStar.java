package miku.united_as_one.genesis.contents.items;

import miku.bai_ze_li.genesis.api.annotation.GenesisAnnotations;
import miku.bai_ze_li.genesis.api.annotation.GenesisItemNameEffect;
import miku.bai_ze_li.genesis.api.annotation.GenesisTextEffect.Preset;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@GenesisItemNameEffect(preset = Preset.BLUE_GRADIENT)
public class CreateStar extends Item {

    public CreateStar(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public Component getName(ItemStack stack) {
        Component originalName = super.getName(stack);
        if (stack.hasCustomHoverName()) {
            return originalName;
        }
        stack.getOrCreateTag().putInt("HideFlags", 2);
        return GenesisAnnotations.applyItemNameEffect(CreateStar.class, originalName);
    }
}
