package miku.united_as_one.genesis.entity.boss.behavior;

import com.google.common.collect.ImmutableMap;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class FireBlastBehavior extends AnimatedActionBehavior<BloodBoss> {
    public FireBlastBehavior() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        return entity.getTarget() != null && entity.distanceToSqr(entity.getTarget()) < 100;
    }

    @Override
    protected int getActionTimestamp() {
        return 15; // 第15 tick 发射火球
    }

    @Override
    protected int getActionDuration() {
        return 40; // 整个动作持续 40 tick
    }

    @Override
    protected int getCooldown() {
        return 100; // 结束后冷却 100 tick
    }

    @Override
    protected String getAnimationId() {
        return "fire_blast";
    }

    @Override
    protected void doAction(BloodBoss entity) {
    }
}