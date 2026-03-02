package miku.united_as_one.genesis.common.items.tool.axe;

import miku.united_as_one.genesis.init.registry.TierRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VioletAxe extends AxeItem {
    public VioletAxe(Properties props) {
        super(TierRegistry.VIOLET_GALAXY_INGOT, 26.0F, -3.3F, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§d紫极之力凝聚的重斧"));
        tooltip.add(Component.literal("§7特殊能力: §f砍伐木材时可触发连锁采集"));
    }
}