package miku.united_as_one.genesis.client;

import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.client.renderer.AfterImageManager;
import miku.united_as_one.genesis.client.renderer.AfterImageRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID)
public class ClientEvent {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            AfterImageManager.tick();
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.player != null && mc.player.getPersistentData().getBoolean("isUnparalleledActive")) {
                PoseStack poseStack = event.getPoseStack();
                Camera camera = event.getCamera();
                float partialTick = event.getPartialTick();
                MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
//                RenderSystem.enableBlend();
//                RenderSystem.defaultBlendFunc();
//                RenderSystem.enableDepthTest();
//                RenderSystem.depthMask(false);
                AfterImageRenderer.renderAfterImages(poseStack, bufferSource, camera, partialTick);
                bufferSource.endBatch();
//                RenderSystem.depthMask(true);
//                RenderSystem.disableBlend();
            }
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            AfterImageManager.clear();
        }
    }
}