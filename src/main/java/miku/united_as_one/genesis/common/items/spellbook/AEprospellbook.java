package miku.united_as_one.genesis.common.items.spellbook;

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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class AEprospellbook extends SpellBook {
    public AEprospellbook() {
        super(13);
        this.withSpellbookAttributes(new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05,
                AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.15,
                AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.CAST_TIME_REDUCTION, 0.15,
                AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.15,
                AttributeModifier.Operation.MULTIPLY_BASE), new AttributeContainer(AttributeRegistry.MAX_MANA, 300,
                AttributeModifier.Operation.ADDITION));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            public @NotNull Font getFont(ItemStack stack, IClientItemExtensions.FontContext context) {
                return FuckFont1.getFont();
            }
        });
    }

    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.iron_spells_genesis.disk_spell_book.description"));
    }
}
