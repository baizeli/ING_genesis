package miku.united_as_one.genesis.client.render.tooltip;

import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.item.Scroll;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class TooltipBackgroundRenderContext {
    private TooltipBackgroundRenderContext() {
    }

    public static Style resolve(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return Style.NONE;
        }

        Item item = stack.getItem();
        if (item instanceof Scroll) {
            try {
                SchoolType schoolType = ISpellContainer.getOrCreate(stack).getSpellAtIndex(0).getSpell().getSchoolType();
                if (schoolType.equals(SpellSchoolRegistry.CHAOS.get())) {
                    return Style.CHAOS;
                }
                if (schoolType.equals(SpellSchoolRegistry.CELESTIAL_SOURCE.get())) {
                    return Style.CELESTIAL_SOURCE;
                }
            } catch (RuntimeException ignored) {
                return Style.NONE;
            }
        }

        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName != null && registryName.getNamespace().equals(Genesis.MOD_ID)) {
            return Style.TRANSPARENT;
        }

        return Style.NONE;
    }

    public enum Style {
        NONE(0),
        TRANSPARENT(0),
        CHAOS(14),
        CELESTIAL_SOURCE(9);

        private final int cosmicType;

        Style(int cosmicType) {
            this.cosmicType = cosmicType;
        }

        public boolean drawsCosmic() {
            return this == CHAOS || this == CELESTIAL_SOURCE;
        }

        public int cosmicType() {
            return cosmicType;
        }
    }
}
