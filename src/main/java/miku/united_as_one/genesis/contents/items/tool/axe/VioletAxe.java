package miku.united_as_one.genesis.contents.items.tool.axe;

import miku.united_as_one.genesis.registries.item.TierRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VioletAxe extends AxeItem {
    public VioletAxe(Properties props) {
        super(TierRegistry.VIOLET_GALAXY_INGOT, 26.0F, -3.3F, props);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.united_as_one.violet_axe.line1"));
    }
}