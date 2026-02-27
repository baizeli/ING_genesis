package miku.united_as_one.genesis.common.entity.arrow;

import miku.united_as_one.genesis.init.registry.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ThunderArrowEntity extends Entity {
    private boolean hasHit = false;
    private int lifeTicks = 0;
    private final int maxLifeTicks = 600;
    
    // 拖尾相关
    private static final int trailLength = 20;
    private final List<Vec3> trailPositions = new ArrayList<>();
    private boolean shouldRecordTrail = true;
    
    // 死亡动画
    private boolean dying = false;
    private int deathTicks = 0;
    private static final int DEATH_DURATION = 40;
    
    // 停留时间（落地后停留 60 ticks = 3 秒）
    private int groundTicks = 0;
    private static final int GROUND_DURATION = 60;
    
    // 发射者
    private LivingEntity shooter;
    private double baseDamage = 5.0;
    
    // 命中位置
    private Vec3 hitPosition = null;

    private static final EntityDataAccessor<Boolean> DYING = SynchedEntityData.defineId(ThunderArrowEntity.class, EntityDataSerializers.BOOLEAN);

    public ThunderArrowEntity(EntityType<? extends ThunderArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThunderArrowEntity(Level level, LivingEntity shooter, double baseDamage) {
        super(EntityRegistry.THUNDER_ARROW.get(), level);
        this.shooter = shooter;
        this.baseDamage = baseDamage;
        this.setPos(shooter.getX(), shooter.getY() + shooter.getEyeHeight(), shooter.getZ());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DYING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide && this.entityData.get(DYING)) {
            dying = true;
        }

        if (dying) {
            deathTicks++;
            handleDeathTrail();
            if (deathTicks >= DEATH_DURATION) {
                if (this.level().isClientSide) trailPositions.clear();
                this.discard();
            }
            return;
        }

        // 应用物理运动（重力和速度）
        if (!hasHit && !onGround()) {
            Vec3 deltaMovement = this.getDeltaMovement();
            // 应用重力
            this.setDeltaMovement(deltaMovement.add(0.0, -0.06, 0.0));
            // 空气阻力
            this.setDeltaMovement(this.getDeltaMovement().scale(0.99));
        }

        // 更新位置
        if (hasHit) {
            // 命中后固定在命中位置
            if (hitPosition != null) {
                this.setPos(hitPosition.x, hitPosition.y, hitPosition.z);
            }
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            this.setPos(this.getX() + this.getDeltaMovement().x, 
                        this.getY() + this.getDeltaMovement().y, 
                        this.getZ() + this.getDeltaMovement().z);
        }

        if (shouldRecordTrail && this.level().isClientSide) {
            recordTrailPosition();
        }

        // 命中后先停留在地面
        if (hasHit) {
            groundTicks++;
            if (groundTicks >= GROUND_DURATION) {
                startDying();
            }
            return;
        }

        lifeTicks++;
        if (lifeTicks > maxLifeTicks) {
            startDying();
        }
    }

    private void startDying() {
        this.dying = true;
        this.deathTicks = 0;
        this.shouldRecordTrail = false;
        this.entityData.set(DYING, true);
    }

    private void handleDeathTrail() {
        if (!this.level().isClientSide) return;
        float progress = getDeathProgress();
        int targetLength = (int) ((1.0f - progress) * (trailLength + 1));
        while (trailPositions.size() > targetLength && !trailPositions.isEmpty()) {
            trailPositions.remove(0);
        }
    }

    public boolean isDying() {
        return dying;
    }

    public float getDeathProgress() {
        if (!dying) return 0.0f;
        return Math.min(1.0f, (float) deathTicks / DEATH_DURATION);
    }

    public List<Vec3> getTrailPositions() {
        return new ArrayList<>(trailPositions);
    }

    private void recordTrailPosition() {
        Vec3 currentPos = this.position();
        Vec3 deltaMovement = this.getDeltaMovement();

        double movementThreshold = 0.001;
        if (deltaMovement.lengthSqr() < movementThreshold * movementThreshold) {
            this.shouldRecordTrail = false;
            return;
        }

        if (!trailPositions.isEmpty()) {
            Vec3 lastPos = trailPositions.get(trailPositions.size() - 1);
            if (currentPos.distanceToSqr(lastPos) < 0.001) return;
        }

        trailPositions.add(currentPos);
        while (trailPositions.size() > (trailLength + 1)) {
            trailPositions.remove(0);
        }
    }

    public void shoot(Vec3 direction, float velocity, float inaccuracy) {
        Vec3 normalized = direction.normalize();
        this.setDeltaMovement(normalized.scale(velocity));
    }

    public void hitEntity(EntityHitResult hitResult) {
        if (this.level().isClientSide) {
            spawnHitParticles(hitResult.getLocation());
        }
        
        // 记录命中位置
        this.hitPosition = hitResult.getLocation();
        
        Entity entity = hitResult.getEntity();
        if (entity instanceof LivingEntity livingEntity && shooter != null) {
            float damage = (float) this.baseDamage;
            entity.hurt(this.level().damageSources().mobProjectile(this, shooter), damage);
        }
        
        this.hasHit = true;
    }

    public void hitBlock(BlockHitResult hitResult) {
        if (this.level().isClientSide) {
            spawnHitParticles(hitResult.getLocation());
        }
        
        // 记录命中位置
        this.hitPosition = hitResult.getLocation();
        
        this.hasHit = true;
    }

    private void spawnHitParticles(Vec3 location) {
        for (int i = 0; i < 8; i++) {
            double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
            double offsetY = (this.random.nextDouble() - 0.5) * 0.5;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;
            
            this.level().addParticle(
                ParticleTypes.CRIT,
                location.x + offsetX,
                location.y + offsetY,
                location.z + offsetZ,
                0.0,
                0.0,
                0.0
            );
        }
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
        if (this.hitPosition != null) {
            CompoundTag posTag = new CompoundTag();
            posTag.putDouble("X", this.hitPosition.x);
            posTag.putDouble("Y", this.hitPosition.y);
            posTag.putDouble("Z", this.hitPosition.z);
            compound.put("HitPosition", posTag);
        }
    }
}
