package miku.united_as_one.genesis.datagen;

import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.damage.DamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER;

    public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", EternisStarrySky.MOD_ID));
    }

    static {
        BUILDER = (new RegistrySetBuilder()).add(Registries.DAMAGE_TYPE, DamageTypes::bootstrap);
    }
}
