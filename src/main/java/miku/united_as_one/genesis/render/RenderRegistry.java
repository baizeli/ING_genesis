package miku.united_as_one.genesis.render;

import miku.united_as_one.genesis.registry.EntityRegistry;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.renderer.boss.BloodBossRenderer;
import miku.united_as_one.genesis.render.cosmic.AvaritiaShaders;
import miku.united_as_one.genesis.render.cosmic.CosmicModelLoader;
import miku.united_as_one.genesis.render.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderRegistry {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.LIGHTNING_BOLT, PurpleLightningRenderer::new);
        event.registerEntityRenderer(EntityRegistry.CUSTOM_ARROW.get(), CustomArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_BOSS.get(), BloodBossRenderer::new);
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