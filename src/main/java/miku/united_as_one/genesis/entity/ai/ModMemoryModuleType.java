package miku.united_as_one.genesis.entity.ai;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

import miku.united_as_one.genesis.util.codec.CodecUtils;
import static miku.united_as_one.genesis.Genesis.MODID;
public class ModMemoryModuleType {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES =
            DeferredRegister.create(ForgeRegistries.Keys.MEMORY_MODULE_TYPES, MODID);
    // 实体类型数量Codec
    static Codec<Map<EntityType<?>, Integer>> ENTITY_TYPE_COUNT_CODEC;

    // 记录生物列表
    public static final RegistryObject<MemoryModuleType<Map<EntityType<?>, Integer>>> ENTITY_TYPE_COUNT;
    // 记录Boss阶段
    public static final RegistryObject<MemoryModuleType<Integer>> BOSS_STAGE;
    // 测试记录整数
    public static final RegistryObject<MemoryModuleType<Integer>> INT_TEST_MEMORY_MODULE;
    // 测试记录布尔
    public static final RegistryObject<MemoryModuleType<Boolean>> BOOLEAN_TEST_MEMORY_MODULE;

    // 测试记录NBT列表
    public static final RegistryObject<MemoryModuleType<List<CompoundTag>>> NBT_TEST_MEMORY_MODULE;

    //codec
    static {
        ENTITY_TYPE_COUNT_CODEC = CodecUtils.createEntityTypeCountCodec();
    }

    //
    static {
        ENTITY_TYPE_COUNT = MEMORY_MODULES.register("entities_killed_insatiable_hunger",
                () -> new MemoryModuleType<>(Optional.of(ENTITY_TYPE_COUNT_CODEC)));

        BOSS_STAGE = MEMORY_MODULES.register("boss_stage",
                () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
        BOOLEAN_TEST_MEMORY_MODULE = MEMORY_MODULES.register("boolean_test_memory_module",
                () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));
        INT_TEST_MEMORY_MODULE = MEMORY_MODULES.register("int_test_memory_module",
                () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
        NBT_TEST_MEMORY_MODULE = MEMORY_MODULES.register("nbt_test_memory_module",
                () -> new MemoryModuleType<>(Optional.of(CompoundTag.CODEC.listOf())));
    }

    public static void register(IEventBus modEventBus) {
        MEMORY_MODULES.register(modEventBus);
    }




}
