package miku.united_as_one.genesis.contents.block;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenesisTreeGrower extends AbstractTreeGrower {
    private final ResourceKey<ConfiguredFeature<?, ?>> configuredFeature;

    public GenesisTreeGrower(String configuredFeatureName) {
        this.configuredFeature = ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                new ResourceLocation(Genesis.MOD_ID, configuredFeatureName)
        );
    }

    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean hasFlowers) {
        return configuredFeature;
    }
}
