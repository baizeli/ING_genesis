package com.baizeli.eternisstarrysky.network;

import com.baizeli.eternisstarrysky.client.WireBoxRenderer;
import com.baizeli.eternisstarrysky.util.EntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class WireBoxSyncPacket {
    private final UUID entityUuid;
    private final boolean add;
    private final long expireTime;

    public WireBoxSyncPacket(UUID entityUuid, boolean add, long expireTime) {
        this.entityUuid = entityUuid;
        this.add = add;
        this.expireTime = expireTime;
    }

    public static void encode(WireBoxSyncPacket pkt, FriendlyByteBuf buf) {
        buf.writeUUID(pkt.entityUuid);
        buf.writeBoolean(pkt.add);
        buf.writeLong(pkt.expireTime);
    }

    public static WireBoxSyncPacket decode(FriendlyByteBuf buf) {
        return new WireBoxSyncPacket(buf.readUUID(), buf.readBoolean(), buf.readLong());
    }

    public static void handle(WireBoxSyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;

            Entity entity = level.entityStorage.entityGetter.get(pkt.entityUuid);
            if (entity instanceof LivingEntity living) {
                if (pkt.add) {
                    EntityData data = new EntityData(pkt.expireTime);
                    WireBoxRenderer.entitiesForRenderWireBoxRenderer.put(living, data);
                } else {
                    WireBoxRenderer.entitiesForRenderWireBoxRenderer.remove(living);
                    WireBoxRenderer.entityRotationMap.remove(living);
                    WireBoxRenderer.entityAxisMap.remove(living);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
