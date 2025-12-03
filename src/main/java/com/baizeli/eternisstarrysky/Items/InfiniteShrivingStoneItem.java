package com.baizeli.eternisstarrysky.Items;

import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.*;

public class InfiniteShrivingStoneItem extends Item {
    public InfiniteShrivingStoneItem() {
        super(ItemPropertiesHelper.material());
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level context, List<Component> lines, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, lines, pIsAdvanced);
        lines.add(Component.translatable("item.irons_spellbooks.shriving_stone_desc").withStyle(ChatFormatting.GRAY));
    }
}