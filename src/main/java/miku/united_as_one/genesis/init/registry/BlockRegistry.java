package miku.united_as_one.genesis.init.registry;

import com.tterrag.registrate.util.entry.BlockEntry;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.block.ChaosPortalBlock;
import miku.united_as_one.genesis.common.block.util.SimpleBlockSet;
import miku.united_as_one.genesis.common.data.content.arcaneWorkbench.ArcaneWorkbenchBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.registries.*;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Genesis.MOD_ID);

    // 风化材质方块
    public static final SimpleBlockSet<Block> WEATHERED_SANDSTONE = SimpleBlockSet.buildStone("weathered_sandstone", Blocks.SANDSTONE).simpleStone();
    public static final SimpleBlockSet<Block> WEATHERED_STONE_BRICKS = SimpleBlockSet.buildStone("weathered_stone_bricks", Blocks.STONE_BRICKS).simpleStone();
    public static final SimpleBlockSet<Block> WEATHERED_SAND = SimpleBlockSet.buildStone("weathered_sand", Blocks.SAND);
    // 深怖材质方块
    public static final SimpleBlockSet<Block> DEEP_FEAR_STONE = SimpleBlockSet.buildStone("deep_fear_stone", Blocks.STONE).simpleStone();
    public static final SimpleBlockSet<Block> SMOOTH_DEEP_FEAR_STONE = SimpleBlockSet.buildStone("smooth_deep_fear_stone", Blocks.SMOOTH_STONE).simpleStone();
    public static final SimpleBlockSet<Block> DEEP_FEAR_STONE_BRICKS = SimpleBlockSet.buildStone("deep_fear_stone_bricks", Blocks.STONE_BRICKS).simpleStone();
    public static final SimpleBlockSet<Block> CRACKED_DEEP_FEAR_STONE_BRICKS = SimpleBlockSet.buildStone("cracked_deep_fear_stone_bricks", Blocks.CRACKED_STONE_BRICKS).simpleStone();
    // 恣睢材质方块
    public static final SimpleBlockSet<Block> SWAY_STONE = SimpleBlockSet.buildStone("sway_stone", Blocks.STONE).simpleStone();
    public static final SimpleBlockSet<RotatedPillarBlock> SWAY_LOG = SimpleBlockSet.buildLog("sway_log", Blocks.OAK_LOG).addStrippedLog();
    public static final SimpleBlockSet<Block> SWAY_PLANKS = SimpleBlockSet.buildPlanks("sway", Blocks.OAK_PLANKS).simplePlank(BlockSetType.OAK);
    public static final SimpleBlockSet<Block> SWAY_DIRT = SimpleBlockSet.buildDirt("sway", Blocks.DIRT).addGrass();
    // 蕴谧材质方块
    public static final SimpleBlockSet<RotatedPillarBlock> QUIETNESS_LOG = SimpleBlockSet.buildLog("quietness_log", Blocks.OAK_LOG).addStrippedLog();
    public static final SimpleBlockSet<Block> QUIETNESS_PLANKS = SimpleBlockSet.buildPlanks("quietness", Blocks.OAK_PLANKS).simplePlank();
    public static final SimpleBlockSet<Block> QUIETNESS_DIRT = SimpleBlockSet.buildDirt("quietness", Blocks.DIRT).addGrass();

    // 混沌传送门框架
    public static final BlockEntry<Block> CHAOS_PORTAL_FRAME = Genesis.L2_REGISTRATE
            .block("chaos_portal_frame", Block::new)
            .properties(p -> p.strength(-1, 9999))
            .simpleItem()
            .register();

    // 星源块
    public static final BlockEntry<Block> CELESTIAL_SOURCE_BLOCK = Genesis.L2_REGISTRATE
            .block("celestial_source_block", Block::new)
            .properties(p -> p.requiresCorrectToolForDrops().strength(20, 9999).sound(SoundType.NETHERITE_BLOCK))
            .simpleItem()
            .register();

    public static final RegistryObject<Block> ARCANE_WORKBENCH = BLOCKS.register("arcane_workbench",
            () -> new ArcaneWorkbenchBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));

    public static final RegistryObject<Block> ARCANE_CRYSTAL_ORE = BLOCKS.register("arcane_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9).mapColor(DyeColor.GRAY).requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F).sound(SoundType.ANCIENT_DEBRIS), UniformInt.of(3, 7)));

    public static final RegistryObject<Block> ARCANE_CRYSTAL_ORE_DEEPSLATE = BLOCKS.register("deepslate_arcane_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9).mapColor(DyeColor.GRAY).requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F).sound(SoundType.ANCIENT_DEBRIS), UniformInt.of(3, 7)));

    public static final RegistryObject<Block> NETHER_ARCANE_CRYSTAL_ORE = BLOCKS.register("nether_arcane_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9).mapColor(DyeColor.GRAY).requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F).sound(SoundType.ANCIENT_DEBRIS), UniformInt.of(3, 7)));

    public static final RegistryObject<Block> END_ARCANE_CRYSTAL_ORE = BLOCKS.register("end_arcane_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9).mapColor(DyeColor.GRAY).requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F).sound(SoundType.ANCIENT_DEBRIS), UniformInt.of(3, 7)));

    // 混沌传送门方块
    public static final RegistryObject<ChaosPortalBlock> CHAOS_PORTAL = BLOCKS.register("chaos_portal",
            () -> new ChaosPortalBlock(BlockBehaviour.Properties.of()
                    .noCollission().randomTicks().strength(-1).sound(SoundType.GLASS)
                    .lightLevel((state) -> 11).noOcclusion()));
}
