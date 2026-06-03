package miku.united_as_one.genesis.contents.items.spell.staff;

import io.redspace.ironsspellbooks.item.weapons.*;
import net.minecraft.world.item.*;

public class ChaosStaff extends StaffItem {
    public ChaosStaff() {
        super(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
            new StaffTier(0, 0)
        );
    }
}
