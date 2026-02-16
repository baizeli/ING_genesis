package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import com.google.common.collect.ImmutableMap;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BloodBossStageTransitionBehavior extends AnimatedActionBehavior<BloodBoss> {

    public static final int STAGE_TRANSITION_DURATION = (int)(21*20);

    public BloodBossStageTransitionBehavior() {
        super(ImmutableMap.of(
                MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.BOSS_STAGE.get(), MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        int stage = entity.getBrain()
                .getMemory(ModMemoryModuleType.BOSS_STAGE.get())
                .orElse(1);

        boolean hasPlayed = entity.getBrain()
                .getMemory(ModMemoryModuleType.HAS_PLAYED_STAGE_TRANSITION.get())
                .orElse(false);
        int stunCount = entity.getBrain()
                .getMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get())
                .orElse(0);


        return stage ==1 && !hasPlayed && stunCount >= 1;
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss owner, long gameTime) {

        Brain<BloodBoss> brain = owner.getBrain();
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);

        super.tick(level, owner, gameTime);
    }

    @Override
    protected int getActionTimestamp() {
        return 20;
    }

    @Override
    protected int getActionDuration() {
        return STAGE_TRANSITION_DURATION;
    }

    @Override
    protected int getCooldown() {
        return 0;
    }

    @Override
    protected String getAnimationId() {
        return "stage_transition_animation";
    }

    @Override
    protected void doAction(BloodBoss entity) {
        entity.getBrain().setMemory(
                ModMemoryModuleType.HAS_PLAYED_STAGE_TRANSITION.get(),
                true
        );
        entity.getBrain().setMemory(ModMemoryModuleType.BOSS_STAGE.get(), 2);
    }

    @Override
    protected void stop(ServerLevel level, BloodBoss entity, long gameTime) {
        entity.getBrain().eraseMemory(MemoryModuleType.IS_EMERGING);
        super.stop(level, entity, gameTime);
    }
}
