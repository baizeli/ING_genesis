package miku.united_as_one.genesis.entity.boss.behavior;

import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Map;

public class SelectTargetBehavior extends Behavior<BloodBoss> {
    public SelectTargetBehavior(Map entryCondition) {
        super(entryCondition);
    }

    public SelectTargetBehavior(){
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.BOOLEAN_TEST_MEMORY_MODULE.get(), MemoryStatus.VALUE_ABSENT
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
                        if (!(target instanceof FakePlayer)){
                            brain.setMemory(MemoryModuleType.ATTACK_TARGET, nearestLivingEntities.get(0));
                            break;
                        }
                    }

                });


    }

    @Override
    protected boolean canStillUse(ServerLevel level, BloodBoss entity, long gameTime) {
        // 这是一个一次性行为，一旦开始消化就不需要继续执行
        return false;
    }




}
