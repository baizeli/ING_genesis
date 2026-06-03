package miku.united_as_one.genesis.contents.items.spell.spellbook;

import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.*;

import java.util.List;

public class LightningSpellBook extends SpellBook {
    public LightningSpellBook() {
        super(10);
    }

    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.genesis_magic.lightning_spell_book.description_1"));
        pTooltipComponents.add(Component.translatable("tooltip.genesis_magic.lightning_spell_book.description_2"));
    }
}
