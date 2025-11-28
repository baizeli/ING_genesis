package com.baizeli.eternisstarrysky.Content.Workbenchs;

/*import com.baizeli.eternisstarrysky.Content.Workbenchs.arcane.ArcaneWorkbenchBlockEntity;*/
import com.baizeli.eternisstarrysky.Content.ModBlock;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EternisStarrySky.MOD_ID);

    public static final RegistryObject<BlockEntityType<VanillaWorkbenchBlockEntity>> VANILLA_WORKBENCH =
            BLOCK_ENTITIES.register("vanilla_workbench", () ->
                    BlockEntityType.Builder.of(VanillaWorkbenchBlockEntity::new, ModBlock.workbench.get()).build(null));
                    
/*    public static final RegistryObject<BlockEntityType<ArcaneWorkbenchBlockEntity>> ARCANE_WORKBENCH =
            BLOCK_ENTITIES.register("arcane_workbench", () ->
                    BlockEntityType.Builder.of(ArcaneWorkbenchBlockEntity::new, ModBlock.arcane_workbench.get()).build(null));*/

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}