package miku.united_as_one.genesis.contents.entity.boss.behavior.bloodbossskill;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.util.Unit;

import miku.united_as_one.genesis.contents.entity.boss.BloodBoss;
import miku.united_as_one.genesis.registries.entity.ai.ModMemoryModuleType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BloodBossStageStunCoreBehavior extends Behavior<BloodBoss> {


    public BloodBossStageStunCoreBehavior() {
        super(Map.of(
                MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleType.BOSS_STAGE.get(), MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, BloodBoss boss) {
        int stage = boss.getBrain()
                .getMemory(ModMemoryModuleType.BOSS_STAGE.get())
                .orElse(1);
        if (stage > 1){
            return false;
        }

        int stunCount = boss.getBrain()
                .getMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get())
                .orElse(0);
        float health = boss.getHealth();
        float maxHealth = boss.getMaxHealth();


        if (stage == 1) {
            if (stunCount < 1){
                return health<maxHealth * 0.7f;
            } else {
                return health<maxHealth * 0.4f;
            }
        }
        return false;
    }

    @Override
    protected void start(@NotNull ServerLevel level, BloodBoss boss, long gameTime) {
        boss.getBrain().setMemory(MemoryModuleType.IS_EMERGING, Unit.INSTANCE);
    }


    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull BloodBoss boss, long gameTime) {
        return false;
    }
}

