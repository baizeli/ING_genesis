package miku.united_as_one.genesis.items.bow;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceProjectile;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ThunderLongbow extends BowItem {
    public ThunderLongbow(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack) -> false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player) || level.isClientSide) return;

        float power = getPowerForTime(this.getUseDuration(stack) - timeLeft);

        if (power < 1) return;
        
        LightningLanceProjectile lightningLance = new LightningLanceProjectile(level, player);

        lightningLance.setPos(player.position().add(0, player.getEyeHeight(), 0).add(player.getForward()));
        lightningLance.shoot(player.getLookAngle());

        lightningLance.setDamage(16/* + (power * 6)*/);

        level.addFreshEntity(lightningLance);
        
        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(10, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            MagicData magicData = MagicData.getPlayerMagicData(player);
            magicData.setMana(magicData.getMana() - 20);
        }
    }
}