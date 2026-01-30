package miku.united_as_one.genesis.items.bow;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.eldritch_blast.EldritchBlastVisualEntity;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.api.util.*;
import io.redspace.ironsspellbooks.spells.eldritch.EldritchBlastSpell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.world.phys.*;
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

    @SuppressWarnings("removal")
    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player) || level.isClientSide) return;

        float power = getPowerForTime(this.getUseDuration(stack) - timeLeft);

        if (power < 0.5) return;

        float range = 22;
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getForward().scale(range));

        for (Entity target : level.getEntities(player, player.getBoundingBox().expandTowards(end.subtract(start)))) {
            if (Utils.checkEntityIntersecting(target, start, end, .4f).getType() != HitResult.Type.MISS) {
                DamageSources.applyDamage(target, power * 16, level.damageSources().sonicBoom(player));
            }
        }

        Vec3 playerRight = player.getLookAngle();
        Vec3 leftStart = start.add(playerRight.scale(0)).add(new Vec3(0, -0.5, 0));
        /*Vec3 rightStart = start.add(playerRight.scale(0)).add(new Vec3(0, -0.5, 0));*/

        // 左侧巫术湮灭射线
        Vec3 leftEnd = leftStart.add(player.getForward().scale(range));
        level.addFreshEntity(new EldritchBlastVisualEntity(level, leftStart, leftEnd, player));

        /*for (Entity target : level.getEntities(player, player.getBoundingBox().expandTowards(leftEnd.subtract(leftStart)))) {
            if (Utils.checkEntityIntersecting(target, leftStart, leftEnd, .4f).getType() != HitResult.Type.MISS) {
                DamageSources.applyDamage(target, power * 8, new EldritchBlastSpell().getDamageSource(target, player));
            }
        }*/

        // 右侧巫术湮灭射线
        /*Vec3 rightEnd = rightStart.add(player.getForward().scale(range));
        level.addFreshEntity(new EldritchBlastVisualEntity(level, rightStart, rightEnd, player));

        for (Entity target : level.getEntities(player, player.getBoundingBox().expandTowards(rightEnd.subtract(rightStart)))) {
            if (Utils.checkEntityIntersecting(target, rightStart, rightEnd, .4f).getType() != HitResult.Type.MISS) {
                DamageSources.applyDamage(target, power * 8, new EldritchBlastSpell().getDamageSource(target, player));
            }
        }*/

        level.playSound(
            null, player.getX(), player.getY(), player.getZ(), 
            SoundEvents.WARDEN_SONIC_BOOM, 
            player.getSoundSource(), 3.5f, .9f + level.random.nextFloat() * .2f
        );

        for (int i = 0; i < range; i++) {
            var vec3 = player.getLookAngle().normalize().scale(i).add(player.getEyePosition());

            MagicManager.spawnParticles(
                level, 
                ParticleTypes.SONIC_BOOM, 
                vec3.x, vec3.y, vec3.z, 
                1, 0, 0, 0, 0, false
            );
        }

        CameraShakeManager.addCameraShake(new CameraShakeData(30, player.position(), range));

        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(100, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            MagicData magicData = MagicData.getPlayerMagicData(player);
            var event = new SpellOnCastEvent(player, "sonic_boom", 1, 100, SchoolRegistry.ELDRITCH.get(), CastSource.SWORD);
            MinecraftForge.EVENT_BUS.post(event);
            magicData.setMana(Math.max(magicData.getMana() - event.getManaCost(), 0));
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncManaPacket(magicData));
        }
    }
}