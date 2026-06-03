package miku.united_as_one.genesis.contents.entity.spell.celestial_source;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import miku.bai_ze_li.genesis.api.entity.PositionTrailBuffer;
import miku.united_as_one.genesis.registries.client.ParticleRegistry;
import miku.united_as_one.genesis.registries.entity.EntityRegistry;
import miku.united_as_one.genesis.registries.item.CreativeTabRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;

public class UltimateWhisperArrowEntity extends AbstractArrow {
    public static final float EXPLOSION_RADIUS = 2.0F;
    public static final float SPLASH_DAMAGE_MULTIPLIER = 0.5F;

    private static final int TRAIL_LENGTH = 36;
    private static final int MAX_LIFE_TICKS = 80;
    private static final double HOMING_RANGE = 16.0D;
    private static final double HOMING_MIN_DOT = 0.35D;
    private static final double HOMING_STRENGTH = 0.12D;
    private static final List<Integer> IMPACT_COLORS = List.of(
            0xFF4040, 0xFFAA40, 0xFFFF40, 0x40FF70, 0x40FFFF, 0x4080FF, 0xAA40FF, 0xFF40D0
    );
    private static final Vector3f BLASTWAVE_COLOR = new Vector3f(0.48F, 0.86F, 1.0F);

    private final PositionTrailBuffer trailPositions = new PositionTrailBuffer(TRAIL_LENGTH + 1);
    private float spellDamage = 15.0F;
    private float maxRange = 30.0F;
    private double traveledDistance;
    private Vec3 previousPosition = Vec3.ZERO;

    public UltimateWhisperArrowEntity(EntityType<? extends UltimateWhisperArrowEntity> entityType, Level level) {
        super(entityType, level);
        configureProjectile();
    }

    public UltimateWhisperArrowEntity(Level level, LivingEntity shooter, float spellDamage, float maxRange) {
        super(EntityRegistry.ULTIMATE_WHISPER_ARROW.get(), shooter, level);
        this.spellDamage = spellDamage;
        this.maxRange = maxRange;
        configureProjectile();
        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
    }

