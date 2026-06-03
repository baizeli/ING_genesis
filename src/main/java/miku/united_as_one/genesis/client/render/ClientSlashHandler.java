package miku.united_as_one.genesis.client.render;

import miku.bai_ze_li.genesis.api.render.effect.BaiZeLiSlashEffect;
import miku.bai_ze_li.genesis.api.render.effect.SlashEffectManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientSlashHandler {

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (event.getItemStack().getItem() != Items.DIAMOND_SWORD) return;

        var player = event.getEntity();
        var center = player.position();
        float yaw = player.getYRot();
        float pitch = player.getXRot();


        SlashEffectManager.add(new BaiZeLiSlashEffect(center, yaw, pitch));
    }
}
