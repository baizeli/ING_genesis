package miku.united_as_one.genesis.entity.boss;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import io.redspace.ironsspellbooks.api.config.IronConfigParameters;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.slf4j.Logger;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BloodBoss extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker, IEntityAdditionalSpawnData, IClientEventEntity {
    private static final Logger BLOOD_BOSS_LOGGER = LogUtils.getLogger();
    int spawnTimer;
    private final AnimationController<BloodBoss> skillAnimationController;
    RawAnimation animationToPlay;
    private final AnimationController<BloodBoss> animationControllerWalk;
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);


    public BloodBoss(EntityType<? extends AbstractSpellCastingMob> entityType, Level level) {
        super(entityType, level);
        this.animationControllerWalk =new AnimationController(
                this, "walk_controller", 5, this::walkPredicate);
        this.skillAnimationController = new AnimationController(
                this, "skill_animation_controller",
                0, this::animationPredicate);

    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 950)
            .add(Attributes.MOVEMENT_SPEED, 0.21)
            .add(Attributes.ATTACK_DAMAGE, 10)
            .add(Attributes.ARMOR, 20)
            .add(AttributeRegistry.MAX_MANA.get(), 50000.0)  // 最大法力值
            .add(ForgeMod.ENTITY_GRAVITY.get(), 0.03)
            .add(ForgeMod.ENTITY_REACH.get(), 3.0)
            .add(AttributeRegistry.SPELL_POWER.get(), 1.25);
    }





    @Override
    public void handleClientEvent(byte b) {

    }


    @Override
    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.spawnTimer);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        this.spawnTimer = friendlyByteBuf.readInt();
    }

    @Override
    public boolean isCasting() {
        return super.isCasting(); // 或你的自定义逻辑
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse((LivingEntity)null);
    }

//===========================动画===========================

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
    @Override
    public void playAnimation(String s) {

    }

    @Override
    public boolean shouldBeExtraAnimated() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        super.registerControllers(controllerRegistrar); // 保持原有的施法动画控制器
        controllerRegistrar.add(animationControllerWalk); // 添加行走动画控制器
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
    /**
     * 动画控制器的动画状态谓词方法
     * 处理当前待播放动画的设置和播放逻辑
     *
     * @param animationEvent 包含动画控制器和相关数据的动画状态事件
     * @return 返回动画播放状态，始终返回CONTINUE以继续播放
     */
    private PlayState animationPredicate(AnimationState<BloodBoss> animationEvent) {

        // 获取动画控制器实例
        AnimationController<BloodBoss> controller = animationEvent.getController();
        // 检查是否有待播放的动画
        if (this.animationToPlay != null) {
            // 强制重置动画控制器，确保新动画能够正确播放
            controller.forceAnimationReset();
            // 设置要播放的新动画
            controller.setAnimation(this.animationToPlay);
            // 清空待播放动画引用，避免重复播放
            this.animationToPlay = null;
        }

        return PlayState.CONTINUE;
    }


    //-----------------------------------AI------------------------------------------
    @Override
    protected void customServerAiStep() {
        if (isSpellConfigLoaded()){
            ServerLevel serverlevel = (ServerLevel) this.level();

            serverlevel.getProfiler().push("BloodBossBrain");
            this.getBrain().tick(serverlevel, this);
            serverlevel.getProfiler().pop();

            super.customServerAiStep();
            BloodBossAi.updateActivity(this);
            // 调试输出：当前大脑的活动
            if (this.tickCount % 40 == 0) {
                printLog();
            }
        }



        updateStage();
    }

    public boolean isSpellConfigLoaded() {
        try {
            // 随便尝试获取一个法术的开关状态，这会触发内部对 config 的访问
            // 如果 config 为 null，这里会抛出 NPE
            SpellConfigManager.getSpellConfigValue(SpellRegistry.none(), IronConfigParameters.ENABLED);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }
    public void updateStage() {

    }

    private void printLog() {

        BLOOD_BOSS_LOGGER.debug("当前activity: {}", this.getBrain().getActiveActivities());
        //打印当前攻击目标实体id
        this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(target -> {
            BLOOD_BOSS_LOGGER.debug("攻击目标: {}", target.getType().getDescription().getString());
        });
        //打印剩余法力值
        BLOOD_BOSS_LOGGER.debug("剩余法力值: {}", this.getMagicData().getMana());


        BLOOD_BOSS_LOGGER.debug("坐标: {}", this.blockPosition());
        BLOOD_BOSS_LOGGER.debug("移动: {}", this.getDeltaMovement());
        BLOOD_BOSS_LOGGER.debug("是否在地上: {}", this.onGround());
        BLOOD_BOSS_LOGGER.debug("当前阶段: {}", this.getBrain().getMemory(ModMemoryModuleType.BOSS_STAGE.get()));
        // 检查移动目标记忆
        this.getBrain().getMemory(MemoryModuleType.WALK_TARGET).ifPresent(walkTarget -> {
            BLOOD_BOSS_LOGGER.debug("Walk target: {}", walkTarget.getTarget().currentBlockPosition());
        });
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

        //打印实体信息
        Optional<List<CompoundTag>> memory = this.getBrain()
                .getMemory(ModMemoryModuleType.NBT_TEST_MEMORY_MODULE.get());

        memory
                .ifPresent(stomach -> {
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



    }

    @Override
    public Brain<BloodBoss> getBrain() {
        return (Brain<BloodBoss>) super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return BloodBossAi.makeBrain(this,dynamic);
    }



}