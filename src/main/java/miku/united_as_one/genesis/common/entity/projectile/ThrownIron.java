package miku.united_as_one.genesis.common.entity.projectile;

import miku.united_as_one.genesis.init.registry.EntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

public class ThrownIron extends ThrowableItemProjectile {
    private float damage = 6;
    private int lifeTime = 60;
    
    public ThrownIron(EntityType<? extends ThrownIron> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public ThrownIron(Level level, LivingEntity shooter) {
        super(EntityRegistry.THROWN_IRON.get(), shooter, level);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.IRON_INGOT;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), this.damage);
        if (!this.level().isClientSide()) this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
            SoundEvents.ANVIL_LAND, this.getSoundSource(), 1, 1
        );

        if (result.getEntity() instanceof LivingEntity livingEntity) 
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) this.discard();
    }

    @Override
    public void tick() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) this.onHit(hitresult);

        this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        this.setNoGravity(true);

        if (!this.level().isClientSide && this.tickCount >= this.lifeTime) this.discard();
    }
}