package miku.united_as_one.genesis.contents.entity.boss.behavior;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import miku.united_as_one.genesis.registries.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.contents.entity.boss.BloodBoss;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

/**
 * 用于处理具有特定动画、施法前摇和冷却时间的 Boss 行为。
 *
 * @param <E> 必须同时是 Mob, IMagicEntity 和 IAnimatedAttacker 的实体类型
 */
public abstract class AnimatedActionBehavior<E extends Mob & IMagicEntity & IAnimatedAttacker> extends Behavior<E> {

    // 用于追踪动画播放进度的计时器
    protected int abilityTimer;
    // 用于基于世界时间计算冷却
    protected long nextAttackGameTime;

    /**
     * @param entryCondition 进入该行为所需的记忆状态
     */
    public AnimatedActionBehavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
        // 设置一个较长的最大持续时间 (1200 ticks / 60秒)，
        // 确保 Behavior 系统不会在动画结束前通过 timedOut 强制停止行为。
        // 实际的停止逻辑由 canStillUse 中的 getActionDuration 控制。
        super(entryCondition, 1200);
        this.nextAttackGameTime = 0;
    }

    /**
     * 检查是否满足开始条件：
     * 1. 必须拥有所需的记忆 (由 super.tryStart 处理)
     * 2. 冷却时间已结束
     * 3. 具体实现类的 canStartAction 返回 true
     */
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
        long gameTime = level.getGameTime();
        if (owner instanceof BloodBoss boss){
            if (boss.isReallyCasting()){
                return false;
            }
        }

        // 检查冷却是否结束，以及自定义条件，并且确保实体当前没有在施法
        return gameTime >= this.nextAttackGameTime && this.canStartAction(owner) && !owner.isCasting();
    }


    /**
     * 行为能否继续运行。
     * 只要 abilityTimer 未超过动画总时长，行为就继续。
     */
    @Override
    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return this.abilityTimer < this.getActionDuration();
    }

    /**
     * 行为开始时触发：
     * 1. 重置动画计时器
     * 2. 向服务端发送动画触发指令
     */
    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        this.abilityTimer = 0;


        // 触发动画
        entity.serverTriggerAnimation(this.getAnimationId());
        entity.getBrain().setMemory(ModMemoryModuleType.IS_CASTING_SKILL.get(), true);
    }

    /**
     * 每 tick 执行逻辑：
     * 1. 锁定视线到目标
     * 2. 在特定时间点 (Timestamp) 执行动作 (doAction)
     * 3. 更新计时器
     */
    @Override
    protected void tick(ServerLevel level, E owner, long gameTime) {

        LivingEntity target = owner.getTarget();
        if (target != null) {
            owner.getLookControl().setLookAt(target);
        }

        // 在指定的时间点触发具体动作
        if (this.abilityTimer == this.getActionTimestamp()) {
            this.doAction(owner);
        }

        this.abilityTimer++;
    }

    /**
     * 行为结束时触发：
     * 设置下一次允许攻击的时间 (当前时间 + 冷却时间)
     */
    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        entity.getBrain().eraseMemory(ModMemoryModuleType.IS_CASTING_SKILL.get());
        super.stop(level, entity, gameTime);
        // 设定冷却时间
        this.nextAttackGameTime = gameTime + this.getCooldown();
        this.abilityTimer = 0;
    }

    // ==========================================
    // 抽象方法定义
    // ==========================================

    /**
     * 是否可以开始动作 (例如检查法力值、视线等)
     */
    protected abstract boolean canStartAction(E entity);

    /**
     * 动画开始后第几 tick 触发 doAction
     */
    protected abstract int getActionTimestamp();

    /**
     * 整个行为/动画的持续时间 (ticks)
     */
    protected abstract int getActionDuration();

    /**
     * 行为结束后的冷却时间 (ticks)
     */
    protected abstract int getCooldown();

    /**
     * 动画 ID (用于 GeckoLib 或其他动画系统)
     */
    protected abstract String getAnimationId();

    /**
     * 执行具体的技能逻辑
     */
    protected abstract void doAction(E entity);
}