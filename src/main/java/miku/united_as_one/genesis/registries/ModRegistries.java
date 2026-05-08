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
        ModNetworkRegistry.register();

        ItemRegistry.register();
        CreativeTabRegistry.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        BlockRegistry.register();
        FluidRegistry.register();
        SoundRegister.SOUND_EVENTS.register(modEventBus);

        SpellSchoolRegistry.register(modEventBus);
        SpellAttributesRegistry.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        EffectRegistry.register(modEventBus);
        ParticleRegistry.register(modEventBus);

        ModActivity.register(modEventBus);
        ModMemoryModuleType.register(modEventBus);
        EntityAttributeRegistry.register(modEventBus);
    }
}
