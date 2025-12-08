package com.baizeli.eternisstarrysky.Mixin.ironsspellbooks.gui.arcane_anvil;

import com.baizeli.eternisstarrysky.Items.InfiniteShrivingStoneItem;
import io.redspace.ironsspellbooks.gui.arcane_anvil.ArcaneAnvilMenu;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ArcaneAnvilMenu.class)
public class ArcaneAnvilMenuMixin {
    
    @Redirect(
        method = "createResult", 
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean redirectShrivingStoneCheck(ItemStack modifierItemStack, Item shrivingStone) {
        return modifierItemStack.is(shrivingStone) || modifierItemStack.getItem() instanceof InfiniteShrivingStoneItem;
    }
    
    @Redirect(
        method = "onTake", 
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
        )
    )
    private void redirectShrink(ItemStack itemStack, int count) {
        if (itemStack.getItem() instanceof InfiniteShrivingStoneItem) {
            return;
        }

        itemStack.shrink(count);
    }
}