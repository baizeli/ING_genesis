package miku.united_as_one.genesis.entity.boss;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import miku.united_as_one.genesis.entity.ai.ModActivity;
import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.entity.boss.behavior.*;
import miku.united_as_one.genesis.entity.boss.behavior.bloodbossskill.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.warden.Emerging;
import net.minecraft.world.entity.ai.behavior.warden.SetWardenLookTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.schedule.Activity;

import java.util.*;
import java.util.function.Predicate;


public class BloodBossAi {

    static int emergePriority = 150;
    static int idleStartPriority = 100;
    static int fightStartPriority = 50;
    protected static ImmutableList<MemoryModuleType<?>> MEMORY_TYPES;
    protected static ImmutableList<SensorType<? extends Sensor<? super BloodBoss>>> SENSOR_TYPES;
    static {
        {//传感器
            SENSOR_TYPES = ImmutableList.of(
                    SensorType.NEAREST_LIVING_ENTITIES,//获取最近的实体
                    SensorType.NEAREST_PLAYERS,//获取最近的玩家
                    SensorType.NEAREST_ITEMS,//获取最近的物品
                    SensorType.HURT_BY);//获取伤害来源
        }
        {
            MEMORY_TYPES=ImmutableList.of(
                    MemoryModuleType.LOOK_TARGET,                           // 存储实体需要看向的目标
                    MemoryModuleType.DOORS_TO_CLOSE,                        // 存储需要关闭的门
                    MemoryModuleType.NEAREST_LIVING_ENTITIES,              // 存储最近的存活实体列表
                    MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,      // 存储最近可见的存活实体列表
                    MemoryModuleType.NEAREST_VISIBLE_PLAYER,               // 存储最近可见的玩家
                    MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,    // 存储最近可见的可攻击玩家
                    MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,        // 存储最近可见的成年猪灵
                    MemoryModuleType.NEARBY_ADULT_PIGLINS,                 // 存储附近的成年猪灵
                    MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,          // 存储最近可见的想要的物品
                    MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,           // 存储物品拾取冷却时间
                    MemoryModuleType.HURT_BY,                              // 存储造成伤害的来源
                    MemoryModuleType.HURT_BY_ENTITY,
                    MemoryModuleType.WALK_TARGET,                  // 存储行走目标位置
                    MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, // 存储无法到达行走目标的开始时间
                    MemoryModuleType.ATTACK_TARGET,                // 存储攻击目标
                    MemoryModuleType.ATTACK_COOLING_DOWN,          // 存储攻击冷却时间
                    MemoryModuleType.INTERACTION_TARGET,           // 存储交互目标
                    MemoryModuleType.PATH,                         // 存储当前路径信息
                    MemoryModuleType.ANGRY_AT,                     // 存储愤怒目标（特定于通用愤怒）
                    MemoryModuleType.UNIVERSAL_ANGER,              // 存储通用愤怒状态
                    MemoryModuleType.AVOID_TARGET,                 // 存储需要避免的目标
                    MemoryModuleType.ADMIRING_ITEM,                // 存储正在欣赏的物品
                    MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, // 存储尝试到达欣赏物品的时间
                    MemoryModuleType.ADMIRING_DISABLED,            // 存储物品欣赏功能是否被禁用
                    MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM,  // 存储是否禁用走向欣赏物品
                    MemoryModuleType.CELEBRATE_LOCATION,           // 存储庆祝位置
                    MemoryModuleType.DANCING,                      // 存储跳舞状态
                    MemoryModuleType.HUNTED_RECENTLY,              // 存储最近狩猎状态
                    MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN,  // 存储最近可见的幼年疣猪兽
                    MemoryModuleType.NEAREST_VISIBLE_NEMESIS,      // 存储最近可见的宿敌(例如:卫道士对玩家)
                    MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,    // 存储最近可见的僵尸化实体
                    MemoryModuleType.RIDE_TARGET,                  // 存储骑乘目标
                    MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, // 存储最近可见的可狩猎疣猪兽
                    MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, // 存储最近可见的未穿戴黄金的可攻击玩家
                    MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, // 存储最近持有想要物品的玩家
                    MemoryModuleType.ATE_RECENTLY,                 // 存储最近进食状态
                    MemoryModuleType.NEAREST_REPELLENT,          // 存储最近的驱避物
                    MemoryModuleType.IS_EMERGING,                   // 存储出场动画状态
                    ModMemoryModuleType.ENTITY_TYPE_COUNT.get(),//杀死过的实体种类
                    ModMemoryModuleType.BOSS_STAGE.get(),//阶段
                    ModMemoryModuleType.BOOLEAN_TEST_MEMORY_MODULE.get(),//布尔
                    ModMemoryModuleType.NBT_TEST_MEMORY_MODULE.get(),//NBT
                    ModMemoryModuleType.INT_TEST_MEMORY_MODULE.get(),//INT
                    ModMemoryModuleType.IS_CASTING_SKILL.get(),//标记正在释放boss技能
                    ModMemoryModuleType.HAS_PLAYED_STAGE_TRANSITION.get(),//当前阶段是否播放了转阶段动画
                    ModMemoryModuleType.STAGE_STUN_COUNT.get()//当前阶段硬直次数

                     );
        }

    }


