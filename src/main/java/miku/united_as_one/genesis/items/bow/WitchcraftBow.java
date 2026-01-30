package miku.united_as_one.genesis.items.bow;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.entity.spells.eldritch_blast.EldritchBlastVisualEntity;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class WitchcraftBow extends BowItem {
    public WitchcraftBow(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack) -> stack.getItem() instanceof ArrowItem;
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

        level.addFreshEntity(
            new EldritchBlastVisualEntity(
                level, 
                player.getEyePosition().subtract(0, .75f, 0), 
                player.getEyePosition().add(player.getLookAngle().scale(30)), 
                player
            )
        );

        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(100, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            MagicData magicData = MagicData.getPlayerMagicData(player);
            var event = new SpellOnCastEvent(player, "eldritch_blast", 1, 100, SchoolRegistry.ELDRITCH.get(), CastSource.SWORD);
            MinecraftForge.EVENT_BUS.post(event);
            magicData.setMana(Math.max(magicData.getMana() - event.getManaCost(), 0));
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncManaPacket(magicData));
        }
    }
}