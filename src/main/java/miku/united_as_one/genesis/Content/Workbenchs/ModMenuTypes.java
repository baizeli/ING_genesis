package miku.united_as_one.genesis.Content.Workbenchs;

import miku.united_as_one.genesis.Content.ArcaneWorkbench.ArcaneWorkbenchMenu;
import miku.united_as_one.genesis.Content.ArcaneWorkbench.ArcaneWorkbenchBlockEntity;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Genesis.MOD_ID);

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