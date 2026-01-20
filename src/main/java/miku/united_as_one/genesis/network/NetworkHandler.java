package miku.united_as_one.genesis.network;

import miku.united_as_one.genesis.Content.ArcaneWorkbench.ArcaneWorkbenchRecipeTransferPacket;
import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.network.manuscript.LearnSpellPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EternisStarrySky.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, BowTypePacket.class, BowTypePacket::encode, BowTypePacket::decode, BowTypePacket::handle);
        INSTANCE.registerMessage(id++, ArcaneWorkbenchRecipeTransferPacket.class,
                ArcaneWorkbenchRecipeTransferPacket::encode,
                ArcaneWorkbenchRecipeTransferPacket::new,
                ArcaneWorkbenchRecipeTransferPacket::handle);
        INSTANCE.registerMessage(id++, LearnSpellPacket.class,
                LearnSpellPacket::encode,
                LearnSpellPacket::decode,
                LearnSpellPacket::handle);
    }
}