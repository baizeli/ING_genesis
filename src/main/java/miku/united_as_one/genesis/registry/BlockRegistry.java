package miku.united_as_one.genesis.registry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import miku.united_as_one.genesis.content.ChaosPortalBlock;
import miku.united_as_one.genesis.content.arcaneWorkbench.ArcaneWorkbenchBlock;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraftforge.registries.*;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Genesis.MOD_ID);

    // 星源块
    public static final RegistryEntry<Block> CELESTIAL_SOURCE_BLOCK = Genesis.L2_REGISTRATE
            .block("celestial_source_block", Block::new)
            .properties(properties -> properties
                    .requiresCorrectToolForDrops()
                    .strength(20, 9999)
                    .sound(SoundType.NETHERITE_BLOCK))
            .register();

    // 奥术工作台
    public static final RegistryObject<Block> ARCANE_WORKBENCH = BLOCKS.register("arcane_workbench",
            () -> new ArcaneWorkbenchBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));

    // 奥术水晶矿
    public static final RegistryObject<Block> ARCANE_CRYSTAL_ORE = BLOCKS.register("arcane_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9)
                    .mapColor(DyeColor.GRAY)
                    .requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F)
                    .sound(SoundType.ANCIENT_DEBRIS),
                    UniformInt.of(3, 7)));

    // 深层奥术水晶矿
    public static final RegistryObject<Block> ARCANE_CRYSTAL_ORE_DEEPSLATE = BLOCKS.register("deepslate_arcane_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9)
                    .mapColor(DyeColor.GRAY)
                    .requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F)
                    .sound(SoundType.ANCIENT_DEBRIS),
                    UniformInt.of(3, 7)));

    // 下界奥术水晶矿
    public static final RegistryObject<Block> NETHER_ARCANE_CRYSTAL_ORE = BLOCKS.register("nether_arcane_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9)
                    .mapColor(DyeColor.GRAY)
                    .requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F)
                    .sound(SoundType.ANCIENT_DEBRIS),
                    UniformInt.of(3, 7)));

    // 末地奥术水晶矿
    public static final RegistryObject<Block> END_ARCANE_CRYSTAL_ORE = BLOCKS.register("end_arcane_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9)
                    .mapColor(DyeColor.GRAY)
                    .requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F)
                    .sound(SoundType.ANCIENT_DEBRIS),
                    UniformInt.of(3, 7)));

    // 混沌传送门方块
    public static final RegistryObject<ChaosPortalBlock> CHAOS_PORTAL = BLOCKS.register("chaos_portal",
            () -> new ChaosPortalBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .randomTicks()
                    .strength(-1.0F)
                    .sound(SoundType.GLASS)
                    .lightLevel((state) -> 11)
                    .noOcclusion()));
}