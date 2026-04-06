package miku.united_as_one.genesis.common.data.datagen.provider;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Genesis.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
