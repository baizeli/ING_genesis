package miku.united_as_one.genesis.event.spell.celestial_source;

import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.effect.spell.ModEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID)
public class IFlyEvent {
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player player = event.player;

            if (player.hasEffect(ModEffect.I_FLY.get())) {
                if (!player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = true;
                    /*player.onUpdateAbilities();*/
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEntity() instanceof Player player && event.getEffect() == ModEffect.I_FLY.get()) {
            if (!player.isCreative() && !player.isSpectator()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
    }
    
    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        if (event.getEntity() instanceof Player player && event.getEffectInstance().getEffect() == ModEffect.I_FLY.get()) {
            if (!player.isCreative() && !player.isSpectator()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
    }
}