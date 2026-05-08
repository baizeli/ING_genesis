package miku.united_as_one.genesis.registries;

import miku.united_as_one.genesis.registries.client.ParticleRegistry;
import miku.united_as_one.genesis.registries.entity.ai.ModActivity;
import miku.united_as_one.genesis.registries.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.registries.spell.SpellAttributesRegistry;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
import miku.united_as_one.genesis.registries.workbench.ModBlockEntities;
import miku.united_as_one.genesis.registries.workbench.ModMenuTypes;
import miku.united_as_one.genesis.registries.workbench.ModRecipeSerializers;
import miku.united_as_one.genesis.registries.workbench.ModRecipeTypes;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ModRegistries {
    private ModRegistries() {
    }

    public static void register(IEventBus modEventBus) {
        // 网络包没有 DeferredRegister，先显式初始化通道和消息。
        ModNetworkRegistry.register();

        // 基础内容注册：物品、创造标签页、实体、方块、流体和声音。
        ItemRegistry.register();
        CreativeTabRegistry.register(modEventBus);
        EntityRegistry.register(modEventBus);
        BlockRegistry.register();
        FluidRegistry.register();
        SoundRegister.register(modEventBus);

        // 法术系统注册：法术流派、自定义属性、状态效果和客户端粒子。
        SpellSchoolRegistry.register(modEventBus);
        SpellAttributesRegistry.register(modEventBus);
        EffectRegistry.register(modEventBus);
        ParticleRegistry.register(modEventBus);

        // 奥术工作台相关注册：方块实体、菜单、配方类型和配方序列化器。
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);

        // Boss AI 相关注册：自定义活动、记忆模块和实体属性创建事件。
        ModActivity.register(modEventBus);
        ModMemoryModuleType.register(modEventBus);
        EntityAttributeRegistry.register(modEventBus);
    }
}
