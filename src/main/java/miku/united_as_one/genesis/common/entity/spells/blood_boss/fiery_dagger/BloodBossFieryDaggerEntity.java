package miku.united_as_one.genesis.common.entity.spells.blood_boss.fiery_dagger;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import miku.united_as_one.genesis.common.entity.spells.blood_boss.BloodBossFireEruptionAoe;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import miku.united_as_one.genesis.util.mixinutil.ParticleSuppressionManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.lang.reflect.Field;

public class BloodBossFieryDaggerEntity extends FieryDaggerEntity {
    private static final Field field;

    static {
        try {
            field = FieryDaggerEntity.class.getDeclaredField("isGrounded");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public BloodBossFieryDaggerEntity(Level level) {
        this(EntityRegistry.BLOOD_BOSS_FIERY_DAGGER_PROJECTILE.get(), level);
    }

    public BloodBossFieryDaggerEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        BloodBossFireEruptionAoe aoe = new BloodBossFireEruptionAoe(level, 8.0F);
        aoe.setOwner(this.getOwner());
        aoe.setDamage(SpellRegistry.RAISE_HELL_SPELL.get().getSpellPower(1, this.getOwner()) + Utils.getWeaponDamage((LivingEntity) this.getOwner(), MobType.UNDEFINED));
        aoe.moveTo(hitresult.getLocation());
        level.addFreshEntity(aoe);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (this.getOwner() instanceof LivingEntity livingEntity)
            livingEntity.heal(this.damage);
    }

    @Override
    public void tick() {
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleHelper.BLOOD, this.getX(), this.getY() + (this.getBbHeight() * 0.5), this.getZ(), 0.0, 0.0, 0.0);
        }

        ParticleSuppressionManager.setSuppressEmbers(true);
        try {
            super.tick();
        } finally {
            ParticleSuppressionManager.setSuppressEmbers(false);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.BLOOD, x, y, z, 5, 0.1, 0.1, 0.1, (double)0.25F, true);
    }

    public void trailParticles() {
        float yHeading = -((float)(Mth.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * (double)(180F / (float)Math.PI)) + 90.0F);
        float radius = 0.25F;
        int steps = 2;
        Vec3 vec = this.getDeltaMovement();
        double x2 = this.getX();
        double x1 = x2 - vec.x;
        double y2 = this.getY();
        double y1 = y2 - vec.y;
        double z2 = this.getZ();
        double z1 = z2 - vec.z;

        for(int j = 0; j < steps; ++j) {
            double offset = 1.0 / steps * j;
            double radians = (this.tickCount + offset) / 7.5 * 360.0 * (Math.PI / 180);
            Vec3 swirl = (new Vec3(Math.cos(radians) * (double)radius, Math.sin(radians) * (double)radius, 0.0F)).yRot(yHeading * ((float)Math.PI / 180F));
            double x = Mth.lerp(offset, x1, x2) + swirl.x;
            double y = Mth.lerp(offset, y1, y2) + swirl.y + (double)(this.getBbHeight() / 2.0F);
            double z = Mth.lerp(offset, z1, z2) + swirl.z;
            Vec3 jitter = Vec3.ZERO;
            this.level.addParticle(ParticleHelper.BLOOD, x, y, z, jitter.x, jitter.y, jitter.z);
        }

    }
}
