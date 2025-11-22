package com.baizeli.eternisstarrysky.Render;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModArmorRender
{
    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event)
    {
        addHaloLayer(event, "default");
        addHaloLayer(event, "slim");
    }

    private static void addHaloLayer(EntityRenderersEvent.AddLayers event, String skinType)
    {
        PlayerRenderer playerRenderer = event.getSkin(skinType);
        if (playerRenderer != null)
        {
            playerRenderer.addLayer(new HaloRenderLayer(playerRenderer));
        }
    }
}
