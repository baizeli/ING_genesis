package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;


import com.google.common.collect.ImmutableMap;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BloodBossStunBehavior extends AnimatedActionBehavior<BloodBoss> {

    public static final int STUN_DURATION = (int)(20*9.25);

    public BloodBossStunBehavior() {
        super(ImmutableMap.of(
                MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        Brain<BloodBoss> brain = entity.getBrain();
        Integer stunCount = brain.getMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get()).orElse(0);
        if (stunCount>=1){
            return false;
        }
        if (stunCount==0){
            float health = entity.getHealth();
            float maxHealth = entity.getMaxHealth();
            return health <= maxHealth * 0.7;
        }

        return stunCount > 0;
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
        return STUN_DURATION;
    }

    @Override
    protected int getCooldown() {
        return 0;
    }

    @Override
    protected String getAnimationId() {
        return "stun_animation";
    }

    @Override
    protected void doAction(BloodBoss entity) {
    }

    @Override
    protected void stop(ServerLevel level, BloodBoss entity, long gameTime) {
        Brain<BloodBoss> brain = entity.getBrain();
        brain.eraseMemory(MemoryModuleType.IS_EMERGING);
        brain.setMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get(), brain.getMemory(ModMemoryModuleType.STAGE_STUN_COUNT.get()).orElse(0) +1);
        super.stop(level, entity, gameTime);
    }
}
