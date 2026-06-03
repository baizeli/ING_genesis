package miku.united_as_one.genesis.contents.items.curios.rune_plus;

import miku.united_as_one.genesis.contents.items.curios.ESSCurioItem;
import net.minecraft.world.item.ItemStack;

public class BloodRunePlus extends ESSCurioItem {
    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof BloodRunePlus;
    }
}
