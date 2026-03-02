package miku.united_as_one.genesis.common.items.tool.shovel;

import miku.united_as_one.genesis.init.registry.TierRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VioletShovel extends ShovelItem {
    public VioletShovel(Properties props) {
        super(TierRegistry.VIOLET_GALAXY_INGOT, 4.0F, -1.0F, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§d紫极之力凝聚的轻铲"));
        tooltip.add(Component.literal("§7特殊能力: §f蹲下挖掘可触发 5x5 范围挖掘"));
    }
}