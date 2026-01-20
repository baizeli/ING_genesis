package miku.united_as_one.genesis.Items.curios.rune_plus;

import miku.united_as_one.genesis.Items.curios.ESSCurioItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class NatureRunePlus extends ESSCurioItem {

    public NatureRunePlus() {
        attributeModifiers.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                "Nature Rune Plus Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
        attributeModifiers.put(AttributeRegistry.NATURE_SPELL_POWER.get(), new AttributeModifier(
                "Nature Rune Plus Nature Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
    }

    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof NatureRunePlus;
    }
}
