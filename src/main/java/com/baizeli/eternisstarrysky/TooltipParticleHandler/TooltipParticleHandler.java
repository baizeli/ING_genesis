package com.baizeli.eternisstarrysky.TooltipParticleHandler;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipParticleHandler
{
    private static int tickCounter = 0;
    private static ItemStack lastTooltipItem = ItemStack.EMPTY;
    private static int tooltipX = 0, tooltipY = 0, tooltipWidth = 0, tooltipHeight = 0;
    private static long lastFrameTime = System.nanoTime();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END) tickCounter++;
    }

    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.Pre event)
    {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof ITooltipParticleItem particleItem)
        {
            tooltipX = 0;
            tooltipY = 0;
            tooltipWidth = event.getScreenWidth();
            tooltipHeight = event.getScreenHeight();
            lastTooltipItem = stack;

            if (particleItem.shouldSpawnParticles(stack) && tickCounter % particleItem.getParticleSpawnRate() == 0) TooltipParticleSystem.getInstance().spawnParticlesInTooltip(tooltipX, tooltipY, tooltipWidth, tooltipHeight, particleItem.getParticleConfig());
        }
    }

    @SubscribeEvent
    public static void onRenderTooltipPost(RenderTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof ITooltipParticleItem)
        {
            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f;
            lastFrameTime = currentTime;
            deltaTime = Math.min(deltaTime, 0.05f);

            // 更新粒子
            TooltipParticleSystem.getInstance().update(deltaTime);
            TooltipParticleSystem.getInstance().render(event.getGraphics(), 1.0f);
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent.Post event)
    {
        if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type())
        {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null)
            {
                long currentTime = System.nanoTime();
                float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f;
                lastFrameTime = currentTime;
                deltaTime = Math.min(deltaTime, 0.05f);

                // 依旧更新
                TooltipParticleSystem.getInstance().update(deltaTime);
                TooltipParticleSystem.getInstance().render(event.getGuiGraphics(), 1.0f);
            }
        }
    }
}