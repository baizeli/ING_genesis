package miku.united_as_one.genesis.handlers;

import miku.united_as_one.genesis.client.ClientEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

public final class ModEventHandlers {
    private ModEventHandlers() {
    }

    public static void register() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.register(ClientEvent.class);
        }
    }
}
