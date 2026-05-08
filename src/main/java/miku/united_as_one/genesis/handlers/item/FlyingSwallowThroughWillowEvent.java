package miku.united_as_one.genesis.handlers.item;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
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