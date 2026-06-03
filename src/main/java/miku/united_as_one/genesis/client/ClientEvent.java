package miku.united_as_one.genesis.client;

import miku.bai_ze_li.genesis.api.nbt.GenesisPersistentData;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.renderer.AfterImageManager;
import miku.united_as_one.genesis.client.renderer.AfterImageRenderer;
import miku.united_as_one.genesis.contents.effect.spell.celestial_source.UnparalleledEffect;
import miku.bai_ze_li.genesis.api.render.effect.SlashEffectManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Genesis.MODID, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            AfterImageManager.tick();
            SlashEffectManager.tick(); // 刀光
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        float partialTick = event.getPartialTick();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        // 残影
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            if (GenesisPersistentData.getBoolean(mc.player, UnparalleledEffect.ACTIVE)) {
                AfterImageRenderer.renderAfterImages(poseStack, bufferSource, camera, partialTick);
            }
        }

        // 刀光
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            if (!TrailRender.shouldDeferWorldEffects()) {
                SlashEffectManager.render(poseStack, bufferSource, partialTick);
            }
        }

        bufferSource.endBatch();
    }

    @SubscribeEvent
    public static void onWorldUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            AfterImageManager.clear();
            SlashEffectManager.clear();
        }
    }
}
