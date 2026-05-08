package miku.united_as_one.genesis.contents.entity;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import miku.united_as_one.genesis.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TremorAoeEntity extends AoeEntity {
    private float tremorHeight = 0.4F;   
    private float expansionSpeed = 1.0F; 
    private float currentRadius = 0.0F;  

    public TremorAoeEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.reapplicationDelay = 25;
        this.setCircular();
    }

    
    public TremorAoeEntity(Level level, float maxRadius, float height, float speed) {
        this(EntityRegistry.TREMOR_AOE_ENTITY.get(), level);
        this.setRadius(maxRadius);
        this.tremorHeight = height;
        this.expansionSpeed = speed;
    }

    
    public TremorAoeEntity withHeight(float height) { this.tremorHeight = height; return this; }
    public TremorAoeEntity withSpeed(float speed) { this.expansionSpeed = speed; return this; }

    @Override
    public void applyEffect(LivingEntity target) {
        
        double verticalImpulse = 0.2 + (tremorHeight * 0.8);
        target.setDeltaMovement(target.getDeltaMovement().add(0.0, verticalImpulse, 0.0));
        target.hurtMarked = true;
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("TremorHeight", this.tremorHeight);
        pCompound.putFloat("ExpansionSpeed", this.expansionSpeed);
        pCompound.putFloat("CurrentRadius", this.currentRadius);
    }

    @Override
    protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.tremorHeight = pCompound.getFloat("TremorHeight");
        this.expansionSpeed = pCompound.getFloat("ExpansionSpeed");
        this.currentRadius = pCompound.getFloat("CurrentRadius");
    }

    @Override
    public void tick() {
        if (this.level.isClientSide) {
            this.ambientParticles();
        }

        float maxRadius = this.getRadius();

        if (currentRadius < maxRadius) {
            
            float lastRadius = currentRadius;
            currentRadius += expansionSpeed;

            if (!this.level.isClientSide) {
                
                if (this.tickCount % 2 == 0) {
                    float volume = Mth.clamp(currentRadius / 8.0F, 0.5F, 1.5F);
                    this.playSound(SoundRegistry.EARTHQUAKE_IMPACT.get(), volume, 1.0F);
                }

                
                
                for (float r = lastRadius; r < currentRadius; r += 0.5F) {
                    spawnTremorRing(r);
                }

                
                this.checkHits();
            }
        } else if (this.tickCount > 200) { 
            this.discard();
        }

        
        if (currentRadius >= maxRadius && this.tickCount % 10 == 0) {
            this.discard();
        }
    }

    private void spawnTremorRing(float r) {
        if (r <= 0) return;

        float circumference = 2.0F * 3.14F * r;
        int blocksCount = Mth.clamp((int) (circumference * 0.8F), 4, 64);
        float angleStep = 360.0F / (float) blocksCount;

        for (int i = 0; i < blocksCount; ++i) {
            double angle = angleStep * i * Mth.DEG_TO_RAD;
            Vec3 offset = new Vec3(r * Mth.cos((float) angle), 0, r * Mth.sin((float) angle));

            BlockPos groundPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(this.level, this.position().add(offset), 4)).below();

            
            Utils.createTremorBlock(this.level, groundPos, tremorHeight);
        }
    }

    @Override
    protected void checkHits() {
        if (!this.level.isClientSide) {
            
            float rInner = currentRadius - expansionSpeed - 0.5F;
            float rOuter = currentRadius + 0.5F;

            this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.0)).forEach(target -> {
                double dist = target.distanceTo(this);
                if (dist >= rInner && dist <= rOuter && this.canHitEntity(target)) {
                    this.applyEffect(target);
                }
            });
        }
    }

    @Override public float getParticleCount() { return 0.0F; }
    @Override public Optional<ParticleOptions> getParticle() { return Optional.empty(); }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 2.0F);
    }
}