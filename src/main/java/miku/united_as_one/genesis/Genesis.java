package miku.united_as_one.genesis;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import miku.united_as_one.genesis.common.data.content.arcaneWorkbench.*;
import miku.united_as_one.genesis.common.data.content.workbenchs.*;
import miku.united_as_one.genesis.common.entity.*;
import miku.united_as_one.genesis.init.registry.*;
import miku.united_as_one.genesis.common.entity.ai.ModActivity;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.spells.celestial_source.*;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.spells.celestial_source.notuse.*;
import miku.united_as_one.genesis.client.ClientEvent;
import miku.united_as_one.genesis.init.registry.BlockRegistry;
import miku.united_as_one.genesis.init.registry.CreativeTabRegistry;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import miku.united_as_one.genesis.init.registry.ItemRegistry;
import miku.united_as_one.genesis.init.registry.client.ParticleRegistry;
import miku.united_as_one.genesis.client.renderer.DistortWorldRender;
import miku.united_as_one.genesis.client.renderer.spell.celestial_source.DeadStarDecreeCometRenderer;
import miku.united_as_one.genesis.init.config.*;
import miku.united_as_one.genesis.common.network.*;
import miku.united_as_one.genesis.init.registry.spell.SpellAttributesRegistry;
import miku.united_as_one.genesis.init.registry.spell.SpellSchoolRegistry;
import dev.xkmc.l2library.base.L2Registrate;
import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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

@SuppressWarnings("removal")
@Mod(Genesis.MOD_ID)
public class Genesis
{
    
    public static final String MOD_ID = "iron_spells_genesis";
    public static final String MODID = MOD_ID; // 添加这个别名以保持兼容性
    public static final L2Registrate L2_REGISTRATE = new L2Registrate(MOD_ID);
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

    public Genesis(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ItemRegistry.register(modEventBus);
        CreativeTabRegistry.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        SoundRegister.SOUND_EVENTS.register(modEventBus);

        SpellSchoolRegistry.register(modEventBus);
        /*SpellRegistry.register(modEventBus);*/
        SpellAttributesRegistry.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        EffectRegistry.register(modEventBus);
        ParticleRegistry.register(modEventBus);

        ModActivity.register(modEventBus);
        ModMemoryModuleType.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addToVanillaTabs); // 给自己的物品加到别人的标签栏里面
        modEventBus.addListener(this::onAttributeCreate);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(ClientEvent.class);

        context.registerConfig(ModConfig.Type.COMMON, Configuration.SPECIFICATION);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.register();
            LOGGER.info("Fuck TTTTTT");
        });
    }

    private void addToVanillaTabs(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == io.redspace.ironsspellbooks.registries.CreativeTabRegistry.MATERIALS_TAB.getKey()) {
            // 加到自然符文的后面
            e.getEntries().putAfter(io.redspace.ironsspellbooks.registries.ItemRegistry.NATURE_RUNE.get().getDefaultInstance(),
                    ItemRegistry.CHAOS_RUNE.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            // 混沌后面
            e.getEntries().putAfter(ItemRegistry.CHAOS_RUNE.get().getDefaultInstance(),
                    ItemRegistry.CELESTIAL_SOURCE_RUNE.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            // 加到自然升级法球的后面
            e.getEntries().putAfter(io.redspace.ironsspellbooks.registries.ItemRegistry.NATURE_UPGRADE_ORB.get().getDefaultInstance(),
                    ItemRegistry.CHAOS_UPGRADE_ORB.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            // 混沌后面
            e.getEntries().putAfter(ItemRegistry.CHAOS_UPGRADE_ORB.get().getDefaultInstance(),
                    ItemRegistry.CELESTIAL_SOURCE_UPGRADE_ORB.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        } else if (e.getTab() == io.redspace.ironsspellbooks.registries.CreativeTabRegistry.SCROLLS_TAB.get()) {
            // 构造地狱浮现的法术卷轴
            ItemStack raiseHellMaxStack = new ItemStack(io.redspace.ironsspellbooks.registries.ItemRegistry.SCROLL.get());
            AbstractSpell raiseHell = SpellRegistry.RAISE_HELL_SPELL.get();
            ISpellContainer.createScrollContainer(raiseHell, raiseHell.getMaxLevel(), raiseHellMaxStack);

            // 构造七连炽焰飞剑的法术卷轴
            AbstractSpell spell = CreativeTabRegistry.BLAZING_BLADE_BARRAGE_SPELL.get();

            // 循环插入法术的所有等级
            for (int i = spell.getMaxLevel(); i >= spell.getMinLevel(); --i) { // 先插入等级高的，这样等级低的就可以在等级高的前面了
                ItemStack levelStack = new ItemStack(io.redspace.ironsspellbooks.registries.ItemRegistry.SCROLL.get());
                ISpellContainer.createScrollContainer(spell, i, levelStack);
                // Raise Hell法术的最高级卷轴后面
                e.getEntries().putAfter(raiseHellMaxStack, levelStack,
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

    public void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.MAGIC_CIRCLE.get(), MagicCircle.createAttributes().build());
        event.put(EntityRegistry.BOX_ENTIYT.get(), BoxEntity.createAttributes().build());
        event.put(EntityRegistry.SWORD_ENTITY.get(), SwordEntity.createAttributes().build());
        event.put(EntityRegistry.BLOOD_BOSS.get(), BloodBoss.setAttributes().build());
        event.put(EntityRegistry.BLOOD_TENTACLE.get(), VoidTentacle.createLivingAttributes().build());
    }

    public static String resource(String location)
    {
        return MOD_ID + ":" + location;
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.ARCANE_WORKBENCH_MENU.get(), ArcaneWorkbenchScreen::new);
                EntityRenderers.register(EntityRegistry.NYAN_CAT.get(), NyanCatRenderer::new);
                EntityRenderers.register(EntityRegistry.MAGIC_CIRCLE.get(), MagicCircleRenderer::new);
                EntityRenderers.register(EntityRegistry.BOX_ENTIYT.get(), BoxEntityRenderer::new);
                EntityRenderers.register(EntityRegistry.LIGHTNING_BOLT.get(), LightningBoltRenderer::new);
                EntityRenderers.register(EntityRegistry.SWORD_ENTITY.get(), SwordEntityRenderer::new);
//                EntityRenderers.register(EntityRegistry.THROW_BLOOD_AND_WOUNDS.get(), ThrowBloodAndWoundsRenderer::new);

                EntityRenderers.register(EntityRegistry.DEAD_STAR_DECREE_COMET.get(),
                    context -> new DeadStarDecreeCometRenderer(context, 0.25f)
                );
                
                EntityRenderers.register(EntityRegistry.DEAD_STAR_DECREE_LARGE_COMET.get(),
                    context -> new DeadStarDecreeCometRenderer(context, 6.0f)
                );

                CuriosRendererRegistry.register(ItemRegistry.CHAOS_SPELL_BOOK.get(), SpellBookCurioRenderer::new);
                CuriosRendererRegistry.register(ItemRegistry.CELESTIAL_SOURCE_SPELL_BOOK.get(), SpellBookCurioRenderer::new);
                DistortWorldRender.initChain(Minecraft.getInstance());
            });
            MinecraftForge.registerConfigScreen(new ConfigurationFactory());
            //Minecraft.getInstance().font = FuckFont1.font;
        }

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
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