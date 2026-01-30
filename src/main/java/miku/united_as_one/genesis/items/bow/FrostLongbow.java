package miku.united_as_one.genesis.items.bow;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.registry.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.*;
import io.redspace.ironsspellbooks.entity.spells.ray_of_frost.RayOfFrostVisualEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class FrostLongbow extends BowItem {
    public FrostLongbow(Properties properties) {
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.POWER_ARROWS;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player) || level.isClientSide) return;

        float power = getPowerForTime(this.getUseDuration(stack) - timeLeft);

        if (power < 0.5) return;

        var hitResult = Utils.raycastForEntity(level, player, 17, true, .15f);
        level.addFreshEntity(new RayOfFrostVisualEntity(level, player.getEyePosition(), hitResult.getLocation(), player));

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            DamageSources.applyDamage(
                ((EntityHitResult) hitResult).getEntity(), power * 15, SpellDamageSource.source(player, 
                    SpellRegistry.RAY_OF_FROST_SPELL.get()
                ).setFreezeTicks((int)(power * 15 * 20))
            );
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            MagicManager.spawnParticles(
                level, ParticleHelper.ICY_FOG, 
                hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 
                4, 0, 0, 0, .3, true
            );
        }
        MagicManager.spawnParticles(
            level, ParticleHelper.SNOWFLAKE, 
            hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 
            50, 0, 0, 0, .3, false
        );

        level.playSound(
            null, player.getX(), player.getY(), player.getZ(),
            SoundRegistry.RAY_OF_FROST.get(), player.getSoundSource(),
            1, level.random.nextFloat() + 0.3F + 1
        );
        
        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(10, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            MagicData magicData = MagicData.getPlayerMagicData(player);
            var event = new SpellOnCastEvent(player, "ray_of_frost", 1, 50, SchoolRegistry.ICE.get(), CastSource.SWORD);
            MinecraftForge.EVENT_BUS.post(event);
            magicData.setMana(Math.max(magicData.getMana() - event.getManaCost(), 0));
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncManaPacket(magicData));
        }
    }
}