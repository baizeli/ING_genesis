package miku.united_as_one.genesis.common.entity.boss;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.backwards_compat.AttributeHelper;
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
import io.redspace.ironsspellbooks.api.util.BossbarManager;
import io.redspace.ironsspellbooks.api.util.MusicManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.ExtendedServerBossEvent;
import io.redspace.ironsspellbooks.network.EntityEventPacket;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill.BloodBossEmergingBehavior;
import miku.united_as_one.genesis.common.entity.boss.damage.BloodBossDamageSource;
import miku.united_as_one.genesis.init.registry.SoundRegister;
import miku.united_as_one.genesis.init.registry.client.ParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.world.BossEvent;

import static miku.united_as_one.genesis.Genesis.MODID;

@SuppressWarnings("deprecation")
public class BloodBoss extends Monster implements GeoEntity, Enemy, IAnimatedAttacker, IEntityAdditionalSpawnData, IClientEventEntity, IMagicEntity {
    private static final Logger BLOOD_BOSS_LOGGER = LogUtils.getLogger();

    public static final ThreadLocal<Boolean> INTERNAL_CALL = ThreadLocal.withInitial(() -> false);

    // 魔法相关字段
    private static final EntityDataAccessor<Boolean> DATA_CANCEL_CAST = SynchedEntityData.defineId(BloodBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_DRINKING_POTION = SynchedEntityData.defineId(BloodBoss.class, EntityDataSerializers.BOOLEAN);
    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(
            UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E"),
            "Drinking speed penalty", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL
    );

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle"); // 待机
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk_cycle"); // 行走循环
    private static final RawAnimation TENTACLE_WALKING = RawAnimation.begin().thenLoop("tentacle_walking"); // 触手行走
//    private static final RawAnimation EMPTY = RawAnimation.begin().thenLoop("blank"); // 行走循环
//    private static final RawAnimation CAST_IDLE = RawAnimation.begin().thenLoop("施法待机");
//    private static final RawAnimation CAST_WALK = RawAnimation.begin().thenLoop("施法行走循环");

    private static final BossbarManager.BossbarSprite BLOOD_BOSSBAR_SPRITE = new BossbarManager.BossbarSprite(
            new ResourceLocation(MODID, "boss_bars/blood_bossbar"),
            219,
            45,
            47,
            -1
    );

    private static final int CASTING_POST_DELAY = 20;

    private static final EntityDataAccessor<Boolean> DATA_IS_CASTING_SKILL = SynchedEntityData.defineId(BloodBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_VISIBLE = SynchedEntityData.defineId(BloodBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BOSS_STAGE_DATA = SynchedEntityData.defineId(BloodBoss.class, EntityDataSerializers.INT);

    // 音乐播放事件
    public static final byte START_MUSIC = 10;
    public static final byte STOP_MUSIC = 11;

    //boss血条
    public static final byte START_BOSSBAR = 12;
    public static final byte STOP_BOSSBAR  = 13;

    //刀光
    private final TrailComponent trailComponent = new TrailComponent(64);

    // 动画控制器
    private final AnimationController<BloodBoss> instantCastController;
    private final AnimationController<BloodBoss> longCastController;
    private final AnimationController<BloodBoss> continuousCastController;

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);

    private final MagicData magicData = new MagicData(true);

    private int spawnTimer;
    private final AnimationController<BloodBoss> skillAnimationController;
    private final AnimationController<BloodBoss> animationControllerWalk;
    private RawAnimation animationToPlay;

    private AbstractSpell lastCastSpellType = SpellRegistry.none();
    private AbstractSpell instantCastSpellType = SpellRegistry.none();
    private boolean cancelCastAnimation = false;

    @Nullable
    private SpellData castingSpell;
    private int drinkTime;
    private boolean hasUsedSingleAttack;
    private boolean recreateSpell;
    //用于延迟释放瞬时法术
    private int delayedCastTick = -1;
    private int delayedSpellLevel;
    private AbstractSpell delayedSpell;

    private int lastCastTick = -100;

    private ExtendedServerBossEvent bossEvent;

    private int shouldSetWalkTransitionLengthDelay = 0;

    //用于深渊庇佑计数
    private int abyssalAsylumTriggers = 0; // 已触发次数
    private float nextHealthThreshold = 0.80f; // 下一次触发的血量百分比 (100% - 20%)


    //================================================================ 方法 ========================================================================

    public TrailComponent getTrailComponent() {
        return trailComponent;
    }

    public boolean isCastingSkill() {
        return this.entityData.get(DATA_IS_CASTING_SKILL);
    }

    public void setCastingSkill(boolean castingSkill) {
        this.entityData.set(DATA_IS_CASTING_SKILL, castingSkill);
    }

    public int getBossStage(){
        return this.getBrain().getMemory(ModMemoryModuleType.BOSS_STAGE.get()).orElse(0);
    }
    public int getBossStageData(){
        return this.entityData.get(BOSS_STAGE_DATA);
    }

    private void syncBossStageData(){
        this.entityData.set(BOSS_STAGE_DATA, getBossStage());
    }

    public void setVisable(boolean visable){
        this.entityData.set(DATA_IS_VISIBLE, visable);
    }
    public boolean getVisable(){
        return this.entityData.get(DATA_IS_VISIBLE);
    }

    //属性
    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 950)
                .add(Attributes.MOVEMENT_SPEED, 0.21)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.ARMOR, 15)
                .add(AttributeRegistry.MAX_MANA.get(), 10000.0)
                .add(ForgeMod.ENTITY_GRAVITY.get(), 0.03)
                .add(ForgeMod.ENTITY_REACH.get(), 3.0)
                .add(Attributes.KNOCKBACK_RESISTANCE,1.0)
                .add(Attributes.FOLLOW_RANGE,128)
                .add(AttributeRegistry.SPELL_POWER.get(), 1.25);
    }

    public boolean isReallyCasting() {
        if (this.magicData.isCasting()) return true;
        if (this.castingSpell != null) return true;
        return this.tickCount - this.lastCastTick <= CASTING_POST_DELAY;
    }

    //================================================================ 生命周期 ========================================================================

    public BloodBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new BloodBossMoveControl(this);
