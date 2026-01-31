package miku.united_as_one.genesis.client;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.renderer.*;
import miku.united_as_one.genesis.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = Genesis.MODID)
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

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
class ClientModEvents {
    @SubscribeEvent
    public static void registerBowAnimationProperties(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Stream.of(
                ItemRegistry.THUNDER_LONGBOW.get(),
                ItemRegistry.FROST_LONGBOW.get(),
                ItemRegistry.WITCHCRAFT_BOW.get(),
                ItemRegistry.FLAME_BOW.get()
            ).forEach(bow -> {
                ItemProperties.register(bow, new ResourceLocation("pull"), (stack, level, livingEntity, i) -> {
                    if (livingEntity == null) {
                        return 0;
                    } else {
                        return livingEntity.getUseItem() != stack ? 0 : (float)
                        (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20;
                    }
                });

                ItemProperties.register(bow, new ResourceLocation("pulling"),
                    (stack, level, livingEntity, i) -> livingEntity != null &&
                    livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1 : 0
                );
            });
        });
    }
}