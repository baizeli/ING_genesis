package miku.united_as_one.genesis.common.entity.laser;

import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;

public class DeathLaserEntity extends AbstractLaserEntity {
    public float laserLength = 20;

    public DeathLaserEntity(EntityType<? extends DeathLaserEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected DamageSource createDamageSource() {
        return new DamageSource(this.level().registryAccess()
            .registryOrThrow(Registries.DAMAGE_TYPE)
            .getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, 
                ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "lightning_magic")
            )), this, this.caster
        );
    }

    @Override
    protected void spawnCollisionParticles() {
        for (int i = 0; i < 20; i++) {
            this.level().addParticle(ParticleHelper.ELECTRICITY,
                this.collidePosX + this.random.nextDouble() * 0.4 - 0.2,
                this.collidePosY + 0.1d + this.random.nextDouble() * 0.4 - 0.2,
                this.collidePosZ + this.random.nextDouble() * 0.4 - 0.2,
                (this.random.nextDouble() - 0.5) * 0.3,
                (this.random.nextDouble() - 0.5) * 0.3,
                (this.random.nextDouble() - 0.5) * 0.3
            );
        }
    }

    @Override
    protected void spawnBeamParticles() {
        /*double steps = this.getLaserLength() / 1.5;
        for (int i = 0; i < steps; i++) {
            double ratio = i / steps;
            double particleX = this.getX() + (this.endPosX - this.getX()) * ratio;
            double particleY = this.getY() + (this.endPosY - this.getY()) * ratio;
            double particleZ = this.getZ() + (this.endPosZ - this.getZ()) * ratio;

            double angle = this.random.nextDouble() * Math.PI * 2;
            double radius = this.random.nextDouble() * 0.3;
            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;
            double offsetY = (this.random.nextDouble() - 0.5) * 0.3;
            
            this.level().addParticle(ParticleHelper.ELECTRICITY,
                particleX + offsetX,
                particleY + offsetY,
                particleZ + offsetZ,
                (this.random.nextDouble() - 0.5) * 0.2,
                (this.random.nextDouble() - 0.5) * 0.2,
                (this.random.nextDouble() - 0.5) * 0.2
            );
        }*/
    }

    @Override
    public void setLaserLength(float length) {
        this.laserLength = length;
        super.setLaserLength(length);
    }
}