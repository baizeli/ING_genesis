package miku.united_as_one.genesis.contents.items.tool.axe;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.*;

import java.util.List;

public class DivineMetalAxe extends AxeItem {
    public DivineMetalAxe(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, -tier.getAttackDamageBonus(), 0.0F, properties);
    }

    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.genesis_magic.divine_metal.description_1"));
    }
}
