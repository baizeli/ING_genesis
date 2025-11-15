package com.baizeli.eternisstarrysky.event;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.client.WireBoxRenderer;
import com.baizeli.eternisstarrysky.client.network.WireBoxSyncPacket;
import com.baizeli.eternisstarrysky.save.SaveManager;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.baizeli.eternisstarrysky.EternisStarrySky.CHANNEL;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    public static Map<UUID, Long> affectedEntities = new HashMap<>();

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if (WireBoxRenderer.entitiesForRenderWireBoxRenderer.containsKey(event.getEntity().getUUID())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        if (Arrays.toString(Thread.currentThread().getStackTrace()).contains("doLoad")) return;
        SaveManager.init(event.getServer());
        SaveManager.save();
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        SaveManager.init(event.getServer());
        SaveManager.load();
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        CHANNEL.send(net.minecraftforge.network.PacketDistributor.ALL.noArg(),
                new WireBoxSyncPacket(WireBoxRenderer.entitiesForRenderWireBoxRenderer,
                        WireBoxRenderer.entityRotationMap,
                        WireBoxRenderer.entityAxisMap));
    }
}