//        this.lookControl = new BloodBossLookControl(this);
//        this.jumpControl = new BloodBossJumpControl(this);

        this.animationControllerWalk = new AnimationController<>(this, "walk_controller", 10, this::walkPredicate);
        this.skillAnimationController = new AnimationController<>(this, "skill_animation_controller", 0, this::skillAnimationPredicate);

        this.instantCastController = new AnimationController<>(this, "instant_cast", 0, this::instantCastingPredicate);
        this.longCastController = new AnimationController<>(this, "long_cast", 0, this::longCastingPredicate);
        this.continuousCastController = new AnimationController<>(this, "continuous_cast", 0, this::continuousCastingPredicate);

        // 初始化魔法数据
        this.magicData.setSyncedData(new SyncedSpellData(this));
        this.noCulling = true;


        this.createBossEvent();
        this.bossEvent.setDarkenScreen(true); // 可选：压暗屏幕
        this.setInvisible(true);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        int extraPlayers = Math.max(0, level.players().stream().filter((player) -> this.distanceToSqr(player) < 3600.0 && !player.isSpectator() && !player.isCreative()).toList().size() - 1);
        double extraHealthPercent = extraPlayers * 0.4 + extraPlayers * extraPlayers * 0.1;

        if (extraHealthPercent != 0.0) {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier(AttributeHelper.uuidFromId(IronsSpellbooks.id("player_scale")), "player_scale", extraHealthPercent, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        this.getBrain().setMemoryWithExpiry(MemoryModuleType.IS_EMERGING, Unit.INSTANCE,
                BloodBossEmergingBehavior.EMERGE_SPAWN_DURATION);
        this.playSound(SoundEvents.WARDEN_AGITATED, 5.0F, 1.0F);

        if (!this.level.isClientSide()){
            this.setVisable(this.getBossStage() > 0);
        }

        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    protected boolean isImmobile() {
        if (this.getBrain().getMemory(MemoryModuleType.IS_EMERGING).isPresent()){
            return false;
        }
        return super.isImmobile();
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        if(this.getBrain().getMemory(MemoryModuleType.IS_EMERGING).isPresent())
            return true;
        return super.isInvulnerableTo(source);
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);

        // 同步更新血条显示
        if (!this.level().isClientSide && this.bossEvent != null) {
            float progress = health / this.getMaxHealth();
            this.bossEvent.setProgress(Mth.clamp(progress, 0.0F, 1.0F));
        }
    }

    private void detectAndApplyAbyssalAsylum() {
        if (this.abyssalAsylumTriggers >= 5) {
            return;
        }

        float healthPercentage = this.getHealth() / this.getMaxHealth();

        if (healthPercentage <= nextHealthThreshold) {
            this.addEffect(new MobEffectInstance(
                    MobEffectRegistry.ABYSSAL_SHROUD.get(), 9 * 20, 0, false, false, false
            ));

            this.abyssalAsylumTriggers++;
            this.nextHealthThreshold -= 0.20f;
        }
    }

    @Override
    public void tick() {
        var iterator = this.activeEffects.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            MobEffect effect = entry.getKey();
            MobEffectInstance instance = entry.getValue();

            String id = instance.getDescriptionId();
            if (!effect.isBeneficial() &&
                    !id.startsWith("effect." + Genesis.MOD_ID) && !id.startsWith("effect." + "irons_spellbooks")) {
                this.effectsDirty = true;
                effect.removeAttributeModifiers(this, this.getAttributes(), instance.getAmplifier());

                for(Entity entity : this.getPassengers()) {
                    if (entity instanceof ServerPlayer serverplayer) {
                        serverplayer.connection.send(new ClientboundRemoveMobEffectPacket(this.getId(), effect));
                    }
                }

                iterator.remove();
            }
        }

        if (!level.isClientSide) {
            detectAndApplyAbyssalAsylum();
            syncBossStageData();
            this.setCastingSkill(this.getBrain().getMemory(ModMemoryModuleType.IS_CASTING_SKILL.get()).orElse(false));
        }

        if (!isSpellConfigLoaded()) {
            return;
        }
        super.tick();

        if (this.level().isClientSide && this.isCasting()) {
            this.lastCastTick = this.tickCount;
        }

        if (this.level().isClientSide) {
            trailComponent.setHasTrail(this.isCastingSkill()&&this.getBossStageData()<2);
            if(getBossStageData()>1){
                spawnTrailParticles();
            }
            this.setInvisible(!this.getVisable());
        }

        if (!this.level().isClientSide && this.bossEvent != null) {
            float currentHealth = this.getHealth();
            float maxHealth = this.getMaxHealth();
            float progress = currentHealth / maxHealth;

            if (Math.abs(this.bossEvent.getProgress() - progress) > 0.001f) {
                this.bossEvent.setProgress(Mth.clamp(progress, 0.0F, 1.0F));
            }
        }
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        if (!this.level().isClientSide) {
            this.serverTriggerEvent(STOP_MUSIC);
            this.serverTriggerEvent(STOP_BOSSBAR);

            if (this.bossEvent != null) {
                this.bossEvent.removeAllPlayers();
                this.bossEvent.setVisible(false);
            }

        }
        super.die(cause);
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        if (!this.level().isClientSide) {
            this.serverTriggerEvent(STOP_MUSIC);
            this.serverTriggerEvent(STOP_BOSSBAR);

            if (this.bossEvent != null) {
                this.bossEvent.removeAllPlayers();
                this.bossEvent.setVisible(false);
            }
        }
        super.remove(reason);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void setTarget(@org.jetbrains.annotations.Nullable LivingEntity target) {
        super.setTarget(target);
        if (target != null) {
            this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        } else {
            this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CANCEL_CAST, false);
        this.entityData.define(DATA_DRINKING_POTION, false);
        this.entityData.define(DATA_IS_CASTING_SKILL, false);
        this.entityData.define(DATA_IS_VISIBLE, true);
        this.entityData.define(BOSS_STAGE_DATA, 0);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return effectInstance.getEffect().isBeneficial() ||
                effectInstance.getDescriptionId().contains(Genesis.MOD_ID) ||
                effectInstance.getDescriptionId().contains("irons_spellbooks");
    }

    @Override
    public void push(@NotNull Entity entity) {
    }

    @Override
    public void push(double x, double y, double z) {
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

    public void playAnimation(String animationId) {
        this.animationToPlay = RawAnimation.begin().thenPlay(animationId);

        this.cancelCastAnimation = true;
        if (this.isCasting()) {
            this.cancelCast();
        }
    }

    private PlayState instantCastingPredicate(AnimationState<BloodBoss> event) {

        if (this.cancelCastAnimation || this.skillAnimationController.getAnimationState() != AnimationController.State.STOPPED) {
            return PlayState.STOP;
        }

        AnimationController<BloodBoss> controller = event.getController();
        if (this.instantCastSpellType != SpellRegistry.none() &&
                controller.getAnimationState() == AnimationController.State.STOPPED) {
            this.setStartAnimationFromSpell(controller, this.instantCastSpellType);
            this.instantCastSpellType = SpellRegistry.none();
        }

        return PlayState.CONTINUE;
    }

    private PlayState longCastingPredicate(AnimationState<BloodBoss> event) {
        if (this.skillAnimationController.getAnimationState() != AnimationController.State.STOPPED) {
            return PlayState.STOP;
        }

        AnimationController<BloodBoss> controller = event.getController();
        if (this.isCasting() && controller.getAnimationState() == AnimationController.State.STOPPED &&
                this.castingSpell != null) {
            this.setStartAnimationFromSpell(controller, this.castingSpell.getSpell());
        } else if (this.lastCastSpellType.getCastType() == CastType.LONG) {
            this.setFinishAnimationFromSpell(controller, this.lastCastSpellType);
        }

        return PlayState.CONTINUE;
    }

    private PlayState continuousCastingPredicate(AnimationState<BloodBoss> event) {
        if (this.skillAnimationController.getAnimationState() != AnimationController.State.STOPPED) {
            return PlayState.STOP;
        }

        AnimationController<BloodBoss> controller = event.getController();
        if (this.isCasting() && controller.getAnimationState() == AnimationController.State.STOPPED &&
                this.castingSpell != null) {
            if (this.castingSpell.getSpell().getCastType() == CastType.CONTINUOUS) {
                this.setStartAnimationFromSpell(controller, this.castingSpell.getSpell());
            }
            return PlayState.CONTINUE;
        }

        return PlayState.CONTINUE;
    }

    private void setStartAnimationFromSpell(AnimationController<BloodBoss> controller, AbstractSpell spell) {
        spell.getCastStartAnimation().getForMob().ifPresentOrElse(animationBuilder -> {
            controller.forceAnimationReset();
            controller.setAnimation(animationBuilder);
            this.lastCastSpellType = spell;
            this.cancelCastAnimation = false;
        }, () -> this.cancelCastAnimation = true);
    }

    private void setFinishAnimationFromSpell(AnimationController<BloodBoss> controller, AbstractSpell spell) {
        if (spell.getCastFinishAnimation().isPass) {
            this.cancelCastAnimation = false;
        } else {
            spell.getCastFinishAnimation().getForMob().ifPresentOrElse(animationBuilder -> {
                controller.forceAnimationReset();
                controller.transitionLength(8);
                controller.setAnimation(animationBuilder);
                this.lastCastSpellType = SpellRegistry.none();
            }, () -> this.cancelCastAnimation = true);
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
        boolean isCastingSkill = this.isCastingSkill();

        if (isCastingSkill) {
            animationControllerWalk.setTransitionLength(0);
            animationControllerWalk.setAnimationSpeed(1.0);

            shouldSetWalkTransitionLengthDelay = 0;
            return PlayState.STOP;
        }

        if (shouldSetWalkTransitionLengthDelay <= 10) {
            animationControllerWalk.setTransitionLength(0);
            shouldSetWalkTransitionLengthDelay++;
        } else {
            animationControllerWalk.setTransitionLength(10);
        }

        if (this.isCasting()) {
            animationControllerWalk.setAnimationSpeed(1.0);
            return state.setAndContinue(IDLE);
        }

        double horizontalSpeed = this.getDeltaMovement().horizontalDistance();
        if (horizontalSpeed > 0.01) {
            double speedMultiplier = (horizontalSpeed / 0.053) * 1.2;
            animationControllerWalk.setAnimationSpeed(
                    Mth.clamp(speedMultiplier, 0.6, 1.8)
            );

            if (getBossStageData()<=1){
                return state.setAndContinue(WALK);

            } else {
               return state.setAndContinue(TENTACLE_WALKING);
            }
        }

        animationControllerWalk.setAnimationSpeed(1.0);
        return state.setAndContinue(IDLE);
    }

    private PlayState skillAnimationPredicate(AnimationState<BloodBoss> animationEvent) {
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

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<BloodBoss> getBrain() {
        return (Brain<BloodBoss>) super.getBrain();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return BloodBossAi.makeBrain(this, dynamic);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        // 处理法术重现
        if (this.recreateSpell) {
            this.recreateSpell = false;
            SyncedSpellData syncedSpellData = this.magicData.getSyncedData();
            AbstractSpell spell = SpellRegistry.getSpell(syncedSpellData.getCastingSpellId());
            this.initiateCastSpell(spell, syncedSpellData.getCastingSpellLevel());
        }

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
            double d1 = ( target.getBoundingBox().minY+(target.getBoundingBox().maxY- target.getBoundingBox().minY)/2.0) - this.getEyeY();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float f = (float)(Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F;
            float f1 = (float)(-(Mth.atan2(d1, d3) * 57.2957763671875));
            this.setXRot(f1);
            this.setYRot(f);
        }
    }

    public boolean isSpellConfigLoaded() {
        try {
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
    }

    private void printLog() {
        Brain<BloodBoss> brain = this.getBrain();

        BLOOD_BOSS_LOGGER.debug("==============开始打印bloodBoss信息=================");
        BLOOD_BOSS_LOGGER.debug("当前activity: {}", brain.getActiveActivities());
        
        // 打印当前攻击目标实体id
        if (brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            brain.getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(target ->
                    BLOOD_BOSS_LOGGER.debug("攻击目标: {}", target.getType().getDescription().getString()));
        } else {
            BLOOD_BOSS_LOGGER.debug("攻击目标: 无");
        }
        
        // 打印剩余法力值
        BLOOD_BOSS_LOGGER.debug("剩余法力值: {}", this.getMagicData().getMana());

        BLOOD_BOSS_LOGGER.debug("坐标: {}", this.blockPosition());
        BLOOD_BOSS_LOGGER.debug("移动: {}", this.getDeltaMovement());
        BLOOD_BOSS_LOGGER.debug("是否在地上: {}", this.onGround());
        
        // 打印当前阶段
        if (brain.hasMemoryValue(ModMemoryModuleType.BOSS_STAGE.get())) {
            brain.getMemory(ModMemoryModuleType.BOSS_STAGE.get()).ifPresent(stage ->
                    BLOOD_BOSS_LOGGER.debug("当前阶段: {}", stage));
        } else {
            BLOOD_BOSS_LOGGER.debug("当前阶段: 无");
        }
        
        // 检查移动目标记忆
        if (brain.hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            brain.getMemory(MemoryModuleType.WALK_TARGET).ifPresent(walkTarget ->
                    BLOOD_BOSS_LOGGER.debug("Walk target: {}", walkTarget.getTarget().currentBlockPosition()));
        } else {
            BLOOD_BOSS_LOGGER.debug("Walk target: 无");
        }
        
        // 打印实体类型计数
        if (brain.hasMemoryValue(ModMemoryModuleType.ENTITY_TYPE_COUNT.get())) {
            brain.getMemory(ModMemoryModuleType.ENTITY_TYPE_COUNT.get())
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
        if (brain.hasMemoryValue(ModMemoryModuleType.NBT_TEST_MEMORY_MODULE.get())) {
            Optional<List<CompoundTag>> memory = brain.getMemory(ModMemoryModuleType.NBT_TEST_MEMORY_MODULE.get());

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
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL)) {
            return false;
        }

        double threshold = this.getMaxHealth() * 0.025;

        if (amount > threshold) {
            amount = (float) (threshold + (amount - threshold) * 0.7);
        }

        if (source.getEntity() instanceof LivingEntity target){
            this.setTarget(target);
        }

        return super.hurt(source, amount);
    }

    @Override
    public void knockback(double strength, double x, double z) {
    }

    @Override
    public void teleportTo(double x, double y, double z) {
        super.teleportTo(x, y, z);
    }

    /**
     * @deprecated 请勿直接调用此方法，请使用 {@link #realSetDeltaMovement(Vec3)}，
     * 或者用withInternalCall
     */
    @Deprecated(since = "1.0")
    @Override
    public void setDeltaMovement(@NotNull Vec3 deltaMovement) {
        if (INTERNAL_CALL.get()) {
            super.setDeltaMovement(deltaMovement);
        }
    }

    /**
     * @deprecated 请勿直接调用此方法，请使用 {@link #realSetDeltaMovement(double, double, double)}，
     * 或者用withInternalCall
     */
    @Deprecated(since = "1.0")
    @Override
    public void setDeltaMovement(double x, double y, double z) {
        if (INTERNAL_CALL.get()) {
            super.setDeltaMovement(x, y, z);
        }
    }

    public void realSetDeltaMovement(@NotNull Vec3 deltaMovement) {
        super.setDeltaMovement(deltaMovement);
    }

    public void realSetDeltaMovement(double x, double y, double z) {
        super.setDeltaMovement(x, y, z);
    }

    public static void withInternalCall(Runnable action) {
        boolean old = INTERNAL_CALL.get();
        INTERNAL_CALL.set(true);
        try {
            action.run();
        } finally {
            INTERNAL_CALL.set(old);
        }
    }

    public static <T> T withInternalCall(Supplier<T> action) {
        boolean old = INTERNAL_CALL.get();
        INTERNAL_CALL.set(true);
        try {
            return action.get();
        } finally {
            INTERNAL_CALL.set(old);
        }
    }

    @Override
    public void move(@NotNull MoverType type, @NotNull Vec3 pos) {
        withInternalCall(() -> super.move(type, pos));
    }

    @Override
    public void moveRelative(float amount, @NotNull Vec3 relative) {
        withInternalCall(() -> super.moveRelative(amount, relative));
    }

    @Override
    public void rideTick() {
        withInternalCall(super::rideTick);
    }

    @Override
    public void lerpMotion(double x, double y, double z) {
        withInternalCall(() -> super.lerpMotion(x, y, z));
    }

    @Override
    public void onAboveBubbleCol(boolean downwards) {
        withInternalCall(() -> super.onAboveBubbleCol(downwards));
    }

    @Override
    public void onInsideBubbleColumn(boolean downwards) {
        withInternalCall(() -> super.onInsideBubbleColumn(downwards));
    }

    @Override
    protected void moveTowardsClosestSpace(double x, double y, double z) {
        withInternalCall(() -> super.moveTowardsClosestSpace(x, y, z));
    }

    @Override
    public @Nullable Entity changeDimension(@NotNull ServerLevel destination, @NotNull ITeleporter teleporter) {
        return withInternalCall(() -> super.changeDimension(destination, teleporter));
    }

    @Override
    public void updateFluidHeightAndDoFluidPushing(@NotNull Predicate<FluidState> shouldUpdate) {
        withInternalCall(() -> super.updateFluidHeightAndDoFluidPushing(shouldUpdate));
    }

    @Override
    protected void tickLeash() {
        withInternalCall(super::tickLeash);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        return withInternalCall(() -> super.doHurtTarget(entity));
    }

    @Override
    public void jumpInLiquidInternal(@NotNull Runnable onSuper) {
        withInternalCall(() -> super.jumpInLiquidInternal(onSuper));
    }

    @Override
    protected void jumpFromGround() {
        withInternalCall(super::jumpFromGround);
    }

    @Override
    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluidTag) {
        withInternalCall(() -> super.jumpInLiquid(fluidTag));
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        withInternalCall(() -> super.travel(travelVector));
    }

    @Override
    public @NotNull Vec3 handleRelativeFrictionAndCalculateMovement(@NotNull Vec3 deltaMovement, float friction) {
        return withInternalCall(() -> super.handleRelativeFrictionAndCalculateMovement(deltaMovement, friction));
    }

    @Override
    public void aiStep() {
        withInternalCall(super::aiStep);
    }

    @Override
    protected void checkAutoSpinAttack(@NotNull AABB boundingBoxBeforeSpin, @NotNull AABB boundingBoxAfterSpin) {
        withInternalCall(() -> super.checkAutoSpinAttack(boundingBoxBeforeSpin, boundingBoxAfterSpin));
    }

    @Override
    public void startSleeping(@NotNull BlockPos pos) {
        withInternalCall(() -> super.startSleeping(pos));
    }

    @Override
    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket packet) {
        withInternalCall(() -> super.recreateFromPacket(packet));
    }

    private void createBossEvent() {
        this.bossEvent = (ExtendedServerBossEvent)(
                new ExtendedServerBossEvent(
                        this.getUUID(),
                        this.getDisplayName(),
                        BossEvent.BossBarColor.RED,
                        BossEvent.BossBarOverlay.PROGRESS
                )
        ).setCreateWorldFog(true);
        this.bossEvent.setDarkenScreen(true);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        withInternalCall(() -> super.load(compound));

        if (!this.level.isClientSide) {
            // 同步血条进度
            if (this.bossEvent != null) {
                float progress = this.getHealth() / this.getMaxHealth();
                this.bossEvent.setProgress(Mth.clamp(progress, 0.0F, 1.0F));
                this.bossEvent.setVisible(this.isAlive()); // 根据存活状态设置可见性
            }
        }

        if (!this.level.isClientSide) {
            this.createBossEvent();
        }
    }

    @Override
    public void handleClientEvent(byte eventId) {
        switch (eventId) {
            case START_MUSIC -> MusicManager.createEvent(
                    this,
                    new BloodBossMusicHandler(getBossMusicEvent())
            );
            case STOP_MUSIC -> MusicManager.stopEvent(this.getUUID());
            case START_BOSSBAR -> BossbarManager.startTracking(this.getUUID(), BLOOD_BOSSBAR_SPRITE);
            case STOP_BOSSBAR -> BossbarManager.stopTracking(this.getUUID());
        }
    }

    private void spawnTrailParticles() {
        Vec3 motion = this.getDeltaMovement();
        double speed = motion.length();

        // 几乎不动就不生成
        if (speed < 0.05) return;

        Vec3 direction = motion.normalize();

        // 粒子数量随速度变化（可自己调系数）
        int count = Mth.clamp((int)(speed * 20), 2, 40);

        // 碰撞箱
        AABB box = this.getBoundingBox();
        double height = box.maxY - box.minY;

        for (int i = 0; i < count; i++) {

            double backOffset = this.random.nextDouble() * speed;

            Vec3 basePos = this.position().subtract(direction.scale(backOffset));

            double yOffset = this.random.nextDouble() * height;

            double xzSpread = 1d;
            double xOffset = (this.random.nextDouble() - 0.5) * xzSpread;
            double zOffset = (this.random.nextDouble() - 0.5) * xzSpread;

            Vec3 particlePos = new Vec3(
                    basePos.x + xOffset,
                    box.minY + yOffset,
                    basePos.z + zOffset
            );

            if (this.random.nextInt(5) == 0) {
                this.level().addParticle(
                        ParticleRegistry.BLOOD_DRIP_TWIST.get(),
                        particlePos.x, particlePos.y, particlePos.z,
                        0.0, 0.0, 0.0
                );
            }

            this.level().addParticle(
                    ParticleRegistry.BLOOD_DRIP_HANG.get(),
                    particlePos.x, particlePos.y, particlePos.z,
                    0, 0, 0
            );

        }
    }

    private SoundEvent getBossMusicEvent() {
        return SoundRegister.BLOOD_BOSS_MUSIC.get();
    }

    public void startSeenByPlayer(@NotNull ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);

        this.bossEvent.addPlayer(pPlayer);
        PacketDistributor.sendToPlayer(pPlayer, new EntityEventPacket<>(this, START_BOSSBAR));
        PacketDistributor.sendToPlayer(pPlayer, new EntityEventPacket<>(this, START_MUSIC));
    }

    public void stopSeenByPlayer(@NotNull ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);

        this.bossEvent.removePlayer(pPlayer);
        PacketDistributor.sendToPlayer(pPlayer, new EntityEventPacket<>(this, STOP_BOSSBAR));
        PacketDistributor.sendToPlayer(pPlayer, new EntityEventPacket<>(this, STOP_MUSIC));
    }

    @Override
    public void notifyDangerousProjectile(Projectile projectile) {
        // 自定义危险投射物处理逻辑
    }

    @Override
    public boolean setTeleportLocationBehindTarget(int distance) {
        // 自定义传送位置逻辑
        LivingEntity target = this.getTarget();
        return target != null;
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

            AttributeInstance attributeinstance = Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED));
            attributeinstance.removeModifier(SPEED_MODIFIER_DRINKING);
            attributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
        }
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

        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(SPEED_MODIFIER_DRINKING);

        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0F,
                    0.8F + this.random.nextFloat() * 0.4F);
        }
    }

    // 添加NBT数据保存和读取
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        this.magicData.getSyncedData().saveNBTData(compound, this.level.registryAccess());
        compound.putBoolean("usedSpecial", this.hasUsedSingleAttack);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        SyncedSpellData syncedSpellData = new SyncedSpellData(this);
        syncedSpellData.loadNBTData(compound, this.level.registryAccess());
        if (syncedSpellData.isCasting()) {
            this.recreateSpell = true;
        }

        this.magicData.setSyncedData(syncedSpellData);
        this.hasUsedSingleAttack = compound.getBoolean("usedSpecial");
    }
    
    //================================================================ 战斗伤害方法 ========================================================================
    
    /**
     * 获取基础攻击伤害
     * 
     * @return 基础攻击伤害值
     */
    public float getBaseAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }
    
    /**
     * 造成技能伤害
     * 
     * @param target 目标实体
     * @param damageMultiplier 伤害倍数
     */
    public void applySkillDamage(LivingEntity target, float damageMultiplier) {
        float baseDamage = getBaseAttackDamage();
        float skillDamage = baseDamage * damageMultiplier;
        target.invulnerableTime = 0;
        target.hurt(new BloodBossDamageSource(this, this, null), skillDamage);
    }
}