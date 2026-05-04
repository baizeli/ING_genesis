package miku.united_as_one.genesis.init.registry;

import com.tterrag.registrate.util.entry.BlockEntry;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.block.ChaosPortalBlock;
import miku.united_as_one.genesis.common.block.util.SimpleBlockSet;
import miku.united_as_one.genesis.common.data.content.arcaneWorkbench.ArcaneWorkbenchBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.registries.*;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Genesis.MOD_ID);

    public static final SimpleBlockSet<Block> WEATHERED_SANDSTONE = SimpleBlockSet.buildStone("weathered_sandstone", Blocks.SANDSTONE).simpleStone();
    public static final SimpleBlockSet<Block> WEATHERED_STONE_BRICKS = SimpleBlockSet.buildStone("weathered_stone_bricks", Blocks.STONE_BRICKS).simpleStone();
    public static final SimpleBlockSet<Block> WEATHERED_SAND = SimpleBlockSet.buildStone("weathered_sand", Blocks.SAND);

    public static final SimpleBlockSet<Block> BLOOD_SAND = SimpleBlockSet.buildStone("blood_sand", Blocks.SAND);
    public static final SimpleBlockSet<Block> FIRE_SAND = SimpleBlockSet.buildStone("fire_sand", Blocks.SAND);
    public static final SimpleBlockSet<Block> HEART_SCULPTING = SimpleBlockSet.buildStone("heart_sculpting", Blocks.STONE).simpleStone();

    public static final SimpleBlockSet<Block> FIRE_STONE = SimpleBlockSet.buildStone("fire_stone", Blocks.STONE).simpleStone();

    public static final SimpleBlockSet<Block> DEEP_FEAR_STONE = SimpleBlockSet.buildStone("deep_fear_stone", Blocks.STONE).simpleStone();
    public static final SimpleBlockSet<Block> SMOOTH_DEEP_FEAR_STONE = SimpleBlockSet.buildStone("smooth_deep_fear_stone", Blocks.SMOOTH_STONE).simpleStone();
    public static final SimpleBlockSet<Block> DEEP_FEAR_STONE_BRICKS = SimpleBlockSet.buildStone("deep_fear_stone_bricks", Blocks.STONE_BRICKS).simpleStone();
    public static final SimpleBlockSet<Block> CRACKED_DEEP_FEAR_STONE_BRICKS = SimpleBlockSet.buildStone("cracked_deep_fear_stone_bricks", Blocks.CRACKED_STONE_BRICKS).simpleStone();

    public static final SimpleBlockSet<Block> SWAY_STONE = SimpleBlockSet.buildStone("sway_stone", Blocks.STONE).simpleStone();
    public static final SimpleBlockSet<RotatedPillarBlock> SWAY_LOG = SimpleBlockSet.buildLog("sway_log", Blocks.OAK_LOG).addStrippedLog().addWood().addStrippedWood();
    public static final SimpleBlockSet<Block> SWAY_PLANKS = SimpleBlockSet.buildPlanks("sway", Blocks.OAK_PLANKS).simplePlank(BlockSetType.OAK);

    public static final SimpleBlockSet<RotatedPillarBlock> QUIETNESS_LOG = SimpleBlockSet.buildLog("quietness_log", Blocks.OAK_LOG).addStrippedLog().addWood().addStrippedWood();
    public static final SimpleBlockSet<Block> QUIETNESS_PLANKS = SimpleBlockSet.buildPlanks("quietness", Blocks.OAK_PLANKS).simplePlank();

    public static final SimpleBlockSet<Block> GENESIS_DIRT = SimpleBlockSet.buildDirt("genesis", Blocks.DIRT).addGrassVariant("sway").addGrassVariant("quietness");

    public static final BlockEntry<Block> ARCANE_CRYSTAL_BLOCK = Genesis.L2_REGISTRATE
            .block("arcane_crystal_block", Block::new)
            .initialProperties(() -> Blocks.AMETHYST_BLOCK)
            .properties(p -> p.strength(5.0F, 6.0F).requiresCorrectToolForDrops())
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
            .item()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static final BlockEntry<Block> FEAR_CRYSTALS = Genesis.L2_REGISTRATE
            .block("fear_crystals", Block::new)
            .initialProperties(() -> Blocks.AMETHYST_BLOCK)
            .properties(p -> p.strength(5.0F, 6.0F).requiresCorrectToolForDrops())
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
            .item()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static final BlockEntry<DropExperienceBlock> ARCANE_CRYSTAL_ORE = Genesis.L2_REGISTRATE
            .block("arcane_crystal_ore", p -> new DropExperienceBlock(p, UniformInt.of(3, 7)))
            .initialProperties(() -> Blocks.DIAMOND_ORE)
            .properties(p -> p.lightLevel(s -> 9).strength(3.0f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.STONE))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
            .item()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static final BlockEntry<DropExperienceBlock> ARCANE_CRYSTAL_ORE_DEEPSLATE = Genesis.L2_REGISTRATE
            .block("deepslate_arcane_crystal_ore", p -> new DropExperienceBlock(p, UniformInt.of(3, 7)))
            .initialProperties(() -> Blocks.DEEPSLATE_DIAMOND_ORE)
            .properties(p -> p.lightLevel(s -> 9).strength(4.5f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
            .item()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static final BlockEntry<DropExperienceBlock> NETHER_ARCANE_CRYSTAL_ORE = Genesis.L2_REGISTRATE
            .block("nether_arcane_crystal_ore", p -> new DropExperienceBlock(p, UniformInt.of(3, 7)))
            .initialProperties(() -> Blocks.NETHER_QUARTZ_ORE)
            .properties(p -> p.lightLevel(s -> 9).strength(3.0f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.NETHER_ORE))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
            .item()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static final BlockEntry<DropExperienceBlock> END_ARCANE_CRYSTAL_ORE = Genesis.L2_REGISTRATE
            .block("end_arcane_crystal_ore", p -> new DropExperienceBlock(p, UniformInt.of(3, 7)))
            .initialProperties(() -> Blocks.END_STONE)
            .properties(p -> p.lightLevel(s -> 9).strength(3.0f, 9.0f).requiresCorrectToolForDrops().sound(SoundType.STONE))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
            .item()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static final BlockEntry<ArcaneWorkbenchBlock> ARCANE_WORKBENCH = Genesis.L2_REGISTRATE
            .block("arcane_workbench", ArcaneWorkbenchBlock::new)
            .properties(p -> p.lightLevel(s -> 9).strength(3.0f, 9.0f).requiresCorrectToolForDrops().sound(SoundType.STONE))
            .register();

    public static final BlockEntry<Block> CHAOS_PORTAL_FRAME = Genesis.L2_REGISTRATE
            .block("chaos_portal_frame", Block::new)
            .properties(p -> p.strength(-1, 9999).noOcclusion())
            .register();

    public static final BlockEntry<ChaosPortalBlock> CHAOS_PORTAL = Genesis.L2_REGISTRATE
            .block("chaos_portal", ChaosPortalBlock::new)
            .properties(p -> p.noCollission().randomTicks().strength(-1).sound(SoundType.GLASS).lightLevel(s -> 11).noOcclusion())
            .register();

    public static final BlockEntry<Block> CELESTIAL_SOURCE_BLOCK = Genesis.L2_REGISTRATE
            .block("celestial_source_block", Block::new)
            .properties(p -> p.requiresCorrectToolForDrops().strength(20, 9999).sound(SoundType.NETHERITE_BLOCK))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
            .register();

    public static void register() {}
}