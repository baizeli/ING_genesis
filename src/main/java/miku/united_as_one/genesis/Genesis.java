package miku.united_as_one.genesis;

import miku.united_as_one.genesis.Content.ArcaneWorkbench.*;
import miku.united_as_one.genesis.Content.*;
import miku.united_as_one.genesis.Content.Workbenchs.*;
import miku.united_as_one.genesis.Entity.*;
import miku.united_as_one.genesis.Entity.spells.celestial_source.*;
import miku.united_as_one.genesis.Entity.spells.celestial_source.notuse.*;
import miku.united_as_one.genesis.Items.ModItems;
import miku.united_as_one.genesis.client.ClientEvent;
import miku.united_as_one.genesis.client.particles.ModParticles;
import miku.united_as_one.genesis.client.renderer.DistortWorldRender;
import miku.united_as_one.genesis.client.renderer.spell.celestial_source.DeadStarDecreeCometRenderer;
import miku.united_as_one.genesis.config.*;
import miku.united_as_one.genesis.effect.spell.ModEffect;
import miku.united_as_one.genesis.fonts.FuckFont1;
import miku.united_as_one.genesis.network.*;
import miku.united_as_one.genesis.sound.SoundsRegister;
import miku.united_as_one.genesis.spell.*;
import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.registries.*;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod(Genesis.MOD_ID)
public class Genesis
{
    
    public static final String MOD_ID = "iron_spells_genesis";
    public static final String MODID = MOD_ID; // 添加这个别名以保持兼容性
    private static final Logger LOGGER = LogUtils.getLogger();

    public static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "wirebox_sync"),
            () -> "1.0",
            "1.0"::equals,
            "1.0"::equals
    );

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public Genesis(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        SoundsRegister.SOUND_EVENTS.register(modEventBus);

        SpellSchool.register(modEventBus);
        Spells.register(modEventBus);
        SpellAttributes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModEffect.register(modEventBus);
        ModParticles.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addAttribute); // 添加实体属性创建事件监听器
        modEventBus.addListener(this::addToVanillaTabs); // 给自己的物品加到别人的标签栏里面
        modEventBus.addListener(this::onAttributeCreate);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(ClientEvent.class);

        context.registerConfig(ModConfig.Type.COMMON, Configuration.SPECIFICATION);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            NetworkHandler.register();
            LOGGER.info("Fuck TTTTTT");
        });
    }

    // 添加实体属性创建事件处理程序
    private void addAttribute(EntityAttributeCreationEvent event)
    {
        event.put(ModEntities.BLOOD_BOSS.get(), Monster.createMonsterAttributes().build());
    }

    private void addToVanillaTabs(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == CreativeTabRegistry.MATERIALS_TAB.getKey()) {
            // 加到自然符文的后面
            e.getEntries().putAfter(ItemRegistry.NATURE_RUNE.get().getDefaultInstance(),
                    ModItems.CHAOS_RUNE.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            // 混沌后面
            e.getEntries().putAfter(ModItems.CHAOS_RUNE.get().getDefaultInstance(),
                    ModItems.CELESTIAL_SOURCE_RUNE.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            // 加到自然升级法球的后面
            e.getEntries().putAfter(ItemRegistry.NATURE_UPGRADE_ORB.get().getDefaultInstance(),
                    ModItems.CHAOS_UPGRADE_ORB.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            // 混沌后面
            e.getEntries().putAfter(ModItems.CHAOS_UPGRADE_ORB.get().getDefaultInstance(),
                    ModItems.CELESTIAL_SOURCE_UPGRADE_ORB.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    public void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MAGIC_CIRCLE.get(), MagicCircle.createAttributes().build());
        event.put(ModEntities.BOX_ENTIYT.get(), BoxEntity.createAttributes().build());
        event.put(ModEntities.SWORD_ENTITY.get(), SwordEntity.createAttributes().build());
    }

    public static String resource(String location)
    {
        return MOD_ID + ":" + location;
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.VANILLA_WORKBENCH_MENU.get(), VanillaWorkbenchScreen::new);
                MenuScreens.register(ModMenuTypes.ARCANE_WORKBENCH_MENU.get(), ArcaneWorkbenchScreen::new);
                EntityRenderers.register(ModEntities.NYAN_CAT.get(), NyanCatRenderer::new);
                EntityRenderers.register(ModEntities.MAGIC_CIRCLE.get(), MagicCircleRenderer::new);
                EntityRenderers.register(ModEntities.BOX_ENTIYT.get(), BoxEntityRenderer::new);
                EntityRenderers.register(ModEntities.LIGHTNING_BOLT.get(), LightningBoltRenderer::new);
                EntityRenderers.register(ModEntities.SWORD_ENTITY.get(), SwordEntityRenderer::new);

                EntityRenderers.register(ModEntities.DEAD_STAR_DECREE_COMET.get(), 
                    context -> new DeadStarDecreeCometRenderer(context, 0.25f)
                );
                
                EntityRenderers.register(ModEntities.DEAD_STAR_DECREE_LARGE_COMET.get(), 
                    context -> new DeadStarDecreeCometRenderer(context, 6.0f)
                );

                CuriosRendererRegistry.register(ModItems.CHAOS_SPELL_BOOK.get(), SpellBookCurioRenderer::new);
                CuriosRendererRegistry.register(ModItems.CELESTIAL_SOURCE_SPELL_BOOK.get(), SpellBookCurioRenderer::new);
                DistortWorldRender.initChain(Minecraft.getInstance());
            });
            MinecraftForge.registerConfigScreen(new ConfigurationFactory());
            Minecraft.getInstance().font = FuckFont1.font;
        }

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event)
        {
            CHANNEL.registerMessage(0, WireBoxSyncPacket.class,
                    WireBoxSyncPacket::encode,
                    WireBoxSyncPacket::decode,
                    WireBoxSyncPacket::handle
            );

            CHANNEL.registerMessage(1, DeadListSyncPacket.class,
                    DeadListSyncPacket::encode,
                    DeadListSyncPacket::decode,
                    DeadListSyncPacket::handle
            );

            CHANNEL.registerMessage(2, MarkDeadPacket.class,
                    MarkDeadPacket::encode,
                    MarkDeadPacket::decode,
                    MarkDeadPacket::handle
            );
        }
    }
}