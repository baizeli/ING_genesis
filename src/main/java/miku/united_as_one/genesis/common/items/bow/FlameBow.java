package miku.united_as_one.genesis.common.items.bow;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.entity.spells.fire_arrow.FireArrowProjectile;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class FlameBow extends BowItem {
    public FlameBow(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack) -> stack.getItem() instanceof net.minecraft.world.item.ArrowItem;
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

        if (power < 0.5) return;

        FireArrowProjectile fireArrow = new FireArrowProjectile(level, player);

        fireArrow.setPos(player.position().add(0, player.getEyeHeight(), 0).add(player.getForward()));
        fireArrow.shoot(player.getLookAngle());

        fireArrow.setDamage(power * 8);
        fireArrow.setExplosionRadius(5);

        level.addFreshEntity(fireArrow);

        level.playSound(
            null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.FIRECHARGE_USE, player.getSoundSource(),
            1, 1 / (level.random.nextFloat() * 0.4F + 1.2F) + power * 0.5F
        );

        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(10, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            MagicData magicData = MagicData.getPlayerMagicData(player);
            var event = new SpellOnCastEvent(player, "fire_arrow", 1, 50, SchoolRegistry.FIRE.get(), CastSource.SWORD);
            MinecraftForge.EVENT_BUS.post(event);
            magicData.setMana(Math.max(magicData.getMana() - event.getManaCost(), 0));
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncManaPacket(magicData));
        }
    }
}