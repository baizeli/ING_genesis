
package miku.united_as_one.genesis.contents.entity;

import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.ApiRangedBowAttackGoal;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.MeleeGoal;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.EnumSet;
import java.util.List;

public class WardenSpellcaster
extends ApiPathfinderMob
implements IFlagMob
{
    protected static final EntityDataAccessor<Integer> DATA_FLAGS;
    protected static final EntityDataAccessor<Integer> DATA_ANIM_TICK;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int IDLE = 0;
    public static final int START = 1;
    public static final int LEAVE = 2;
    public static final int ROAR = 3;
    public static final int SMELL = 4;
    public static final int COMBAT = 5;
    public static final int SKILL = 6;
    public static final int SPELL_1 = 7;
    public static final int SPELL_2 = 8;
    public static final int SPELL_3 = 9;
    public AnimationState idle = new AnimationState();
    public AnimationState start = new AnimationState();
    public AnimationState leave = new AnimationState();
    public AnimationState roar = new AnimationState();
    public AnimationState smell = new AnimationState();
    public AnimationState combat = new AnimationState();
    public AnimationState skill = new AnimationState();
    public AnimationState spell1 = new AnimationState();
    public AnimationState spell2 = new AnimationState();
    public AnimationState spell3 = new AnimationState();
    private int sonicCooldown = 0;
    public WardenSpellcaster(EntityType<? extends WardenSpellcaster> type, Level level)
    {
        super(type, level);
        this.xpReward = 50;
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
        this.entityData.define(DATA_ANIM_TICK, 0);
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new WSMeleeGoal(this));
        this.goalSelector.addGoal(2, new WSRangedAttackGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WardenSpellcaster.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public void aiStep()
    {
        super.aiStep();
        if (this.sonicCooldown > 0) --this.sonicCooldown;
        if (this.getTarget() != null) {
            if (this.getFlag() == 0) {
                this.pickFlag(this.getTarget());
            }
            this.tickFlag(this.getTarget());
        }
    }

    protected void clientAiStep()
    {
        this.idle.startIfStopped(this.tickCount);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey)
    {
        if (pKey.equals(DATA_FLAGS)) {
            if (this.level.isClientSide) {
                switch (this.getFlag()) {
                    case START -> startAnimAfterStop(start);
                    case LEAVE -> startAnimAfterStop(leave);
                    case ROAR -> startAnimAfterStop(roar);
                    case SMELL -> startAnimAfterStop(smell);
                    case COMBAT -> startAnimAfterStop(combat);
                    case SKILL -> startAnimAfterStop(skill);
                    case SPELL_1 -> startAnimAfterStop(spell1);
                    case SPELL_2 -> startAnimAfterStop(spell2);
                    case SPELL_3 -> startAnimAfterStop(spell3);
                }
            } else {
                if (this.getFlag() < 0 || this.getFlag() > SPELL_3) {
                    this.resetFlag();
                    LOGGER.warn("Invalid flag for {}, resetting to 0", this.getClass().getSimpleName());
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    private void pickFlag(LivingEntity pTarget)
    {
        int flag = 0;
        if (this.closerThan(pTarget, 15.0D)) {
            flag = COMBAT;
        } else if (this.sonicCooldown <= 0) {
            float chance = this.randomUtil.nextFloat();
            if (chance < 0.4F) {
                flag = SPELL_1;
            } else if (chance < 0.6F) {
                flag = SPELL_2;
            } else if (chance < 0.8F) {
                flag = SPELL_3;
            } else {
                flag = SKILL;
            }
        }
        this.setFlag(flag);
    }

    private void tickFlag(LivingEntity pTarget)
    {
        int i = this.getFlag();
        if (i > 0) {
            this.increaseAniTick();
        }
        switch (i) {
            case START -> {
                if (this.aniTickEquals(1)) {
                    this.playSound(SoundEvents.WARDEN_AGITATED, 3.0F, 1.0F);
                }
                if (this.aniTick((int)Maths.toTick(6.63F))) {
                    this.resetState();
                }
            }
            case LEAVE -> {
                if (this.aniTickEquals(1)) {
                    this.playSound(SoundEvents.WARDEN_DIG, 3.0F, 1.0F);
                }
                if (this.aniTick(90)) {
                    this.resetState();
                    this.discard();
                }
            }
            case ROAR -> {
                if (this.aniTickEquals(30)) {
                    this.playSound(SoundEvents.WARDEN_ANGRY, 2.0F, 1.0F);
                }
                if (this.aniTick((int)Maths.toTick(4.21F))) {
                    this.resetState();
                }
            }
            case SMELL -> {
                if (this.aniTickEquals(1)) {
                    this.playSound(SoundEvents.WARDEN_SNIFF, 2.0F, 1.0F);
                }
                if (this.aniTick((int)Maths.toTick(3.33F))) {
                    this.resetState();
                }
            }
            case COMBAT -> {
                if (this.aniTickEquals(4)) {
                    if (this.closerThan(pTarget, 3.0D)) {
                        this.doHurtTarget(pTarget);
                    }
                }
                if (this.aniTick((int)Maths.toTick(0.33F))) {
                    this.resetState();
                }
            }
            case SKILL -> {
                if (this.aniTickEquals(1)) {
                    this.playSound(SoundEvents.WARDEN_SONIC_CHARGE, 2.0F, 1.0F);
                }
                if (this.aniTickEquals((int)Maths.toTick(1.92F))) {
                    this.sonicBoom(pTarget);
                }
                if (this.aniTick(60)) {
                    this.resetState();
                    this.setSonicCooldown();
                }
            }
            case SPELL_1 -> {
                if (this.aniTickEquals(50)) {
                    //TODO spell1
                }
                if (this.aniTick((int)Maths.toTick(2.79F))) {
                    this.resetState();
                }
            }
            case SPELL_2 -> {
                //TODO 这里我不知道是个什么逻辑，自己看动画吧，用
                /// {@linkplain this#aniTickEquals(int)} 来控制时间，1 = 1tick
                if (this.aniTick(10)) {
                    this.resetState();
                }
            }
            case SPELL_3 -> {
                if (this.aniTickEquals(4)) {
                    //TODO spell3
                }
                if (this.aniTick(10)) {
                    this.resetState();
                }
            }
        }
    }

    public boolean doHurtTarget(Entity pEntity)
    {
        this.playSound(SoundEvents.WARDEN_ATTACK_IMPACT, 2.0F, 1.0F);
        return super.doHurtTarget(pEntity);
    }

    private void sonicBoom(LivingEntity p_217704_)
    {
        if (this.closerThan(p_217704_, 15.0D, 20.0D)) {
            Vec3 vec3 = this.position().add(0.0D, 1.6D, 0.0D);
            Vec3 vec31 = p_217704_.getEyePosition().subtract(vec3);
            Vec3 vec32 = vec31.normalize();
            for (double i = 1;i < (double)Mth.floor(vec31.length()) + 7;++i) {
                Vec3 vec33 = vec3.add(vec32.scale(i));
                serverLevel().sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0D,
                        0.0D, 0.0D, 0.0D);
            }
            this.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
            p_217704_.hurt(level.damageSources().sonicBoom(this), this.getAttackDamage() * 0.75F);
            double d1 = 0.5D * (1.0D - p_217704_.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            double d0 = 2.5D * (1.0D - p_217704_.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            p_217704_.push(vec32.x * d0, vec32.y * d1, vec32.z * d0);
        }
    }
    private void setSonicCooldown() {this.sonicCooldown = 40;}
    public int getFlag()
    {
        return this.entityData.get(DATA_FLAGS);
    }
    public void setFlag(int i)
    {
        this.entityData.set(DATA_FLAGS, i);
    }
    public int getAniTick()
    {
        return this.entityData.get(DATA_ANIM_TICK);
    }
    public void setAniTick(int aniTick)
    {
        this.entityData.set(DATA_ANIM_TICK, aniTick);
    }
    protected boolean canRide(Entity vehicle) {return false;}
    public boolean isInvulnerable() {return this.isFlag(START) || this.isFlag(LEAVE) || super.isInvulnerable();}
    protected void playStepSound(BlockPos pos, BlockState state){this.playSound(SoundEvents.WARDEN_STEP, 2.0F, 1.0F);}
    protected SoundEvent getAmbientSound() {return SoundEvents.WARDEN_AMBIENT;}
    protected SoundEvent getHurtSound(DamageSource damageSource) {return SoundEvents.WARDEN_HURT;}
    protected SoundEvent getDeathSound(){return SoundEvents.WARDEN_DEATH;}
    public boolean isPushable(){return !this.isFlag(START) && !isFlag(LEAVE) && super.isPushable();}
    public boolean canDisableShield() {return true;}
    protected float nextStep() {return this.moveDist + 0.55F;}
    private List<AnimationState> states = null;
    private List<AnimationState> getAllAnimations() {
        if (states == null) states = List.of(start, leave, roar, combat, skill, smell, spell1, spell2, spell3);
        return states;
    }

    private void startAnimAfterStop(AnimationState state) {
        for (var s : this.getAllAnimations()) {s.stop();state.startIfStopped(this.tickCount);}
    }

    Warden warden;

    public float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    private boolean checkDistance()
    {
        return this.closerThan(this.getTarget(), 15.0D);
    }

    /// 属性
    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.MAX_HEALTH, 350.0D)
                .add(Attributes.ATTACK_DAMAGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(WardenSpellcaster.class, EntityDataSerializers.INT);
        DATA_ANIM_TICK = SynchedEntityData.defineId(WardenSpellcaster.class, EntityDataSerializers.INT);
    }

    private static class WSMeleeGoal extends MeleeGoal {
        private final WardenSpellcaster spellcaster;
        public WSMeleeGoal(WardenSpellcaster finder)
        {
            super(finder, 1.0D);
            this.spellcaster =finder;
        }

        public boolean canUse()
        {
            return super.canUse() && spellcaster.checkDistance();
        }

        public boolean canContinueToUse()
        {
            return super.canContinueToUse() && spellcaster.checkDistance();
        }
    }

    /// 15格外小白ai，
    /// @see ApiRangedBowAttackGoal
    private static class WSRangedAttackGoal extends Goal
    {
        protected final WardenSpellcaster mob;
        protected final double speedModifier;
        protected final float attackRadiusSqr;
        protected int attackTime;
        protected int seeTime;
        protected boolean strafingClockwise;
        protected boolean strafingBackwards;
        protected int strafingTime;
        public WSRangedAttackGoal(WardenSpellcaster p_25773_, double p_25774_)
        {
            this.mob = p_25773_;
            this.attackTime = -1;
            this.strafingTime = -1;
            this.speedModifier = p_25774_;
            this.attackRadiusSqr = 16.0F * 16.0F;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public boolean canUse()
        {
            return this.mob.getTarget() != null && this.mob.getTarget().isAlive()
                    && !this.mob.checkDistance();
        }

        public void start() {
            this.mob.setAggressive(true);
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone());
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1) {
                    this.seeTime = 0;
                }
                if (flag) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }
                this.moveOrStrafe(livingentity, d0);
            }
        }

        public void moveOrStrafe(LivingEntity livingentity, double distance) {
            if (distance <= (double)this.attackRadiusSqr && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }
            if (this.strafingTime >= 20) {
                if (this.mob.getRandom().nextFloat() < 0.3F) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if (this.mob.getRandom().nextFloat() < 0.3F) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }
            if (this.strafingTime > -1) {
                if (distance > (double)(this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (distance < (double)(this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.mob.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }
        }

        public void stop() {
            this.seeTime = 0;
            this.attackTime = -1;
        }
    }
}
