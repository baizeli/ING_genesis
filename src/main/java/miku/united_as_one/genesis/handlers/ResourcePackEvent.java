package miku.united_as_one.genesis.handlers;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.*;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;

import java.util.List;

@Mod.EventBusSubscriber
public class ResourcePackEvent {

    @SubscribeEvent
    public static void addbuiltinPack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo modFileInfo = ModList.get().getModFileById(Genesis.MODID);
            if (modFileInfo == null) return;
       
            var builtinPack = List.of(
                new ResourcePackInfo("genesis_old", "Genesis Old", false)
            );
            
            for (var packInfo : builtinPack) {
                final String folderName = packInfo.folderName;
                event.addRepositorySource((consumer) -> {
                    Pack pack = Pack.readMetaAndCreate(
                        Genesis.MODID + ":" + folderName,
                        Component.literal(packInfo.displayName),
                        packInfo.optional,
                        (id) -> new PathPackResources(id, modFileInfo.getFile().findResource("resourcepacks/" + folderName), true),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP,
                        PackSource.BUILT_IN
                    );
                    if (pack != null) consumer.accept(pack);
                });
            }
        }
    }
    record ResourcePackInfo(String folderName, String displayName, boolean optional) {}
}