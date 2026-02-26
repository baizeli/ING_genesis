package miku.united_as_one.genesis.common.items;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class FireBossDagger extends SwordItem {
    public FireBossDagger(int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(new Tier() {
            @Override
            public int getUses() {
                return 0;
            }

            @Override
            public float getSpeed() {
                return 1.6F;
            }

            @Override
            public float getAttackDamageBonus() {
                return Float.POSITIVE_INFINITY;
            }

            @Override
            public int getLevel() {
                return Integer.MAX_VALUE;
            }

            @Override
            public int getEnchantmentValue() {
                return Integer.MAX_VALUE;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return null;
            }
        }, attackDamageModifier, attackSpeedModifier, properties);
    }
}
