package miku.united_as_one.genesis.client.render;

import miku.united_as_one.genesis.client.renderer.entity.test.BaiZeLiRenderer;
import miku.united_as_one.genesis.client.renderer.entity.warlock.WardenMageRenderer;
import miku.united_as_one.genesis.common.entity.spell.blood_boss.blood_dagger.BloodDaggerRenderer;
import miku.united_as_one.genesis.common.entity.test.BaiZeLiEntity;
import miku.united_as_one.genesis.init.registry.BaiZeEntities;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.renderer.entity.boss.BloodBossRenderer;
import miku.united_as_one.genesis.client.render.cosmic.AvaritiaShaders;
import miku.united_as_one.genesis.client.render.cosmic.CosmicModelLoader;
import miku.united_as_one.genesis.client.render.entity.*;
import miku.united_as_one.genesis.client.render.entity.arrow.BloodArrowRenderer;
import miku.united_as_one.genesis.client.render.entity.arrow.HolyArrowRenderer;
import miku.united_as_one.genesis.client.render.entity.arrow.StellarArrowRenderer;
import miku.united_as_one.genesis.client.render.entity.arrow.ThunderArrowRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
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
        event.registerEntityRenderer(EntityRegistry.THROW_BLOOD_AND_WOUNDS.get(), ThrowBloodAndWoundsRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_BOSS.get(), BloodBossRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_TENTACLE.get(), BloodTentacleRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_BOSS_FIRE_ERUPTION_AOE.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_DAGGER_PROJECTILE.get(), BloodDaggerRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_FIELD.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.TREMOR_AOE_ENTITY.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.WARDEN_MANCER.get(), WardenMageRenderer::new);
        event.registerEntityRenderer(BaiZeEntities.BAI_ZE.get(), BaiZeLiRenderer::new);



        // 特效箭矢渲染器注册
        event.registerEntityRenderer(EntityRegistry.THUNDER_ARROW.get(), ThunderArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.HOLY_ARROW.get(), HolyArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_ARROW.get(), BloodArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.STELLAR_ARROW.get(), StellarArrowRenderer::new);
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