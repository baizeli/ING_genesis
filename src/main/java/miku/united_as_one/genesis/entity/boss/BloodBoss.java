package miku.united_as_one.genesis.entity.boss;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import io.redspace.ironsspellbooks.api.config.IronConfigParameters;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BloodBoss extends Monster implements GeoEntity, Enemy, IAnimatedAttacker, IEntityAdditionalSpawnData, IClientEventEntity, IMagicEntity {
    private static final Logger BLOOD_BOSS_LOGGER = LogUtils.getLogger();

    // 魔法相关字段
    private static final EntityDataAccessor<Boolean> DATA_CANCEL_CAST = SynchedEntityData.defineId(BloodBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_DRINKING_POTION = SynchedEntityData.defineId(BloodBoss.class, EntityDataSerializers.BOOLEAN);
    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E"), "Drinking speed penalty", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);

    private final MagicData magicData = new MagicData(true);
    @Nullable
    private SpellData castingSpell;
    private int drinkTime;
    private boolean hasUsedSingleAttack;
    private boolean recreateSpell;

    int spawnTimer;
    private final AnimationController<BloodBoss> skillAnimationController;
    RawAnimation animationToPlay;
    private final AnimationController<BloodBoss> animationControllerWalk;
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);

    private AbstractSpell lastCastSpellType = SpellRegistry.none();
    private AbstractSpell instantCastSpellType = SpellRegistry.none();
    private boolean cancelCastAnimation = false;

    // 新增动画控制器
    private final AnimationController<BloodBoss> instantCastController;
    private final AnimationController<BloodBoss> longCastController;
    private final AnimationController<BloodBoss> continuousCastController;

    //用于延迟释放瞬时法术
    private int delayedCastTick = -1;
    private AbstractSpell delayedSpell;
    private int delayedSpellLevel;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("待机");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("行走循环");
    private static final RawAnimation CAST_IDLE = RawAnimation.begin().thenLoop("施法待机");
    private static final RawAnimation CAST_WALK = RawAnimation.begin().thenLoop("施法行走循环");

    // 施法缓冲时间（20 ticks = 1秒），你可以根据动作的收招长度调整
    private static final int CASTING_POST_DELAY = 20;
    private int lastCastTick = -100; // 初始化为一个较小的值，防止刚生成时触发

    public BloodBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new BloodBossMoveControl(this);
//        this.lookControl = new BloodBossLookControl(this);
//        this.jumpControl = new BloodBossJumpControl(this);

        this.animationControllerWalk = new AnimationController<>(this, "walk_controller", 10, this::walkPredicate);
        this.skillAnimationController = new AnimationController<>(this, "skill_animation_controller", 5, this::animationPredicate);


        this.instantCastController = new AnimationController<>(this, "instant_cast", 5, this::instantCastingPredicate);
        this.longCastController = new AnimationController<>(this, "long_cast", 5, this::longCastingPredicate);
        this.continuousCastController = new AnimationController<>(this, "continuous_cast", 5, this::continuousCastingPredicate);


        // 初始化魔法数据
        this.magicData.setSyncedData(new SyncedSpellData(this));
        this.noCulling = true;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 950)
                .add(Attributes.MOVEMENT_SPEED, 0.21)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.ARMOR, 20)
                .add(AttributeRegistry.MAX_MANA.get(), 50000.0)
                .add(ForgeMod.ENTITY_GRAVITY.get(), 0.03)
                .add(ForgeMod.ENTITY_REACH.get(), 3.0)
                .add(Attributes.KNOCKBACK_RESISTANCE,5.0)
                .add(AttributeRegistry.SPELL_POWER.get(), 1.25);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CANCEL_CAST, false);
        this.entityData.define(DATA_DRINKING_POTION, false);
    }

    //================================================================ 魔法/法术 ========================================================================

    // IMagicEntity 接口实现
    @Override
    public MagicData getMagicData() {
        return this.magicData;
    }

    @Override
    public void setSyncedSpellData(SyncedSpellData syncedSpellData) {
        if (this.level.isClientSide) {
            boolean isCasting = this.magicData.isCasting();
            this.magicData.setSyncedData(syncedSpellData);
            this.castingSpell = this.magicData.getCastingSpell();
            if (this.castingSpell != null) {
                if (!this.magicData.isCasting() && isCasting) {
                    this.castComplete();
                } else if (this.magicData.isCasting() && !isCasting) {
                    AbstractSpell spell = this.magicData.getCastingSpell().getSpell();
                    this.initiateCastSpell(spell, this.magicData.getCastingSpellLevel());
                    if (this.castingSpell.getSpell().getCastType() == CastType.INSTANT) {
                        this.castingSpell.getSpell().onClientPreCast(this.level, this.castingSpell.getLevel(), this, InteractionHand.MAIN_HAND, this.magicData);
                        this.castComplete();
                    }
                }
            }
        }
    }

    @Override
    public boolean isCasting() {
        return this.magicData.isCasting();
    }

    @Override
    public void cancelCast() {
        if (this.isCasting()) {
            if (this.level.isClientSide) {
                // 客户端取消动画逻辑
            } else {
                this.entityData.set(DATA_CANCEL_CAST, !this.entityData.get(DATA_CANCEL_CAST));
            }
            this.castComplete();
        }
    }

    @Override
    public void castComplete() {
        if (!this.level.isClientSide) {
            if (this.castingSpell != null) {
                this.castingSpell.getSpell().onServerCastComplete(this.level, this.castingSpell.getLevel(), this, this.magicData, false);
            }
        } else {
            this.magicData.resetCastingState();
        }
        this.castingSpell = null;

    }

    @Override
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        if (spell == SpellRegistry.none()) {
            this.castingSpell = null;
        } else {
            if (this.level.isClientSide) {
                this.cancelCastAnimation = false;
                this.lastCastTick = this.tickCount;
            }

            this.castingSpell = new SpellData(spell, spellLevel);

            // 施法开始时立即看向目标
            if (this.getTarget() != null) {
                this.forceLookAtTarget(this.getTarget());
            }

            if (!this.level().isClientSide && !spell.checkPreCastConditions(this.level(), spellLevel, this, this.magicData)) {
                this.castingSpell = null;
            } else {
                // 特殊法术处理（传送/位移类）
                if (spell != SpellRegistry.TELEPORT_SPELL.get() && spell != SpellRegistry.FROST_STEP_SPELL.get()) {
                    if (spell == SpellRegistry.BLOOD_STEP_SPELL.get()) {
                        this.setTeleportLocationBehindTarget(3);
                    } else if (spell == SpellRegistry.BURNING_DASH_SPELL.get()) {
                        this.setBurningDashDirectionData();
                    }
                } else {
                    this.setTeleportLocationBehindTarget(10);
                }

                this.magicData.initiateCast(spell, spellLevel,
                        spell.getEffectiveCastTime(spellLevel, this), CastSource.MOB,
                        SpellSelectionManager.MAINHAND);

                // 处理瞬时施法逻辑
                if (spell.getCastType() == CastType.INSTANT) {
                    this.instantCastSpellType = spell;
                    if (this.level().isClientSide) {
                        spell.onClientPreCast(this.level(), spellLevel, this, InteractionHand.MAIN_HAND, this.magicData);
                        this.castComplete();
                    } else {
                        // --- 核心修改：设置延迟释放 ---
                        this.delayedCastTick = 10;
                        this.delayedSpell = spell;
                        this.delayedSpellLevel = spellLevel;
                        // 注意：此处不调用 castComplete()，直到延迟结束
                    }
                } else {
                    if (!this.level().isClientSide) {
                        spell.onServerPreCast(this.level(), spellLevel, this, this.magicData);
                    }
                }
            }
        }
    }
    //================================================================ 动画 ========================================================================

    // 动画相关方法
    public void playAnimation(String animationId) {
        this.animationToPlay = RawAnimation.begin().thenPlay(animationId);
    }

    private PlayState instantCastingPredicate(AnimationState<BloodBoss> event) {
        if (this.cancelCastAnimation) {
            return PlayState.STOP;
        }

        AnimationController<BloodBoss> controller = event.getController();
        if (this.instantCastSpellType != SpellRegistry.none() &&
                controller.getAnimationState() == AnimationController.State.STOPPED) {

            // 设置瞬时施法动画
            this.setStartAnimationFromSpell(controller, this.instantCastSpellType);
            this.instantCastSpellType = SpellRegistry.none();
        }

        return PlayState.CONTINUE;
    }

    private PlayState longCastingPredicate(AnimationState<BloodBoss> event) {
        AnimationController<BloodBoss> controller = event.getController();

        if (this.cancelCastAnimation ||
                (controller.getAnimationState() == AnimationController.State.STOPPED &&
                        (!this.isCasting() || this.castingSpell == null ||
                                this.castingSpell.getSpell().getCastType() != CastType.LONG))) {
            return PlayState.STOP;
        }

        if (this.isCasting()) {
            if (controller.getAnimationState() == AnimationController.State.STOPPED) {
                this.setStartAnimationFromSpell(controller, this.castingSpell.getSpell());
            }
        } else if (this.lastCastSpellType.getCastType() == CastType.LONG) {
            this.setFinishAnimationFromSpell(controller, this.lastCastSpellType);
        }

        return PlayState.CONTINUE;
    }

    private PlayState continuousCastingPredicate(AnimationState<BloodBoss> event) {
        if (this.cancelCastAnimation) {
            return PlayState.STOP;
        }

        AnimationController<BloodBoss> controller = event.getController();
        if (this.isCasting() && this.castingSpell != null &&
                controller.getAnimationState() == AnimationController.State.STOPPED) {

            if (this.castingSpell.getSpell().getCastType() == CastType.CONTINUOUS) {
                this.setStartAnimationFromSpell(controller, this.castingSpell.getSpell());
            }
            return PlayState.CONTINUE;
        }

        return this.isCasting() ? PlayState.CONTINUE : PlayState.STOP;
    }


    private void setStartAnimationFromSpell(AnimationController<BloodBoss> controller, AbstractSpell spell) {
        spell.getCastStartAnimation().getForMob().ifPresentOrElse(animationBuilder -> {
            controller.forceAnimationReset();
            controller.setAnimation(animationBuilder);
            this.lastCastSpellType = spell;
            this.cancelCastAnimation = false;
        }, () -> {
            this.cancelCastAnimation = true;
        });
    }

    private void setFinishAnimationFromSpell(AnimationController<BloodBoss> controller, AbstractSpell spell) {
        if (spell.getCastFinishAnimation().isPass) {
            // 如果没有收招动画，不要直接停止，给它一个缓冲时间
            this.cancelCastAnimation = false;
        } else {
            spell.getCastFinishAnimation().getForMob().ifPresentOrElse(animationBuilder -> {
                controller.forceAnimationReset();
                // 设置一个更长的收招过渡
                controller.transitionLength(8);
                controller.setAnimation(animationBuilder);
                this.lastCastSpellType = SpellRegistry.none();
            }, () -> {
                // 如果没配置动画，手动平滑淡出
                this.cancelCastAnimation = true;
            });
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(animationControllerWalk);
        controllerRegistrar.add(skillAnimationController);
        controllerRegistrar.add(instantCastController);
        controllerRegistrar.add(longCastController);
        controllerRegistrar.add(continuousCastController);
    }
    private PlayState walkPredicate(AnimationState<BloodBoss> state) {
        double horizontalSpeed = this.getDeltaMovement().horizontalDistance();

        if (state.isMoving()) {
            double speedMultiplier = (horizontalSpeed / 0.053) * 1.5;
            animationControllerWalk.setAnimationSpeed(Math.max(0.5, speedMultiplier));
        } else {
            animationControllerWalk.setAnimationSpeed(1.0);
        }


        if (this.isCasting()) {
            this.lastCastTick = this.tickCount;
        }

        boolean isRecentlyCasting = (this.tickCount - this.lastCastTick) < CASTING_POST_DELAY;

        RawAnimation target;


        if (this.isCasting() || isRecentlyCasting) {
            target = state.isMoving() ? CAST_WALK : CAST_IDLE;
        } else {
            target = state.isMoving() ? WALK : IDLE;
        }

        return state.setAndContinue(target);
    }

    private PlayState animationPredicate(AnimationState<BloodBoss> animationEvent) {
        AnimationController<BloodBoss> controller = animationEvent.getController();
        if (this.animationToPlay != null) {
            controller.forceAnimationReset();
            controller.setAnimation(this.animationToPlay);
            this.animationToPlay = null;
        }
        return PlayState.CONTINUE;
    }

    //================================================================ AI ========================================================================

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    @Override
    public Brain<BloodBoss> getBrain() {
        return (Brain<BloodBoss>) super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return BloodBossAi.makeBrain(this, dynamic);
    }

    @Override
    protected void customServerAiStep() {
        // 先调用父类逻辑
        super.customServerAiStep();

        // 处理法术重现
        if (this.recreateSpell) {
            this.recreateSpell = false;
            SyncedSpellData syncedSpellData = this.magicData.getSyncedData();
            AbstractSpell spell = SpellRegistry.getSpell(syncedSpellData.getCastingSpellId());
            this.initiateCastSpell(spell, syncedSpellData.getCastingSpellLevel());
        }

        // --- 核心修改：处理瞬时法术的延迟释放 ---
        if (this.delayedCastTick > 0) {
            this.delayedCastTick--;
            // 延迟期间持续看向目标以保证指向性法术精度
            if (this.getTarget() != null) {
                this.forceLookAtTarget(this.getTarget());
            }

            if (this.delayedCastTick == 0) {
                if (this.delayedSpell != SpellRegistry.none()) {
                    // 真正执行法术效果
                    this.delayedSpell.onCast(this.level(), this.delayedSpellLevel, this, CastSource.MOB, this.magicData);
                    this.castComplete();
                    this.delayedSpell = SpellRegistry.none();
                }
            }
        }

        // 处理药水饮用
        if (this.isDrinkingPotion()) {
            if (this.drinkTime-- <= 0) {
                this.finishDrinkingPotion();
            } else if (this.drinkTime % 4 == 0 && !this.isSilent()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.GENERIC_DRINK, this.getSoundSource(), 1.0F,
                        Utils.random.nextFloat() * 0.1F + 0.9F);
            }
        }

        // 安全检查：配置是否加载
        if (!isSpellConfigLoaded()) {
            updateStage();
            return;
        }

        // 处理非瞬时施法（持续或长法术）状态
        if (this.castingSpell != null && this.castingSpell.getSpell().getCastType() != CastType.INSTANT) {
            this.magicData.handleCastDuration();

            if (this.magicData.isCasting()) {
                this.castingSpell.getSpell().onServerCastTick(this.level(), this.castingSpell.getLevel(), this, this.magicData);
            }

            this.forceLookAtTarget(this.getTarget());

            if (this.magicData.getCastDurationRemaining() <= 0) {
                CastType castType = this.castingSpell.getSpell().getCastType();
                if (castType == CastType.LONG) {
                    this.castingSpell.getSpell().onCast(this.level(), this.castingSpell.getLevel(), this, CastSource.MOB, this.magicData);
                }
                this.castComplete();
            } else if (this.castingSpell.getSpell().getCastType() == CastType.CONTINUOUS &&
                    (this.magicData.getCastDurationRemaining() + 1) % 10 == 0) {
                this.castingSpell.getSpell().onCast(this.level(), this.castingSpell.getLevel(), this, CastSource.MOB, this.magicData);
            }
        }

        // AI 脑部逻辑
        ServerLevel serverlevel = (ServerLevel) this.level();
        serverlevel.getProfiler().push("BloodBossBrain");
        this.getBrain().tick(serverlevel, this);
        serverlevel.getProfiler().pop();
        BloodBossAi.updateActivity(this);

        if (this.tickCount % 40 == 0) {
            printLog();
        }

        updateStage();
    }

    private void forceLookAtTarget(@Nullable LivingEntity target) {
        if (target != null) {
            double d0 = target.getX() - this.getX();
            double d2 = target.getZ() - this.getZ();
            double d1 = target.getEyeY() - this.getEyeY();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float f = (float)(Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F;
            float f1 = (float)(-(Mth.atan2(d1, d3) * 57.2957763671875));
            this.setXRot(f1);
            this.setYRot(f);
        }
    }

    public boolean isSpellConfigLoaded() {
        try {
            // 添加多层安全检查
            if (SpellConfigManager.INSTANCE == null) {
                return false;
            }
            SpellConfigManager.getSpellConfigValue(SpellRegistry.none(), IronConfigParameters.ENABLED);
            return true;
        } catch (NullPointerException e) {
            BLOOD_BOSS_LOGGER.warn("法术配置未加载，跳过AI逻辑");
            return false;
        } catch (Exception e) {
            BLOOD_BOSS_LOGGER.error("检查法术配置时发生错误", e);
            return false;
        }
    }

    public void updateStage() {
        // 自定义阶段更新逻辑
    }

    private void printLog() {
        BLOOD_BOSS_LOGGER.debug("==============开始打印bloodBoss信息=================");
        BLOOD_BOSS_LOGGER.debug("当前activity: {}", this.getBrain().getActiveActivities());
        
        // 打印当前攻击目标实体id
        if (this.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(target -> {
                BLOOD_BOSS_LOGGER.debug("攻击目标: {}", target.getType().getDescription().getString());
            });
        } else {
            BLOOD_BOSS_LOGGER.debug("攻击目标: 无");
        }
        
        // 打印剩余法力值
        BLOOD_BOSS_LOGGER.debug("剩余法力值: {}", this.getMagicData().getMana());

        BLOOD_BOSS_LOGGER.debug("坐标: {}", this.blockPosition());
        BLOOD_BOSS_LOGGER.debug("移动: {}", this.getDeltaMovement());
        BLOOD_BOSS_LOGGER.debug("是否在地上: {}", this.onGround());
        
        // 打印当前阶段
        if (this.getBrain().hasMemoryValue(ModMemoryModuleType.BOSS_STAGE.get())) {
            this.getBrain().getMemory(ModMemoryModuleType.BOSS_STAGE.get()).ifPresent(stage -> {
                BLOOD_BOSS_LOGGER.debug("当前阶段: {}", stage);
            });
        } else {
            BLOOD_BOSS_LOGGER.debug("当前阶段: 无");
        }
        
        // 检查移动目标记忆
        if (this.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            this.getBrain().getMemory(MemoryModuleType.WALK_TARGET).ifPresent(walkTarget -> {
                BLOOD_BOSS_LOGGER.debug("Walk target: {}", walkTarget.getTarget().currentBlockPosition());
            });
        } else {
            BLOOD_BOSS_LOGGER.debug("Walk target: 无");
        }
        
        // 打印实体类型计数
        if (this.getBrain().hasMemoryValue(ModMemoryModuleType.ENTITY_TYPE_COUNT.get())) {
            this.getBrain()
                    .getMemory(ModMemoryModuleType.ENTITY_TYPE_COUNT.get())
                    .ifPresent(entityTypeCount -> {
                        BLOOD_BOSS_LOGGER.debug(
                                "Total entity types: {}",
                                entityTypeCount.size()
                        );

                        for (Map.Entry<EntityType<?>, Integer> entry : entityTypeCount.entrySet()) {
                            EntityType<?> type = entry.getKey();
                            int count = entry.getValue();

                            BLOOD_BOSS_LOGGER.debug(
                                    "entity: {} × {}",
                                    type.getDescription().getString(),
                                    count
                            );
                        }
                    });
        } else {
            BLOOD_BOSS_LOGGER.debug("Total entity types: 0");
        }

        // 打印实体信息
        if (this.getBrain().hasMemoryValue(ModMemoryModuleType.NBT_TEST_MEMORY_MODULE.get())) {
            Optional<List<CompoundTag>> memory = this.getBrain()
                    .getMemory(ModMemoryModuleType.NBT_TEST_MEMORY_MODULE.get());

            memory.ifPresent(stomach -> {
                BLOOD_BOSS_LOGGER.debug(
                        "Stomach size: {}",
                        stomach.size()
                );

                for (int i = 0; i < stomach.size(); i++) {
                    CompoundTag entry = stomach.get(i);
                    String entityId = entry.getString("id");
                    BLOOD_BOSS_LOGGER.debug(
                            "  [{}] {}",
                            i,
                            entityId
                    );
                }
            });
        } else {
            BLOOD_BOSS_LOGGER.debug("Stomach size: 0");
        }
    }

    //================================================================ 其他方法 ========================================================================


    @Override
    public void tick() {
        if (!isSpellConfigLoaded()) {

                return;

        }
        super.tick();

        if (this.level().isClientSide && this.isCasting()) {
            this.lastCastTick = this.tickCount;
        }
    }

    @Override
    public void notifyDangerousProjectile(Projectile projectile) {
        // 自定义危险投射物处理逻辑
    }

    @Override
    public boolean setTeleportLocationBehindTarget(int distance) {
        // 自定义传送位置逻辑
        LivingEntity target = this.getTarget();
        if (target != null) {
            return true;
        }
        return false;
    }

    @Override
    public void setBurningDashDirectionData() {
        // 自定义燃烧冲刺方向数据
    }

    @Override
    public boolean isDrinkingPotion() {
        return this.entityData.get(DATA_DRINKING_POTION);
    }

    @Override
    public boolean getHasUsedSingleAttack() {
        return this.hasUsedSingleAttack;
    }

    @Override
    public void setHasUsedSingleAttack(boolean hasUsedSingleAttack) {
        this.hasUsedSingleAttack = hasUsedSingleAttack;
    }

    @Override
    public void startDrinkingPotion() {
        if (!this.level.isClientSide) {
            this.entityData.set(DATA_DRINKING_POTION, true);
            this.drinkTime = 35;
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_DRINKING);
            attributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
        }
    }

    // 其他方法保持不变
    @Override
    public void handleClientEvent(byte b) {
        // 自定义客户端事件处理
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.spawnTimer);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        this.spawnTimer = friendlyByteBuf.readInt();
    }

    private void finishDrinkingPotion() {
        this.entityData.set(DATA_DRINKING_POTION, false);
        this.heal(Math.min(Math.max(10.0F, this.getMaxHealth() / 10.0F), this.getMaxHealth() / 4.0F));
        this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0F,
                    0.8F + this.random.nextFloat() * 0.4F);
        }
    }

    // 添加NBT数据保存和读取
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.magicData.getSyncedData().saveNBTData(compound, this.level.registryAccess());
        compound.putBoolean("usedSpecial", this.hasUsedSingleAttack);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        SyncedSpellData syncedSpellData = new SyncedSpellData(this);
        syncedSpellData.loadNBTData(compound, this.level.registryAccess());
        if (syncedSpellData.isCasting()) {
            this.recreateSpell = true;
        }
        this.magicData.setSyncedData(syncedSpellData);
        this.hasUsedSingleAttack = compound.getBoolean("usedSpecial");
    }
}