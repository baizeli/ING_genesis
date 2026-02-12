package miku.united_as_one.genesis.common.data.datagen;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.data.datagen.provider.*;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Genesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ModDatapackEntriesProvider registryProvider = new ModDatapackEntriesProvider(output, lookupProvider);
        CompletableFuture<HolderLookup.Provider> fullLookupProvider = registryProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), registryProvider);
        generator.addProvider(event.includeServer(), new ModDamageTypeTagProvider(output, fullLookupProvider, existingFileHelper));
        PackMetadataGenerator packMeta = new PackMetadataGenerator(output);
        MutableComponent description = Component.literal("Resources for Iron SpellRegistry Genesis");

        PackMetadataSection metadata = new PackMetadataSection(
                description,
                DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
                Arrays.stream(PackType.values()).collect(
                        Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::getPackVersion)
                )
        );

        generator.addProvider(true, packMeta.add(PackMetadataSection.TYPE, metadata));
        generator.addProvider(event.includeServer(), new ModRecipesProvider(output));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
    }
}