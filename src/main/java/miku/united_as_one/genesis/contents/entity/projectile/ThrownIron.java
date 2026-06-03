package miku.united_as_one.genesis.contents.entity.projectile;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import miku.bai_ze_li.genesis.api.entity.PositionTrailBuffer;
import miku.united_as_one.genesis.registries.entity.EntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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

import java.util.List;

public class ThrownIron extends ThrowableItemProjectile {
    private static final int TRAIL_LENGTH = 36;

    private final PositionTrailBuffer trailPositions = new PositionTrailBuffer(TRAIL_LENGTH + 1);
    private float damage = 6.0F;
    private int lifeTime = 100;

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
        return distance < 2048.0D;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.level().isClientSide) {
            this.trailPositions.record(this.position());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            this.trailPositions.record(this.position(), 0.001D);
        } else if (this.tickCount >= this.lifeTime) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        if (result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), this.damage)) {
            result.getEntity().invulnerableTime = 0;
        }
        Vec3 center = result.getEntity().getBoundingBox().getCenter();
        if (!this.level().isClientSide()) {
            MagicManager.spawnParticles(this.level(), new BlastwaveParticleOptions(new Vector3f(1, 1, 1),
                            result.getEntity().getBbWidth() * 1.5F + 1.5F),
                    center.x, center.y, center.z, 1, 0.0, 0.F, 0.0, 0.0, true);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ANVIL_LAND, this.getSoundSource(), 1, 1);
            if (result.getEntity() instanceof LivingEntity livingEntity)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
            this.discard();
        }
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) this.discard();
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.IRON_INGOT;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public float getDamage() {
        return this.damage;
    }

    public List<Vec3> getTrailPositions(float partialTicks) {
        Vec3 renderPosition = new Vec3(
                Mth.lerp(partialTicks, this.xOld, getX()),
                Mth.lerp(partialTicks, this.yOld, getY()),
                Mth.lerp(partialTicks, this.zOld, getZ())
        );
        return this.trailPositions.renderSnapshot(renderPosition, getDeltaMovement());
    }
}
