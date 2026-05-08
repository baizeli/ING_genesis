package miku.united_as_one.genesis.registries;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("removal")
public final class ResourcePackRegistry {
    private ResourcePackRegistry() {
    }

    public static void registerOptionalTexturePack(ResourceLocation folderName, Component displayName, boolean defaultEnabled) {
        registerResourcePack(PackType.CLIENT_RESOURCES, () -> {
            IModFile file = ModList.get().getModFileById(folderName.getNamespace()).getFile();

            try (PathPackResources pack = new PathPackResources(folderName.toString(), true, file.findResource("resourcepacks/" + folderName.getPath()))) {
                PackMetadataSection metadata = Objects.requireNonNull(pack.getMetadataSection(PackMetadataSection.TYPE), "Missing pack.mcmeta for pack " + folderName);
                return Pack.create(folderName.toString(), displayName, defaultEnabled, (s) -> pack, new Pack.Info(metadata.getDescription(), metadata.getPackFormat(), FeatureFlagSet.of()), PackType.CLIENT_RESOURCES, Pack.Position.TOP, false, PackSource.BUILT_IN);
            } catch (Exception ee) {
                if (!DatagenModLoader.isRunningDataGen()) {
                    ee.printStackTrace();
                }

                return null;
            }
        });
    }

    public static void registerResourcePack(PackType packType, @Nullable Supplier<Pack> packSupplier) {
        if (packSupplier != null) {
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            Consumer<AddPackFindersEvent> consumer = (event) -> {
                if (event.getPackType() == packType) {
                    Pack p = packSupplier.get();
                    if (p != null) {
                        event.addRepositorySource((infoConsumer) -> infoConsumer.accept(packSupplier.get()));
                    }
                }
            };
            bus.addListener(consumer);
        }
    }
}
