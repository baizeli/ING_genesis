package miku.united_as_one.genesis.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public class EntitySolarBeam extends Entity {
    public LivingEntity caster;

    public double endPosX;
    public double endPosY;
    public double endPosZ;

    public double collidePosX;
    public double collidePosY;
    public double collidePosZ;

    public double prevCollidePosX;
    public double prevCollidePosY;
    public double prevCollidePosZ;

    public float renderYaw;
    public float renderPitch;

    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    public Direction blockSide = null;

    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_PLAYER = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.INT);

    public float prevYaw;
    public float prevPitch;

    @OnlyIn(Dist.CLIENT)
    private Vec3[] attractorPos;

    public EntitySolarBeam(EntityType<? extends EntitySolarBeam> type, Level world) {
        super(type, world);
        this.noCulling = true;
        if (world.isClientSide)
            this.attractorPos = new Vec3[] { new Vec3(0.0D, 0.0D, 0.0D) };
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }
    public boolean hurt(DamageSource p_19946_, float p_19947_) {
        return false;
    }
    public void tick() {
        super.tick();
        this.prevCollidePosX = this.collidePosX;
        this.prevCollidePosY = this.collidePosY;
        this.prevCollidePosZ = this.collidePosZ;
        this.prevYaw = this.renderYaw;
        this.prevPitch = this.renderPitch;
        this.xo = getX();
        this.yo = getY();
        this.zo = getZ();
        if (this.tickCount == 1 && this.level().isClientSide)
            this.caster = (LivingEntity)this.level().getEntity(getCasterID());
        if (!this.level().isClientSide)
            if (getHasPlayer()) {
                updateWithPlayer();
            } else if (this.caster instanceof Salmon) {
                updateWithBarako();
            }
        if (this.caster != null) {
            this.renderYaw = (float)((this.caster.yHeadRot + 90.0D) * Math.PI / 180.0D);
            this.renderPitch = (float)(-this.caster.getXRot() * Math.PI / 180.0D);
            this.setPos(caster.getX(),caster.getY()+1,caster.getZ());
            this.teleportTo(caster.getX(),caster.getY()+1,caster.getZ());
            this.setDeltaMovement(caster.getDeltaMovement().x,caster.getDeltaMovement().y,caster.getDeltaMovement().z);
        }
        if (!this.on && this.appear.getTimer() == 0)
            discard();
        if (this.on && this.tickCount > 20) {
            this.appear.increaseTimer();
        } else {
            this.appear.decreaseTimer();
        }
        if (this.caster != null && !this.caster.isAlive())
            discard();
        if (this.level().isClientSide && this.tickCount <= 10 && this.caster != null) {
            int particleCount = 8;
            while (--particleCount != 0) {
                double rootX = this.caster.getX();
                double rootY = this.caster.getY() + (this.caster.getBbHeight() / 2.0F) + 0.30000001192092896D;
                double rootZ = this.caster.getZ();
                this.attractorPos[0] = new Vec3(rootX, rootY, rootZ);
            }
        }
        if (this.tickCount > 20) {
            calculateEndPos();
            List<LivingEntity> hit = (raytraceEntities(this.level(), new Vec3(getX(), getY(), getZ()), new Vec3(this.endPosX, this.endPosY, this.endPosZ), true)).entities;
            if (this.blockSide != null)
                spawnExplosionParticles(2);
            if (!this.level().isClientSide)
                for (LivingEntity target : hit) {
                    float damageMob = 3.0F;
                    target.invulnerableTime = 0;
                    target.hurt(damageSources().indirectMagic(this, (Entity)this.caster), damageMob);
                }
        }
        if (this.tickCount - 20 > getDuration())
            this.on = false;
    }

    private void spawnExplosionParticles(int amount) {
        int i;
        for (i = 0; i < amount; i++) {
            float yaw = (float)((this.random.nextFloat() * 2.0F) * Math.PI);
            float motionY = this.random.nextFloat() * 0.08F;
            float motionX = 0.1F * Mth.cos(yaw);
            float motionZ = 0.1F * Mth.sin(yaw);
            this.level().addParticle(ParticleTypes.FLAME, this.collidePosX, this.collidePosY + 0.1D, this.collidePosZ, motionX, motionY, motionZ);
        }
        for (i = 0; i < amount / 2; i++)
            this.level().addParticle(ParticleTypes.LAVA, this.collidePosX, this.collidePosY + 0.1D, this.collidePosZ, 0.0D, 0.0D, 0.0D);
    }

    protected void defineSynchedData() {
        getEntityData().define(YAW, Float.valueOf(0.0F));
        getEntityData().define(PITCH, Float.valueOf(0.0F));
        getEntityData().define(DURATION, Integer.valueOf(0));
        getEntityData().define(HAS_PLAYER, Boolean.valueOf(false));
        getEntityData().define(CASTER, Integer.valueOf(-1));
    }

    public float getYaw() {
        return getEntityData().get(YAW).floatValue();
    }

    public void setYaw(float yaw) {
        getEntityData().set(YAW, Float.valueOf(yaw));
    }

    public float getPitch() {
        return getEntityData().get(PITCH).floatValue();
    }

    public void setPitch(float pitch) {
        getEntityData().set(PITCH, Float.valueOf(pitch));
    }

    public int getDuration() {
        return getEntityData().get(DURATION).intValue();
    }

    public void setDuration(int duration) {
        getEntityData().set(DURATION, Integer.valueOf(duration));
    }

    public boolean getHasPlayer() {
        return getEntityData().get(HAS_PLAYER).booleanValue();
    }

    public int getCasterID() {
        return getEntityData().get(CASTER).intValue();
    }

    protected void readAdditionalSaveData(CompoundTag nbt) {}

    protected void addAdditionalSaveData(CompoundTag nbt) {}

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void calculateEndPos() {
        double radius = (this.caster instanceof Salmon) ? 30.0D : 20.0D;
        if (this.level().isClientSide()) {
            this.endPosX = getX() + radius * Math.cos(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosZ = getZ() + radius * Math.sin(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosY = getY() + radius * Math.sin(this.renderPitch);
        } else {
            this.endPosX = getX() + radius * Math.cos(getYaw()) * Math.cos(getPitch());
            this.endPosZ = getZ() + radius * Math.sin(getYaw()) * Math.cos(getPitch());
            this.endPosY = getY() + radius * Math.sin(getPitch());
        }
    }

    public SolarbeamHitResult raytraceEntities(Level world, Vec3 from, Vec3 to, boolean ignoreBlockWithoutBoundingBox) {
        SolarbeamHitResult result = new SolarbeamHitResult();
        result.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (result.blockHit != null) {
            Vec3 hitVec = result.blockHit.getLocation();
            this.collidePosX = hitVec.x;
            this.collidePosY = hitVec.y;
            this.collidePosZ = hitVec.z;
            this.blockSide = result.blockHit.getDirection();
        } else {
            this.collidePosX = this.endPosX;
            this.collidePosY = this.endPosY;
            this.collidePosZ = this.endPosZ;
            this.blockSide = null;
        }
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, (new AABB(Math.min(getX(), this.collidePosX), Math.min(getY(), this.collidePosY), Math.min(getZ(), this.collidePosZ), Math.max(getX(), this.collidePosX), Math.max(getY(), this.collidePosY), Math.max(getZ(), this.collidePosZ))).inflate(1.0D, 1.0D, 1.0D));
        for (LivingEntity entity : entities) {
            if (entity == this.caster)
                continue;
            float pad = entity.getPickRadius() + 0.5F;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
                continue;
            }
            if (hit.isPresent())
                result.addEntityHit(entity);
        }
        return result;
    }

    public void push(Entity entityIn) {}

    public boolean isPickable() {
        return false;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        return (distance < 1024.0D);
    }

    private void updateWithPlayer() {
        setYaw((float)((this.caster.yHeadRot + 90.0F) * Math.PI / 180.0D));
        setPitch((float)(-this.caster.getXRot() * Math.PI / 180.0D));
        Vec3 vecOffset = this.caster.getLookAngle().normalize().scale(1.0D);
        setPos(this.caster.getX() + vecOffset.x(), this.caster.getY() + 1.2000000476837158D + vecOffset.y(), this.caster.getZ() + vecOffset.z());
    }

    private void updateWithBarako() {
        setYaw((float)((this.caster.yHeadRot + 90.0F) * Math.PI / 180.0D));
        setPitch((float)(-this.caster.getXRot() * Math.PI / 180.0D));
        Vec3 vecOffset1 = (new Vec3(0.0D, 0.0D, 0.6D)).yRot((float)Math.toRadians(-this.caster.getYRot()));
        Vec3 vecOffset2 = (new Vec3(1.2D, 0.0D, 0.0D)).yRot(-getYaw()).xRot(getPitch());
        setPos(this.caster.getX() + vecOffset1.x() + vecOffset2.x(), this.caster.getY() + 1.399999976158142D + vecOffset1.y() + vecOffset2.y(), this.caster.getZ() + vecOffset1.z() + vecOffset2.z());
    }

    public void remove(Entity.RemovalReason reason) {
        super.remove(reason);
    }

    public static class SolarbeamHitResult {
        private BlockHitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult)rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            this.entities.add(entity);
        }
    }
}
