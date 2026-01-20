package miku.united_as_one.genesis.Items.curios.rune_plus;

import miku.united_as_one.genesis.Items.curios.ESSCurioItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class EnderRunePlus extends ESSCurioItem {

    public EnderRunePlus() {
        attributeModifiers.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                "Ender Rune Plus Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
        attributeModifiers.put(AttributeRegistry.ENDER_SPELL_POWER.get(), new AttributeModifier(
                "Ender Rune Plus Ender Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
    }

    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof EnderRunePlus;
    }
}
