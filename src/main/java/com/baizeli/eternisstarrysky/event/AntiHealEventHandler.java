package com.baizeli.eternisstarrysky.event;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.client.WireBoxRenderer;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AntiHealEventHandler {

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if (WireBoxRenderer.entitiesForRenderWireBoxRenderer.containsKey(event.getEntity())) {
            event.setCanceled(true);
        }
    }
}