package com.baizeli.eternisstarrysky.Content;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.Set;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EternisStarrySky.MODID);
    public static final CreativeModeTab.Builder build = CreativeModeTab.builder();

    static {
        MutableComponent mutableComponent = Component.translatable(
                ModLang.TranslatableMessage.CREATIVE_TAB_NAME.getKey()
        );
        build.title(mutableComponent);
        Set<RegistryObject<Item>> registryEntries = new LinkedHashSet<>(ModItems.ITEMS.getEntries());
        build.icon(() -> ModItems.GALAXY_SCROLL.get().getDefaultInstance());
        build.displayItems(((itemDisplayParameters, output) ->
                registryEntries.forEach(itemRegistryObject ->
                        output.accept(itemRegistryObject.get())
                )
        ));

        REGISTRY.register(EternisStarrySky.MODID, build::build);
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
