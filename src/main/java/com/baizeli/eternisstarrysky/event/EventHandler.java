package com.baizeli.eternisstarrysky.event;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Items.CelestialSourceSpellBook;
import com.baizeli.eternisstarrysky.Items.ModItems;
import com.baizeli.eternisstarrysky.Items.Staff.CelestialSourceStaff;
import com.baizeli.eternisstarrysky.Items.armor.CelestialSourceSpellArmor;
import com.baizeli.eternisstarrysky.client.WireBoxRenderer;
import com.baizeli.eternisstarrysky.cora.utils.EventUtil;
import com.baizeli.eternisstarrysky.network.DeadListSyncPacket;
import com.baizeli.eternisstarrysky.network.WireBoxSyncPacket;
import com.baizeli.eternisstarrysky.save.SaveManager;
import com.google.common.collect.Iterables;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

import static com.baizeli.eternisstarrysky.EternisStarrySky.CHANNEL;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    public static Map<UUID, Long> affectedEntities = new HashMap<>();

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if (WireBoxRenderer.entitiesForRenderWireBoxRenderer.containsKey(event.getEntity().getUUID())) {
            event.setCanceled(true);
        }
    }

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
        CHANNEL.send(net.minecraftforge.network.PacketDistributor.ALL.noArg(),
                new WireBoxSyncPacket(WireBoxRenderer.entitiesForRenderWireBoxRenderer,
                        WireBoxRenderer.entityRotationMap,
                        WireBoxRenderer.entityAxisMap));

        CHANNEL.send(PacketDistributor.ALL.noArg(), new DeadListSyncPacket(EventUtil.deadList));
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level instanceof ServerLevel serverLevel) {
            List<UUID> toRemove = new ArrayList<>();

            // 收集需要移除的实体
            EventUtil.deadList.removeIf(uuid -> {
                LevelEntityGetter<Entity> entityGetter = serverLevel.entityManager.entityGetter;
                if (Iterables.size(entityGetter.getAll()) == 0) {
                    return false;
                }
                Entity entity = entityGetter.get(uuid);
                boolean shouldRemove = entity == null;
                if (entity == null) {
                    toRemove.add(uuid);
                }
                return shouldRemove;
            });

            // 批量发送
            if (!toRemove.isEmpty()) {
                CHANNEL.send(PacketDistributor.ALL.noArg(), DeadListSyncPacket.removeAll(toRemove));
            }
        }
    }

    @SubscribeEvent
    public static void onTooltipColor(RenderTooltipEvent.Color event) {
        Item item = event.getItemStack().getItem();

        if (item instanceof CelestialSourceSpellArmor ||
                item instanceof CelestialSourceStaff ||
                item instanceof CelestialSourceSpellBook ||
                item instanceof ModItems.CelestialSourceIngotItem ||
                item instanceof ModItems.CelestialSourcePearlItem) {
            // 每 150 ticks
            long gameTime = Minecraft.instance.level.levelData.getGameTime();
            float hue = (gameTime % 150) / 150.0f; // 色相：0.0 → 1.0

            // 边框起始颜色
            int rgbStart = hsbToRGB(hue, 0.8f, 1.0f);
            event.setBorderStart(0xFF000000 | rgbStart);

            // 边框结束颜色
            int rgbEnd = hsbToRGB((hue + 0.3f) % 1.0f, 0.8f, 1.0f);
            event.setBorderEnd(0xFF000000 | rgbEnd);

            event.setBackgroundStart(0xF0000000); // 半透明黑背景
            event.setBackgroundEnd(0xF0000000);
        }
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