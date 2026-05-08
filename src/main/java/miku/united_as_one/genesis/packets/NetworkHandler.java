package miku.united_as_one.genesis.packets;

import miku.united_as_one.genesis.packets.workbench.ArcaneWorkbenchRecipeTransferPacket;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.packets.manuscript.LearnSpellPacket;
import miku.united_as_one.genesis.packets.packet.SpawnSlashPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static boolean registered = false;

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Genesis.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;

        int id = 0;

        INSTANCE.registerMessage(id++, BowTypePacket.class,
                BowTypePacket::encode,
                BowTypePacket::decode,
                BowTypePacket::handle);

        INSTANCE.registerMessage(id++, ArcaneWorkbenchRecipeTransferPacket.class,
                ArcaneWorkbenchRecipeTransferPacket::encode,
                ArcaneWorkbenchRecipeTransferPacket::new,
                ArcaneWorkbenchRecipeTransferPacket::handle);

        INSTANCE.registerMessage(id++, LearnSpellPacket.class,
                LearnSpellPacket::encode,
                LearnSpellPacket::decode,
                LearnSpellPacket::handle);

        INSTANCE.registerMessage(id++, WireBoxSyncPacket.class,
                WireBoxSyncPacket::encode,
                WireBoxSyncPacket::decode,
                WireBoxSyncPacket::handle);

        INSTANCE.registerMessage(id++, DeadListSyncPacket.class,
                DeadListSyncPacket::encode,
                DeadListSyncPacket::decode,
                DeadListSyncPacket::handle);

        INSTANCE.registerMessage(id++, MarkDeadPacket.class,
                MarkDeadPacket::encode,
                MarkDeadPacket::decode,
                MarkDeadPacket::handle);

        INSTANCE.registerMessage(id++, SpawnSlashPacket.class,
                SpawnSlashPacket::encode,
                SpawnSlashPacket::decode,
                SpawnSlashPacket::handle);
    }
}
