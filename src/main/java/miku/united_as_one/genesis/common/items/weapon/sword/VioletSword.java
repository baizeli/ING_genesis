package miku.united_as_one.genesis.common.items.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import miku.united_as_one.genesis.init.registry.TierRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class VioletSword extends SwordItem {
    private static final UUID SPELL_COOLDOWN_UUID = UUID.fromString("648D6614-2BA3-4C94-8D52-873F3B4A6F6B");

    public VioletSword(Properties props) {
        super(TierRegistry.VIOLET_GALAXY_INGOT, 0, 0, props);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.united_as_one.violet_sword.line1"));
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 20.0D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.7D, AttributeModifier.Operation.ADDITION));

            Attribute cooldown = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("irons_spellbooks", "cooldown_reduction"));
            if (cooldown == null) cooldown = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("irons_spellbooks", "spell_cooldown"));

            if (cooldown != null) {
                builder.put(cooldown, new AttributeModifier(SPELL_COOLDOWN_UUID, "Spell Cooldown", 0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        // 返回 BLOCK 状态，这将被 Renderer 事件捕捉并渲染特殊姿态
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.isSameItem(oldStack, newStack);
    }
}