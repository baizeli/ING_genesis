package com.baizeli.eternisstarrysky.Items;

import com.baizeli.eternisstarrysky.spell.Attributes;
import com.baizeli.eternisstarrysky.spell.Spells;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChaosSpellBook extends UniqueSpellBook implements UniqueItem {
    public ChaosSpellBook() {
        super(SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.ECHOING_STRIKES_SPELL, 10)), 11);
        this.withSpellbookAttributes(new AttributeContainer[]{new AttributeContainer(Attributes.CHAOS_SPELL_POWER, 0.2, AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, -0.2, AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.2, AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.MAX_MANA, 600, AttributeModifier.Operation.ADDITION)});
    }

    public void appendHoverText(@NotNull ItemStack itemStack, Level context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, context, lines, flag);
        AffinityData affinityData = AffinityData.getAffinityData(itemStack);
        if (!affinityData.affinityData().isEmpty()) {
            int i = TooltipsUtils.indexOfComponent(lines, "tooltip.irons_spellbooks.spellbook_spell_count");
            lines.addAll(i < 0 ? lines.size() : i + 1, affinityData.getDescriptionComponent());
        }

    }
}
