package miku.united_as_one.genesis.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record BowTypePacket(boolean jumping) {
    public static boolean BowType = false;
    public static void encode(BowTypePacket msg, FriendlyByteBuf buffer) {
        buffer.writeBoolean(msg.jumping);
    }
    
    public static BowTypePacket decode(FriendlyByteBuf buffer) {
        return new BowTypePacket(buffer.readBoolean());
    }
    
    public static void handle(BowTypePacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer == null) return;
            BowType = msg.jumping;
        context.setPacketHandled(true);
    });
  }
}