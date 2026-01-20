package miku.united_as_one.genesis.event;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.effect.spell.celestial_source.IFlyEffect;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectSyncHandler {

    @SubscribeEvent
    public static void onEffectApplied(MobEffectEvent.Added event) {
        handleEffectUpdate(event);
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        handleEffectRemoval(event);
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        handleEffectRemoval(event);
    }

    private static void handleEffectUpdate(MobEffectEvent event) {
        MobEffect activeEffect = null;
        if (event.getEffectInstance() != null) {
            activeEffect = event.getEffectInstance().getEffect();
        }
        LivingEntity targetEntity = event.getEntity();

        if (activeEffect instanceof IFlyEffect && targetEntity.isAlive()) {
            ServerChunkCache chunkManager = (ServerChunkCache) targetEntity.getCommandSenderWorld().getChunkSource();
            chunkManager.broadcastAndSend(targetEntity,
                    new ClientboundUpdateMobEffectPacket(targetEntity.getId(), event.getEffectInstance()));
        }
    }

    private static void handleEffectRemoval(MobEffectEvent event) {
        MobEffect activeEffect = null;
        if (event.getEffectInstance() != null) {
            activeEffect = event.getEffectInstance().getEffect();
        }
        LivingEntity targetEntity = event.getEntity();

        if (activeEffect instanceof IFlyEffect) {
            ServerChunkCache chunkManager = (ServerChunkCache) targetEntity.getCommandSenderWorld().getChunkSource();
            chunkManager.broadcastAndSend(targetEntity,
                    new ClientboundRemoveMobEffectPacket(targetEntity.getId(), activeEffect));
        }
    }
}