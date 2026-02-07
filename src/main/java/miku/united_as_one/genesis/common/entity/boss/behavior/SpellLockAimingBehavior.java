package miku.united_as_one.genesis.common.entity.boss.behavior;

import com.google.common.collect.ImmutableMap;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class SpellLockAimingBehavior extends Behavior<BloodBoss> {

    public SpellLockAimingBehavior() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BloodBoss owner) {
        // 仅在施法期间生效
        return owner.isCasting();
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(target -> {

            owner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));

        });
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BloodBoss owner, long gameTime) {
        return owner.isCasting();
    }
}