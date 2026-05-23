package miku.united_as_one.genesis.registries.client;

import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperRenderer;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.render.cosmic.AvaritiaShaders;
import miku.united_as_one.genesis.client.render.cosmic.CosmicModelLoader;
import miku.united_as_one.genesis.client.render.entity.*;
import miku.united_as_one.genesis.client.render.entity.arrow.BloodArrowRenderer;
import miku.united_as_one.genesis.client.render.entity.arrow.HolyArrowRenderer;
import miku.united_as_one.genesis.client.render.entity.arrow.StellarArrowRenderer;
import miku.united_as_one.genesis.client.render.entity.arrow.ThunderArrowRenderer;
import miku.united_as_one.genesis.client.renderer.WSRenderer;
import miku.united_as_one.genesis.client.renderer.entity.boss.*;
import miku.united_as_one.genesis.client.renderer.entity.laser.DeathLaserRenderer;
import miku.united_as_one.genesis.client.renderer.entity.spell.celestial_source.DeadStarDecreeCometRenderer;
import miku.united_as_one.genesis.client.renderer.entity.test.BaiZeLiRenderer;
import miku.united_as_one.genesis.client.renderer.projectile.ThrownIronRenderer;
import miku.united_as_one.genesis.contents.entity.LightningBoltRenderer;
import miku.united_as_one.genesis.contents.entity.NyanCatRenderer;
import miku.united_as_one.genesis.contents.entity.gungnir.GungnirDaggerRenderer;
import miku.united_as_one.genesis.contents.entity.spell.blood_boss.blood_dagger.BloodDaggerRenderer;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.BoxEntityRenderer;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.blade_works.MagicCircleRenderer;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.blade_works.SwordEntityRenderer;
import miku.united_as_one.genesis.contents.workbench.arcane_cauldron.ArcaneCauldronRenderer;
import miku.united_as_one.genesis.registries.entity.EntityRegistry;
import miku.united_as_one.genesis.registries.workbench.ModBlockEntities;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.WardenRenderer;
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
        // 原版闪电替换渲染器
        event.registerEntityRenderer(EntityType.LIGHTNING_BOLT, PurpleLightningRenderer::new);

        // 通用投射物与法术特效实体
        event.registerEntityRenderer(EntityRegistry.CUSTOM_ARROW.get(), CustomArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.NYAN_CAT.get(), NyanCatRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MAGIC_CIRCLE.get(), MagicCircleRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BOX_ENTITY.get(), BoxEntityRenderer::new);
        event.registerEntityRenderer(EntityRegistry.LIGHTNING_BOLT.get(), LightningBoltRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SWORD_ENTITY.get(), SwordEntityRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DEATH_LASER.get(), DeathLaserRenderer::new);
        event.registerEntityRenderer(EntityRegistry.THROWN_IRON.get(), ThrownIronRenderer::new);
        event.registerEntityRenderer(EntityRegistry.THROW_BLOOD_AND_WOUNDS.get(), ThrowBloodAndWoundsRenderer::new);

        // Boss、召唤物与 Boss 技能区域实体
        event.registerEntityRenderer(EntityRegistry.BLOOD_BOSS.get(), BloodBossRenderer::new);
        event.registerEntityRenderer(EntityRegistry.HAMMER_MOB.get(), HammerMobRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_TENTACLE.get(), BloodTentacleRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_BOSS_FIRE_ERUPTION_AOE.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_DAGGER_PROJECTILE.get(), BloodDaggerRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_FIELD.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.TREMOR_AOE_ENTITY.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BAI_ZE_LI.get(), BaiZeLiRenderer::new);
        event.registerEntityRenderer(EntityRegistry.GUNGNIR_DAGGER_PROJECTILE.get(), GungnirDaggerRenderer::new);
        event.registerEntityRenderer(EntityRegistry.GUNGNIR_CHAIN_LIGHTNING_PROJECTILE.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.WARDEN_SPELLCASTER.get(), WSRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DEAD_STAR_DECREE_COMET.get(),
                context -> new DeadStarDecreeCometRenderer(context, 0.25f)
        );
        event.registerEntityRenderer(EntityRegistry.DEAD_STAR_DECREE_LARGE_COMET.get(),
                context -> new DeadStarDecreeCometRenderer(context, 6.0f)
        );
        event.registerEntityRenderer(EntityRegistry.SUMMONED_KEEPER.get(), KeeperRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_WARDEN.get(), WardenRenderer::new);

        // 特效箭渲染器注册
        event.registerEntityRenderer(EntityRegistry.THUNDER_ARROW.get(), ThunderArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.HOLY_ARROW.get(), HolyArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_ARROW.get(), BloodArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.STELLAR_ARROW.get(), StellarArrowRenderer::new);

        event.registerBlockEntityRenderer(ModBlockEntities.ARCANE_CAULDRON.get(), ArcaneCauldronRenderer::new);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterShaders(RegisterShadersEvent event) {
        AvaritiaShaders.onRegisterShaders(event);
    }

    @SubscribeEvent
    public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("cosmic", CosmicModelLoader.INSTANCE);
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
