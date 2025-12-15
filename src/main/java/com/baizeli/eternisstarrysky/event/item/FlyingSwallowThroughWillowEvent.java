package com.baizeli.eternisstarrysky.event.item;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID)
public class FlyingSwallowThroughWillowEvent {

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().getGameTime() < player.getPersistentData().getLong("FlyingSwallowFallImmunity")) {
                event.setCanceled(true);
            }
        }
    }
}