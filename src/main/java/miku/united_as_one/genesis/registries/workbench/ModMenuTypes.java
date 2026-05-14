package miku.united_as_one.genesis.registries.workbench;

import miku.united_as_one.genesis.contents.workbench.arcane.ArcaneWorkbenchMenu;
import miku.united_as_one.genesis.contents.workbench.arcane.ArcaneWorkbenchBlockEntity;
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

    // 奥术工作台菜单：从网络缓冲读取方块坐标，再绑定对应方块实体。
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
