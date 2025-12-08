package com.baizeli.eternisstarrysky.Content.Workbenchs;

import com.baizeli.eternisstarrysky.Content.ArcaneWorkbench.ArcaneWorkbenchBlockEntity;
import com.baizeli.eternisstarrysky.Content.ArcaneWorkbench.ArcaneWorkbenchMenu;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, EternisStarrySky.MOD_ID);

    public static final RegistryObject<MenuType<VanillaWorkbenchMenu>> VANILLA_WORKBENCH_MENU =
            MENUS.register("vanilla_workbench_menu", () ->
                    IForgeMenuType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        VanillaWorkbenchBlockEntity entity = (VanillaWorkbenchBlockEntity) inv.player.level()
                                .getBlockEntity(pos);
                        return new VanillaWorkbenchMenu(windowId, inv, entity, pos);
                    }));

    public static final RegistryObject<MenuType<ArcaneWorkbenchMenu>> ARCANE_WORKBENCH_MENU =
            MENUS.register("arcane_workbench_menu", () ->
                    IForgeMenuType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        ArcaneWorkbenchBlockEntity entity = (ArcaneWorkbenchBlockEntity) inv.player.level()
                                .getBlockEntity(pos);
                        return new ArcaneWorkbenchMenu(ModMenuTypes.ARCANE_WORKBENCH_MENU.get(), windowId, inv, entity);
                    }));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}