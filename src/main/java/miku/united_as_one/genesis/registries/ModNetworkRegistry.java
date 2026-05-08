package miku.united_as_one.genesis.registries;

import miku.united_as_one.genesis.packets.ModPacketHandler;
import miku.united_as_one.genesis.packets.NetworkHandler;

public final class ModNetworkRegistry {
    private ModNetworkRegistry() {
    }

    public static void register() {
        NetworkHandler.register();
        ModPacketHandler.register();
    }
}
