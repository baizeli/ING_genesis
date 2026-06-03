package miku.united_as_one.genesis.packets;

import miku.united_as_one.genesis.contents.items.GoodCake;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GoodCakeDamageAdjustPacket {
    private final int steps;

    public GoodCakeDamageAdjustPacket(int steps) {
        this.steps = steps > 0 ? 1 : -1;
    }

    public static void encode(GoodCakeDamageAdjustPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.steps);
    }

    public static GoodCakeDamageAdjustPacket decode(FriendlyByteBuf buffer) {
        return new GoodCakeDamageAdjustPacket(buffer.readVarInt());
    }

    public static void handle(GoodCakeDamageAdjustPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                GoodCake.adjustHeldCakeDamage(player, packet.steps);
            }
        });
        context.setPacketHandled(true);
    }
}
