package miku.united_as_one.genesis.entity.boss.behavior;

import com.google.common.collect.ImmutableMap;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import miku.united_as_one.genesis.entity.boss.BloodBoss;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.List;
import java.util.Optional;

/**
 * 施法行为
 */
public class SpellCastingBehavior extends Behavior<BloodBoss> {
    private final List<AbstractSpell> availableSpells;
    private final int cooldownTicks;
    private final float maxCastDistanceSq;
    private long lastCastGameTime;

    public SpellCastingBehavior(List<AbstractSpell> spells, int cooldownTicks, float maxCastDistance) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
//                ,MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED
        ), 100);

        this.availableSpells = spells;
        this.cooldownTicks = cooldownTicks;
        this.maxCastDistanceSq = maxCastDistance * maxCastDistance;
        this.lastCastGameTime = -cooldownTicks;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BloodBoss owner) {
        // 检查冷却时间
        if (level.getGameTime() - lastCastGameTime < cooldownTicks) {
            return false;
        }

        // 检查是否已在施法中（AbstractSpellCastingMob的内置检查）
        if (owner.isCasting()) {
            return false;
        }

        Optional<LivingEntity> targetOpt = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (targetOpt.isEmpty()) {
            return false;
        }

        LivingEntity target = targetOpt.get();

        // 检查目标是否有效
        if (!target.isAlive() || !owner.canAttack(target)) {
            return false;
        }


        // 检查距离
        double distanceSq = owner.distanceToSqr(target);
        if (distanceSq > maxCastDistanceSq) {
            return false;
        }

        // 检查视线
        if (!Utils.hasLineOfSight(owner.level(), owner, target, true)) {
            return false;
        }

        return true;
    }


    @Override
    protected void start(ServerLevel level, BloodBoss owner, long gameTime) {
        Optional<LivingEntity> targetOpt = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (targetOpt.isEmpty()) {
            return;
        }

        LivingEntity target = targetOpt.get();

        // 选择法术
        AbstractSpell selectedSpell = selectSpell(owner, target);
        if (selectedSpell == null) {
            return;
        }

        // 计算法术等级
        int spellLevel = calculateSpellLevel(owner, selectedSpell);

        // 使用AbstractSpellCastingMob的内置方法开始施法
        // 这会自动处理动画、音效等
        owner.initiateCastSpell(selectedSpell, spellLevel);

        // 记录施法时间
        this.lastCastGameTime = gameTime;
    }

    @Override
    protected void tick(ServerLevel level, BloodBoss owner, long gameTime) {
        // 在施法期间持续看向目标
        Optional<LivingEntity> targetOpt = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (targetOpt.isPresent()) {
            LivingEntity target = targetOpt.get();

            // 强制看向目标，确保施法方向正确
            owner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BloodBoss owner, long gameTime) {
        // 如果仍在施法中，继续运行
        // AbstractSpellCastingMob的isCasting()方法会返回正确的状态
        return owner.isCasting();
    }

    @Override
    protected void stop(ServerLevel level, BloodBoss owner, long gameTime) {
        // 如果行为被中断，取消施法
        // AbstractSpellCastingMob会处理动画取消
        if (owner.isCasting()) {
            owner.cancelCast();
        }
    }

    /**
     * 选择要施放的法术
     */
    private AbstractSpell selectSpell(BloodBoss owner, LivingEntity target) {
        if (availableSpells.isEmpty()) {
            return null;
        }

        // 简单的随机选择策略
        int index = owner.getRandom().nextInt(availableSpells.size());
        return availableSpells.get(index);
    }

    /**
     * 计算法术等级
     */
    private int calculateSpellLevel(BloodBoss owner, AbstractSpell spell) {
        // 返回固定等级，可根据需要调整
        return 1;
    }
}