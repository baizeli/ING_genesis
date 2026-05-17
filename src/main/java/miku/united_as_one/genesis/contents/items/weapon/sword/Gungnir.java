package miku.united_as_one.genesis.contents.items.weapon.sword;

import miku.united_as_one.genesis.api.mixin.DamageSourceInterface;
import miku.united_as_one.genesis.api.mixin.LivingEventEC;
import miku.united_as_one.genesis.contents.entity.gungnir.GungnirDaggerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class Gungnir extends SwordItem implements Vanishable {
    public Gungnir(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player) {
            if (this.getUseDuration(stack) - timeCharged >= 10) {
                if (!level.isClientSide) {
                    Vec3 pos = player.position();

                    Vec3 offset = new Vec3((double) 1.5F * (double) player.getScale(), 0.0, 0.0).zRot(Mth.lerp(Float.NaN, 0.0F, -(float) Math.PI)).yRot(0).add(0.0, player.getEyeHeight(), 0.0);
                    GungnirDaggerEntity dagger = new GungnirDaggerEntity(level);
                    dagger.setOwner(player);
                    dagger.ownerTrack = offset;
                    dagger.setTarget(getTargetInSight(player, 100));
                    dagger.setPos(pos.add(offset.yRot(player.getYRot())));
                    dagger.delay = 1;
                    player.level.addFreshEntity(dagger);

                    player.getCooldowns().addCooldown(this, 240);
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public static Entity getTargetInSight(Player player, double reach) {
        Level level = player.level();

        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);

        AABB searchBox = player.getBoundingBox()
                .expandTowards(lookVec.scale(reach))
                .inflate(1.0D);

        Predicate<Entity> filter = e -> e.isPickable() && e != player && e.isAlive();

        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
                level, player, eyePos, endPos, searchBox, filter, 0.3F
        );

        return hitResult != null ? hitResult.getEntity() : null;
    }

    public void onAttack(ItemStack itemStack, LivingAttackEvent event) {
        LivingEventEC ec = (LivingEventEC) event;
        ((DamageSourceInterface) event.getSource()).revelationfix$setBypassAll(true);
        ec.revelationfix$hackedUnCancelable(true);
        ec.revelationfix$hackedOnlyAmountUp(true);
    }

    public void onHurt(ItemStack itemStack, LivingHurtEvent event) {
        LivingEventEC ec = (LivingEventEC) event;
        ((DamageSourceInterface) event.getSource()).revelationfix$setBypassAll(true);
        ec.revelationfix$hackedUnCancelable(true);
        ec.revelationfix$hackedOnlyAmountUp(true);
    }

    public void onDamage(ItemStack itemStack, LivingDamageEvent event) {
        LivingEventEC ec = (LivingEventEC) event;
        ec.revelationfix$hackedUnCancelable(true);
        ec.revelationfix$hackedOnlyAmountUp(true);
    }

    public void onDeath(ItemStack itemStack, LivingDeathEvent event, EventPriority priority) {
        LivingEventEC ec = (LivingEventEC) event;
        ec.revelationfix$hackedUnCancelable(true);
        event.getEntity().setHealth(0F);
    }
}
