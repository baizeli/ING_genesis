package com.baizeli.eternisstarrysky.items;

import com.baizeli.eternisstarrysky.config.Configuration;
import com.baizeli.eternisstarrysky.entity.CustomArrowEntity;
import com.baizeli.eternisstarrysky.event.ClientEventsBowKey;
import com.baizeli.eternisstarrysky.network.BowTypePacket;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.awt.Color;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import java.util.List;

public class NewBowItem extends BowItem {

    @Override 
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack) -> stack.getItem() instanceof ArrowItem;
    }
    
    public NewBowItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false; 
    }
    
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    // 添加自动射箭模式的NBT键
    private static final String AUTO_SHOOT_TAG = "AutoShootMode";

    // 切换自动射箭模式的方法
    private void toggleAutoShootMode(ItemStack stack, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        boolean currentMode = tag.getBoolean(AUTO_SHOOT_TAG);
        boolean newMode = !currentMode;
        tag.putBoolean(AUTO_SHOOT_TAG, newMode);
        
        // 向玩家发送模式切换消息
        if (newMode) {
            player.displayClientMessage(Component.literal("§a自动射箭模式已开启"), true);
        } else {
            player.displayClientMessage(Component.literal("§c自动射箭模式已关闭"), true);
        }
    }

    // 检查是否处于自动射箭模式
    public static boolean isAutoShootMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(AUTO_SHOOT_TAG);
    }
    
    public static boolean isAutoShootModeStatic(ItemStack stack) {
        if (stack.getItem() instanceof NewBowItem) {
            return ((NewBowItem) stack.getItem()).isAutoShootMode(stack);
        }
        return false;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        // 检查是否按下Shift键
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                toggleAutoShootMode(stack, player);
            }
            return InteractionResultHolder.success(stack);
        }
        
        // 如果是自动射箭模式，直接射箭
        if (isAutoShootMode(stack)) {
            if (!level.isClientSide) {
                // 立即射箭
                shootArrow(stack, level, player);
                
                // 设置一个标记，表示正在自动射箭
                stack.getOrCreateTag().putBoolean("AutoShooting", true);
            }
            return InteractionResultHolder.consume(stack);
        }
        
        // 否则使用默认行为开始拉弓
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }


    // 提取射箭逻辑到单独的方法
    public static void shootArrow(ItemStack stack, Level level, Player player) {
        boolean hasInfiniteArrows = player.getAbilities().instabuild ||
                EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;

        ItemStack arrowStack = player.getProjectile(stack);
        if (arrowStack.isEmpty() && !hasInfiniteArrows) {
            return;
        }

        if (arrowStack.isEmpty()) {
            arrowStack = new ItemStack(Items.ARROW);
        }

        ArrowItem arrowItem = (ArrowItem) (arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);

        CustomArrowEntity arrow = new CustomArrowEntity(level, player);
        arrow.setBaseDamage(Configuration.WHISPER_OF_THE_PAST_DAMAGE.get());
        if (ClientEventsBowKey.BowType || BowTypePacket.BowType) {
            arrow.setTrack(true);
        } else {
            arrow.setSpawn(true);
        }

        // 使用最大力量射箭
        float power = 1.0F; // 最大力量

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 3.0F, 1.0F);

        arrow.setCritArrow(true); // 总是暴击，因为是满弓

        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
        if (powerLevel > 0) arrow.setBaseDamage(arrow.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);

        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
        if (punchLevel > 0) arrow.setKnockback(punchLevel);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) arrow.setSecondsOnFire(100);

        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));

        if (hasInfiniteArrows && (arrowStack.getItem() == Items.ARROW || arrowStack.getItem() == Items.ARROW)) {
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        level.addFreshEntity(arrow);

        if (!hasInfiniteArrows && !player.getAbilities().instabuild) {
            arrowStack.shrink(1);
            if (arrowStack.isEmpty()) {
                player.getInventory().removeItem(arrowStack);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeCharged) {
        // 如果是自动模式，不处理释放逻辑
        if (shooter instanceof Player && isAutoShootMode(stack)) {
            return;
        }
        
        // 非自动模式使用原版逻辑
        if (shooter instanceof Player player) {
            boolean hasInfiniteArrows = player.getAbilities().instabuild ||
                    EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;

            ItemStack arrowStack = player.getProjectile(stack);
            if (arrowStack.isEmpty() && !hasInfiniteArrows) {
                return;
            }

            if (arrowStack.isEmpty()) {
                arrowStack = new ItemStack(Items.ARROW);
            }

            if (!level.isClientSide) {
                ArrowItem arrowItem = (ArrowItem) (arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);

                CustomArrowEntity arrow = new CustomArrowEntity(level, player);
                arrow.setBaseDamage(Configuration.WHISPER_OF_THE_PAST_DAMAGE.get());
                if (ClientEventsBowKey.BowType || BowTypePacket.BowType) {
                    arrow.setTrack(true);
                } else {
                    arrow.setSpawn(true);
                }

                int drawTime = this.getUseDuration(stack) - timeCharged;
                float power = getPowerForTime(drawTime);

                if (power >= 0.1D) {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
                    arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 3.0F, 1.0F);

                    if (power == 1.0F) arrow.setCritArrow(true);

                    int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                    if (powerLevel > 0) arrow.setBaseDamage(arrow.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);

                    int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                    if (punchLevel > 0) arrow.setKnockback(punchLevel);
                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) arrow.setSecondsOnFire(100);

                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));

                    if (hasInfiniteArrows && (arrowStack.getItem() == Items.ARROW || arrowStack.getItem() == Items.ARROW)) {
                        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    level.addFreshEntity(arrow);
                }

                if (!hasInfiniteArrows && !player.getAbilities().instabuild) {
                    arrowStack.shrink(1);
                    if (arrowStack.isEmpty()) {
                        player.getInventory().removeItem(arrowStack);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§6无法破坏"));
        
        // 显示当前射箭模式
        if (isAutoShootMode(stack)) {
            tooltip.add(Component.literal("§a自动射箭模式: 开启"));
            tooltip.add(Component.literal("§7按住右键自动连续射箭"));
        } else {
            tooltip.add(Component.literal("§c自动射箭模式: 关闭"));
            tooltip.add(Component.literal("§7Shift+右键切换模式"));
        }
        
        super.appendHoverText(stack, level, tooltip, flag);
    }
    
    @Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
    public static class BowEvent {
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        Player player = e.player;
        Level level = player.level();
        ItemStack stack = player.getMainHandItem();
        if(stack.getItem() instanceof NewBowItem && NewBowItem.isAutoShootModeStatic(stack)){
            if (player.isUsingItem() && player.getUseItem() == stack) {
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.getBoolean("AutoShooting")) {
                    int useTime = player.getTicksUsingItem();
                    if (useTime >= 5) { // 5 ticks后射箭，实现快速连续射击
                        NewBowItem.shootArrow(stack, level, player);
                        
                        // 重置使用时间，重新开始计数
                        player.stopUsingItem();
                        player.startUsingItem(player.getUsedItemHand());
                    }
                }
        }
    }
  }
}
}