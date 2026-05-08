package miku.united_as_one.genesis.api;

import miku.united_as_one.genesis.contents.items.armor.CelestialSourceSpellArmor;
import miku.united_as_one.genesis.contents.items.armor.VioletZenithArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
    
    public static boolean hasFullVioletZenithSet(LivingEntity entity) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = entity.getItemBySlot(slot);

                if (!(stack.getItem() instanceof VioletZenithArmor)) {
                    return false;
                }
            }
        }

        return true;
    }
    
    public static boolean isWearingVioletZenithHelmet(LivingEntity entity) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
        return stack.getItem() instanceof VioletZenithArmor;
    }
    
    public static boolean isWearingVioletZenithChestplate(LivingEntity entity) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        return stack.getItem() instanceof VioletZenithArmor;
    }
    
    public static boolean isWearingVioletZenithLeggings(LivingEntity entity) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.LEGS);
        return stack.getItem() instanceof VioletZenithArmor;
    }
    
    public static boolean isWearingVioletZenithBoots(LivingEntity entity) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.FEET);
        return stack.getItem() instanceof VioletZenithArmor;
    }
}