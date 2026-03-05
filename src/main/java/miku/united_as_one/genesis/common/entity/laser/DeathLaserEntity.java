package miku.united_as_one.genesis.common.entity.laser;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
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
        return new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(
            Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "holy_magic")
        )), this.caster, this);
    }

    @Override
    protected void spawnExplosionParticles() {
        int i;
        for (i = 0; i < 2; i++) {
            float yaw = (float)((this.random.nextFloat() * 2f) * Math.PI);
            float motionY = this.random.nextFloat() * 0.08f;
            float motionX = 0.1f * Mth.cos(yaw);
            float motionZ = 0.1f * Mth.sin(yaw);
            this.level().addParticle(ParticleTypes.FLAME, this.collidePosX, this.collidePosY + 0.1d, this.collidePosZ, motionX, motionY, motionZ);
        }
        for (i = 0; i < 1; i++)
            this.level().addParticle(ParticleTypes.LAVA, this.collidePosX, this.collidePosY + 0.1d, this.collidePosZ, 0d, 0d, 0d);
    }

    @Override
    public void setLaserLength(float length) {
        this.laserLength = length;
        super.setLaserLength(length);
    }
}