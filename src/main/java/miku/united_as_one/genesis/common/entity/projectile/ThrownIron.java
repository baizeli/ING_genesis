package miku.united_as_one.genesis.common.entity.projectile;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ThrownIron extends ThrowableItemProjectile {
    private float damage = 6.0F;
    private int lifeTime = 100;
    private LivingEntity target;
    public final List<Vec3> trailPositions = new ArrayList<>();

    public ThrownIron(EntityType<? extends ThrownIron> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    public ThrownIron(Level level, LivingEntity shooter) {
        super(EntityRegistry.THROWN_IRON.get(), shooter, level);
        this.setNoGravity(true);
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(20.0D);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 2048.0D; // 比如 45 格的平方，足够远
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.level().isClientSide) {
            for (int i = 0; i < 5; i++) {
                trailPositions.add(this.position());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.tickCount % 5 == 0 && (target == null || !target.isAlive())) {
                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(15.0D), // 追踪范围
                        e -> e != this.getOwner() && e.isAlive());
                if (!entities.isEmpty()) this.target = entities.get(0); // 追踪最近的敌人
            }
            if (target != null) {
                Vec3 dir = target.getBoundingBox().getCenter().subtract(this.position()).normalize();
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(dir.scale(0.15D))); // 混合当前速度与追踪方向
            }
            if (this.tickCount >= this.lifeTime) this.discard(); // 达到生命周期后消失
        }

        if (this.level().isClientSide) {
            trailPositions.add(this.position());
            if (trailPositions.size() > 20) trailPositions.remove(0); // 保持轨迹点数量，控制拖尾长度
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        if (result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), this.damage)) {
            result.getEntity().invulnerableTime = 0;
        }
        Vec3 center = result.getEntity().getBoundingBox().getCenter();
        if (!this.level().isClientSide()) {
            MagicManager.spawnParticles(this.level, new BlastwaveParticleOptions(new Vector3f(1, 1, 1),
                            result.getEntity().getBbWidth() * 1.5F + 1.5F),
                    center.x, center.y, center.z, 1, 0.0, 0.F, 0.0, 0.0, true);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ANVIL_LAND, this.getSoundSource(), 1, 1);
            if (result.getEntity() instanceof LivingEntity livingEntity)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
            this.discard();
        }
    }

    public void setDamage(float damage) { this.damage = damage; }
    public void setLifeTime(int lifeTime) { this.lifeTime = lifeTime; }
    public float getDamage() { return this.damage; } //

    @Override
    protected @NotNull Item getDefaultItem() { return Items.IRON_INGOT; }
    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) this.discard();
    }
}