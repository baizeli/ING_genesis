package miku.united_as_one.genesis.entity.boss.behavior.bloodbossskill;

import com.google.common.collect.ImmutableMap;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.entity.boss.behavior.AnimatedActionBehavior;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class BloodBossEmergingBehavior extends AnimatedActionBehavior<BloodBoss> {
    public static final int EMERGE_DURATION = 175;

    /**
     */
    public BloodBossEmergingBehavior() {
        super(ImmutableMap.of(MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT));
    }


    @Override
    protected boolean canStartAction(BloodBoss entity) {
        return true;
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


}
