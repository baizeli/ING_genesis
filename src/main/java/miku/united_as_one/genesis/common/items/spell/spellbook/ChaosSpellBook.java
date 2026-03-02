package miku.united_as_one.genesis.common.items.spell.spellbook;

import miku.united_as_one.genesis.client.fonts.FuckFont1;
import miku.united_as_one.genesis.init.registry.spell.SpellAttributesRegistry;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ChaosSpellBook extends SpellBook {
    public ChaosSpellBook() {
        super(14);
        this.withSpellbookAttributes(new AttributeContainer(SpellAttributesRegistry.CHAOS_SPELL_POWER, 0.1,
                AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.3,
                AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.1,
                AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.MAX_MANA, 500,
                AttributeModifier.Operation.ADDITION));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            public @NotNull Font getFont(ItemStack stack, IClientItemExtensions.FontContext context) {
                return FuckFont1.getFont();
            }
        });
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
