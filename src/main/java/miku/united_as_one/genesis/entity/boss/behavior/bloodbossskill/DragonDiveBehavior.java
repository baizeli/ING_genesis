package miku.united_as_one.genesis.entity.boss.behavior.bloodbossskill;

import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import miku.united_as_one.genesis.entity.boss.BloodBossMoveControl;
import miku.united_as_one.genesis.entity.boss.behavior.AnimatedActionBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class DragonDiveBehavior extends AnimatedActionBehavior<BloodBoss> {

    public static final String ANIMATION_ID = "dragon_slam"; // 龙！

    private static final int DURATION  = 24; 
    private static final int IMPACT_T  = 15; 
    private static final int COOLDOWN  = 5 * 20;

    private double groundY;
    private int diveTimer;
    private boolean impactDone;

    public DragonDiveBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
    }

    

    @Override
    protected boolean canStartAction(BloodBoss boss) {
        if (boss.onGround()) return false;

        groundY = findGroundY(boss);
        if (groundY < 0) return false;

        
        if (boss.getY() - groundY < 12) return false;

        
        AABB box = new AABB(
                boss.getX() - 8, groundY - 2, boss.getZ() - 8,
                boss.getX() + 8, groundY + 6, boss.getZ() + 8
        );

        return !boss.level().getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        ).isEmpty();
    }

    

    @Override
    protected void start(ServerLevel level, BloodBoss boss, long gameTime) {
        super.start(level, boss, gameTime);

        boss.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);

        diveTimer = 0;
        impactDone = false;

        boss.setDeltaMovement(Vec3.ZERO);
        boss.setNoGravity(true);

        if (boss.getMoveControl() instanceof BloodBossMoveControl move) {
            move.clearSkillMovements();
        }
    }

    

    @Override
    protected void tick(ServerLevel level, BloodBoss boss, long gameTime) {
        super.tick(level, boss, gameTime);
        diveTimer++;

        
        if (diveTimer < 6) {
            boss.setDeltaMovement(0, 0.6, 0);          
        } else if (diveTimer < 10) {
            boss.setDeltaMovement(0, -0.05, 0);       
        } else if (diveTimer < IMPACT_T) {
            boss.setDeltaMovement(0, -2.2, 0);        
            applyDivePressure(boss);                  
        }

        
        if (!impactDone && boss.getY() <= groundY + 0.1) {
            impactDone = true;
            boss.setPos(boss.getX(), groundY, boss.getZ());
            doImpact(level, boss);
        }
    }

    

    private void applyDivePressure(BloodBoss boss) {
        float yawRad = boss.yBodyRot * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));

        Vec3 center = boss.position().add(forward.scale(1.0));
        AABB box = AABB.ofSize(center, 3, 4, 3);

        List<LivingEntity> list = boss.level().getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity e : list) {
            e.setDeltaMovement(boss.getDeltaMovement());
            e.hasImpulse = true;
        }
    }

    

    private void doImpact(ServerLevel level, BloodBoss boss) {
        boss.setNoGravity(false);

        AABB box = AABB.ofSize(boss.position(), 5, 3, 5);
        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != boss && e.isAlive() && boss.canAttack(e)
        );

        for (LivingEntity t : targets) {
            boss.applySkillDamage(t, 3.0F);
            t.setDeltaMovement(t.getDeltaMovement().add(0, 0.5, 0));
        }
    }

    

    @Override
    protected void stop(ServerLevel level, BloodBoss boss, long gameTime) {
        super.stop(level, boss, gameTime);
        boss.getBrain().eraseMemory(ModMemoryModuleType.IS_CASTING_SKILL.get());
        boss.setNoGravity(false);
    }

    @Override protected int getActionTimestamp() { return 0; }
    @Override protected int getActionDuration() { return DURATION; }
    @Override protected int getCooldown() { return COOLDOWN; }
    @Override protected String getAnimationId() { return ANIMATION_ID; }
    @Override protected void doAction(BloodBoss entity) {}

    

    private double findGroundY(BloodBoss boss) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(
                boss.getX(), boss.getY(), boss.getZ()
        );

        for (int i = 0; i < 200; i++) {
            pos.move(0, -1, 0);
            BlockState state = boss.level().getBlockState(pos);
            if (!state.isAir()) {
                return pos.getY() + 1;
            }
        }
        return -1;
    }
}
