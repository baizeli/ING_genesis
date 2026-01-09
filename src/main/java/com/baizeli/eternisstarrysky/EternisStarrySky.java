package com.baizeli.eternisstarrysky;

import com.baizeli.eternisstarrysky.Content.ArcaneWorkbench.ArcaneWorkbenchScreen;
import com.baizeli.eternisstarrysky.Content.ModBlocks;
import com.baizeli.eternisstarrysky.Content.Workbenchs.*;
import com.baizeli.eternisstarrysky.Entity.*;
import com.baizeli.eternisstarrysky.Entity.spells.celestial_source.*;
import com.baizeli.eternisstarrysky.Entity.spells.celestial_source.notuse.*;
import com.baizeli.eternisstarrysky.Items.ModItems;
import com.baizeli.eternisstarrysky.client.ClientEvent;
import com.baizeli.eternisstarrysky.client.particles.ModParticles;
import com.baizeli.eternisstarrysky.client.renderer.spell.celestial_source.DeadStarDecreeCometRenderer;
import com.baizeli.eternisstarrysky.config.Configuration;
import com.baizeli.eternisstarrysky.config.ConfigurationFactory;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import com.baizeli.eternisstarrysky.fonts.FuckFont1;
import com.baizeli.eternisstarrysky.network.DeadListSyncPacket;
import com.baizeli.eternisstarrysky.network.MarkDeadPacket;
import com.baizeli.eternisstarrysky.network.NetworkHandler;
import com.baizeli.eternisstarrysky.network.WireBoxSyncPacket;
import com.baizeli.eternisstarrysky.sound.SoundsRegister;
import com.baizeli.eternisstarrysky.spell.SpellAttributes;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import com.baizeli.eternisstarrysky.spell.Spells;
import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.registries.CreativeTabRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(EternisStarrySky.MOD_ID)
public class EternisStarrySky
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

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);


    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("iron_spells_genesis_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("itemGroup." + MOD_ID + ".iron_spells_genesis_tab"))
            .icon(() -> ModItems.GALAXY_SCROLL.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.ETERNIS_APPLE.get());
                output.accept(ModItems.PURPLEITE_GALAXY_INGOT.get());
                output.accept(ModItems.WHISPER_OF_THE_PAST.get());
                output.accept(ModItems.CREATE_STAR.get());
                output.accept(ModItems.GALAXY_SCROLL.get());
                output.accept(ModItems.GOOD_CAKE.get());

                output.accept(ModItems.INFINITY_SWORD.get());
                output.accept(ModItems.AVARITIA_SWORD.get());
                
                /*output.accept(ModItems.INFINITY_ETERNAL_HELMET.get());
                output.accept(ModItems.INFINITY_ETERNAL_CHESTPLATE.get());
                output.accept(ModItems.INFINITY_ETERNAL_LEGGINGS.get());
                output.accept(ModItems.INFINITY_ETERNAL_BOOTS.get());*/

                // 神圣金属套
                output.accept(ModItems.DIVINE_METAL_HELMET.get());
                output.accept(ModItems.DIVINE_METAL_CHESTPLATE.get());
                output.accept(ModItems.DIVINE_METAL_LEGGINGS.get());
                output.accept(ModItems.DIVINE_METAL_BOOTS.get());

                // 星源法术套
                output.accept(ModItems.CELESTIAL_SOURCE_SPELL_HELMET.get());
                output.accept(ModItems.CELESTIAL_SOURCE_SPELL_CHESTPLATE.get());
                output.accept(ModItems.CELESTIAL_SOURCE_SPELL_LEGGINGS.get());
                output.accept(ModItems.CELESTIAL_SOURCE_SPELL_BOOTS.get());
                
                // 混沌法术套
                output.accept(ModItems.CHAOS_SPELL_HELMET.get());
                output.accept(ModItems.CHAOS_SPELL_CHESTPLATE.get());
                output.accept(ModItems.CHAOS_SPELL_LEGGINGS.get());
                output.accept(ModItems.CHAOS_SPELL_BOOTS.get());

                // 紫极战斗套
                output.accept(ModItems.VIOLET_ZENITH_HELMET.get());
                output.accept(ModItems.VIOLET_ZENITH_CHESTPLATE.get());
                output.accept(ModItems.VIOLET_ZENITH_LEGGINGS.get());
                output.accept(ModItems.VIOLET_ZENITH_BOOTS.get());

                output.accept(ModItems.WORKBENCH.get());

                // 奥术工作台
                /*output.accept(ModItems.ARCANE_WORKBENCH.get());*/
                
                output.accept(ModItems.CHAOS_SPELL_BOOK.get());
                output.accept(ModItems.CELESTIAL_SOURCE_SPELL_BOOK.get());

                // 混沌法杖
                output.accept(ModItems.CHAOS_STAFF.get());

                // 星源法杖
                output.accept(ModItems.CELESTIAL_SOURCE_STAFF.get());

                // 扭曲之混沌
                output.accept(ModItems.TWISTED_CHAOS.get());

                // 星源珍珠
                output.accept(ModItems.CELESTIAL_SOURCE_PEARL.get());

                // 星源铁锭
                output.accept(ModItems.CELESTIAL_SOURCE_INGOT.get());

                // 神圣金属锭
                output.accept(ModItems.DIVINE_METAL_INGOT.get());

                // 扭曲混沌锭
                output.accept(ModItems.TWISTED_CHAOS_INGOT.get());
                
                // 老王237
                output.accept(ModItems.LAO_WANG_237.get());

                // 创世之诅咒
                output.accept(ModItems.GENESIS_CURSE.get());

                // 无限忏悔石
                output.accept(ModItems.INFINITE_SHRIVING_STONE.get());

                // 飞燕穿柳
                output.accept(ModItems.FLYING_SWALLOW_THROUGH_Willow.get());

                // 水晶
                output.accept(ModItems.ARCANE_CRYSTAL.get());
                output.accept(ModItems.BLOOD_CRYSTAL.get());
                output.accept(ModItems.ELDRITCH_CRYSTAL.get());
                output.accept(ModItems.ENDER_CRYSTAL.get());
                output.accept(ModItems.EVOCATION_CRYSTAL.get());
                output.accept(ModItems.FIRE_CRYSTAL.get());
                output.accept(ModItems.HOLY_CRYSTAL.get());
                output.accept(ModItems.ICE_CRYSTAL.get());
                output.accept(ModItems.LIGHTNING_CRYSTAL.get());
                output.accept(ModItems.NATURE_CRYSTAL.get());
                output.accept(ModItems.CHAOS_CRYSTAL.get());
                output.accept(ModItems.CELESTIAL_SOURCE_CRYSTAL.get());

                // 手稿
                output.accept(ModItems.CHAOS_MANUSCRIPT.get());
                output.accept(ModItems.CELESTIAL_SOURCE_MANUSCRIPT.get());

                // 手稿碎片
                output.accept(ModItems.CHAOS_MANUSCRIPT_FRAGMENT.get());
                output.accept(ModItems.CELESTIAL_SOURCE_MANUSCRIPT_FRAGMENT.get());

                // 奥术水晶矿
                output.accept(ModItems.ARCANE_CRYSTAL_ORE_ITEM.get());
                output.accept(ModItems.ARCANE_CRYSTAL_ORE_DEEPSLATE_ITEM.get());
                output.accept(ModItems.NETHER_ARCANE_CRYSTAL_ORE_ITEM.get());
                output.accept(ModItems.END_ARCANE_CRYSTAL_ORE_ITEM.get());
            }).build());

    public EternisStarrySky(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
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