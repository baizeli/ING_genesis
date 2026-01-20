package miku.united_as_one.genesis.event.armor;

import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.util.ArmorSetUtil;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class VioletZenithArmorEvent {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        
        Player player = event.player;

        if (player.level().isClientSide) {
            return;
        }

        if (ArmorSetUtil.isWearingVioletZenithHelmet(player)) {
            // 夜视
            player.addEffect(
                new MobEffectInstance(
                    MobEffects.NIGHT_VISION, 2, 0, false, false
                )
            );
        }
        
        if (ArmorSetUtil.isWearingVioletZenithChestplate(player)) {
            // 急迫2
            player.addEffect(
                new MobEffectInstance(
                    MobEffects.DIG_SPEED, 2, 1, false, false
                )
            );
        }
        
        if (ArmorSetUtil.isWearingVioletZenithLeggings(player)) {
            // 跳跃提升2
            player.addEffect(
                new MobEffectInstance(
                    MobEffects.JUMP, 2, 1, false, false
                )
            );
        }
        
        if (ArmorSetUtil.isWearingVioletZenithBoots(player)) {
            // 速度2
            player.addEffect(
                new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED, 2, 1, false, false
                )
            );
        }
    }
}