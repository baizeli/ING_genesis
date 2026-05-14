
package miku.united_as_one.genesis.contents.entity;

import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.api.mobs.MobUtils;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.MeleeGoal;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Consumer;

public class Knight
extends ApiPathfinderMob
implements IFlagMob
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private static final EntityDataAccessor<Integer> DATA_ANI_TICK;
    public static final int FLAG_SWORD_DOUBLE_SLASH = 1;
    public static final int FLAG_SWORD_LUNGE = 2;
    public static final int FLAG_SWORD_SLASH_STAB = 3;
    public static final int FLAG_SWORD_TRIPLE_SLASH = 4;
    public static final int FLAG_SWORD_SINGLE_UPWARD = 5;
    public static final int FLAG_SWORD_SINGLE_HORIZONTAL = 6;
    public static final int FLAG_SWORD_SINGLE_HORIZONTAL_FAST = 7;
    public static final int FLAG_SWORD_STAB = 8;
    public static final int FLAG_SWORD_DEFLECT_1 = 9;
    public AnimationState sword_double_slash = new AnimationState();
    public AnimationState sword_lunge = new AnimationState();
    public AnimationState sword_slash_stab = new AnimationState();
    public AnimationState sword_triple_slash = new AnimationState();
    public AnimationState sword_single_upward = new AnimationState();
    public AnimationState sword_single_horizontal = new AnimationState();
    public AnimationState sword_single_horizontal_fast = new AnimationState();
    public AnimationState sword_stab = new AnimationState();
    public AnimationState sword_deflect_1 = new AnimationState();
    public Knight(EntityType<? extends Knight> entityType, Level level)
    {
        super(entityType, level);
        this.setMainHandItem(ItemRegistry.LEGIONNAIRE_FLAMBERGE.get());
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
        this.entityData.define(DATA_ANI_TICK, 0);
    }

    public void aiStep()
    {
        super.aiStep();
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new MeleeGoal(this, 1.0D));
        OwnableMob.addBehaviorGoals(this, 5, 0.8D, 10F, false, true);
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, true));
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key)
    {
        if (key.equals(DATA_FLAGS)) {
            int flag = this.getFlag();
            if (this.level().isClientSide)
            {
                if (flag > 0)
                {
                    stopAllAnims();
                }
                switch (flag)
                {
                    case 0:{
                        break;
                    }
                    case FLAG_SWORD_DOUBLE_SLASH:{
                        this.sword_double_slash.startIfStopped(this.tickCount);
                        break;
                    }
                    case FLAG_SWORD_LUNGE:{
                        this.sword_lunge.startIfStopped(this.tickCount);
                        break;
                    }
                    case FLAG_SWORD_SLASH_STAB:{
                        this.sword_slash_stab.startIfStopped(this.tickCount);
                        break;
                    }
                    case FLAG_SWORD_TRIPLE_SLASH:{
                        this.sword_triple_slash.startIfStopped(this.tickCount);
                        break;
                    }
                    case FLAG_SWORD_SINGLE_UPWARD:{
                        this.sword_single_upward.startIfStopped(this.tickCount);
                        break;
                    }
                    case FLAG_SWORD_SINGLE_HORIZONTAL:{
                        this.sword_single_horizontal.startIfStopped(this.tickCount);
                        break;
                    }
                    case FLAG_SWORD_SINGLE_HORIZONTAL_FAST:{
                        this.sword_single_horizontal_fast.startIfStopped(this.tickCount);
                        break;
                    }
                    case FLAG_SWORD_STAB:{
                        this.sword_stab.startIfStopped(this.tickCount);
                        break;
                    }
                    case FLAG_SWORD_DEFLECT_1:{
                        this.sword_deflect_1.startIfStopped(this.tickCount);
                        break;
                    }
                    default:{
                        LOGGER.warn("Unknown flag value: {}", flag);
                        break;
                    }
                }
            } else {
                if (this.getFlag() < 0 || this.getFlag() > FLAG_SWORD_DEFLECT_1)
                {
                    this.setFlag(0);
                }
            }
        }
        super.onSyncedDataUpdated(key);
    }

    public void sword_double_slash()
    {
        increaseAniTick();
        if (this.aniTickEquals(14))
        {
            this.attackForward();
        }
        if (this.aniTickEquals(30))
        {
            this.attackForward();
        }
        if (this.aniTickEquals(44))
        {
            this.resetState();
        }
    }

    public void sword_lunge()
    {
        increaseAniTick();
        if (this.aniTickEquals(64))
        {
            this.moveForward(1.5);
            this.attackForward(15.0F, this.damageSources().mobAttack(this), entity ->
                    this.heal(2.0F));
        }
        if (this.aniTickEquals((int)Maths.toTick(3.8F)))
        {
            this.resetState();
        }
    }

    public void sword_slash_stab()
    {
        increaseAniTick();
        if (this.aniTickEquals((int)Maths.toTick(0.72F)))
        {
            this.attackForward(12.0F, this.damageSources().mobAttack(this));
        }
        if (this.aniTickEquals((int)Maths.toTick(1.76F)))
        {
            this.attackForward();
        }
        if (this.aniTickEquals((int)Maths.toTick(1.88F)))
        {
            this.resetState();
        }
    }

    public void sword_triple_slash()
    {
        increaseAniTick();
        if (this.aniTickEquals((int)Maths.toTick(0.64F)))
        {
            this.attackForward(17.9F, this.damageSources().mobAttack(this));
        }
        if (this.aniTickEquals((int)Maths.toTick(2.88F)))
        {
            this.attackForward();
        }
        if (this.aniTickEquals((int)Maths.toTick(3.28F)))
        {
            this.resetState();
        }
    }

    public void sword_single_upward()
    {
        increaseAniTick();
        if (this.aniTickEquals((int)Maths.toTick(0.92F)))
        {
            this.attackForward(12.0F, this.damageSources().mobAttack(this));
        }
        if (this.aniTickEquals((int)Maths.toTick(1.28F)))
        {
            this.resetState();
        }
    }

    public void sword_single_horizontal()
    {
        increaseAniTick();
        if (this.aniTickEquals((int)Maths.toTick(0.76F)))
        {
            this.attackForward(14.9F, this.damageSources().mobAttack(this));
        }
        if (this.aniTickEquals((int)Maths.toTick(1.4F)))
        {
            this.resetState();
        }
    }

    public void sword_single_horizontal_fast()
    {
        increaseAniTick();
        if (this.aniTickEquals((int)Maths.toTick(0.68F)))
        {
            this.attackForward(16.9F, this.damageSources().mobAttack(this));
        }
        if (this.aniTickEquals((int)Maths.toTick(1.2F)))
        {
            this.resetState();
        }
    }

    public void sword_stab()
    {
        increaseAniTick();
        if (this.aniTickEquals((int)Maths.toTick(0.72F)))
        {
            this.attackForward(18.0F, this.damageSources().mobAttack(this));
        }
        if (this.aniTickEquals((int)Maths.toTick(1.08F)))
        {
            this.resetState();
        }
    }

    public void sword_deflect_1()
    {
        increaseAniTick();
        if (this.aniTickEquals((int)Maths.toTick(0.8F)))
        {
            this.hurtEntities();
        }
        if (this.aniTickEquals((int)Maths.toTick(1.6F)))
        {
            this.resetState();
        }
    }

    public void moveForward(double pSpeed)
    {
        Vec3 vector = this.getLookAngle();
        this.setDeltaMovement(vector.x * pSpeed, this.getDeltaMovement().y, vector.z * pSpeed);
    }

    public void moveBackward()
    {
        this.moveBackward(2);
    }

    public void moveBackward(double pSpeed)
    {
        this.moveForward(-pSpeed);
    }

    public void moveForward()
    {
        this.moveForward(2);
    }

    public void hurtEntities()
    {
        this.hurtEntities(3d, 3d, 3d);
    }

    public void hurtEntities(double x, double y, double z)
    {
        for (var entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(x, y, z)))
        {
            this.doHurtTarget(entity);
        }
    }

    public void attackForward()
    {
        this.attackForward(null);
    }

    public void attackForward(Consumer<LivingEntity> consumer)
    {
        this.attack(2d, 2d, 2d, 2d, 2d, 2d, 2d, consumer);
    }

    public void attackForward(float pDamage, DamageSource pSource)
    {
        this.attackForward(null);
    }

    public void attackForward(float pDamage, DamageSource pSource, Consumer<LivingEntity> consumer)
    {
        this.attack(2d, 2d, 2d, 2d, 2d, 2d, 2d, pDamage, pSource, consumer);
    }

    public void attack(double dv, double x, double y, double z, double x1, double y1, double z1,
                       Consumer<LivingEntity> consumer)
    {
        List<LivingEntity> livingEntities = this.level.getEntitiesOfClass(LivingEntity.class, getRange(this, dv, x, y, z, x1, y1, z1),
                this::canAttack);
        if (!livingEntities.isEmpty())
        {
            for (var livingEntity : livingEntities)
            {
                if (this.doHurtTarget(livingEntity) && consumer != null)
                {
                    consumer.accept(livingEntity);
                }
            }
        }
    }

    public void attack(double dv, double x, double y, double z, double x1, double y1, double z1,
                       float pDamage, DamageSource pSource, Consumer<LivingEntity> consumer)
    {
        List<LivingEntity> livingEntities = this.level.getEntitiesOfClass(LivingEntity.class, getRange(this, dv, x, y, z, x1, y1, z1),
                this::canAttack);
        if (!livingEntities.isEmpty())
        {
            for (var livingEntity : livingEntities)
            {
                if (livingEntity.hurt(pSource, pDamage) && consumer != null)
                {
                    consumer.accept(livingEntity);
                }
            }
        }
    }

    public int getFlag()
    {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int flag)
    {
        this.entityData.set(DATA_FLAGS, flag);
    }

    public int getAniTick()
    {
        return this.entityData.get(DATA_ANI_TICK);
    }

    public void setAniTick(int aniTick)
    {
        this.entityData.set(DATA_ANI_TICK, aniTick);
    }

    private List<AnimationState> allAnims = null;

    private List<AnimationState> getAllAnimationStates()
    {
        if (allAnims == null)
            allAnims =
                    List.of(
                            sword_double_slash,
                            sword_lunge,
                            sword_slash_stab,
                            sword_triple_slash,
                            sword_single_upward,
                            sword_single_horizontal,
                            sword_single_horizontal_fast,
                            sword_stab,
                            sword_deflect_1
                    );
        return allAnims;
    }

    public void stopAllAnims()
    {
        for (AnimationState anim : getAllAnimationStates())
        {
            anim.stop();
        }
    }

    public boolean canAttack(LivingEntity target)
    {
        if (target == this) return false;
        return super.canAttack(target);
    }

    //来自芙神
    public static AABB getRange(Mob mob, double dv, double x, double y, double z, double x1, double y1, double z1)
    {
        float bodyYawRad = mob.yBodyRot * Mth.DEG_TO_RAD;
        double dx = -Mth.sin(bodyYawRad) * dv;
        double dz = Mth.cos(bodyYawRad) * dv;
        Vec3 center = new Vec3(mob.getX() + dx, mob.getY() + mob.getBbHeight() * 0.5f,
                mob.getZ() + dz);
        return new AABB(center.x - x / 2, center.y - y / 2, center.z - z / 2,
                center.x + x1 / 2, center.y + y1 / 2, center.z + z1 / 2);
    }

    static
    {
        DATA_FLAGS = SynchedEntityData.defineId(Knight.class, EntityDataSerializers.INT);
        DATA_ANI_TICK = SynchedEntityData.defineId(Knight.class, EntityDataSerializers.INT);
    }
}
