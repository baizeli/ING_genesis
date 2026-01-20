package miku.united_as_one.genesis.event;

import miku.united_as_one.genesis.EternisStarrySky;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class ArmorEventHandler {

//    @SubscribeEvent
//    public static void onLivingKnockBack(LivingKnockBackEvent event) {
//        if (event.getEntity() instanceof Player player) {
//
//            if (InfinityEternalArmorItem.hasFullSet(player)) {
//
//                event.setCanceled(true);
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onLivingHurt(LivingHurtEvent event) {
//        if (event.getEntity() instanceof Player player) {
//            if (InfinityEternalArmorItem.hasFullSet(player)) {
//
//                if (player.getHealth() / player.getMaxHealth() < 0.7f) {
//                    event.setAmount(event.getAmount() * 0.4f);
//                }
//            }
//        }
//    }
}