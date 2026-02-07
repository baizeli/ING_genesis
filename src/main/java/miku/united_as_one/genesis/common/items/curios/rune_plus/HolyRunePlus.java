package miku.united_as_one.genesis.common.items.curios.rune_plus;

import miku.united_as_one.genesis.common.items.curios.ESSCurioItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class HolyRunePlus extends ESSCurioItem {

    public HolyRunePlus() {
        attributeModifiers.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                "Holy Rune Plus Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
        attributeModifiers.put(AttributeRegistry.ENDER_SPELL_POWER.get(), new AttributeModifier(
                "Holy Rune Plus Holy Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
    }

    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof HolyRunePlus;
    }
}
