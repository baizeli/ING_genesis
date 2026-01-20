package miku.united_as_one.genesis.items.curios.rune_plus;

import miku.united_as_one.genesis.items.curios.ESSCurioItem;
import net.minecraft.world.item.ItemStack;

public class EldritchRunePlus extends ESSCurioItem {

    public EldritchRunePlus() {

    }

    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof EldritchRunePlus;
    }
}
