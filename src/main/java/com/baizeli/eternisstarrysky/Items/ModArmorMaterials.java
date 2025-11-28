package com.baizeli.eternisstarrysky.Items;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;

public class ModArmorMaterials {
    public static final ArmorMaterial INFINITY_ETERNAL = new ArmorMaterial() {

        @Override public Ingredient getRepairIngredient() {return Ingredient.of(ModItems.ETERNIS_APPLE.get());}
        @Override public int getDurabilityForType(ArmorItem.Type type) {return Integer.MAX_VALUE;}
        @Override public SoundEvent getEquipSound() {return SoundEvents.ARMOR_EQUIP_NETHERITE;}
        @Override public String getName() {return "infinity_eternal";}
        @Override public float getKnockbackResistance() {return 0.2F;}
        @Override public int getEnchantmentValue() {return 30;}
        @Override public float getToughness() {return 8.0F;}

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 7;
                case CHESTPLATE -> 12;
                case LEGGINGS -> 9;
                case BOOTS -> 6;
            };
        }
    };
    
    public static final ArmorMaterial DIVINE_METAL = new IronsExtendedArmorMaterial() {

        @Override public Ingredient getRepairIngredient() {
            return Ingredient.of(Items.NETHERITE_INGOT);
        }

        @Override public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override public String getName() {
            return "divine_metal";
        }

        @Override public int getEnchantmentValue() {
            return 40;
        }

        @Override public float getToughness() {
            return 5.0F;
        }

        @Override public float getKnockbackResistance() {
            return 0.2F;
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 1418;
                case CHESTPLATE -> 1608;
                case LEGGINGS -> 1570;
                case BOOTS -> 1494;
            };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 5;
                case CHESTPLATE -> 10;
                case LEGGINGS -> 8;
                case BOOTS -> 5;
            };
        }

        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of(
                AttributeRegistry.MAX_MANA.get(), new AttributeModifier(
                    "Divine Mana", 150, AttributeModifier.Operation.ADDITION
                ),
                AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                    "Divine Spell Power", 0.10, AttributeModifier.Operation.MULTIPLY_BASE
                ),
                AttributeRegistry.HOLY_SPELL_POWER.get(), new AttributeModifier(
                    "Divine Holy Power", 0.10, AttributeModifier.Operation.MULTIPLY_BASE
                ),
                AttributeRegistry.CAST_TIME_REDUCTION.get(), new AttributeModifier(
                    "Divine Cast Time Reduction", 0.10, AttributeModifier.Operation.MULTIPLY_BASE
                )
            );
        }
    };
    
    public static final IronsExtendedArmorMaterial CELESTIAL_SOURCE_SPELL = new IronsExtendedArmorMaterial() {
        
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return 0;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 7;
                case CHESTPLATE -> 12;
                case LEGGINGS -> 9;
                case BOOTS -> 6;
            };
        }

        @Override
        public int getEnchantmentValue() {
            return 40;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of();
        }

        @Override
        public String getName() {
            return "celestial_source_spell";
        }

        @Override
        public float getToughness() {
            return 7.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }

        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of();
        }
    };
    
    public static final IronsExtendedArmorMaterial CHAOS_SPELL = new IronsExtendedArmorMaterial() {
        
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return 3200;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 6;
                case CHESTPLATE -> 8;
                case LEGGINGS -> 6;
                case BOOTS -> 3;
            };
        }

        @Override
        public int getEnchantmentValue() {
            return 40;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of();
        }

        @Override
        public String getName() {
            return "chaos_spell";
        }

        @Override
        public float getToughness() {
            return 4.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }

        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of();
        }
    };
}