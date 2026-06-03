package miku.united_as_one.genesis.packets;

import miku.united_as_one.genesis.contents.items.GoodCake;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GoodCakeLocateOrePacket {
    public static void encode(GoodCakeLocateOrePacket packet, FriendlyByteBuf buffer) {
    }

    public static GoodCakeLocateOrePacket decode(FriendlyByteBuf buffer) {
        return new GoodCakeLocateOrePacket();
    }

    public static void handle(GoodCakeLocateOrePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                GoodCake.locateHeldCakeOre(player);
            }
        });
        context.setPacketHandled(true);
    }
}
