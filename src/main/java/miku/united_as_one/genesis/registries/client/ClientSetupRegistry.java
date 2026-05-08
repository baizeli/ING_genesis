package miku.united_as_one.genesis.registries.client;

import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperRenderer;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.renderer.DistortWorldRender;
import miku.united_as_one.genesis.client.renderer.WSRenderer;
import miku.united_as_one.genesis.client.renderer.entity.laser.DeathLaserRenderer;
import miku.united_as_one.genesis.client.renderer.entity.spell.celestial_source.DeadStarDecreeCometRenderer;
import miku.united_as_one.genesis.client.renderer.projectile.ThrownIronRenderer;
import miku.united_as_one.genesis.config.menu.ConfigMenu;
import miku.united_as_one.genesis.contents.entity.LightningBoltRenderer;
import miku.united_as_one.genesis.contents.entity.NyanCatRenderer;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.BoxEntityRenderer;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.notuse.MagicCircleRenderer;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.notuse.SwordEntityRenderer;
import miku.united_as_one.genesis.contents.workbench.arcane.ArcaneWorkbenchScreen;
import miku.united_as_one.genesis.registries.EntityRegistry;
import miku.united_as_one.genesis.registries.ItemRegistry;
import miku.united_as_one.genesis.registries.workbench.ModMenuTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.WardenRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetupRegistry {
    private ClientSetupRegistry() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.ARCANE_WORKBENCH_MENU.get(), ArcaneWorkbenchScreen::new);
            EntityRenderers.register(EntityRegistry.NYAN_CAT.get(), NyanCatRenderer::new);
            EntityRenderers.register(EntityRegistry.MAGIC_CIRCLE.get(), MagicCircleRenderer::new);
            EntityRenderers.register(EntityRegistry.BOX_ENTIYT.get(), BoxEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.LIGHTNING_BOLT.get(), LightningBoltRenderer::new);
            EntityRenderers.register(EntityRegistry.SWORD_ENTITY.get(), SwordEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.DEATH_LASER.get(), DeathLaserRenderer::new);
            EntityRenderers.register(EntityRegistry.THROWN_IRON.get(), ThrownIronRenderer::new);
            EntityRenderers.register(EntityRegistry.WARDEN_SPELLCASTER.get(), WSRenderer::new);
            EntityRenderers.register(EntityRegistry.DEAD_STAR_DECREE_COMET.get(),
                    context -> new DeadStarDecreeCometRenderer(context, 0.25f)
            );

            EntityRenderers.register(EntityRegistry.DEAD_STAR_DECREE_LARGE_COMET.get(),
                    context -> new DeadStarDecreeCometRenderer(context, 6.0f)
            );

            EntityRenderers.register(EntityRegistry.SUMMONED_KEEPER.get(), KeeperRenderer::new);
            EntityRenderers.register(EntityRegistry.SUMMONED_WARDEN.get(), WardenRenderer::new);

            CuriosRendererRegistry.register(ItemRegistry.CHAOS_SPELL_BOOK.get(), SpellBookCurioRenderer::new);
            CuriosRendererRegistry.register(ItemRegistry.CELESTIAL_SOURCE_SPELL_BOOK.get(), SpellBookCurioRenderer::new);
            DistortWorldRender.initChain(Minecraft.getInstance());

            miku.united_as_one.genesis.client.render.luminous.GenesisRegistry.init();
        });
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, lastScreen) -> new ConfigMenu(lastScreen))
        );
    }
}
