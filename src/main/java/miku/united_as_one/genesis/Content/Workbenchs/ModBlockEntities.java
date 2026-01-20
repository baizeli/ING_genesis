package miku.united_as_one.genesis.Content.Workbenchs;

import miku.united_as_one.genesis.Content.ArcaneWorkbench.ArcaneWorkbenchBlockEntity;
import miku.united_as_one.genesis.Content.ModBlocks;
import miku.united_as_one.genesis.EternisStarrySky;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EternisStarrySky.MOD_ID);

    public static final RegistryObject<BlockEntityType<VanillaWorkbenchBlockEntity>> VANILLA_WORKBENCH =
            BLOCK_ENTITIES.register("vanilla_workbench", () ->
                    BlockEntityType.Builder.of(VanillaWorkbenchBlockEntity::new, ModBlocks.workbench.get()).build(null));

    public static final RegistryObject<BlockEntityType<ArcaneWorkbenchBlockEntity>> ARCANE_WORKBENCH =
            BLOCK_ENTITIES.register("arcane_workbench",
                    () -> BlockEntityType.Builder.of(ArcaneWorkbenchBlockEntity::new, ModBlocks.ARCANE_WORKBENCH.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}