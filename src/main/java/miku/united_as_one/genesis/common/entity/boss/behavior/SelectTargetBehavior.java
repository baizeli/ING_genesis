package miku.united_as_one.genesis.common.entity.boss.behavior;

import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Map;

public class SelectTargetBehavior extends Behavior<BloodBoss> {
    public SelectTargetBehavior(Map entryCondition) {
        super(entryCondition);
    }

    public SelectTargetBehavior(){
        //所需记忆模块的状态
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,//无攻击目标
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT,//有最近的生物
                ModMemoryModuleType.BOOLEAN_TEST_MEMORY_MODULE.get(), MemoryStatus.VALUE_ABSENT//无布尔测试记忆模块
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BloodBoss owner) {
        return super.checkExtraStartConditions(level, owner);
    }

    @Override
    protected void start(ServerLevel level, BloodBoss entity, long gameTime) {
        super.start(level, entity, gameTime);

        Brain<BloodBoss> brain = entity.getBrain();
        brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES)
                .ifPresent(nearestLivingEntities -> {
                    for (Entity target : nearestLivingEntities){


                        // 排除假玩家、创造模式玩家、或旁观者模式玩家、
                        if (!(target instanceof FakePlayer) && 
                            (target instanceof Player player ? 
                            (!player.isCreative() && !player.isSpectator()) :
                                target.isAlive())) {
                            if (entity.isAlliedTo(target)){
                                continue;
                            }
                            if (!(target instanceof Player)){
                                continue;
                            }
                            brain.setMemory(MemoryModuleType.ATTACK_TARGET, nearestLivingEntities.get(0));
                            break;
                        }
                    }

                });


    }

    @Override
    protected boolean canStillUse(ServerLevel level, BloodBoss entity, long gameTime) {
        // 这是一个一次性行为，一旦开始就不需要继续执行
        return false;
    }




}
