package miku.united_as_one.genesis.contents.items.spell.spellbook;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.*;

import java.util.List;

public class LightningSpellBook extends SpellBook {
    public LightningSpellBook() {
        super(10);
        this.withSpellbookAttributes(
            new AttributeContainer(AttributeRegistry.MAX_MANA, 100, AttributeModifier.Operation.ADDITION),
            new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.1, AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.08, AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.08, AttributeModifier.Operation.MULTIPLY_BASE)
        );
    }

    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.iron_spells_genesis.lightning_spell_book.description_1"));
        pTooltipComponents.add(Component.translatable("tooltip.iron_spells_genesis.lightning_spell_book.description_2"));
    }
}