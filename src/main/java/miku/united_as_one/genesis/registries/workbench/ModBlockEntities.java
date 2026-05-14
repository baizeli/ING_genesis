package miku.united_as_one.genesis.registries.workbench;

import miku.united_as_one.genesis.contents.workbench.arcane.ArcaneWorkbenchBlockEntity;
import miku.united_as_one.genesis.registries.block.BlockRegistry;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Genesis.MOD_ID);

    // 奥术工作台的方块实体，用于保存 5x5 合成网格和结果槽。
    public static final RegistryObject<BlockEntityType<ArcaneWorkbenchBlockEntity>> ARCANE_WORKBENCH =
            BLOCK_ENTITIES.register("arcane_workbench",
                    () -> BlockEntityType.Builder.of(ArcaneWorkbenchBlockEntity::new, BlockRegistry.ARCANE_WORKBENCH.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
