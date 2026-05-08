package miku.united_as_one.genesis.registries.client;

import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.renderer.DistortWorldRender;
import miku.united_as_one.genesis.config.menu.ConfigMenu;
import miku.united_as_one.genesis.contents.workbench.arcane.ArcaneWorkbenchScreen;
import miku.united_as_one.genesis.registries.ItemRegistry;
import miku.united_as_one.genesis.registries.workbench.ModMenuTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
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
