//
//package miku.united_as_one.genesis.contents.entity.boss;
//
//import com.github.NineAbyss9.ix_api.api.mobs.ApiBoss;
//import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.MeleeGoal;
//import com.github.NineAbyss9.ix_api.util.ParticleUtil;
//import com.mojang.logging.LogUtils;
//import net.minecraft.core.particles.BlockParticleOption;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.*;
//import net.minecraft.world.entity.ai.goal.*;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import org.NineAbyss9.math.MathSupport;
//import org.slf4j.Logger;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//public class SwordMan
//extends PathfinderMob
//implements ApiBoss
//{
//    private static final Logger LOGGER = LogUtils.getLogger();
//    private static final EntityDataAccessor<Boolean> TWO_SWORD;
//    private static final EntityDataAccessor<Integer> DATA_FLAGS;
//    private static final EntityDataAccessor<Integer> DATA_ANIMATION_TICK;
//    private static final EntityDataAccessor<Boolean> DATA_SECOND_PHASE;
//    public static final int A01 = 1;
//    public static final int A02 = 2;
//    public static final int B01 = 3;
//    public static final int B02 = 4;
//    public static final int B03 = 5;
//    public static final int B03_Hit = 6;
//    public static final int C01 = 7;
//    public AnimationState idle = new AnimationState();
//    public AnimationState a01 = new AnimationState();
//    public AnimationState a02 = new AnimationState();
//    public AnimationState b01 = new AnimationState();
//    public AnimationState b02 = new AnimationState();
//    public AnimationState b03 = new AnimationState();
//    public AnimationState c01 = new AnimationState();
//    public AnimationState summon = new AnimationState();
//    public SwordMan(EntityType<? extends SwordMan> pEntityType, Level pLevel)
//    {
//        super(pEntityType, pLevel);
//    }
//
//    protected void defineSynchedData()
//    {
//        super.defineSynchedData();
//        this.entityData.define(TWO_SWORD, Boolean.FALSE);
//        this.entityData.define(DATA_ANIMATION_TICK, 0);
//        this.entityData.define(DATA_FLAGS, 0);
//        this.entityData.define(DATA_SECOND_PHASE, false);
//    }
//
//    protected void registerGoals()
//    {
//        this.goalSelector.addGoal(1, new MeleeGoal(this, 1D));
//        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
//        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8));
//        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 10.0F));
//        this.goalSelector.addGoal(6, new FloatGoal(this));
//    }
//
//    public void aiStep()
//    {
//        super.aiStep();
//        if (this.level().isClientSide)
//        {
//            this.idle.startIfStopped(tickCount);
//        }
//        if (this.getTarget() != null && this.isFlag(0))
//        {
//            this.pickFlags();
//        }
//        if (isFlag(A01))
//            a01();
//        else if (isFlag(A02))
//            a02();
//        else if (isFlag(B01))
//            b01();
//        else if (isFlag(B02))
//            b02();
//        else if (isFlag(B03))
//            b03();
//        else if (isFlag(C01))
//            this.c01();
//    }
//
//    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey)
//    {
//        if (DATA_FLAGS.equals(pKey) && this.level().isClientSide)
//        {
//            switch (this.getFlag())
//            {
//                case 0:
//                    break;
//                case A01:
//                {
//                    this.stopAllAnimations();
//                    this.a01.startIfStopped(tickCount);
//                    break;
//                }
//                case A02:
//                {
//                    this.stopAllAnimations();
//                    this.a02.startIfStopped(tickCount);
//                    break;
//                }
//                case B01:
//                {
//                    this.stopAllAnimations();
//                    this.b01.startIfStopped(tickCount);
//                    break;
//                }
//                case B02:
//                {
//                    this.stopAllAnimations();
//                    this.b02.startIfStopped(tickCount);
//                    break;
//                }
//                case B03:
//                {
//                    this.stopAllAnimations();
//                    this.b03.startIfStopped(tickCount);
//                    break;
//                }
//                case C01:
//                {
//                    this.stopAllAnimations();
//                    this.c01.startIfStopped(tickCount);
//                    break;
//                }
//                default:
//                {
//                    LOGGER.warn("Can't handle synced event in {}, call NineAbyss9!", this.getClass().getSimpleName());
//                    this.setFlag(0);
//                    break;
//                }
//            }
//        }
//        super.onSyncedDataUpdated(pKey);
//    }
//
//    public void a01()
//    {
//        increaseAnimationTick();
//        if (this.animationTickEquals(5) || this.animationTickEquals(20))
//        {
//            this.moveForward();
//        }
//        if (this.animationTickEquals(10) || this.animationTickEquals(20))
//        {
//            this.attackForward();
//        }
//        if (this.animationTickGreaterThan(40))
//        {
//            this.resetState();
//        }
//    }
//
//    public void attack1_R()
//    {
//        increaseAnimationTick();
//        if (this.animationTickEquals(10))
//            this.moveForward();
//        if (this.animationTickEquals(15))
//        {
//            this.hurtEntities();
//        }
//        if (this.animationTickGreaterThan(40))
//        {
//            this.resetState();
//        }
//    }
//
//    public void a02()
//    {
//        increaseAnimationTick();
//        if (this.animationTickEquals(5) || this.animationTickEquals(20))
//        {
//            this.moveForward();
//        }
//        if (this.animationTickEquals(10) || this.animationTickEquals(22))
//        {
//            this.attackForward();
//        }
//        if (this.animationTickGreaterThan(40))
//        {
//            this.resetState();
//        }
//    }
//
//    public void b01()
//    {
//        increaseAnimationTick();
//        if (this.tickCount % 5 == 0 && this.getAnimationTick() > 10 &&
//                this.getAnimationTick() < 55)
//        {
//            this.hurtEntities();
//        }
//        if (this.animationTickGreaterThan(85))
//        {
//            this.resetState();
//        }
//    }
//
//    public void b02()
//    {
//        increaseAnimationTick();
//        if (this.animationTickEquals(5))
//        {
//            this.moveBackward();
//        }
//        if (this.animationTickEquals(20))
//        {
//            this.attackForward();
//            this.moveForward();
//        }
//        if (this.animationTickEquals(25))
//        {
//            this.attackForward();
//        }
//        if (this.animationTickGreaterThan(40))
//        {
//            this.resetState();
//        }
//    }
//
//    public void b03()
//    {
//        increaseAnimationTick();
//        if (this.animationTickEquals(13))
//        {
//            this.attack(4d, 1.5d, 1.5d, 1.5d, 3d, 3d, 3d, entity -> {
//                this.heal(0.5f);
//            });
//        }
//        if (this.animationTickGreaterThan(30))
//        {
//            this.resetState();
//        }
//    }
//
//    public void c01()
//    {
//        increaseAnimationTick();
//        if (this.animationTickEquals(20))
//        {
//            this.hurtEntities();
//            this.jumpFromGround();
//            this.moveForward(1.5d);
//        }
//        if (this.animationTickEquals(32))
//        {
//            if (!this.level().isClientSide)
//            {
//                this.groundParticles();
//            }
//            this.hurtEntities(2d, 0.5d, 2d);
//        }
//        if (this.animationTickGreaterThan(80))
//        {
//            this.resetState();
//        }
//    }
//
//    public void attack3Tick()
//    {
//        increaseAnimationTick();
//        if (this.animationTickEquals(5))
//            this.moveForward();
//        if (this.animationTickEquals(10))
//            this.hurtEntities();
//        if (this.animationTickGreaterThan(40))
//            this.resetState();
//    }
//
//    public void attack4()
//    {
//        increaseAnimationTick();
//        if (this.animationTickEquals(10) || this.animationTickEquals(20)
//                || this.animationTickEquals(30) || this.animationTickEquals(40))
//            this.moveForward(1);
//        if (this.getAnimationTick() > 10 && this.getAnimationTick() < 55 && this.getAnimationTick() % 5 == 0)
//            this.hurtEntities();
//        if (this.animationTickGreaterThan(75))
//            this.resetState();
//    }
//
//    public void summonTick()
//    {
//        increaseAnimationTick();
//        if (this.animationTickGreaterThan(50))
//        {
//            this.resetState();
//        }
//    }
//
//    //TODO
//    public void pickFlags()
//    {
//        float rand = MathSupport.random.nextFloat();
//        if (rand < 0.25F)
//        {
//            this.setFlag(A01);
//        } else if (rand < 0.5F)
//        {
//            this.setFlag(A02);
//        } else if (rand < 0.75F)
//        {
//            this.setFlag(3);
//        }
//    }
//
//    public void moveForward(double pSpeed)
//    {
//        Vec3 vector = this.getLookAngle();
//        this.setDeltaMovement(vector.x * pSpeed, this.getDeltaMovement().y, vector.z * pSpeed);
//    }
//
//    public void moveBackward()
//    {
//        this.moveBackward(2);
//    }
//
//    public void moveBackward(double pSpeed)
//    {
//        this.moveForward(-pSpeed);
//    }
//
//    public void moveForward()
//    {
//        this.moveForward(2);
//    }
//
//    public void hurtEntities()
//    {
//        this.hurtEntities(3d, 3d, 3d);
//    }
//
//    public void hurtEntities(double x, double y, double z)
//    {
//        for (var entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(x, y, z)))
//        {
//            this.doHurtTarget(entity);
//        }
//    }
//
//    public void attackForward()
//    {
//        this.attackForward(null);
//    }
//
//    public void attackForward(Consumer<LivingEntity> consumer)
//    {
//        this.attack(2d, 2d, 2d, 2d, 2d, 2d, 2d, consumer);
//    }
//
//    public void groundParticles()
//    {
//        ParticleUtil.sendParticles((ServerLevel)this.level(), new BlockParticleOption(ParticleTypes.BLOCK,
//                        this.level().getBlockState(this.blockPosition().below())),
//                position(), 20,
//                0.9d, 0.1d, 0.9d,
//                MathSupport.random.nextDouble() * 0.2d);
//    }
//
//    public boolean isSecondPhase()
//    {
//        return this.entityData.get(DATA_SECOND_PHASE);
//    }
//
//    public void setSecondPhase(boolean flag)
//    {
//        this.entityData.set(DATA_SECOND_PHASE, flag);
//    }
//
//    public int getFlag()
//    {
//        return this.entityData.get(DATA_FLAGS);
//    }
//
//    public void setFlag(int flag)
//    {
//        this.entityData.set(DATA_FLAGS, flag);
//    }
//
//    public boolean isFlag(int flag)
//    {
//        return this.getFlag() == flag;
//    }
//
//    public int getAnimationTick()
//    {
//        return this.entityData.get(DATA_ANIMATION_TICK);
//    }
//
//    public void setAnimationTick(int tick)
//    {
//        this.entityData.set(DATA_ANIMATION_TICK, tick);
//    }
//
//    public void increaseAnimationTick()
//    {
//        this.setAnimationTick(this.getAnimationTick() + 1);
//    }
//
//    public boolean animationTickEquals(int tick)
//    {
//        return this.getAnimationTick() == tick;
//    }
//
//    public boolean animationTickGreaterThan(int tick)
//    {
//        return this.getAnimationTick() >= tick;
//    }
//
//    public void resetState()
//    {
//        this.setFlag(0);
//        this.setAnimationTick(0);
//    }
//
//    private final List<AnimationState> allAnimations
//            = List.of(a01, b01, b02, a02, b03);
//
//    private List<AnimationState> allAnimations()
//    {
//        return allAnimations;
//    }
//
//    public void stopAllAnimations()
//    {
//        allAnimations().forEach(AnimationState::stop);
//    }
//
//    public void addAdditionalSaveData(CompoundTag compound)
//    {
//        super.addAdditionalSaveData(compound);
//        compound.putBoolean("SecondPhase", this.isSecondPhase());
//        compound.putInt("BossFlag", this.getFlag());
//    }
//
//    /*@Nullable
//    @SuppressWarnings("deprecation")
//    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
//                                        MobSpawnType pReason,
//                                        @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag)
//    {
//        this.setFlag(4);
//        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
//    }*/
//
//    public boolean canAttack(LivingEntity target)
//    {
//        if (target == this) return false;
//        return super.canAttack(target);
//    }
//
//    public void attack(double dv, double x, double y, double z, double x1, double y1, double z1,
//                       Consumer<LivingEntity> consumer)
//    {
//        List<LivingEntity> livingEntities = this.level.getEntitiesOfClass(LivingEntity.class, getRange(this, dv, x, y, z, x1, y1, z1),
//                this::canAttack);
//        if (!livingEntities.isEmpty())
//        {
//            for (var livingEntity : livingEntities)
//            {
//                if (this.doHurtTarget(livingEntity) && consumer != null)
//                {
//                    consumer.accept(livingEntity);
//                }
//            }
//        }
//    }
//
//    //来自芙神
//    public static AABB getRange(Mob mob, double dv, double x, double y, double z, double x1, double y1, double z1)
//    {
//        float bodyYawRad = mob.yBodyRot * Mth.DEG_TO_RAD;
//        double dx = -Mth.sin(bodyYawRad) * dv;
//        double dz = Mth.cos(bodyYawRad) * dv;
//        Vec3 center = new Vec3(mob.getX() + dx, mob.getY() + mob.getBbHeight() * 0.5f,
//                mob.getZ() + dz);
//        return new AABB(center.x - x / 2, center.y - y / 2, center.z - z / 2,
//                center.x + x1 / 2, center.y + y1 / 2, center.z + z1 / 2);
//    }
//
//    static
//    {
//        TWO_SWORD = SynchedEntityData.defineId(SwordMan.class, EntityDataSerializers.BOOLEAN);
//        DATA_FLAGS = SynchedEntityData.defineId(SwordMan.class, EntityDataSerializers.INT);
//        DATA_ANIMATION_TICK = SynchedEntityData.defineId(SwordMan.class, EntityDataSerializers.INT);
//        DATA_SECOND_PHASE = SynchedEntityData.defineId(SwordMan.class, EntityDataSerializers.BOOLEAN);
//    }
//}
