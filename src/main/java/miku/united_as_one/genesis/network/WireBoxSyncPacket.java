package miku.united_as_one.genesis.network;

import miku.united_as_one.genesis.client.renderer.spell.chaos.WireBoxRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class WireBoxSyncPacket {

    private final UUID entityUuid;               // 单实体模式
    private final boolean add;                   // 单实体模式
    private final long expireTime;               // 单实体模式

    // 为了从本地加载
    private final Map<UUID, Long>  timeMap;
    private final Map<UUID, Float> rotMap;
    private final Map<UUID, String> axisStrMap;

    public WireBoxSyncPacket(UUID entityUuid, boolean add, long expireTime) {
        this.entityUuid = entityUuid;
        this.add        = add;
        this.expireTime = expireTime;
        this.timeMap    = null;
        this.rotMap     = null;
        this.axisStrMap = null;
    }

    public WireBoxSyncPacket(Map<UUID, Long>  timeMap, Map<UUID, Float> rotMap, Map<UUID, Direction.Axis> axisMap) {
        this.entityUuid = null;
        this.add        = false;
        this.expireTime = 0L;
        this.timeMap    = new ConcurrentHashMap<>(timeMap);
        this.rotMap     = new ConcurrentHashMap<>(rotMap);
        this.axisStrMap = new ConcurrentHashMap<>();
        axisMap.forEach((uuid, ax) -> {
            switch (ax) {
                case X -> this.axisStrMap.put(uuid, "x");
                case Y -> this.axisStrMap.put(uuid, "y");
            }
        });
    }

    public static void encode(WireBoxSyncPacket pkt, FriendlyByteBuf buf) {
        boolean isSingle = pkt.entityUuid != null;
        buf.writeBoolean(isSingle);          // 1 字节标记
        if (isSingle) {
            buf.writeUUID(pkt.entityUuid);
            buf.writeBoolean(pkt.add);
            buf.writeLong(pkt.expireTime);
        } else {
            buf.writeMap(pkt.timeMap, FriendlyByteBuf::writeUUID, FriendlyByteBuf::writeLong);
            buf.writeMap(pkt.rotMap,  FriendlyByteBuf::writeUUID, FriendlyByteBuf::writeFloat);
            buf.writeMap(pkt.axisStrMap, FriendlyByteBuf::writeUUID, FriendlyByteBuf::writeUtf);
        }
    }

    public static WireBoxSyncPacket decode(FriendlyByteBuf buf) {
        boolean isSingle = buf.readBoolean();
        if (isSingle) {
            return new WireBoxSyncPacket(buf.readUUID(), buf.readBoolean(), buf.readLong());
        } else {
            Map<UUID, Long>  tMap = buf.readMap(FriendlyByteBuf::readUUID, FriendlyByteBuf::readLong);
            Map<UUID, Float> rMap = buf.readMap(FriendlyByteBuf::readUUID, FriendlyByteBuf::readFloat);
            Map<UUID, String> aMap = buf.readMap(FriendlyByteBuf::readUUID, FriendlyByteBuf::readUtf);

            Map<UUID, Direction.Axis> axisMap = new HashMap<>();
            aMap.forEach((uuid, s) -> {
                switch (s) {
                    case "x" -> axisMap.put(uuid, Direction.Axis.X);
                    case "y" -> axisMap.put(uuid, Direction.Axis.Y);
                    default  -> System.out.println("Unknown axis: " + s);
                }
            });
            return new WireBoxSyncPacket(tMap, rMap, axisMap);
        }
    }

    public static void handle(WireBoxSyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;

            if (pkt.entityUuid != null) {
                Entity entity = level.getEntities().get(pkt.entityUuid);
                if (!(entity instanceof LivingEntity)) return;

                if (pkt.add) {
                    WireBoxRenderer.entitiesForRenderWireBoxRenderer.put(pkt.entityUuid, pkt.expireTime);
                } else {
                    WireBoxRenderer.entitiesForRenderWireBoxRenderer.remove(pkt.entityUuid);
                    WireBoxRenderer.entityRotationMap.remove(pkt.entityUuid);
                    WireBoxRenderer.entityAxisMap.remove(pkt.entityUuid);
                }
                return;
            }

            if (pkt.timeMap != null && !pkt.timeMap.isEmpty()) {
                WireBoxRenderer.entitiesForRenderWireBoxRenderer.clear();
                WireBoxRenderer.entityRotationMap.clear();
                WireBoxRenderer.entityAxisMap.clear();

                WireBoxRenderer.entitiesForRenderWireBoxRenderer.putAll(pkt.timeMap);
                WireBoxRenderer.entityRotationMap.putAll(pkt.rotMap);
                pkt.axisStrMap.forEach((uuid, s) -> {
                    switch (s) {
                        case "x" -> WireBoxRenderer.entityAxisMap.put(uuid, Direction.Axis.X);
                        case "y" -> WireBoxRenderer.entityAxisMap.put(uuid, Direction.Axis.Y);
                        default  -> System.out.println("Unknown axis: " + s);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}