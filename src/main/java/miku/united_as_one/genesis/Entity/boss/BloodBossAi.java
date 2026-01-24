package miku.united_as_one.genesis.entity.boss;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import miku.united_as_one.genesis.entity.ai.ModActivity;
import miku.united_as_one.genesis.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.entity.boss.behavior.SelectTargetBehavior;
import miku.united_as_one.genesis.entity.boss.behavior.SpellCastingBehavior;
import miku.united_as_one.genesis.spell.Spells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.schedule.Activity;

import java.util.*;
import java.util.function.Predicate;


public class BloodBossAi {
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
                    ModMemoryModuleType.ENTITY_TYPE_COUNT.get(),//杀死过的实体种类
                    ModMemoryModuleType.BOSS_STAGE.get(),//阶段
                    ModMemoryModuleType.BOOLEAN_TEST_MEMORY_MODULE.get(),//布尔
                    ModMemoryModuleType.NBT_TEST_MEMORY_MODULE.get(),//NBT
                    ModMemoryModuleType.INT_TEST_MEMORY_MODULE.get()//INT
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

        addFightActivities(brain);
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
        BloodBoss.getBrain().setActiveActivityToFirstValid(
                ImmutableList.of(
                        Activity.FIGHT,
                        Activity.IDLE
                )
        );
    }


    private static void addFightActivities(Brain<BloodBoss> brain) {
        Activity activity = Activity.FIGHT;
        int i = fightStartPriority;

        List<AbstractSpell> spellList = List.of(
                SpellRegistry.BLOOD_SLASH_SPELL.get(),
                SpellRegistry.BLOOD_NEEDLES_SPELL.get(),
                SpellRegistry.WITHER_SKULL_SPELL.get(),
                SpellRegistry.ACUPUNCTURE_SPELL.get(),
//                SpellRegistry.SONIC_BOOM_SPELL.get(),
                SpellRegistry.ELDRITCH_BLAST_SPELL.get()

/*                SpellRegistry.FIREBALL_SPELL.get(),

                SpellRegistry.BLOOD_STEP_SPELL.get(),
                SpellRegistry.DEVOUR_SPELL.get(),
                SpellRegistry.HEARTSTOP_SPELL.get(),
                SpellRegistry.RAY_OF_SIPHONING_SPELL.get(),

                SpellRegistry.DRAGON_BREATH_SPELL.get(),
                SpellRegistry.MAGIC_ARROW_SPELL.get(),
                SpellRegistry.MAGIC_MISSILE_SPELL.get(),
                SpellRegistry.TELEPORT_SPELL.get(),
                SpellRegistry.ECHOING_STRIKES_SPELL.get(),
                SpellRegistry.SHADOW_SLASH.get(),
                SpellRegistry.CHAIN_CREEPER_SPELL.get(),
                SpellRegistry.FANG_STRIKE_SPELL.get(),
                SpellRegistry.FIRECRACKER_SPELL.get(),
                SpellRegistry.GUST_SPELL.get(),
                SpellRegistry.LOB_CREEPER_SPELL.get(),
                SpellRegistry.SLOW_SPELL.get(),
                SpellRegistry.ARROW_VOLLEY_SPELL.get(),
                SpellRegistry.THROW_SPELL.get(),
                SpellRegistry.BLAZE_STORM_SPELL.get(),
                SpellRegistry.BURNING_DASH_SPELL.get(),
                SpellRegistry.FIREBOLT_SPELL.get(),
                SpellRegistry.FIRE_BREATH_SPELL.get(),
                SpellRegistry.MAGMA_BOMB_SPELL.get(),
                SpellRegistry.WALL_OF_FIRE_SPELL.get(),
                SpellRegistry.HEAT_SURGE_SPELL.get(),
                SpellRegistry.FLAMING_STRIKE_SPELL.get(),
                SpellRegistry.SCORCH_SPELL.get(),
                SpellRegistry.FLAMING_BARRAGE_SPELL.get(),
                SpellRegistry.FIRE_ARROW_SPELL.get(),
                SpellRegistry.ICICLE_SPELL.get(),
                SpellRegistry.RAY_OF_FROST_SPELL.get(),
                SpellRegistry.FROSTWAVE_SPELL.get(),
                SpellRegistry.ICE_SPIKES_SPELL.get(),
                SpellRegistry.ICE_TOMB_SPELL.get(),
                SpellRegistry.SNOWBALL_SPELL.get(),
                SpellRegistry.FROSTBITE_SPELL.get(),
                SpellRegistry.CHAIN_LIGHTNING_SPELL.get(),
                SpellRegistry.ELECTROCUTE_SPELL.get(),
                SpellRegistry.LIGHTNING_BOLT_SPELL.get(),
                SpellRegistry.LIGHTNING_LANCE_SPELL.get(),
                SpellRegistry.SHOCKWAVE_SPELL.get(),
                SpellRegistry.THUNDERSTORM_SPELL.get(),
                SpellRegistry.BALL_LIGHTNING_SPELL.get(),
                SpellRegistry.VOLT_STRIKE_SPELL.get(),
                SpellRegistry.ACID_ORB_SPELL.get(),
                SpellRegistry.BLIGHT_SPELL.get(),
                SpellRegistry.POISON_ARROW_SPELL.get(),
                SpellRegistry.POISON_BREATH_SPELL.get(),
                SpellRegistry.POISON_SPLASH_SPELL.get(),
                SpellRegistry.ROOT_SPELL.get(),
                SpellRegistry.SPIDER_ASPECT_SPELL.get(),
                SpellRegistry.FIREFLY_SWARM_SPELL.get(),
                SpellRegistry.EARTHQUAKE_SPELL.get(),
                SpellRegistry.STOMP_SPELL.get(),
                SpellRegistry.TOUCH_DIG.get(),
                SpellRegistry.TELEKINESIS_SPELL.get()*/

        );

        SpellCastingBehavior spellCasting = new SpellCastingBehavior(
                spellList,
                10,  // 冷却时间
                20.0f // 最大施法距离
        );

        brain.addActivityAndRemoveMemoryWhenStopped(
                activity,//要添加的活动为蘸豆活动
                i,
                ImmutableList.of(
                        // 停止攻击无效目标
                        StopAttackingIfTargetInvalid.create(
                                livingEntity -> false,
                                (mob, target) -> {},
                                true
                        ),
                        spellCasting
                ),
                MemoryModuleType.ATTACK_TARGET
        );
/*        brain.addActivityAndRemoveMemoryWhenStopped(
                activity,//要添加的活动为蘸豆活动
                i,
                ImmutableList.of(
                        // 停止攻击无效目标
                        StopAttackingIfTargetInvalid.create(
                                livingEntity -> false,
                                (mob, target) -> {},
                                true
                        ),
                        // 移动向目标（从IDLE移到FIGHT）
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(BloodBossAi::getSpeedModifierChasing),

                        // 近战攻击
                        MeleeAttack.create(20),
                        // 备用行为
                        new RunOne<>(ImmutableList.of(
                                Pair.of(RandomStroll.stroll(1.0F), 2),
                                Pair.of(new DoNothing(10, 20), 1)
                        ))
                ),
                MemoryModuleType.ATTACK_TARGET
        );*/
    }

    private static void addIdleActivities(Brain<BloodBoss> brain, BloodBoss BloodBoss) {
        brain.addActivity(Activity.IDLE, idleStartPriority,
            ImmutableList.of(
                // 随机游走
                new RunOne<>(ImmutableList.of(
                        Pair.of(RandomStroll.swim(1.5F), 1),
                        Pair.of(RandomStroll.stroll(1F, false), 1),
                        Pair.of(new DoNothing(1500, 3000), 3)
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
            //在同一个活动内，每tick只执行一个行为
            //不同活动之间是并行的，所以核心活动和主活动可以各执行一个行为
            ImmutableList.of(
//                new AnimalPanic(2.0F),
                new LookAtTargetSink(45, 90)
//                new MoveToTargetSink(100, 200)
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
