package miku.united_as_one.genesis.contents.entity.arrow;

import miku.united_as_one.genesis.registries.client.ParticleRegistry;
import miku.united_as_one.genesis.registries.entity.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecialArrowEntity extends Entity {
    private static final int TRAIL_LENGTH = 20;
    private static final int DEATH_DURATION = 40;
    private static final int GROUND_DURATION = 60;
    private static final int MAX_LIFE_TICKS = 600;

    private static final EntityDataAccessor<Boolean> DYING =
            SynchedEntityData.defineId(SpecialArrowEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> RENDER_DEFINITION =
            SynchedEntityData.defineId(SpecialArrowEntity.class, EntityDataSerializers.INT);

    private final List<Vec3> trailPositions = new ArrayList<>();
    private boolean hasHit = false;
    private boolean shouldRecordTrail = true;
    private boolean dying = false;
    private int lifeTicks = 0;
    private int deathTicks = 0;
    private int groundTicks = 0;
    private LivingEntity shooter;
    private double baseDamage = 5.0D;
    private Vec3 hitPosition = null;

    public SpecialArrowEntity(EntityType<? extends SpecialArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SpecialArrowEntity(Level level, LivingEntity shooter, double baseDamage, ArrowRenderDefinition definition) {
        super(EntityRegistry.SPECIAL_ARROW.get(), level);
        this.shooter = shooter;
        this.baseDamage = baseDamage;
        setRenderDefinition(definition);
        setPos(shooter.getX(), shooter.getY() + shooter.getEyeHeight(), shooter.getZ());
    }

    public SpecialArrowEntity(Level level, LivingEntity shooter, double baseDamage,
                              ArrowRenderDefinition.WeightedEntry... weightedDefinitions) {
        this(level, shooter, baseDamage, ArrowRenderDefinition.weighted(level.random, weightedDefinitions));
    }

    public static SpecialArrowEntity random(Level level, LivingEntity shooter, double baseDamage) {
        return new SpecialArrowEntity(level, shooter, baseDamage, ArrowRenderDefinition.random(level.random));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DYING, false);
        this.entityData.define(RENDER_DEFINITION, ArrowRenderDefinition.THUNDER.id());
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide && this.entityData.get(DYING)) {
            this.dying = true;
        }

        if (this.dying) {
            this.deathTicks++;
            handleDeathTrail();
            spawnGlowCubeParticles(2);
            if (this.deathTicks >= DEATH_DURATION) {
                if (this.level().isClientSide) {
                    this.trailPositions.clear();
                }
                discard();
            }
            return;
        }

        if (this.hasHit) {
            if (this.hitPosition != null) {
                setPos(this.hitPosition.x, this.hitPosition.y, this.hitPosition.z);
            }
            setDeltaMovement(Vec3.ZERO);
        } else {
            updateMovementAndCollision();
        }

        if (this.shouldRecordTrail && this.level().isClientSide) {
            recordTrailPosition();
        }

        if (!this.hasHit && this.level().isClientSide) {
            ArrowRenderDefinition definition = getRenderDefinition();
            if (this.tickCount % definition.flightParticleInterval() == 0) {
                spawnGlowCubeParticles(definition.flightParticleCount());
            }
        }

        if (this.hasHit) {
            this.groundTicks++;
            if (this.groundTicks >= GROUND_DURATION) {
                startDying();
            }
            return;
        }

        this.lifeTicks++;
        if (this.lifeTicks > MAX_LIFE_TICKS) {
            startDying();
        }
    }

    private void updateMovementAndCollision() {
        Vec3 movement = getDeltaMovement();
        if (!onGround()) {
            movement = movement.add(0.0D, -0.06D, 0.0D).scale(0.99D);
            setDeltaMovement(movement);
        }

        HitResult hitResult = findHitResult(movement);
        if (hitResult instanceof EntityHitResult entityHitResult) {
            setPos(entityHitResult.getLocation().x, entityHitResult.getLocation().y, entityHitResult.getLocation().z);
            hitEntity(entityHitResult);
            return;
        }
        if (hitResult instanceof BlockHitResult blockHitResult && blockHitResult.getType() != HitResult.Type.MISS) {
            setPos(blockHitResult.getLocation().x, blockHitResult.getLocation().y, blockHitResult.getLocation().z);
            hitBlock(blockHitResult);
            return;
        }

        setPos(getX() + movement.x, getY() + movement.y, getZ() + movement.z);
    }

    private HitResult findHitResult(Vec3 movement) {
        Vec3 start = position();
        Vec3 end = start.add(movement);
        BlockHitResult blockHit = this.level().clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        ));
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }

        EntityHitResult entityHit = findEntityHit(start, end);
        if (entityHit != null) {
            return entityHit;
        }
        return blockHit;
    }

    private EntityHitResult findEntityHit(Vec3 start, Vec3 end) {
        double closestDistance = Double.MAX_VALUE;
        Entity closestEntity = null;
        Vec3 closestHit = null;
        AABB searchBox = getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D);

        for (Entity entity : this.level().getEntities(this, searchBox, this::canHitEntity)) {
            AABB targetBox = entity.getBoundingBox().inflate(0.3D);
            Optional<Vec3> hit = targetBox.clip(start, end);
            if (targetBox.contains(start)) {
                if (closestDistance >= 0.0D) {
                    closestEntity = entity;
                    closestHit = start;
                    closestDistance = 0.0D;
                }
            } else if (hit.isPresent()) {
                double distance = start.distanceToSqr(hit.get());
                if (distance < closestDistance) {
                    closestEntity = entity;
                    closestHit = hit.get();
                    closestDistance = distance;
                }
            }
        }

        return closestEntity == null ? null : new EntityHitResult(closestEntity, closestHit);
    }

    private boolean canHitEntity(Entity entity) {
        if (!entity.isPickable() || entity.isSpectator() || !entity.isAlive()) {
            return false;
        }
        return entity != this.shooter || this.tickCount >= 5;
    }

    private void startDying() {
        if (this.dying) {
            return;
        }
        this.dying = true;
        this.deathTicks = 0;
        this.shouldRecordTrail = false;
        this.entityData.set(DYING, true);
    }

    private void handleDeathTrail() {
        if (!this.level().isClientSide) {
            return;
        }
        float progress = getDeathProgress();
        int targetLength = (int) ((1.0F - progress) * (TRAIL_LENGTH + 1));
        while (this.trailPositions.size() > targetLength && !this.trailPositions.isEmpty()) {
            this.trailPositions.remove(0);
        }
    }

    private void recordTrailPosition() {
        Vec3 currentPos = position();
        Vec3 movement = getDeltaMovement();

        double movementThreshold = 0.001D;
        if (movement.lengthSqr() < movementThreshold * movementThreshold) {
            this.shouldRecordTrail = false;
            return;
        }

        if (!this.trailPositions.isEmpty()) {
            Vec3 lastPos = this.trailPositions.get(this.trailPositions.size() - 1);
            if (currentPos.distanceToSqr(lastPos) < 0.001D) {
                return;
            }
        }

        this.trailPositions.add(currentPos);
        while (this.trailPositions.size() > TRAIL_LENGTH + 1) {
            this.trailPositions.remove(0);
        }
    }

    private void spawnGlowCubeParticles(int count) {
        if (!this.level().isClientSide || count <= 0) {
            return;
        }
        ArrowRenderDefinition definition = getRenderDefinition();
        for (int i = 0; i < count; i++) {
            float[] color = definition.particleColor(this.tickCount + this.random.nextFloat(), getId());
            this.level().addParticle(
                    ParticleRegistry.GLOW_CUBE.get(),
                    getX() + (this.random.nextDouble() - 0.5D) * 0.35D,
                    getY() + (this.random.nextDouble() - 0.5D) * 0.35D,
                    getZ() + (this.random.nextDouble() - 0.5D) * 0.35D,
                    color[0],
                    color[1],
                    color[2]
            );
        }
    }

    public void shoot(Vec3 direction, float velocity, float inaccuracy) {
        Vec3 normalized = direction.normalize();
        setDeltaMovement(normalized.scale(velocity));
    }

    public void hitEntity(EntityHitResult hitResult) {
        if (this.level().isClientSide) {
            spawnHitParticles(hitResult.getLocation());
        }

        this.hitPosition = hitResult.getLocation();
        Entity entity = hitResult.getEntity();
        if (!this.level().isClientSide && entity instanceof LivingEntity && this.shooter != null) {
            entity.hurt(this.level().damageSources().mobProjectile(this, this.shooter), (float) this.baseDamage);
        }

        this.hasHit = true;
    }

    public void hitBlock(BlockHitResult hitResult) {
        if (this.level().isClientSide) {
            spawnHitParticles(hitResult.getLocation());
        }

        this.hitPosition = hitResult.getLocation();
        this.hasHit = true;
    }

    private void spawnHitParticles(Vec3 location) {
        for (int i = 0; i < 8; i++) {
            double offsetX = (this.random.nextDouble() - 0.5D) * 0.5D;
            double offsetY = (this.random.nextDouble() - 0.5D) * 0.5D;
            double offsetZ = (this.random.nextDouble() - 0.5D) * 0.5D;

            this.level().addParticle(
                    ParticleTypes.CRIT,
                    location.x + offsetX,
                    location.y + offsetY,
                    location.z + offsetZ,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
        spawnGlowCubeParticles(6);
    }

    public void setRenderDefinition(ArrowRenderDefinition definition) {
        this.entityData.set(RENDER_DEFINITION, definition.id());
    }

    public ArrowRenderDefinition getRenderDefinition() {
        return ArrowRenderDefinition.byId(this.entityData.get(RENDER_DEFINITION));
    }

    public boolean isDying() {
        return this.dying;
    }

    public float getDeathProgress() {
        if (!this.dying) {
            return 0.0F;
        }
        return Math.min(1.0F, (float) this.deathTicks / DEATH_DURATION);
    }

    public List<Vec3> getTrailPositions() {
        return new ArrayList<>(this.trailPositions);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(@NotNull net.minecraft.world.damagesource.DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.hasHit = compound.getBoolean("HasHit");
        this.lifeTicks = compound.getInt("LifeTicks");
        this.dying = compound.getBoolean("Dying");
        this.deathTicks = compound.getInt("DeathTicks");
        this.groundTicks = compound.getInt("GroundTicks");
        this.baseDamage = compound.contains("BaseDamage") ? compound.getDouble("BaseDamage") : 5.0D;
        setRenderDefinition(ArrowRenderDefinition.byId(compound.getInt("RenderDefinition")));
        this.entityData.set(DYING, this.dying);
        if (compound.contains("HitPosition")) {
            CompoundTag posTag = compound.getCompound("HitPosition");
            this.hitPosition = new Vec3(posTag.getDouble("X"), posTag.getDouble("Y"), posTag.getDouble("Z"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("HasHit", this.hasHit);
        compound.putInt("LifeTicks", this.lifeTicks);
        compound.putBoolean("Dying", this.dying);
        compound.putInt("DeathTicks", this.deathTicks);
        compound.putInt("GroundTicks", this.groundTicks);
        compound.putDouble("BaseDamage", this.baseDamage);
        compound.putInt("RenderDefinition", getRenderDefinition().id());
        if (this.hitPosition != null) {
            CompoundTag posTag = new CompoundTag();
            posTag.putDouble("X", this.hitPosition.x);
            posTag.putDouble("Y", this.hitPosition.y);
            posTag.putDouble("Z", this.hitPosition.z);
            compound.put("HitPosition", posTag);
        }
    }
}
