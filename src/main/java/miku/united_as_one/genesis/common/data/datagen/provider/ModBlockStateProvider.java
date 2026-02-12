package miku.united_as_one.genesis.common.data.datagen.provider;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.registry.BlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Genesis.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(BlockRegistry.WEATHERED_SANDSTONE.get());
        stairsBlock(BlockRegistry.WEATHERED_SANDSTONE_STAIRS.get(), blockTexture(BlockRegistry.WEATHERED_SANDSTONE.get()));
        slabBlock(BlockRegistry.WEATHERED_SANDSTONE_SLAB.get(), blockTexture(BlockRegistry.WEATHERED_SANDSTONE.get()), blockTexture(BlockRegistry.WEATHERED_SANDSTONE.get()));
        wallBlock(BlockRegistry.WEATHERED_SANDSTONE_WALL.get(), blockTexture(BlockRegistry.WEATHERED_SANDSTONE.get()));
    }
}