    /**
     * 创建BloodBoss的Brain
     *
     * @param BloodBoss
     * @param dynamic
     * @return
     */
    public static Brain<BloodBoss> makeBrain(BloodBoss BloodBoss, Dynamic<?> dynamic){
        Brain.Provider<BloodBoss> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);

        Brain<BloodBoss> brain = provider.makeBrain(dynamic);
        addCoreActivities(brain);
        addIdleActivities(brain, BloodBoss);
        addEmergeActivity(brain);
        addFightActivities(brain);
        addFightStage2Activities(brain);
        addSleepActivities(brain);
        // 设置核心活动和默认活动
        brain.setCoreActivities(ImmutableSet.of(
                Activity.CORE
        ));
        brain.setDefaultActivity(Activity.IDLE);


        brain.useDefaultActivity();

        return brain;
    }

    private static void addSleepActivities(Brain<BloodBoss> brain) {
    }

    /**
     * 更新BloodBoss的活动
     * @param BloodBoss BloodBoss实体
     */
    public static void updateActivity(BloodBoss BloodBoss) {
        if (BloodBoss.getBrain().getMemory(ModMemoryModuleType.BOSS_STAGE.get()).orElse(0)<=1){
            BloodBoss.getBrain().setActiveActivityToFirstValid(
                    ImmutableList.of(
                            Activity.EMERGE,
                            Activity.FIGHT,
                            Activity.IDLE
                    )
            );
        }else {
            BloodBoss.getBrain().setActiveActivityToFirstValid(
                    ImmutableList.of(
                            Activity.EMERGE,
                            ModActivity.FIGHT_STAGE2.get(),
                            Activity.IDLE
                    )
            );
        }


    }
    private static void addEmergeActivity(Brain<BloodBoss> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.EMERGE, emergePriority,
                ImmutableList.of(
                        new BloodBossEmergingBehavior(),        // spawn
                        new BloodBossStunBehavior(),            // 僵直
                        new BloodBossStageTransitionBehavior()  // 转阶段
                ),
                MemoryModuleType.IS_EMERGING);
    }
    private static void addFightActivities(Brain<BloodBoss> brain) {
        Activity activity = Activity.FIGHT;
        int i = fightStartPriority;

        ImmutableList.Builder<BehaviorControl<? super BloodBoss>> fightBuilder = ImmutableList.builder();

        List<AbstractSpell> spellList = List.of(
                SpellRegistry.BLOOD_SLASH_SPELL.get(),
                SpellRegistry.BLOOD_NEEDLES_SPELL.get()

        );

        SpellCastingBehavior spellCasting = new SpellCastingBehavior(
                spellList,
                5*20,  // 冷却时间
                20.0f // 最大施法距离
        );

        //清除无效目标
        fightBuilder.add(StopAttackingIfTargetInvalid.<BloodBoss>create(
                livingEntity -> false, (mob, target) -> {}, true));

        // 添加一阶段追击行为：当敌人离开8格范围时追击
        fightBuilder.add(SetWalkTargetFromAttackTargetIfTargetOutOfReach.<BloodBoss>create(1.0F));

        fightBuilder.add(new DragonDiveBehavior());          //下落攻击
        fightBuilder.add(new LightningWhirlSlashBehavior()); //闪电旋风劈
        fightBuilder.add(new BloodBossGrabBehavior());       //抓取技能
        fightBuilder.add(new DoubleSlashBehavior());         //二连斩技能
        fightBuilder.add(new ZhanZhanCycloneSlashBehavior());//斩斩旋风劈技能
        fightBuilder.add(new GroundSlamBehavior());          //砸地技能
        fightBuilder.add(new StompBehavior());               //跺脚技能
        fightBuilder.add(spellCasting);               //跺脚技能

        ImmutableList<BehaviorControl<? super BloodBoss>> fightBehaviors = fightBuilder.build();

        brain.addActivityAndRemoveMemoryWhenStopped(
                activity,
                i,
                fightBehaviors,
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static void addFightStage2Activities(Brain<BloodBoss> brain) {
        Activity activity = ModActivity.FIGHT_STAGE2.get();
        int i = fightStartPriority;

        // 创建施法行为列表
        ImmutableList<SpellCastingBehavior> spellBehaviors = createSpellCastingBehaviors(
                createSpellData(SpellRegistry.BLOOD_SLASH_SPELL.get(), 8 * 20, 20.0f),
                createSpellData(SpellRegistry.BLOOD_NEEDLES_SPELL.get(), 8 * 20, 18.0f),
                createSpellData(SpellRegistry.WITHER_SKULL_SPELL.get(), 8 * 20, 25.0f),
                createSpellData(SpellRegistry.ACUPUNCTURE_SPELL.get(), 8 * 20, 15.0f),
                createSpellData(SpellRegistry.SONIC_BOOM_SPELL.get(), 8 * 20, 16.0f),
                createSpellData(SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 8 * 20, 22.0f)
        );

        ImmutableList.Builder<BehaviorControl<? super BloodBoss>> fightBuilder = ImmutableList.builder();

        //清除无效目标
        fightBuilder.add(StopAttackingIfTargetInvalid.<BloodBoss>create(livingEntity -> false, (mob, target) -> {}, true));

        // 添加二阶段远离行为：保持8格距离
        fightBuilder.add(BackUpIfTooClose.<BloodBoss>create(16, 5F)); // 8格距离

        fightBuilder.add(new DragonDiveBehavior());          //下落攻击
        fightBuilder.add(new TentacleAttackBehavior());      //触手攻击
        fightBuilder.add(new TentacleGrabBehavior());        //触手抓取
//        fightBuilder.add(new LightningWhirlSlashBehavior()); //闪电旋风劈
//        fightBuilder.add(new BloodBossGrabBehavior());       //抓取技能
        fightBuilder.add(new DoubleSlashBehavior());         //二连斩技能
        fightBuilder.add(new ZhanZhanCycloneSlashBehavior());//斩斩旋风劈技能
        fightBuilder.add(new GroundSlamBehavior());          //砸地技能
        fightBuilder.add(new StompBehavior());               //跺脚技能

        // 添加所有施法行为
        for (SpellCastingBehavior behavior : spellBehaviors) {
            fightBuilder.add(behavior);
        }

        ImmutableList<BehaviorControl<? super BloodBoss>> fightBehaviors = fightBuilder.build();

        brain.addActivityAndRemoveMemoryWhenStopped(
                activity,
                i,
                fightBehaviors,
                MemoryModuleType.ATTACK_TARGET
        );
    }

    /**
     * 创建法术数据，包含法术、冷却时间和最大施法距离
     * 
     * @param spell 法术
     * @param cooldown 冷却时间
     * @param maxCastDistance 最大施法距离
     * @return 法术数据
     */
    private static Pair<AbstractSpell, Pair<Integer, Float>> createSpellData(AbstractSpell spell, int cooldown, float maxCastDistance) {
        return Pair.of(spell, Pair.of(cooldown, maxCastDistance));
    }

    /**
     * 根据法术、冷却时间和施法距离创建施法行为列表
     * 
     * @param spellData 包含法术、冷却时间和最大施法距离的数据
     * @return 施法行为列表
     */
    @SafeVarargs
    private static ImmutableList<SpellCastingBehavior> createSpellCastingBehaviors(Pair<AbstractSpell, Pair<Integer, Float>>... spellData) {
        ImmutableList.Builder<SpellCastingBehavior> builder = ImmutableList.builder();
        
        for (Pair<AbstractSpell, Pair<Integer, Float>> data : spellData) {
            AbstractSpell spell = data.getFirst();
            int cooldown = data.getSecond().getFirst();
            float maxCastDistance = data.getSecond().getSecond();
            
            SpellCastingBehavior behavior = new SpellCastingBehavior(
                List.of(spell),
                cooldown,
                maxCastDistance
            );
            
            builder.add(behavior);
        }
        
        return builder.build();
    }

    private static void addIdleActivities(Brain<BloodBoss> brain, BloodBoss BloodBoss) {
        brain.addActivity(Activity.IDLE, idleStartPriority,
            ImmutableList.of(
                // 随机游走
                new RunOne<>(ImmutableList.of(
                        Pair.of(RandomStroll.swim(1.5F), 1),
                        Pair.of(RandomStroll.stroll(1F, false), 1)
                )),
                // 寻找附近实体 - 触发战斗
                new SelectTargetBehavior(),
                // 仅设置看向目标，不移动
                SetEntityLookTarget.create(
                        (entity) -> isTarget(BloodBoss, entity),
                        (float) BloodBoss.getAttributeValue(Attributes.FOLLOW_RANGE))
            )
        );
    }

    private static boolean isTarget(BloodBoss BloodBoss, LivingEntity entity) {

        boolean isTarget = BloodBoss.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET)
                .filter(target -> target == entity)
                .isPresent();
        return isTarget;
    }

    private static Optional<? extends LivingEntity> findNearestValidTarget(BloodBoss BloodBoss) {
        Optional<? extends LivingEntity> target = BloodBoss.getBrain()
                .getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                .flatMap(entities -> entities.findClosest(isValidTarget(BloodBoss)));
        return target;
    }


    private static Predicate<LivingEntity> isValidTarget(BloodBoss BloodBoss) {
        Predicate<LivingEntity> targetPredicate = target ->
                target instanceof Animal &&
                        target.isAlive() &&
                        !target.isInvulnerable() &&
                        BloodBoss.canAttack(target);
        return targetPredicate;
    }

    private static void addCoreActivities(Brain<BloodBoss> brain) {
        //无论切换到哪个活动，核心活动都会保持激活状态
        brain.addActivity(Activity.CORE, 0,
            //行为列表
            //在同一个活动内，每tick并行执行多个行为,每次都检查行为开始的条件
            //不同活动之间是并行的，所以核心活动和主活动可以各执行一个行为
            ImmutableList.of(
                new BloodBossStageStunCoreBehavior(),
                SetWardenLookTarget.create(),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(100, 200)
            )
        );

    }

    // 添加这些方法到类中
    private static float getSpeedModifierChasing(LivingEntity entity) {
        return entity.isInWaterOrBubble() ? 0.9F : 1.0F;
    }

    private static float getSpeedModifier(LivingEntity entity) {
        return entity.isInWaterOrBubble() ? 1.0F : 1.5F;
    }


    public static void RecordTheNumberSwallowed(Brain<BloodBoss> brain, LivingEntity entity) {
        // 获取实体类型计数的内存，如果存在则更新计数，否则创建新的计数映射
        brain.getMemory(ModMemoryModuleType.ENTITY_TYPE_COUNT.get())
                .ifPresentOrElse(map -> {
                    // 如果内存中已存在实体类型计数映射，则将当前被击杀实体的类型计数加1
                    map.merge(entity.getType(), 1, Integer::sum);
                }, () -> {
                    // 如果内存中不存在实体类型计数映射，则创建一个新的HashMap
                    Map<EntityType<?>, Integer> map = new HashMap<>();
                    // 将当前被击杀实体的类型作为键，计数值设为1
                    map.put(entity.getType(), 1);
                    // 将新创建的计数映射存入实体的脑部记忆中
                    brain.setMemory(ModMemoryModuleType.ENTITY_TYPE_COUNT.get(), map);
                });
    }

    public static void includedInNbtTestMemoryModule(Brain<BloodBoss> brain, LivingEntity entity) {
        Optional<List<CompoundTag>> memory = brain.getMemory(ModMemoryModuleType.NBT_TEST_MEMORY_MODULE.get());
        List<CompoundTag> entityInStomach = memory.orElse(new ArrayList<CompoundTag>());
        CompoundTag tag = new CompoundTag();
        entity.save(tag);
        entityInStomach.add((tag));
        brain.setMemory(ModMemoryModuleType.NBT_TEST_MEMORY_MODULE.get(), entityInStomach);
    }
}
