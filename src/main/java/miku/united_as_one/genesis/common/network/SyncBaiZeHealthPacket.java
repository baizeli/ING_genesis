package miku.united_as_one.genesis.common.network;

import miku.united_as_one.genesis.common.entity.test.BaiZeLiEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncBaiZeHealthPacket {
    private final int entityId;
    private final String healthString;

    public SyncBaiZeHealthPacket(int entityId, String healthString) {
        this.entityId = entityId;
        this.healthString = healthString;
    }

    public SyncBaiZeHealthPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.healthString = buffer.readUtf(32767);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeUtf(this.healthString, 32767);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {

                Minecraft mc = Minecraft.getInstance();
                if (mc.level == null) return;

                Entity entity = mc.level.getEntity(this.entityId);
                if (entity == null) return;

                if (entity instanceof BaiZeLiEntity baiZe) {
                    baiZe.receiveStringHealthUpdate(this.healthString);
                }
            }
        });
        context.setPacketHandled(true);
    }
}