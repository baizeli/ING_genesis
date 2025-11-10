package com.baizeli.eternisstarrysky;

import com.baizeli.eternisstarrysky.client.network.WireBoxSyncPacket;
import com.baizeli.eternisstarrysky.fonts.FuckFont1;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import com.baizeli.eternisstarrysky.client.particles.ModParticles;
import org.slf4j.Logger;

import com.baizeli.eternisstarrysky.Content.ModBlock;
import com.baizeli.eternisstarrysky.Content.Workbenchs.ModBlockEntities;
import com.baizeli.eternisstarrysky.Content.Workbenchs.ModMenuTypes;
import com.baizeli.eternisstarrysky.Content.Workbenchs.ModRecipeSerializers;
import com.baizeli.eternisstarrysky.Content.Workbenchs.ModRecipeTypes;
import com.baizeli.eternisstarrysky.Content.Workbenchs.VanillaWorkbenchScreen;
import com.baizeli.eternisstarrysky.Entity.ModEntities;
import com.baizeli.eternisstarrysky.Entity.SwordManCsdyRenderer;
import com.baizeli.eternisstarrysky.Items.ModItems;
import com.baizeli.eternisstarrysky.spell.Attributes;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import com.baizeli.eternisstarrysky.spell.Spells;
import com.mojang.logging.LogUtils;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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


    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("eternisstarrysky_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("itemGroup." + MOD_ID + ".eternisstarrysky_tab"))
            .icon(() -> ModItems.PURPLEITE_GALAXY_INGOT.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.ETERNIS_APPLE.get());
                output.accept(ModItems.PEACH.get());
                output.accept(ModItems.PURPLEITE_GALAXY_INGOT.get());
                output.accept(ModItems.WHISPER_OF_THE_PAST.get());
                output.accept(ModItems.PRIMOGEM.get());
                output.accept(ModItems.CREATE_STAR.get());
                output.accept(ModItems.GALAXY_SCROLL.get());
                output.accept(ModItems.IMPURE_FRUIT.get());
                output.accept(ModItems.PURE_FRUIT.get());
                output.accept(ModItems.GOOD_CAKE.get());

                output.accept(ModItems.INFINITY_SWORD.get());
                output.accept(ModItems.AVARITIA_SWORD.get());
                
                output.accept(ModItems.INFINITY_ETERNAL_HELMET.get());
                output.accept(ModItems.INFINITY_ETERNAL_CHESTPLATE.get());
                output.accept(ModItems.INFINITY_ETERNAL_LEGGINGS.get());
                output.accept(ModItems.INFINITY_ETERNAL_BOOTS.get());

                output.accept(ModItems.WORKBENCH.get());
                
                output.accept(ModItems.CHAOS_SPELL_BOOK.get());
                output.accept(ModItems.CELESTIAL_SOURCE_SPELL_BOOK.get());

                // 混沌法杖
                output.accept(ModItems.CHAOS_STAFF.get());

                // 星源法杖
                output.accept(ModItems.CELESTIAL_SOURCE_STAFF.get());

                // 扭曲之混沌
                output.accept(ModItems.TWISTED_CHAOS.get());
            }).build());

    public EternisStarrySky(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModBlock.BLOCKS.register(modEventBus);
        SoundsRegister.SOUND_EVENTS.register(modEventBus);

        SpellSchool.register(modEventBus);
        Spells.register(modEventBus);
        Attributes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);

        ModParticles.register(modEventBus);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            modEventBus.addListener(ModKeyBindings::onRegisterKeyMappings);
        }

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addAttribute); // 添加实体属性创建事件监听器

        MinecraftForge.EVENT_BUS.register(this);
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
    private void addAttribute(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SWORD_MAN_CSDY.get(), 
                  Mob.createMobAttributes()
                     .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 4.4)
                     .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 10000.0)
                     .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 1600.0)
                     .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED, 20.0)
                     .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 128)
                     .build());
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
                EntityRenderers.register(ModEntities.SWORD_MAN_CSDY.get(), SwordManCsdyRenderer::new);
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
        }
    }
}