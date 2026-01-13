package com.baizeli.eternisstarrysky.render;

import com.baizeli.eternisstarrysky.Entity.ModEntities;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.render.cosmic.AvaritiaShaders;
import com.baizeli.eternisstarrysky.render.cosmic.CosmicModelLoader;
import com.baizeli.eternisstarrysky.render.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderRegistry {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.LIGHTNING_BOLT, PurpleLightningRenderer::new);
        event.registerEntityRenderer(ModEntities.CUSTOM_ARROW.get(), CustomArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.BLOOD_BOSS.get(), BloodBossRenderer::new);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterShaders(RegisterShadersEvent event) {
        AvaritiaShaders.onRegisterShaders(event);
    }

    @SubscribeEvent
    public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("cosmic", CosmicModelLoader.INSTANCE);
        //event.register("cosmic_1",CosmicModelLoader.INSTANCE);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        addLayer(event, "default");
        addLayer(event, "slim");
    }

    private static void addLayer(EntityRenderersEvent.AddLayers event, String skinType) {
        PlayerRenderer playerRenderer = event.getSkin(skinType);
        if (playerRenderer != null) {
            playerRenderer.addLayer(new HaloRenderLayer(playerRenderer));
            playerRenderer.addLayer(new CrownRenderLayer(playerRenderer));
            playerRenderer.addLayer(new ShieldRenderLayer(playerRenderer));
        }
    }
}