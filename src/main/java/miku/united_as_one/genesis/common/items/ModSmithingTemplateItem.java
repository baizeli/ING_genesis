package miku.united_as_one.genesis.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModSmithingTemplateItem extends SmithingTemplateItem {
    private final String key;
    public ModSmithingTemplateItem(Component appliesTo, Component ingredients, Component updradeDescription, Component baseSlotDescription, Component additionsSlotDescription, List<ResourceLocation> baseSlotEmptyIcons, List<ResourceLocation> additonalSlotEmptyIcons, String key) {
        super(appliesTo, ingredients, updradeDescription, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additonalSlotEmptyIcons);
        this.key = key;
    }

    @Override
    public @NotNull String getDescriptionId() {
        return key;
    }
}
