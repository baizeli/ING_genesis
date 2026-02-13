package miku.united_as_one.genesis.init.registry;

import com.tterrag.registrate.util.entry.BlockEntry;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.block.ChaosPortalBlock;
import miku.united_as_one.genesis.common.data.content.arcaneWorkbench.ArcaneWorkbenchBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Genesis.MOD_ID);


    // 风化砂岩全家桶
    public static final StoneSet WEATHERED_SANDSTONE = new StoneSet("weathered_sandstone", Blocks.SANDSTONE);

    // 星源块
    public static final BlockEntry<Block> CELESTIAL_SOURCE_BLOCK = Genesis.L2_REGISTRATE
            .block("celestial_source_block", Block::new)
            .properties(p -> p.requiresCorrectToolForDrops().strength(20.0F, 9999.0F).sound(SoundType.NETHERITE_BLOCK))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
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

    public static final RegistryObject<ChaosPortalBlock> CHAOS_PORTAL = BLOCKS.register("chaos_portal",
            () -> new ChaosPortalBlock(BlockBehaviour.Properties.of()
                    .noCollission().randomTicks().strength(-1.0F).sound(SoundType.GLASS)
                    .lightLevel((state) -> 11).noOcclusion()));

    public static class StoneSet {
        public final BlockEntry<Block> BASE;
        public final BlockEntry<StairBlock> STAIRS;
        public final BlockEntry<SlabBlock> SLAB;
        public final BlockEntry<WallBlock> WALL;

        public StoneSet(String name, Block vanillaCopy) {
            // 明确指定命名空间和路径：genesis:block/weathered_sandstone
            ResourceLocation tex = new ResourceLocation(Genesis.MOD_ID, "block/" + name);

           // 基础方块
            BASE = Genesis.L2_REGISTRATE.block(name, Block::new)
                    .initialProperties(() -> vanillaCopy)
                    .properties(p -> p.requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.STONE))
                    .blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get()))
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .item()
                    .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                    .build()
                    .register();
            // 楼梯
            STAIRS = Genesis.L2_REGISTRATE.block(name + "_stairs", p -> new StairBlock(BASE::getDefaultState, p))
                    .initialProperties(BASE)
                    .blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), tex))
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.STAIRS)
                    .item()
                    .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                    .tag(ItemTags.STAIRS)
                    .model((ctx, pvd) -> pvd.stairs(ctx.getName(), tex, tex, tex)) // 修复物品模型
                    .build()
                    .register();

            // 半砖 (修复报错的核心点)
            SLAB = Genesis.L2_REGISTRATE.block(name + "_slab", SlabBlock::new)
                    .initialProperties(BASE)
                    .blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(), BASE.getId(), tex))
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.SLABS)
                    .item()
                    .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                    .tag(ItemTags.SLABS)
                    .model((ctx, pvd) -> pvd.slab(ctx.getName(), tex, tex, tex))
                    .build()
                    .register();

            // 围墙
            WALL = Genesis.L2_REGISTRATE.block(name + "_wall", WallBlock::new)
                    .initialProperties(BASE)
                    .blockstate((ctx, pvd) -> pvd.wallBlock(ctx.get(), tex))
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.WALLS)
                    .item()
                    .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                    .tag(ItemTags.WALLS)
                    .model((ctx, pvd) -> pvd.wallInventory(ctx.getName(), tex)) // 围墙专用物品模型
                    .build()
                    .register();
        }
    }
}
