package miku.united_as_one.genesis.packets.packet; // 注意你的包名

import miku.united_as_one.genesis.client.render.SlashEffectAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpawnSlashPacket {
    private final int attackerId;
    private final int targetId;

    public SpawnSlashPacket(int attackerId, int targetId) {
        this.attackerId = attackerId;
        this.targetId = targetId;
    }
    public static SpawnSlashPacket decode(FriendlyByteBuf buf) {
        return new SpawnSlashPacket(buf.readInt(), buf.readInt());
    }
    public static void encode(SpawnSlashPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.attackerId);
        buf.writeInt(msg.targetId);
    }

    public static void handle(SpawnSlashPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handlePacket(msg.attackerId, msg.targetId));
        });
        context.setPacketHandled(true);
    }
    private static class ClientHandler {
        public static void handlePacket(int attackerId, int targetId) {
            if (Minecraft.getInstance().level == null) return;
            Entity attacker = Minecraft.getInstance().level.getEntity(attackerId);
            Entity target = Minecraft.getInstance().level.getEntity(targetId);

            if (attacker instanceof LivingEntity livingAttacker && target != null) {
                SlashEffectAPI.spawnOnEntity(livingAttacker, target);
            }
        }
    }
}