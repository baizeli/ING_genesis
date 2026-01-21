package miku.united_as_one.genesis.entity.boss;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
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
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.slf4j.Logger;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BloodBoss extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker, IEntityAdditionalSpawnData, IClientEventEntity {
    private static final Logger BLOOD_BOSS_LOGGER = LogUtils.getLogger();
    int spawnTimer;


    public BloodBoss(EntityType<? extends AbstractSpellCastingMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 950)
            .add(Attributes.MOVEMENT_SPEED, 0.42)
            .add(Attributes.ATTACK_DAMAGE, 10)
            .add(Attributes.ARMOR, 20);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    public void handleClientEvent(byte b) {

    }

    @Override
    public void playAnimation(String s) {

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



    //-----------------------------------AI------------------------------------------
    @Override
    protected void customServerAiStep() {
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


        updateStage();
    }


    public void updateStage() {

    }

    private void printLog() {

        BLOOD_BOSS_LOGGER.debug("当前activity: {}", this.getBrain().getActiveActivities());
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