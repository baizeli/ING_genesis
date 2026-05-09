package miku.united_as_one.genesis.contents.items;

import miku.united_as_one.genesis.registries.item.ItemRegistry;
import miku.united_as_one.genesis.registries.spell.SpellAttributesRegistry;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ModArmorMaterials {
    
    // 神圣金属套
    public static final ArmorMaterial DIVINE_METAL = new IronsExtendedArmorMaterial() {

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ItemRegistry.DIVINE_METAL_INGOT.get());
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override
        public String getName() {
            return "divine_metal";
        }

        @Override
        public int getEnchantmentValue() {
            return 40;
        }

        @Override
        public float getToughness() {
            return 5.0F;
        }

        @Override
        public float getKnockbackResistance() {
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
                // 最大法力值
                AttributeRegistry.MAX_MANA.get(), new AttributeModifier(
                    "Divine Mana", 150, AttributeModifier.Operation.ADDITION
                ),

                // 法术强度
                AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                    "Divine Spell Power", 0.06, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 神圣法术强度
                AttributeRegistry.HOLY_SPELL_POWER.get(), new AttributeModifier(
                    "Divine Holy Power", 0.06, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 施法时间减少
                AttributeRegistry.CAST_TIME_REDUCTION.get(), new AttributeModifier(
                    "Divine Cast Time Reduction", 0.07, AttributeModifier.Operation.MULTIPLY_BASE
                )
            );
        }
    };
    
    // 星源法术套
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
            return Map.of(
                // 法术强度
                AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                    "Celestial Source Spell Power", 0.10, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 星源法术强度
                SpellAttributesRegistry.CELESTIAL_SOURCE_SPELL_POWER.get(), new AttributeModifier(
                    "Celestial Source School Power", 0.10, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 傻逼移速
                /*Attributes.MOVEMENT_SPEED, new AttributeModifier(
                    "Celestial Source Movement Speed", 1.0, AttributeModifier.Operation.MULTIPLY_BASE
                ),*/

                // 最大法力值
                AttributeRegistry.MAX_MANA.get(), new AttributeModifier(
                    "Celestial Source Max Mana", 1000, AttributeModifier.Operation.ADDITION
                ),

                // 施法时间减少
                AttributeRegistry.CAST_TIME_REDUCTION.get(), new AttributeModifier(
                    "Celestial Source Cast Time Reduction", 0.10, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 法术冷却减少
                AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(
                    "Celestial Source Cooldown Reduction", 0.09, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 法力回复速度
                AttributeRegistry.MANA_REGEN.get(), new AttributeModifier(
                    "Celestial Source Mana Regen", 0.09, AttributeModifier.Operation.MULTIPLY_BASE
                )
            );
        }
    };
    
    // 混沌法术套
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
            return Map.of(
                // 法术强度
                AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                    "Chaos Spell Power", 0.07, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 混沌法术强度
                SpellAttributesRegistry.CHAOS_SPELL_POWER.get(), new AttributeModifier(
                    "Chaos School Power", 0.07, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 最大法力值
                AttributeRegistry.MAX_MANA.get(), new AttributeModifier(
                    "Chaos Max Mana", 500, AttributeModifier.Operation.ADDITION
                ),

                // 生命上限
                Attributes.MAX_HEALTH, new AttributeModifier(
                    "Chaos Max Health", 0.50, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 神圣法术强度
                AttributeRegistry.HOLY_SPELL_POWER.get(), new AttributeModifier(
                    "Chaos Reduction Holy Power", -0.03, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 施法时间减少
                AttributeRegistry.CAST_TIME_REDUCTION.get(), new AttributeModifier(
                    "Chaos Cast Time Reduction", 0.10, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 法术冷却
                AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(
                    "Chaos Cooldown Reduction", 0.06, AttributeModifier.Operation.MULTIPLY_BASE
                ),
                
                // 法力回复速度
                AttributeRegistry.MANA_REGEN.get(), new AttributeModifier(
                    "Chaos Mana Regen", 0.06, AttributeModifier.Operation.MULTIPLY_BASE
                )
            );
        }
    };
    
    // 奥术水晶套
    public static final ArmorMaterial ARCANE_CRYSTAL = new IronsExtendedArmorMaterial() {

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(ItemRegistry.ARCANE_CRYSTAL.get());
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_DIAMOND;
        }

        @Override
        public @NotNull String getName() {
            return "arcane_crystal";
        }

        @Override
        public int getEnchantmentValue() {
            return 30;
        }

        @Override
        public float getToughness() {
            return 3.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }

        @Override
        public int getDurabilityForType(ArmorItem.@NotNull Type type) {
            return 800;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 4;
                case CHESTPLATE -> 9;
                case LEGGINGS -> 7;
                case BOOTS -> 4;
            };
        }

        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of();
        }
    };
    
    // 紫极战斗套
    public static final IronsExtendedArmorMaterial VIOLET_ZENITH = new IronsExtendedArmorMaterial() {
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ItemRegistry.VIOLET_GALAXY_INGOT.get());
        }

        @Override 
        public int getDurabilityForType(ArmorItem.Type type) {
            return Integer.MAX_VALUE;
        }

        @Override 
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override 
        public String getName() {
            return "violet_zenith";
        }

        @Override 
        public float getKnockbackResistance() {
            return 1.0F;
        }

        @Override 
        public int getEnchantmentValue() {
            return 35;
        }
        
        @Override 
        public float getToughness() {
            return 8.0F;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 8;
                case CHESTPLATE -> 13;
                case LEGGINGS -> 10;
                case BOOTS -> 7;
            };
        }
        
        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of(
                // 最大生命值
                Attributes.MAX_HEALTH, new AttributeModifier(
                    "Violet Zenith Max Health", 0.10, AttributeModifier.Operation.MULTIPLY_BASE
                ),

                // 最大法力值
                AttributeRegistry.MAX_MANA.get(), new AttributeModifier(
                    "Violet Zenith Max Mana", 130, AttributeModifier.Operation.ADDITION
                ),
                
                // 法术强度
                AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                    "Violet Zenith Spell Power", 0.07, AttributeModifier.Operation.MULTIPLY_BASE
                    ),


                AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(
                    "Violet Zenith Reduction", 0.06, AttributeModifier.Operation.MULTIPLY_BASE
               )
            );
        }
    };
}