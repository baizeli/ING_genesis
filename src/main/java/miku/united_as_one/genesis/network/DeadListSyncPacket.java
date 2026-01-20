package miku.united_as_one.genesis.network;

import miku.united_as_one.genesis_core.utils.EventUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.*;
import java.util.function.Supplier;

public class DeadListSyncPacket {
    private final List<UUID> deadList;
    private final boolean isRemove; // 操作类型：false=添加, true=移除

    // 添加单个实体
    public DeadListSyncPacket(UUID entityUuid) {
        this(entityUuid, false);
    }

    // 批量添加实体
    public DeadListSyncPacket(Collection<UUID> deadList) {
        this(deadList, false);
    }

    // 移除单个实体
    public static DeadListSyncPacket remove(UUID entityUuid) {
        return new DeadListSyncPacket(entityUuid, true);
    }

    // 批量移除实体
    public static DeadListSyncPacket removeAll(Collection<UUID> deadList) {
        return new DeadListSyncPacket(deadList, true);
    }

    private DeadListSyncPacket(UUID entityUuid, boolean isRemove) {
        this.deadList = Collections.singletonList(entityUuid);
        this.isRemove = isRemove;
    }

    private DeadListSyncPacket(Collection<UUID> deadList, boolean isRemove) {
        this.deadList = new ArrayList<>(deadList);
        this.isRemove = isRemove;
    }

    public static void encode(DeadListSyncPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.deadList.size());
        for (UUID uuid : pkt.deadList) {
            buf.writeUUID(uuid);
        }
        buf.writeBoolean(pkt.isRemove); // 编码操作类型
    }

    public static DeadListSyncPacket decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<UUID> uuids = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            uuids.add(buf.readUUID());
        }
        boolean isRemove = buf.readBoolean(); // 解码操作类型
        return new DeadListSyncPacket(uuids, isRemove);
    }

    public static void handle(DeadListSyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (pkt.isRemove) {
                pkt.deadList.forEach(EventUtil.deadList::remove);
            } else {
                EventUtil.deadList.addAll(pkt.deadList);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}