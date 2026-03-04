package miku.united_as_one.genesis.common.items.tool.hoe;

import miku.united_as_one.genesis.init.registry.TierRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VioletHoe extends HoeItem {
    public VioletHoe(Properties props) {
        super(TierRegistry.VIOLET_GALAXY_INGOT, 0, 96.0F, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§d紫极之力凝聚的灵锄"));
        tooltip.add(Component.literal("§7天恩: §6永久 +6 时运等级"));
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        int level = super.getEnchantmentLevel(stack, enchantment);
        return enchantment == Enchantments.BLOCK_FORTUNE ? level + 6 : level;
    }
}