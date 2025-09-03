package com.baizeli.eternisstarrysky.Items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class EternisAppleItem extends Item
{
    private static final int COOLDOWN_TICKS = 45 * 20;
    public EternisAppleItem(Properties properties) { super(properties); }
    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.EAT; }
    @Override public int getUseDuration(ItemStack stack) { return 32; }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player)
        {
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            FoodData foodData = player.getFoodData();
            foodData.setFoodLevel(20);
            foodData.setSaturation(2000.0f);

            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 9));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 3));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 9));
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(itemstack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }
}