package com.baizeli.eternisstarrysky.Entity;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.sound.SoundsRegister;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class SwordManCsdy extends BossEntity implements GeoEntity {

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.model.stand");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.model.walk");

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        if (name != null && name.getString().contains("沉睡的艺") && !isReal()) {
            this.setHealth(this.getMaxHealth());
            setReal(true);
        }
    }

    private CsdyMeleeGoal meleeGoal; // 持有对近战Goal的引用
    private int attackBehaviorCooldown = 0; // 控制攻击行为（动画+伤害）的整体冷却
    private AnimationController<SwordManCsdy> mainAnimationController; // 主动画控制器

    private transient Object clientBossMusicInstance; // transient 防止序列化，客户端专用
    private boolean musicStarted = false;

    public boolean isDead;
    private float oldHealth;
    private float lastHealth;
    private int updateTimer;
    private RemovalReason oldRemovalReason;
    private boolean damageTooHigh;

    private static final EntityDataAccessor<Float> DATA_HEALTH_ID = getHealthDataAccessor();

    private static EntityDataAccessor<Float> getHealthDataAccessor() {
        for (String fieldName : new String[]{"DATA_HEALTH_ID", "f_20961_", "health"}) {
            try {
                Field field = LivingEntity.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(null);
                if (value instanceof EntityDataAccessor) {
                    return (EntityDataAccessor<Float>) value;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    private final ServerBossEvent bossEvent;
    public SwordManCsdy(EntityType<? extends BossEntity> type, Level level) {
        super(type, level);
        this.entityData.set(DATA_HEALTH_ID,this.getMaxHealth());
        this.setMaxUpStep(0.6F);
        this.xpReward = 0;
        this.setPersistenceRequired();
        this.oldHealth = this.getHealth();
        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(
                this.getDisplayName(),
                BossEvent.BossBarColor.PURPLE, // 血条颜色
                BossEvent.BossBarOverlay.PROGRESS // 血条样式
        )).setDarkenScreen(true); // 是否使屏幕变暗
        this.hasTrail = true;
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ATTACKING, false);
        this.entityData.define(DATA_IS_REAL, false);
    }

    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING =
            SynchedEntityData.defineId(SwordManCsdy.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_REAL =
            SynchedEntityData.defineId(SwordManCsdy.class, EntityDataSerializers.BOOLEAN);

    public boolean isAttacking() {
        return this.entityData.get(DATA_IS_ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(DATA_IS_ATTACKING, attacking);
    }

    public boolean isReal() {
        return this.entityData.get(DATA_IS_REAL);
    }

    public void setReal(boolean real) {
        this.entityData.set(DATA_IS_REAL, real);
    }

    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(EternisStarrySky.MODID, "entities/sword_man_csdy");

    @Override
    protected @NotNull ResourceLocation getDefaultLootTable() {
        return LOOT_TABLE;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void tick() {
        super.tick();

         if (!this.level().isClientSide && this.bossEvent != null) {
             this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
         }

        if (!this.level().isClientSide && this.tickCount % 20 == 0) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            serverLevel.setWeatherParameters(0, 400, true, true);
        }
        this.invulnerableTime = 0;

    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        // 确保当Boss死亡或被移除时，血条也从所有玩家屏幕上消失
        this.bossEvent.removeAllPlayers();
    }

    @Override
    public SoundEvent getBossMusic() {
        return SoundsRegister.GIRL_A.get();
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float damage) {
        if (isReal()) return false;

        // 计算开方后的伤害
        float sqrtDamage = (float) Math.sqrt(damage);
        // 减少80%（即只保留20%）
        float realDamage = sqrtDamage * 0.2f;

        if (realDamage < 100f) {
            return false;
        }
        teleportToAttacker(source);
        return super.hurt(source, realDamage); // 传递处理后的伤害值
    }

    private void teleportToAttacker(DamageSource source) {
        Entity attacker = source.getEntity();
        if (attacker == null || attacker == this) return;

        double x = attacker.getX() + (random.nextDouble() - 0.5) * 1.2;
        double y = attacker.getY() + random.nextInt(2);
        double z = attacker.getZ() + (random.nextDouble() - 0.5) * 1.2;

        this.teleportTo(x,y,z);
    }

    @Override
    public void setHealth(float value) {
        if (isReal()) return;

        float currentHealth = this.getHealth();
        float healthLoss = currentHealth - value;

        // 对血量损失进行同样的处理：开方后减80%
        float processedHealthLoss = (float) (Math.sqrt(healthLoss) * 0.2f);

        float threshold = 325.0f;
        if (processedHealthLoss > threshold) {
            value = currentHealth - processedHealthLoss * 0.2f;
        }

        super.setHealth(value);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        // 行为选择器 (goalSelector)
        this.goalSelector.addGoal(0, new CsdyMeleeGoal(this, 1.0D, false)); // 2: 近战攻击
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));

        // 目标选择器 (targetSelector)
        this.targetSelector.addGoal(1, new PersistentHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
    }

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::mainAnimController));
    }

    private PlayState mainAnimController(AnimationState<SwordManCsdy> state) {
        if (state.isMoving() || this.getTarget() != null || isAttacking()) {
            return state.setAndContinue(WALK_ANIM);
        }
        return state.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 4.4);
        builder = builder.add(Attributes.MAX_HEALTH, 10000.0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 1600.0);
        builder = builder.add(Attributes.ATTACK_SPEED, 20.0);
        builder = builder.add(Attributes.FOLLOW_RANGE, 128);
        return builder;
    }


    //trail
    private Vec3[][] trailPositions = new Vec3[64][2];
    private int trailPointer = -1;
    public boolean hasTrail = false;
    
    // 为第二个刀光添加独立的轨迹数据
    private Vec3[][] trailPositions2 = new Vec3[64][2];
    private int trailPointer2 = -1;

    public Vec3[] getTrailPosition(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3[] d0 = this.trailPositions[j];
        Vec3 t0 = this.trailPositions[i][0].subtract(d0[0]);
        Vec3 t1 = this.trailPositions[i][1].subtract(d0[1]);
        Vec3[] d1 = new Vec3[]{t0,t1};
        Vec3 tt0 = d0[0].add(d1[0].scale(partialTick));
        Vec3 tt1 = d0[1].add(d1[1].scale(partialTick));
        Vec3[] d2 = new Vec3[]{tt1,tt0};

        return d2;
    }
    
    // 获取第二个刀光的轨迹位置
    public Vec3[] getTrailPosition2(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }
        if (trailPointer2 == -1) {
            return new Vec3[]{Vec3.ZERO, Vec3.ZERO};
        }
        
        int i = this.trailPointer2 - pointer & 63;
        int j = this.trailPointer2 - pointer - 1 & 63;
        Vec3[] d0 = this.trailPositions2[j];
        Vec3 t0 = this.trailPositions2[i][0].subtract(d0[0]);
        Vec3 t1 = this.trailPositions2[i][1].subtract(d0[1]);
        Vec3[] d1 = new Vec3[]{t0,t1};
        Vec3 tt0 = d0[0].add(d1[0].scale(partialTick));
        Vec3 tt1 = d0[1].add(d1[1].scale(partialTick));
        Vec3[] d2 = new Vec3[]{tt1,tt0};

        return d2;
    }

    public void updateTrail(Vec3 trailAt1,Vec3 trailAt2) {
        if (trailPointer == -1) {
            Vec3 backAt1 = trailAt1;
            Vec3 backAt2 = trailAt2;
            for (int i = 0; i < trailPositions.length; i++) {
                trailPositions[i] = new Vec3[]{backAt1,backAt2};
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = new Vec3[]{trailAt1,trailAt2};
    }

    public void updateTrail2(Vec3 trailAt1, Vec3 trailAt2) {
        // 使用独立的轨迹数据
        if (trailPointer2 == -1) {
            Vec3 backAt1 = trailAt1;
            Vec3 backAt2 = trailAt2;
            for (int i = 0; i < trailPositions2.length; i++) {
                trailPositions2[i] = new Vec3[]{backAt1, backAt2};
            }
        }
        if (++this.trailPointer2 == this.trailPositions2.length) {
            this.trailPointer2 = 0;
        }
        this.trailPositions2[this.trailPointer2] = new Vec3[]{trailAt1, trailAt2};
    }

    public boolean hasTrail() {
        return trailPointer != -1&&hasTrail;
    }
    
    // 检查是否有第二个刀光轨迹
    public boolean hasTrail2() {
        return trailPointer2 != -1;
    }
}
