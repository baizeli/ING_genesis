package miku.united_as_one.genesis.common.items.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import miku.united_as_one.genesis.init.registry.TierRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class VioletGalaxyingotTool {

    // --- 紫极剑 ---
    public static class Sword extends SwordItem {
        private static final UUID SPELL_COOLDOWN_UUID = UUID.fromString("648D6614-2BA3-4C94-8D52-873F3B4A6F6B");

        public Sword(Properties props) {
            super(TierRegistry.VIOLET_GALAXY_INGOT, 0, 0, props);
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
            if (slot == EquipmentSlot.MAINHAND) {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                // 伤害 21 (1.0基础 + 20.0修正)
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 20.0D, AttributeModifier.Operation.ADDITION));
                // 攻速 1.3 (4.0基础 - 2.7修正)
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.7D, AttributeModifier.Operation.ADDITION));

                // 注入铁魔法冷却缩减
                Attribute cooldown = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("irons_spellbooks", "cooldown_reduction"));
                if (cooldown == null) cooldown = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("irons_spellbooks", "spell_cooldown"));

                if (cooldown != null) {
                    // 使用 -0.15D 代表减少 15% 冷却时间
                    builder.put(cooldown, new AttributeModifier(SPELL_COOLDOWN_UUID, "Spell Cooldown", 0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
                return builder.build();
            }
            return super.getAttributeModifiers(slot, stack);
        }

        @Override
        public UseAnim getUseAnimation(ItemStack stack) {
            return UseAnim.BLOCK;
        }

        @Override
        public int getUseDuration(ItemStack stack) {
            return 72000;
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            ItemStack itemstack = player.getItemInHand(hand);
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }

        @Override
        public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
            // 解决耐久变化导致的格挡动画中断（闪烁）
            return slotChanged || !ItemStack.isSameItem(oldStack, newStack);
        }
    }

    // --- 紫极斧 ---
    public static class Axe extends AxeItem {
        public Axe(Properties props) {
            // 伤害 27 (1+26), 攻速 0.7 (4-3.3)
            super(TierRegistry.VIOLET_GALAXY_INGOT, 26.0F, -3.3F, props);
        }
    }

    // --- 紫极铲 ---
    public static class Shovel extends ShovelItem {
        public Shovel(Properties props) {
            // 伤害 5 (1+4), 攻速 3.0 (4-1.0)
            super(TierRegistry.VIOLET_GALAXY_INGOT, 4.0F, -1.0F, props);
        }
    }

    // --- 紫极锄 ---
    public static class Hoe extends HoeItem {
        public Hoe(Properties props) {
            // 伤害 1 (1+0), 攻速 100 (4+96)
            super(TierRegistry.VIOLET_GALAXY_INGOT, 0, 96.0F, props);
        }
        @Override
        public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
            int level = super.getEnchantmentLevel(stack, enchantment);
            return enchantment == Enchantments.BLOCK_FORTUNE ? level + 6 : level;
        }
    }

    // --- 紫极镐 (如果你也有这个类) ---
    public static class Pickaxe extends PickaxeItem {
        public Pickaxe(Properties props) {
            super(TierRegistry.VIOLET_GALAXY_INGOT, 0, -2.8F, props);
        }
    }
}