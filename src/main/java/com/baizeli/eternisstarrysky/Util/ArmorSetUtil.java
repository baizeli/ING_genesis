package com.baizeli.eternisstarrysky.Util;

import com.baizeli.eternisstarrysky.Items.armor.CelestialSourceSpellArmor;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;

public class ArmorSetUtil {
    
    public static boolean hasFullCelestialSourceSet(LivingEntity entity) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = entity.getItemBySlot(slot);

                if (!(stack.getItem() instanceof CelestialSourceSpellArmor)) {
                    return false;
                }
            }
        }

        return true;
    }
}