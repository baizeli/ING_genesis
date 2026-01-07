package com.baizeli.eternisstarrysky.Items.curios;

import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.world.item.Rarity;

public class GenesisCurseItem extends CurioBaseItem {
    public GenesisCurseItem() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }
}
