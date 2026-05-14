package miku.united_as_one.genesis.contents.items.curios.rune_plus;

import miku.united_as_one.genesis.contents.items.curios.ESSCurioItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class IceRunePlus extends ESSCurioItem {

    public IceRunePlus() {
        attributeModifiers.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                "Ice Rune Plus Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
        attributeModifiers.put(AttributeRegistry.ICE_SPELL_POWER.get(), new AttributeModifier(
                "Ice Rune Plus Ice Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
    }

    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof IceRunePlus;
    }
}
