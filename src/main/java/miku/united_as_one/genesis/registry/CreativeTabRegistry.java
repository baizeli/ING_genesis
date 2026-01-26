package miku.united_as_one.genesis.registry;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.content.ModLang;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Genesis.MODID);
    public static final CreativeModeTab.Builder build = CreativeModeTab.builder();

    static {
        build.title(Component.translatable(
            ModLang.TranslatableMessage.CREATIVE_TAB_NAME.getKey()
        ));
        build.icon(() -> ItemRegistry.GALAXY_SCROLL.get().getDefaultInstance());
        build.displayItems(((itemDisplayParameters, output) -> {
            ItemRegistry.ITEMS.getEntries().forEach(itemRegistryObject ->
                output.accept(itemRegistryObject.get())
            );

            Genesis.L2_REGISTRATE.getAll(Registries.ITEM).forEach(entry -> {
                output.accept(entry.get());
            });
        }));

        REGISTRY.register(Genesis.MODID, build::build);
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}