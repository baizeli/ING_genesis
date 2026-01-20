package miku.united_as_one.genesis.client.renderer;

import miku.united_as_one.genesis.EternisStarrySky;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EvasionAnimationRenderer {
    private static final Map<Integer, EvasionAnimationState> evasionStates = new HashMap<>();
    private static final Map<Integer, Boolean> pushedPoses = new HashMap<>();
    private static float distortionTime = 0.0f;

    private static class EvasionAnimationState {
        public int duration = 0;
        public boolean isActive = false;
        
        public void update() {
            if (isActive) {
                duration--;
                if (duration <= 0) {
                    isActive = false;
                }
            }
        }
        
        public void trigger() {
            isActive = true;
            duration = 15;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        evasionStates.values().forEach(EvasionAnimationState::update);
    }

    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity entity = event.getEntity();
        int entityId = entity.getId();
        EvasionAnimationState state = evasionStates.get(entityId);
        
        if (state != null && state.isActive) {
            PoseStack poseStack = event.getPoseStack();

            poseStack.pushPose();

            pushedPoses.put(entityId, true);

            distortionTime += 0.4f;

            float intensity = (float) state.duration / 15.0f;

            float distortX = (float) Math.sin(distortionTime * 6.0f) * 0.08f * intensity;
            float distortY = (float) Math.cos(distortionTime * 5.0f) * 0.06f * intensity;
            float distortZ = (float) Math.sin(distortionTime * 7.0f) * 0.08f * intensity;

            poseStack.translate(distortX, distortY, distortZ);

            float rotateX = (float) Math.sin(distortionTime * 3.0f) * 8.0f * intensity;
            float rotateY = (float) Math.cos(distortionTime * 2.5f) * 6.0f * intensity;
            float rotateZ = (float) Math.sin(distortionTime * 4.0f) * 5.0f * intensity;

            poseStack.mulPose(Axis.XP.rotationDegrees(rotateX));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotateY));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotateZ));

            float scaleX = 1.0f + (float) Math.sin(distortionTime * 8.0f) * 0.08f * intensity;
            float scaleY = 1.0f + (float) Math.cos(distortionTime * 7.0f) * 0.06f * intensity;
            float scaleZ = 1.0f + (float) Math.sin(distortionTime * 9.0f) * 0.08f * intensity;
            
            poseStack.scale(scaleX, scaleY, scaleZ);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            float baseAlpha = 0.7f;
            float attackAlpha = 0.4f * intensity;
            float finalAlpha = Math.max(baseAlpha - attackAlpha, 0.1f);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, finalAlpha);
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        int entityId = entity.getId();

        Boolean hasPushed = pushedPoses.get(entityId);

        if (hasPushed != null && hasPushed) {
            PoseStack poseStack = event.getPoseStack();
            
            poseStack.popPose();
            
            pushedPoses.remove(entityId);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            RenderSystem.disableBlend();
        }

        EvasionAnimationState state = evasionStates.get(entityId);
        if (state != null && !state.isActive) {
            evasionStates.remove(entityId);
        }
    }

    public static void triggerEvasionAnimation(LivingEntity entity) {
        int entityId = entity.getId();

        EvasionAnimationState state = evasionStates.computeIfAbsent(entityId, k -> new EvasionAnimationState());

        state.trigger();
    }
}