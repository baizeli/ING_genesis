package miku.united_as_one.genesis.common.entity.gungnir;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import miku.united_as_one.genesis.util.mixinutil.ParticleSuppressionManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import org.jetbrains.annotations.NotNull;

public class GungnirDaggerEntity extends FieryDaggerEntity {
    public GungnirDaggerEntity(Level level) {
        this(EntityRegistry.GUNGNIR_DAGGER_PROJECTILE.get(), level);
    }

    public GungnirDaggerEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        entityHitResult.getEntity().hurt(new DamageSource(DamageSources.getHolderFromResource(this, ISSDamageTypes.BLOOD_MAGIC), this, this.getOwner()), 18);
        if (this.getOwner() instanceof LivingEntity livingEntity)
            livingEntity.heal(18 * 0.2F);

        GungnirChainLightning chainLightning = new GungnirChainLightning(this.level, this.getOwner(), this.getTargetEntity());
        chainLightning.setDamage(18);
        chainLightning.range = 25;
        chainLightning.maxConnections = 2;
        this.level.addFreshEntity(chainLightning);

        entityHitResult.getEntity().invulnerableTime = 0;

        this.discard();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
    }

    @Override
    protected void onHit(HitResult hitresult) {
        HitResult.Type hitresult$type = hitresult.getType();
        if (hitresult$type == HitResult.Type.ENTITY && ((EntityHitResult)hitresult).getEntity() == this.getTargetEntity()) {
            this.onHitEntity((EntityHitResult)hitresult);
            this.level.gameEvent(GameEvent.PROJECTILE_LAND, hitresult.getLocation(), GameEvent.Context.of(this, (BlockState)null));
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void handleHitDetection() {
        Vec3 start = this.position();
        Vec3 end = start.add(this.getDeltaMovement());

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                this.level,
                this,
                start,
                end,
                this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D),
                this::canHitEntity
        );

        if (entityHit != null) {
            Vec3 hitLocation = entityHit.getEntity().getBoundingBox()
                    .clip(start, end)
                    .orElse(entityHit.getEntity().position());

            EntityHitResult preciseHit = new EntityHitResult(entityHit.getEntity(), hitLocation);

            if (!MinecraftForge.EVENT_BUS.post(new ProjectileImpactEvent(this, preciseHit))) {
                this.onHit(preciseHit);
            }
        }
    }

    @Override
    public void tick() {
        ParticleSuppressionManager.setSuppressEmbers(true);
        try {
            if (this.tickCount > 100) {
                this.tickCount = 100;
            }
            super.tick();
        } finally {
            ParticleSuppressionManager.setSuppressEmbers(false);
        }
    }
}