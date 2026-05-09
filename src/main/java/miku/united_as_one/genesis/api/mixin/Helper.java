package miku.united_as_one.genesis.api.mixin;

import miku.united_as_one.genesis.contents.items.WeaponRenderConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Helper {
    public static boolean isBlocking(Player player)
    {
        if (player.isUsingItem())
        {
            ItemStack usedItem = player.getUseItem();
            return WeaponRenderConfig.isSpecialWeapon(usedItem);
        }
        return false;
    }
}
