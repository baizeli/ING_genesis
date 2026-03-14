
package miku.united_as_one.genesis.common.entity.boss;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class SwordMan
extends PathfinderMob {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Boolean> TWO_SWORD;
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private static final EntityDataAccessor<Integer> DATA_ANIMATION_TICK;
    private static final EntityDataAccessor<Boolean> DATA_SECOND_PHASE;
    public static final int ATTACK1_R = 5;
    public AnimationState attack1 = new AnimationState();
    public AnimationState attack1_R = new AnimationState();
    public AnimationState attack2 = new AnimationState();
    public AnimationState attack3 = new AnimationState();
    public AnimationState attack4 = new AnimationState();
    public AnimationState summon = new AnimationState();
    public SwordMan(EntityType<? extends SwordMan> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TWO_SWORD, Boolean.FALSE);
        this.entityData.define(DATA_ANIMATION_TICK, 0);
        this.entityData.define(DATA_FLAGS, 0);
        this.entityData.define(DATA_SECOND_PHASE, false);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeGoal(this));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(6, new FloatGoal(this));
    }

    public void aiStep() {
        super.aiStep();
        if (this.getTarget() != null && this.isFlag(0)) {
            this.pickFlags();
        }
        if (isFlag(1))
            this.attack1Tick();
        else if (isFlag(2))
            this.attack2Tick();
        else if (isFlag(3))
            this.attack3Tick();
        else if (isFlag(4))
            this.summonTick();
        else if (isFlag(5))
            this.attack1_R();
        else if (isFlag(6))
            this.attack4();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey) && this.level().isClientSide) {
            switch (this.getFlag()) {
                case 0: break;
                case 1: {
                    this.stopAllAnimations();
                    this.attack1.startIfStopped(tickCount);
                    break;
                }
                case 2: {
                    this.stopAllAnimations();
                    this.attack2.startIfStopped(tickCount);
                    break;
                }
                case 3: {
                    this.stopAllAnimations();
                    this.attack3.startIfStopped(tickCount);
                    break;
                }
                case 4: {
                    this.stopAllAnimations();
                    this.summon.startIfStopped(tickCount);
                    break;
                }
                case 5: {
                    this.stopAllAnimations();
                    this.attack1_R.startIfStopped(tickCount);
                    break;
                }
                case 6: {
                    this.stopAllAnimations();
                    this.attack4.startIfStopped(tickCount);
                    break;
                }
                default: {
                    LOGGER.warn("Can't handle synced event in {}, call NineAbyss9!", this.getClass().getSimpleName());
                    this.setFlag(0);
                    break;
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void attack1Tick() {
        increaseAnimationTick();
        if (this.animationTickEquals(20)) {
            this.moveForward();
            this.hurtEntities();
        }
        if (this.animationTickGreaterThan(40)) {
            this.resetState();
        }
    }

    public void attack1_R() {
        increaseAnimationTick();
        if (this.animationTickEquals(10))
            this.moveForward();
        if (this.animationTickEquals(15)) {
            this.hurtEntities();
        }
        if (this.animationTickGreaterThan(40)) {
            this.resetState();
        }
    }

    public void attack2Tick() {
        increaseAnimationTick();
        if (this.animationTickEquals(15)) {
            this.moveForward();
            this.hurtEntities();
        }
        if (this.animationTickGreaterThan(35)) {
            this.resetState();
        }
    }

    public void attack3Tick() {
        increaseAnimationTick();
        if (this.animationTickEquals(5))
            this.moveForward();
        if (this.animationTickEquals(10))
            this.hurtEntities();
        if (this.animationTickGreaterThan(40))
            this.resetState();
    }

    public void attack4() {
        increaseAnimationTick();
        if (this.animationTickEquals(10) || this.animationTickEquals(20)
        || this.animationTickEquals(30) || this.animationTickEquals(40))
            this.moveForward(1);
        if (this.getAnimationTick() > 10 && this.getAnimationTick() < 55 && this.getAnimationTick() % 5 == 0)
            this.hurtEntities();
        if (this.animationTickGreaterThan(75))
            this.resetState();
    }

    public void summonTick() {
        increaseAnimationTick();
        if (this.animationTickGreaterThan(50)) {
            this.resetState();
        }
    }

    public void pickFlags() {
        float rand = this.random.nextFloat();
        if (rand < 0.25F) {
            this.setFlag(1);
        } else if (rand < 0.5F) {
            this.setFlag(2);
        } else if (rand < 0.75F) {
            this.setFlag(3);
        }
    }

    public void moveForward(double pSpeed) {
        Vec3 vector = this.getLookAngle();
        this.setDeltaMovement(vector.x * pSpeed, this.getDeltaMovement().y, vector.z * pSpeed);
    }

    public void moveForward() {
        this.moveForward(2);
    }

    public void hurtEntities() {
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(4), this::canAttack);
        if (!entities.isEmpty()) {
            for (LivingEntity entity : entities) {
                this.doHurtTarget(entity);
            }
        }
    }

    public boolean isSecondPhase() {
        return this.entityData.get(DATA_SECOND_PHASE);
    }

    public void setSecondPhase(boolean flag) {
        this.entityData.set(DATA_SECOND_PHASE, flag);
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int flag) {
        this.entityData.set(DATA_FLAGS, flag);
    }

    public boolean isFlag(int flag) {
        return this.getFlag() == flag;
    }

    public int getAnimationTick() {
        return this.entityData.get(DATA_ANIMATION_TICK);
    }

    public void setAnimationTick(int tick) {
        this.entityData.set(DATA_ANIMATION_TICK, tick);
    }

    public void increaseAnimationTick() {
        this.setAnimationTick(this.getAnimationTick() + 1);
    }

    public boolean animationTickEquals(int tick) {
        return this.getAnimationTick() == tick;
    }

    public boolean animationTickGreaterThan(int tick) {
        return this.getAnimationTick() >= tick;
    }

    public void resetState() {
        this.setFlag(0);
        this.setAnimationTick(0);
    }

    private List<AnimationState> allAnimations() {
        return List.of(attack1, attack2, attack3, attack1_R, attack4);
    }

    public void stopAllAnimations() {
        allAnimations().forEach(AnimationState::stop);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("SecondPhase", this.isSecondPhase());
        compound.putInt("BossFlag", this.getFlag());
    }

    @Nullable
    @SuppressWarnings("deprecation")
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
                                        MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setFlag(4);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    static {
        TWO_SWORD = SynchedEntityData.defineId(SwordMan.class, EntityDataSerializers.BOOLEAN);
        DATA_FLAGS = SynchedEntityData.defineId(SwordMan.class, EntityDataSerializers.INT);
        DATA_ANIMATION_TICK = SynchedEntityData.defineId(SwordMan.class, EntityDataSerializers.INT);
        DATA_SECOND_PHASE = SynchedEntityData.defineId(SwordMan.class, EntityDataSerializers.BOOLEAN);
    }

    private static final class MeleeGoal extends MeleeAttackGoal {
        public MeleeGoal(PathfinderMob pMob) {
            super(pMob, 1, false);
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        }
    }
}
