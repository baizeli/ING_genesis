package miku.united_as_one.genesis.contents.items;

import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import miku.united_as_one.genesis.registries.item.ItemRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;

public class ModArmorMaterials {
    private static final int DATA_CONTROLLED_INT = 0;
    private static final float DATA_CONTROLLED_FLOAT = 0.0F;

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
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public float getKnockbackResistance() {
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
        }

        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of();
        }
    };

    public static final IronsExtendedArmorMaterial CELESTIAL_SOURCE_SPELL = new IronsExtendedArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
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
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public float getKnockbackResistance() {
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of();
        }
    };

    public static final IronsExtendedArmorMaterial CHAOS_SPELL = new IronsExtendedArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
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
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public float getKnockbackResistance() {
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of();
        }
    };

    public static final ArmorMaterial ARCANE_CRYSTAL = new IronsExtendedArmorMaterial() {
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ItemRegistry.ARCANE_CRYSTAL.get());
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_DIAMOND;
        }

        @Override
        public String getName() {
            return "arcane_crystal";
        }

        @Override
        public int getEnchantmentValue() {
            return 30;
        }

        @Override
        public float getToughness() {
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public float getKnockbackResistance() {
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
        }

        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of();
        }
    };

    public static final IronsExtendedArmorMaterial VIOLET_ZENITH = new IronsExtendedArmorMaterial() {
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ItemRegistry.VIOLET_GALAXY_INGOT.get());
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
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
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public int getEnchantmentValue() {
            return 35;
        }

        @Override
        public float getToughness() {
            return DATA_CONTROLLED_FLOAT;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return DATA_CONTROLLED_INT;
        }

        @Override
        public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
            return Map.of();
        }
    };
}
