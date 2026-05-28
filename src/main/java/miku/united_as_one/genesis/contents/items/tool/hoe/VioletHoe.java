package miku.united_as_one.genesis.contents.items.tool.hoe;

import miku.united_as_one.genesis.registries.item.TierRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VioletHoe extends HoeItem {
    public VioletHoe(Properties props) {
        super(TierRegistry.VIOLET_GALAXY_INGOT, (int) -TierRegistry.VIOLET_GALAXY_INGOT.getAttackDamageBonus(), 0.0F, props);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.united_as_one.violet_hoe.line1"));
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        int level = super.getEnchantmentLevel(stack, enchantment);
        return enchantment == Enchantments.BLOCK_FORTUNE ? level + 6 : level;
    }
}
