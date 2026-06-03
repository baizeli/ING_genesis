package miku.united_as_one.genesis.handlers;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.contents.items.curios.EternalRing;
import miku.bai_ze_li.genesis.api.curios.ModCurios;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Genesis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ESSClientLivingEvent {
    @SubscribeEvent
    public static void overlayModify(RenderGuiOverlayEvent.Pre event) {
        Minecraft instance = Minecraft.getInstance();
        LocalPlayer player = instance.player;
        if(player == null) return;
        NamedGuiOverlay overlay = event.getOverlay();
        if(ModCurios.hasCurios(player, EternalRing::test)) {
            if(overlay.id().equals(VanillaGuiOverlay.FROSTBITE.id()))
                event.setCanceled(true);
        }
    }
}
