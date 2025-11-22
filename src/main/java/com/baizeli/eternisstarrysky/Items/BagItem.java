package com.baizeli.eternisstarrysky.Items;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Util.RainbowEffectHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public final class BagItem extends Item {

    private final int type;

    public BagItem(Properties p_41383_, int type) {
        super(p_41383_);
        this.type = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        if (type == 0) tooltip.add(RainbowEffectHelper.createCustomGradientText(Component.translatable("item." + EternisStarrySky.MOD_ID + ".bag1").getString(), RainbowEffectHelper.DEFAULT_RAINBOW, 2, 1, 0.05F, 2f));
    }
}