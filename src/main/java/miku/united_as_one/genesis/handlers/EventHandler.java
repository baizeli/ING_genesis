package miku.united_as_one.genesis.handlers;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.api.render.RenderUtils;
import miku.united_as_one.genesis.client.render.tooltip.TooltipBackgroundRenderContext;
import miku.united_as_one.genesis.contents.items.CelestialSourceBase;
import miku.united_as_one.genesis.contents.items.ChaosBase;
import miku.united_as_one.genesis.contents.items.spell.spellbook.CelestialSourceSpellBook;
import miku.united_as_one.genesis.contents.items.spell.spellbook.ChaosSpellBook;
import miku.united_as_one.genesis.contents.items.spell.staff.CelestialSourceStaff;
import miku.united_as_one.genesis.contents.items.spell.staff.ChaosStaff;
import miku.united_as_one.genesis.contents.items.armor.CelestialSourceSpellArmor;
import miku.united_as_one.genesis.contents.items.armor.ChaosSpellArmor;
import miku.united_as_one.genesis.client.renderer.entity.spell.chaos.WireBoxRenderer;
import miku.united_as_one.genesis_core.utils.EventUtil;
import miku.united_as_one.genesis.packets.DeadListSyncPacket;
import miku.united_as_one.genesis.packets.NetworkHandler;
import miku.united_as_one.genesis.packets.WireBoxSyncPacket;
import miku.united_as_one.genesis.data.save.SaveManager;
import com.google.common.collect.Iterables;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = Genesis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        if (Arrays.toString(Thread.currentThread().getStackTrace()).contains("doLoad")) return;
        SaveManager.init(event.getServer());
        SaveManager.save();
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        SaveManager.init(event.getServer());
        SaveManager.load();
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkHandler.INSTANCE.send(net.minecraftforge.network.PacketDistributor.ALL.noArg(),
                new WireBoxSyncPacket(WireBoxRenderer.entitiesForRenderWireBoxRenderer,
                        WireBoxRenderer.entityRotationMap,
                        WireBoxRenderer.entityAxisMap));

        NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new DeadListSyncPacket(EventUtil.deadList));
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level instanceof ServerLevel serverLevel) {
            List<UUID> toRemove = new ArrayList<>();

            EventUtil.deadList.removeIf(uuid -> {
                LevelEntityGetter<Entity> entityGetter = serverLevel.entityManager.entityGetter;
                if (Iterables.size(entityGetter.getAll()) == 0) {
                    return false;
                }
                Entity entity = entityGetter.get(uuid);
                boolean shouldRemove = entity == null;
                if (shouldRemove) {
                    toRemove.add(uuid);
                }
                return shouldRemove;
            });

            if (!toRemove.isEmpty()) {
                NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), DeadListSyncPacket.removeAll(toRemove));
            }
        }
    }

// ... existing code ...


    @SubscribeEvent
    public static void onTooltipColor(RenderTooltipEvent.Color event) {
        try {
            TooltipBackgroundRenderContext.Style style = TooltipBackgroundRenderContext.resolve(event.getItemStack());
            if (style == TooltipBackgroundRenderContext.Style.TRANSPARENT) {
                event.setBackground(0);
                event.setBorderStart(0);
                event.setBorderEnd(0);
                return;
            }
            if (style.drawsCosmic()) {
                event.setBackground(0);
                renderCosmicTooltipBackground(event, style.cosmicType());
                return;
            }

            Item item = event.getItemStack().getItem();

            if (item instanceof CelestialSourceSpellArmor ||
                    item instanceof CelestialSourceStaff ||
                    item instanceof CelestialSourceSpellBook ||
                    item instanceof CelestialSourceBase) {
                ClientLevel clientLevel = Minecraft.instance.level;
                // 每 150 ticks
                long gameTime = 0;
                if (clientLevel != null) {
                    gameTime = clientLevel.levelData.getGameTime();
                }
                float hue = (gameTime % 150) / 150.0f; // 色相：0.0 → 1.0

                // 边框起始颜色
                int rgbStart = hsbToRGB(hue, 0.8f, 1.0f);
                event.setBorderStart(0xFF000000 | rgbStart);

                // 边框结束颜色
                int rgbEnd = hsbToRGB((hue + 0.3f) % 1.0f, 0.8f, 1.0f);
                event.setBorderEnd(0xFF000000 | rgbEnd);

                event.setBackgroundStart(0xF0000000); // 半透明黑背景
                event.setBackgroundEnd(0xF0000000);
            } else if (item instanceof ChaosSpellArmor ||
                    item instanceof ChaosStaff ||
                    item instanceof ChaosSpellBook ||
                    item instanceof ChaosBase) {
                event.setBorderStart(0xFFFF0000); // 红色
                event.setBorderEnd(0xFFFF0000);
            }
        } catch (RuntimeException ignored) {}
    }

    private static void renderCosmicTooltipBackground(RenderTooltipEvent.Color event, int cosmicType) {
        int width = 0;
        int height = event.getComponents().size() == 1 ? -2 : 0;

        for (ClientTooltipComponent component : event.getComponents()) {
            width = Math.max(width, component.getWidth(event.getFont()));
            height += component.getHeight();
        }

        if (width <= 0 || height <= 0) {
            return;
        }

        GuiGraphics graphics = event.getGraphics();
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 400.0F);
        RenderUtils.renderCosmicBackground(
                graphics.pose(),
                graphics.bufferSource(),
                null,
                width + 3,
                height + 3,
                event.getX() + width / 2.0,
                event.getY() + height / 2.0,
                15728880,
                cosmicType
        );
        graphics.pose().popPose();
    }

    /**
     * @param hue 色相 [0.0, 1.0)
     * @param saturation 饱和度 [0.0, 1.0]
     * @param brightness 亮度 [0.0, 1.0]
     * @return 颜色值（不含透明度）
     */
    private static int hsbToRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            // 灰度
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            int i = (int) h;
            float f = h - i;
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - saturation * (1.0f - f));
            switch (i) {
                case 0: r = (int)(brightness * 255); g = (int)(t * 255); b = (int)(p * 255); break;
                case 1: r = (int)(q * 255); g = (int)(brightness * 255); b = (int)(p * 255); break;
                case 2: r = (int)(p * 255); g = (int)(brightness * 255); b = (int)(t * 255); break;
                case 3: r = (int)(p * 255); g = (int)(q * 255); b = (int)(brightness * 255); break;
                case 4: r = (int)(t * 255); g = (int)(p * 255); b = (int)(brightness * 255); break;
                case 5: r = (int)(brightness * 255); g = (int)(p * 255); b = (int)(q * 255); break;
            }
        }
        return (r << 16) | (g << 8) | b;
    }
}
