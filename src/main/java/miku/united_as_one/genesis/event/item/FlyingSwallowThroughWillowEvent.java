package miku.united_as_one.genesis.event.item;

import miku.united_as_one.genesis.EternisStarrySky;
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