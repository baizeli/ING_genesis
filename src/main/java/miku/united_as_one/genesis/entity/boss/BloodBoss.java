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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.slf4j.Logger;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import javax.annotation.Nullable;
import java.util.UUID;

public class BloodBoss extends PathfinderMob implements GeoAnimatable, Enemy, IAnimatedAttacker, IEntityAdditionalSpawnData, IClientEventEntity, IMagicEntity {
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

    public BloodBoss(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.animationControllerWalk = new AnimationController(this, "walk_controller", 5, this::walkPredicate);
        this.skillAnimationController = new AnimationController(this, "skill_animation_controller", 0, this::animationPredicate);

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
                .add(AttributeRegistry.SPELL_POWER.get(), 1.25);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CANCEL_CAST, false);
        this.entityData.define(DATA_DRINKING_POTION, false);
    }

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
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        if (spell == SpellRegistry.none()) {
            this.castingSpell = null;
        } else {
            this.castingSpell = new SpellData(spell, spellLevel);

            // 施法开始时立即看向目标
            if (this.getTarget() != null) {
                this.forceLookAtTarget(this.getTarget());
            }

            if (!this.level().isClientSide && !spell.checkPreCastConditions(this.level(), spellLevel, this, this.magicData)) {
                this.castingSpell = null;
            } else {
                this.magicData.initiateCast(spell, spellLevel,
                        spell.getEffectiveCastTime(spellLevel, this), CastSource.MOB,
                        SpellSelectionManager.MAINHAND);

                if (!this.level().isClientSide) {
                    spell.onServerPreCast(this.level(), spellLevel, this, this.magicData);
                }
            }
        }
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

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    // 动画相关方法保持不变
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }

    @Override
    public void playAnimation(String s) {
        // 自定义动画播放逻辑
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(animationControllerWalk);
    }

    private PlayState walkPredicate(AnimationState animationState) {
        if(animationState.isMoving()) {
            animationState.getController().setAnimation(RawAnimation.begin().then("行走循环", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (this.isCasting()) {
            return PlayState.STOP;
        }
        animationState.getController().setAnimation(RawAnimation.begin().then("待机", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
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


    @Override
    protected void customServerAiStep() {
        // 先调用父类逻辑
        super.customServerAiStep();

        // 处理法术重现（从NBT加载时）
        if (this.recreateSpell) {
            this.recreateSpell = false;
            SyncedSpellData syncedSpellData = this.magicData.getSyncedData();
            AbstractSpell spell = SpellRegistry.getSpell(syncedSpellData.getCastingSpellId());
            this.initiateCastSpell(spell, syncedSpellData.getCastingSpellLevel());
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

        // 关键修复：在访问配置之前检查是否已加载
        if (!isSpellConfigLoaded()) {
            // 如果配置未加载，跳过AI逻辑，只执行基本更新
            updateStage();
            return;
        }

        // 处理施法状态 - 添加安全检查
        if (this.castingSpell != null) {
            this.magicData.handleCastDuration();

            if (this.magicData.isCasting()) {
                // 施法中的每tick处理 - 添加null检查
                if (this.castingSpell.getSpell() != null) {
                    this.castingSpell.getSpell().onServerCastTick(this.level(), this.castingSpell.getLevel(), this, this.magicData);
                }
            }

            // 施法时持续看向目标
            this.forceLookAtTarget(this.getTarget());

            // 检查施法是否完成
            if (this.magicData.getCastDurationRemaining() <= 0) {
                // 根据施法类型触发完成事件 - 添加null检查
                if (this.castingSpell.getSpell() != null) {
                    CastType castType = this.castingSpell.getSpell().getCastType();
                    if (castType == CastType.LONG || castType == CastType.INSTANT) {
                        this.castingSpell.getSpell().onCast(this.level(), this.castingSpell.getLevel(), this, CastSource.MOB, this.magicData);
                    }
                }
                this.castComplete();
            } else if (this.castingSpell.getSpell() != null &&
                    this.castingSpell.getSpell().getCastType() == CastType.CONTINUOUS &&
                    (this.magicData.getCastDurationRemaining() + 1) % 10 == 0) {
                // 持续施法的周期性触发
                this.castingSpell.getSpell().onCast(this.level(), this.castingSpell.getLevel(), this, CastSource.MOB, this.magicData);
            }
        }

        // 您原有的AI逻辑 - 只在配置加载后执行
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
        // 调试日志输出
        BLOOD_BOSS_LOGGER.debug("当前activity: {}", this.getBrain().getActiveActivities());
        this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(target -> {
            BLOOD_BOSS_LOGGER.debug("攻击目标: {}", target.getType().getDescription().getString());
        });
        BLOOD_BOSS_LOGGER.debug("剩余法力值: {}", this.getMagicData().getMana());
    }

    @Override
    public Brain<BloodBoss> getBrain() {
        return (Brain<BloodBoss>) super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return BloodBossAi.makeBrain(this, dynamic);
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