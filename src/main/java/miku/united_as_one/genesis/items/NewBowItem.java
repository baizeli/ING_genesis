package miku.united_as_one.genesis.items;

import miku.united_as_one.genesis.entity.CustomArrowEntity;
import miku.united_as_one.genesis.config.Configuration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class NewBowItem extends BowItem {
    private static final String BOW_MODE_KEY = "BowTrackingMode";
    @Override 
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
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
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return false;
    }

    // 切换射箭模式
    private void toggleShootMode(ItemStack stack, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        boolean currentMode = tag.getBoolean(BOW_MODE_KEY);
        boolean newMode = !currentMode;
        tag.putBoolean(BOW_MODE_KEY, newMode);

        // 向玩家发送模式切换消息
        String message = newMode ? "已切换：追踪箭矢" : "已切换：箭雨箭矢";
        player.displayClientMessage(Component.literal(message), true);
    }

    // 检查射箭模式
    private static boolean getShootMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(BOW_MODE_KEY);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        // 检查是否按下Shift键
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                toggleShootMode(stack, player);
            }
            return InteractionResultHolder.success(stack);
        }

        // 否则使用自定义行为开始拉弓
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity shooter, int timeCharged) {
        // 使用自定义逻辑
        if (shooter instanceof Player player) {
            boolean hasInfiniteArrows = player.getAbilities().instabuild ||
                    stack.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0;

            ItemStack arrowStack = player.getProjectile(stack);
            if (arrowStack.isEmpty() && !hasInfiniteArrows) {
                return;
            }

            if (arrowStack.isEmpty()) {
                arrowStack = new ItemStack(Items.ARROW);
            }

            if (!level.isClientSide) {
                CustomArrowEntity arrow = new CustomArrowEntity(level, player);
                arrow.setBaseDamage(Configuration.WHISPER_OF_THE_PAST_DAMAGE.get());
                if (getShootMode(stack)) {
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

                    int powerLevel = stack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
                    if (powerLevel > 0) arrow.setBaseDamage(arrow.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);

                    int punchLevel = stack.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
                    if (punchLevel > 0) arrow.setKnockback(punchLevel);
                    if (stack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) arrow.setSecondsOnFire(100);

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
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.literal("§6无法破坏"));

        // 显示当前射箭模式
        String text = getShootMode(stack) ? "§a模式：追踪箭矢" : "§c模式：箭雨箭矢";
        tooltip.add(Component.literal(text));
        tooltip.add(Component.literal("§7Shift+右键切换模式"));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}