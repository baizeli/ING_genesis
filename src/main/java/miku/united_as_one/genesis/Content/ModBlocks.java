package miku.united_as_one.genesis.Content;

import miku.united_as_one.genesis.Content.ArcaneWorkbench.ArcaneWorkbenchBlock;
import miku.united_as_one.genesis.Content.Workbenchs.VanillaWorkbenchBlock;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Genesis.MOD_ID);

    public static final RegistryObject<Block> workbench = BLOCKS.register("workbench",
            () -> new VanillaWorkbenchBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(3.0f, 1200)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ARCANE_WORKBENCH = BLOCKS.register("arcane_workbench",
            () -> new ArcaneWorkbenchBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));

    // 奥术水晶矿
    public static final RegistryObject<Block> ARCANE_CRYSTAL_ORE = BLOCKS.register("arcane_crystal_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9)
                    .mapColor(DyeColor.GRAY)
                    .requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F)
                    .sound(SoundType.ANCIENT_DEBRIS)));

    public static final RegistryObject<Block> ARCANE_CRYSTAL_ORE_DEEPSLATE = BLOCKS.register("deepslate_arcane_crystal_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9)
                    .mapColor(DyeColor.GRAY)
                    .requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F)
                    .sound(SoundType.ANCIENT_DEBRIS)));

    public static final RegistryObject<Block> NETHER_ARCANE_CRYSTAL_ORE = BLOCKS.register("nether_arcane_crystal_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9)
                    .mapColor(DyeColor.GRAY)
                    .requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F)
                    .sound(SoundType.ANCIENT_DEBRIS)));

    public static final RegistryObject<Block> END_ARCANE_CRYSTAL_ORE = BLOCKS.register("end_arcane_crystal_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .lightLevel((state) -> 9)
                    .mapColor(DyeColor.GRAY)
                    .requiresCorrectToolForDrops()
                    .strength(20.0F, 99999.0F)
                    .sound(SoundType.ANCIENT_DEBRIS)));
}
