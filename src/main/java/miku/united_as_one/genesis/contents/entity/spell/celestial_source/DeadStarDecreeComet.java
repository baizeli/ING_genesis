package miku.united_as_one.genesis.contents.entity.spell.celestial_source;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import miku.united_as_one.genesis.registries.CreativeTabRegistry;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Supplier;

public class DeadStarDecreeComet extends AbstractMagicProjectile {
    
    public DeadStarDecreeComet(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }
    
    public void shoot(Vec3 rotation, float innaccuracy) {
        Vec3 offset = Utils.getRandomVec3(1).normalize().scale(innaccuracy);
        super.shoot(rotation.add(offset));
    }

    @Override
    public void trailParticles() {
        /*var vec = getDeltaMovement();
        var length = vec.length();
        int count = (int) Math.min(20, Math.round(length) * 4) + 1;
        float f = (float) length / count;
        
        for (int i = 0; i < count; i++) {
            Vec3 random = Utils.getRandomVec3(0.04);
            Vec3 p = vec.scale(f * i);

            level().addParticle(
                ParticleHelper.UNSTABLE_ENDER, 
                this.getX() + random.x + p.x, 
                this.getY() + random.y + p.y, 
                this.getZ() + random.z + p.z, 
                random.x, random.y, random.z
            );
        }*/
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        float scale;

        if (this.getBbWidth() > 1.0f) {
            // 大陨石
            scale = 10f;
        } else {
            // 陨石
            scale = 1.25f;
        }
        
        MagicManager.spawnParticles(
            level(), new BlastwaveParticleOptions(
                new Vector3f(1.0f, 1.0f, 0.0f), scale
            ), x, y, z, 1, 0, 0, 0, 0, true
        );
    }

    @Override
    public float getSpeed() {
        return 1.85f;
    }

    @Override
    protected void doImpactSound(Supplier<SoundEvent> sound) {
        float volume = this.getBbWidth() > 1.0f ? 3.0f : 0.8f;
        float pitch = this.getBbWidth() > 1.0f ? 1.0f : (1.35f + Utils.random.nextFloat() * .3f);

        level().playSound(null, this.getX(), this.getY(), this.getZ(), sound.get(),SoundSource.NEUTRAL, volume, pitch);
    }

    @Override
    public Optional<Supplier<SoundEvent>> getImpactSound() {
        return Optional.of(() -> SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!level().isClientSide()) {
            impactParticles(xOld, yOld, zOld);
            getImpactSound().ifPresent(this::doImpactSound);

            float explosionRadius = getExplosionRadius();
            float explosionRadiusSqr = explosionRadius * explosionRadius;
            Vec3 impactLocation = hitResult.getLocation();

            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));

            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(impactLocation);
                
                if (distanceSqr < explosionRadiusSqr && canHitEntity(entity)) {
                    double damageFactor = (1 - distanceSqr / explosionRadiusSqr);
                    float actualDamage = (float) (getDamage() * damageFactor);

                    DamageSources.applyDamage(entity, actualDamage, CreativeTabRegistry.DEAD_STAR_DECREE_SPELL.get().getDamageSource(this, getOwner()));
                }
            }
            
            discard();
        }
    }
}