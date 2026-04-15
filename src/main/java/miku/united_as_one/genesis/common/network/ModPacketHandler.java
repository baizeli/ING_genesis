package miku.united_as_one.genesis.common.network;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Genesis.MOD_ID, "sb"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, SyncBaiZeHealthPacket.class,
                SyncBaiZeHealthPacket::toBytes,
                SyncBaiZeHealthPacket::new,
                SyncBaiZeHealthPacket::handle);
    }
}