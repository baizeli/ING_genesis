package miku.united_as_one.genesis.contents.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import miku.united_as_one.genesis.contents.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class HammerMob extends Monster {
    private static final int AWAKEN_DELAY_TICKS = 30;

    private static final EntityDataAccessor<Boolean> AWAKEN_PLAYED = SynchedEntityData.defineId(HammerMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_STATE = SynchedEntityData.defineId(HammerMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK = SynchedEntityData.defineId(HammerMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ANIM_BUFFER_TICK = SynchedEntityData.defineId(HammerMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_SPRINT_COOLING = SynchedEntityData.defineId(HammerMob.class, EntityDataSerializers.BOOLEAN);

    public static final int ATTACK_IDLE = 0;
    public static final int ATTACK_HEAVY = 1;
    public static final int ATTACK_SWEEP = 2;
    public static final int ATTACK_SPRINT = 3;

    private static final Map<Integer, Integer> ATTACK_WEIGHTS = new HashMap<>();

    static {
        ATTACK_WEIGHTS.put(ATTACK_HEAVY, 1);
        ATTACK_WEIGHTS.put(ATTACK_SWEEP, 1);
    }

    private int selectedAttack = ATTACK_IDLE;

    public final AnimationState awaken = new AnimationState();
    public final AnimationState idle = new AnimationState();
    public final AnimationState sprinting = new AnimationState();
    public final AnimationState death = new AnimationState();
    public final AnimationState jumpAttack = new AnimationState();
    public final AnimationState heavyAttack = new AnimationState();
    public final AnimationState sweepAttack = new AnimationState();
    public final AnimationState throwHammer = new AnimationState();

    public HammerMob(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 25.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.21D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SweepAttackGoal(this));
        this.goalSelector.addGoal(1, new HeavyAttackGoal(this));
        this.goalSelector.addGoal(1, new SprintAttackGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (!this.entityData.get(AWAKEN_PLAYED) && this.tickCount > AWAKEN_DELAY_TICKS) {
                this.entityData.set(AWAKEN_PLAYED, true);
            }

            if (this.getAttackState() != ATTACK_IDLE) {
                this.entityData.set(DATA_ATTACK_TICK, this.entityData.get(DATA_ATTACK_TICK) + 1);
            }

            if (this.getAnimBufferTick() > 0) {
                this.entityData.set(DATA_ANIM_BUFFER_TICK, this.entityData.get(DATA_ANIM_BUFFER_TICK) - 1);
            }

            if (this.getAttackState() == ATTACK_IDLE && this.getTarget() != null && this.getAnimBufferTick() <= 0 && this.selectedAttack == ATTACK_IDLE) {
                int totalWeight = 0;
                for (int weight : ATTACK_WEIGHTS.values()) {
                    totalWeight += weight;
                }
                int random = this.random.nextInt(totalWeight);
                for (Map.Entry<Integer, Integer> entry : ATTACK_WEIGHTS.entrySet()) {
                    random -= entry.getValue();
                    if (random < 0) {
                        this.selectedAttack = entry.getKey();
                        break;
                    }
                }
            } else {
                this.getAttackState();
            }
        }
        if (this.level().isClientSide) {
            if (!this.entityData.get(AWAKEN_PLAYED) && !this.awaken.isStarted()) {
                this.awaken.start(this.tickCount);
            } else if (this.getDeltaMovement().horizontalDistanceSqr() < 0.01 && !this.idle.isStarted() && !this.awaken.isStarted()) {
                this.idle.startIfStopped(this.tickCount);
            } else if (this.getDeltaMovement().horizontalDistanceSqr() >= 0.01) {
                this.idle.stop();
            }

            this.updateAnimations();
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AWAKEN_PLAYED, false);
        this.entityData.define(DATA_ATTACK_STATE, ATTACK_IDLE);
        this.entityData.define(DATA_ATTACK_TICK, 0);
        this.entityData.define(DATA_ANIM_BUFFER_TICK, 0);
        this.entityData.define(DATA_SPRINT_COOLING, false);
    }

    private void updateAnimations() {
        if (this.getAttackState() == ATTACK_SPRINT) {
            if (!this.sprinting.isStarted()) {
                this.sprinting.start(this.tickCount);
            }
        } else if (this.getAnimBufferTick() <= 0 && this.sprinting.isStarted()) {
            this.sprinting.stop();
        }

        if (this.getAttackState() == ATTACK_HEAVY) {
            if (this.getAttackTick() <= 1 || !this.heavyAttack.isStarted()) {
                this.heavyAttack.start(this.tickCount);
            }
        } else if (this.getAnimBufferTick() <= 0 && this.heavyAttack.isStarted()) {
            this.heavyAttack.stop();
        }

        if (this.getAttackState() == ATTACK_SWEEP) {
            if (this.getAttackTick() <= 1 || !this.sweepAttack.isStarted()) {
                this.sweepAttack.start(this.tickCount);
            }
        } else if (this.getAnimBufferTick() <= 0 && this.sweepAttack.isStarted()) {
            this.sweepAttack.stop();
        }
    }

    public boolean isPushable() {
        return false;
    }

    public void checkDespawn() {}

    public int getAttackState() {
        return this.entityData.get(DATA_ATTACK_STATE);
    }

    public int getSelectedAttack() {
        return this.selectedAttack;
    }

    public void setAttackState(int state) {
        this.entityData.set(DATA_ATTACK_STATE, state);
        if (state == ATTACK_IDLE) {
            this.entityData.set(DATA_ATTACK_TICK, 0);
            this.selectedAttack = ATTACK_IDLE;
        }
    }

    public int getAttackTick() {
        return this.entityData.get(DATA_ATTACK_TICK);
    }

    public int getAnimBufferTick() {
        return this.entityData.get(DATA_ANIM_BUFFER_TICK);
    }

    public void setAnimBufferTick(int tick) {
        this.entityData.set(DATA_ANIM_BUFFER_TICK, tick);
    }

    public void forceLookAt(float yRot) {
        this.setYRot(yRot);
        this.yRotO = yRot;
        this.yBodyRot = yRot;
        this.yBodyRotO = yRot;
        this.yHeadRot = yRot;
        this.yHeadRotO = yRot;
    }

    public boolean isSprintAttackCooling() {
        return this.entityData.get(DATA_SPRINT_COOLING);
    }

    public void setSprintAttackCooling(boolean cooling) {
        this.entityData.set(DATA_SPRINT_COOLING, cooling);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("AwakenPlayed", this.entityData.get(AWAKEN_PLAYED));
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(AWAKEN_PLAYED, compound.getBoolean("AwakenPlayed"));
    }
}