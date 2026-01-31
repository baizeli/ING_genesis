package miku.united_as_one.genesis.entity.boss.behavior.bloodbossskill;

import com.google.common.collect.ImmutableMap;
import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.entity.boss.behavior.AnimatedActionBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BloodBossEmergingBehavior extends AnimatedActionBehavior<BloodBoss> {
    public static final int EMERGE_SPAWN_DURATION = 175;

    /**
     */
    public BloodBossEmergingBehavior() {
        super(ImmutableMap.of(MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT));
    }


    @Override
    protected boolean canStartAction(BloodBoss entity) {

        return entity.getBrain()
                .getMemory(ModMemoryModuleType.BOSS_STAGE.get())
                .orElse(0) == 0;
    }


    @Override
    protected int getActionTimestamp() {
        return 5;
    }

    @Override
    protected int getActionDuration() {
        return 175;
    }

    @Override
    protected int getCooldown() {
        return 0;
    }


    @Override
    protected String getAnimationId() {
        return "spawn_animation";
    }

    @Override
    protected void doAction(BloodBoss entity) {

    }

    @Override
    protected void stop(ServerLevel level, BloodBoss entity, long gameTime) {
        Brain<BloodBoss> brain = entity.getBrain();
        brain.eraseMemory(MemoryModuleType.IS_EMERGING);
        brain.setMemory(ModMemoryModuleType.BOSS_STAGE.get(), 1);
        super.stop(level, entity, gameTime);
    }
}
