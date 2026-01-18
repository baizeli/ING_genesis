package com.baizeli.eternisstarrysky.Items.curios.rune_plus;

import com.baizeli.eternisstarrysky.Items.curios.ESSCurioItem;
import net.minecraft.world.item.ItemStack;

public class EldritchRunePlus extends ESSCurioItem {

    public EldritchRunePlus() {

    }

    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof EldritchRunePlus;
    }
}