    private void configureProjectile() {
        setNoGravity(true);
        setBaseDamage(0.0D);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    public void tick() {
        this.previousPosition = position();
        if (!level().isClientSide) {
            updateHoming();
        }
        super.tick();

        if (!level().isClientSide) {
            this.traveledDistance += this.previousPosition.distanceTo(position());
            if (this.tickCount > MAX_LIFE_TICKS || this.traveledDistance >= this.maxRange) {
                discard();
            }
        } else {
            recordTrailPosition();
        }
    }

    private void updateHoming() {
        if (this.tickCount < 2 || this.inGround) {
            return;
        }

        Vec3 motion = getDeltaMovement();
        if (motion.lengthSqr() < 0.001D) {
            return;
        }

        LivingEntity target = findHomingTarget(motion.normalize());
        if (target == null) {
            return;
        }

        double speed = motion.length();
        Vec3 targetCenter = target.getBoundingBox().getCenter();
        Vec3 desired = targetCenter.subtract(position());
        if (desired.lengthSqr() < 0.001D) {
            return;
        }

        Vec3 adjusted = motion.lerp(desired.normalize().scale(speed), HOMING_STRENGTH);
        if (adjusted.lengthSqr() > 0.001D) {
            setDeltaMovement(adjusted.normalize().scale(speed));
        }
    }

    private LivingEntity findHomingTarget(Vec3 forward) {
        Entity owner = getOwner();
        AABB searchBox = getBoundingBox().inflate(HOMING_RANGE);
        LivingEntity bestTarget = null;
        double bestScore = Double.MAX_VALUE;

        for (LivingEntity candidate : level().getEntitiesOfClass(LivingEntity.class, searchBox, this::canHomeTo)) {
            if (candidate == owner || owner != null && candidate.isAlliedTo(owner)) {
                continue;
            }

            Vec3 toTarget = candidate.getBoundingBox().getCenter().subtract(position());
            double distanceSqr = toTarget.lengthSqr();
            if (distanceSqr > HOMING_RANGE * HOMING_RANGE || distanceSqr < 0.001D) {
                continue;
            }

            double dot = forward.dot(toTarget.normalize());
            if (dot < HOMING_MIN_DOT) {
                continue;
            }

            double score = distanceSqr * (1.0D - dot * 0.45D);
            if (score < bestScore) {
                bestScore = score;
                bestTarget = candidate;
            }
        }

        return bestTarget;
    }

    private boolean canHomeTo(LivingEntity candidate) {
        return candidate.isAlive() && candidate.canBeHitByProjectile() && !candidate.isSpectator();
    }

    private void recordTrailPosition() {
        Vec3 currentPos = position();
        this.trailPositions.record(currentPos, 0.001D);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        Entity hitEntity = result.getEntity();
        if (hitEntity == getOwner()) {
            return;
        }

        Vec3 impact = result.getLocation();
        if (!level().isClientSide) {
            if (hitEntity.canBeHitByProjectile()) {
                DamageSources.applyDamage(hitEntity, this.spellDamage, spellDamageSource());
            }
            explode(impact, hitEntity);
            spawnImpactCubes(impact);
            discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (!level().isClientSide) {
            explode(result.getLocation(), null);
            discard();
        }
    }

    private void explode(Vec3 impact, Entity directHit) {
        MagicManager.spawnParticles(
                level(),
                new BlastwaveParticleOptions(BLASTWAVE_COLOR, EXPLOSION_RADIUS * 0.95F),
                impact.x,
                impact.y + 0.15D,
                impact.z,
                1,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                true
        );

        float splashDamage = this.spellDamage * SPLASH_DAMAGE_MULTIPLIER;
        double radiusSqr = EXPLOSION_RADIUS * EXPLOSION_RADIUS;
        AABB area = new AABB(
                impact.x - EXPLOSION_RADIUS,
                impact.y - EXPLOSION_RADIUS,
                impact.z - EXPLOSION_RADIUS,
                impact.x + EXPLOSION_RADIUS,
                impact.y + EXPLOSION_RADIUS,
                impact.z + EXPLOSION_RADIUS
        );

        for (Entity entity : level().getEntities(this, area, Entity::canBeHitByProjectile)) {
            if (entity == directHit || entity == getOwner() || entity.distanceToSqr(impact) > radiusSqr) {
                continue;
            }
            DamageSources.applyDamage(entity, splashDamage, spellDamageSource());
        }
    }

    private SpellDamageSource spellDamageSource() {
        Entity owner = getOwner();
        return SpellDamageSource.source(this, owner == null ? this : owner, CreativeTabRegistry.ULTIMATE_WHISPER_SPELL.get())
                .setIFrames(0);
    }

    private void spawnImpactCubes(Vec3 impact) {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        for (int i = 0; i < 18; i++) {
            float[] color = colorFromInt(IMPACT_COLORS.get((i + this.random.nextInt(IMPACT_COLORS.size())) % IMPACT_COLORS.size()));
            double x = impact.x + (this.random.nextDouble() - 0.5D) * 0.45D;
            double y = impact.y + 0.1D + this.random.nextDouble() * 0.35D;
            double z = impact.z + (this.random.nextDouble() - 0.5D) * 0.45D;
            serverLevel.sendParticles(
                    ParticleRegistry.GLOW_CUBE.get(),
                    x,
                    y,
                    z,
                    0,
                    color[0],
                    color[1],
                    color[2],
                    1.0D
            );
        }
    }

    private static float[] colorFromInt(int color) {
        return new float[]{
                Mth.clamp(((color >> 16) & 0xFF) / 255.0F, 0.0F, 1.0F),
                Mth.clamp(((color >> 8) & 0xFF) / 255.0F, 0.0F, 1.0F),
                Mth.clamp((color & 0xFF) / 255.0F, 0.0F, 1.0F)
        };
    }

    public List<Vec3> getTrailPositions() {
        return this.trailPositions.snapshot();
    }

    public List<Vec3> getTrailPositions(float partialTicks) {
        Vec3 renderPosition = getRenderPosition(partialTicks);
        Vec3 motion = getDeltaMovement();
        return this.trailPositions.renderSnapshot(renderPosition, motion);
    }

    private Vec3 getRenderPosition(float partialTicks) {
        return new Vec3(
                Mth.lerp(partialTicks, this.xOld, getX()),
                Mth.lerp(partialTicks, this.yOld, getY()),
                Mth.lerp(partialTicks, this.zOld, getZ())
        );
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("SpellDamage", this.spellDamage);
        compound.putFloat("MaxRange", this.maxRange);
        compound.putDouble("TraveledDistance", this.traveledDistance);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.spellDamage = compound.contains("SpellDamage") ? compound.getFloat("SpellDamage") : 15.0F;
        this.maxRange = compound.contains("MaxRange") ? compound.getFloat("MaxRange") : 30.0F;
        this.traveledDistance = compound.getDouble("TraveledDistance");
        configureProjectile();
    }
}
