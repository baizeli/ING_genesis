package miku.united_as_one.genesis.contents.items.tool.shovel;

import miku.united_as_one.genesis.registries.item.TierRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VioletShovel extends ShovelItem {
    public VioletShovel(Properties props) {
        super(TierRegistry.VIOLET_GALAXY_INGOT, 4.0F, -1.0F, props);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.united_as_one.violet_shovel.line1"));
    }
}