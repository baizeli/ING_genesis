package com.baizeli.eternisstarrysky.network;

import com.baizeli.eternisstarrysky.genesis_core.utils.EventUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

import static com.baizeli.eternisstarrysky.EternisStarrySky.CHANNEL;

public class MarkDeadPacket {
    private final UUID targetUuid;

    public MarkDeadPacket(UUID targetUuid) {
        this.targetUuid = targetUuid;
    }

    public static void encode(MarkDeadPacket pkt, FriendlyByteBuf buf) {
        buf.writeUUID(pkt.targetUuid);
    }

    public static MarkDeadPacket decode(FriendlyByteBuf buf) {
        return new MarkDeadPacket(buf.readUUID());
    }

    public static void handle(MarkDeadPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();

            if (sender != null) {
                EventUtil.deadList.add(pkt.targetUuid);
                CHANNEL.send(PacketDistributor.ALL.noArg(), new DeadListSyncPacket(pkt.targetUuid));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
