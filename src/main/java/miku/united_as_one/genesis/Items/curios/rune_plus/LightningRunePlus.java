package miku.united_as_one.genesis.Items.curios.rune_plus;

import miku.united_as_one.genesis.Items.curios.ESSCurioItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class LightningRunePlus extends ESSCurioItem {

    public LightningRunePlus() {
        attributeModifiers.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                "Lightning Rune Plus Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
        attributeModifiers.put(AttributeRegistry.LIGHTNING_SPELL_POWER.get(), new AttributeModifier(
                "Lightning Rune Plus Lightning Spell Power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE
        ));
        attributeModifiers.put(AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(
                "Lightning Rune Plus Cooldown Reduction", 0.15, AttributeModifier.Operation.MULTIPLY_BASE
        ));
    }

    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof LightningRunePlus;
    }
}
