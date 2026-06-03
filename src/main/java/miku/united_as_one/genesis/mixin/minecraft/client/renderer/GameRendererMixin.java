package miku.united_as_one.genesis.mixin.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import miku.united_as_one.genesis.client.TrailRender;
import miku.united_as_one.genesis.client.render.luminous.GenesisEffect;
import miku.united_as_one.genesis.client.render.luminous.GenesisOutlineRenderer;
import miku.united_as_one.genesis.client.render.luminous.GenesisRegistry;
import miku.united_as_one.genesis.contents.items.armor.VioletZenithArmor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static miku.united_as_one.genesis.client.TrailRender.renderTrail;
import static miku.bai_ze_li.genesis.api.render.post.DistortWorldRender.*;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final Minecraft minecraft;
    @Shadow @Final public ItemInHandRenderer itemInHandRenderer;
    @Shadow @Final private RenderBuffers renderBuffers;
    @Shadow @Final private LightTexture lightTexture;

    @Unique
    private Matrix4f genesis$handOutlinePose;

    @Unique
    private Matrix3f genesis$handOutlineNormal;

    @Inject(
            method = "renderItemInHand",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LightTexture;turnOnLightLayer()V",
                    shift = At.Shift.BEFORE
            )
    )
    private void genesis$rememberHandOutlinePose(PoseStack poseStack, Camera activeRenderInfo, float partialTicks,
                                                 CallbackInfo ci) {
        if (GenesisOutlineRenderer.isHandMaskCaptureActive()) {
            return;
        }
        genesis$handOutlinePose = new Matrix4f(poseStack.last().pose());
        genesis$handOutlineNormal = new Matrix3f(poseStack.last().normal());
    }

    @Inject(
            method = "renderItemInHand",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LightTexture;turnOffLightLayer()V",
                    shift = At.Shift.AFTER
            )
    )
    private void genesis$captureHandOutlineMasks(PoseStack poseStack, Camera activeRenderInfo, float partialTicks,
                                                CallbackInfo ci) {
        Matrix4f originalPose = genesis$handOutlinePose;
        Matrix3f originalNormal = genesis$handOutlineNormal;
        genesis$handOutlinePose = null;
        genesis$handOutlineNormal = null;

        if (originalPose == null || originalNormal == null) {
            return;
        }

        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }

        MultiBufferSource.BufferSource bufferSource = renderBuffers.bufferSource();
        int packedLight = minecraft.getEntityRenderDispatcher().getPackedLightCoords(player, partialTicks);
        if (TrailRender.shouldDeferWorldEffects()) {
            GenesisOutlineRenderer.queueDeferredHandOutline(
                    originalPose,
                    originalNormal,
                    new Matrix4f(RenderSystem.getProjectionMatrix()),
                    partialTicks,
                    packedLight
            );
            return;
        }

        genesis$captureHandMask(partialTicks, poseStack, bufferSource, player, packedLight,
                originalPose, originalNormal, InteractionHand.MAIN_HAND);
        genesis$captureHandMask(partialTicks, poseStack, bufferSource, player, packedLight,
                originalPose, originalNormal, InteractionHand.OFF_HAND);
    }

    @Unique
    private void genesis$captureHandMask(float partialTicks, PoseStack poseStack,
                                         MultiBufferSource.BufferSource bufferSource, LocalPlayer player,
                                         int packedLight, Matrix4f originalPose,
                                         Matrix3f originalNormal, InteractionHand hand) {
        ItemStack stack = hand == InteractionHand.MAIN_HAND ? player.getMainHandItem() : player.getOffhandItem();
        GenesisEffect effect = stack.isEmpty() ? null : GenesisRegistry.getTargetEffect(stack.getItem());
        if (effect == null || !GenesisOutlineRenderer.beginHandMaskCapture(effect, hand)) {
            return;
        }

        lightTexture.turnOnLightLayer();
        try {
            poseStack.last().pose().set(originalPose);
            poseStack.last().normal().set(originalNormal);
            itemInHandRenderer.renderHandsWithItems(partialTicks, poseStack, bufferSource, player, packedLight);
            bufferSource.endBatch();
        } finally {
            GenesisOutlineRenderer.stopHandMaskCapture();
            lightTexture.turnOffLightLayer();
        }
    }

    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionScale(LivingEntity livingEntity, float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (livingEntity instanceof Player player) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

            if (helmet.getItem() instanceof VioletZenithArmor) {
                MobEffectInstance effect = player.getEffect(MobEffects.NIGHT_VISION);

                if (effect != null) {
                    cir.setReturnValue(1f);
                }
            }
        }
    }

    @Inject(
        method = "render", 
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V",
            shift = At.Shift.AFTER
        )
    )
    private void afterIrisRender(float partialTicks, long finishTimeNano, boolean renderLevel, CallbackInfo ci) {

        renderTrail(partialTicks,finishTimeNano,renderLevel);
        GenesisOutlineRenderer.renderDeferredOutlines(partialTicks, itemInHandRenderer, lightTexture);
    }



    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V",
                    shift = At.Shift.AFTER
            )
    )
    private void afterVanillaPostProcessing(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        processMyPostChain(partialTicks);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void genesis$flushRemainingGuiOutline(float partialTicks, long finishTimeNano, boolean renderLevel, CallbackInfo ci) {
        GenesisOutlineRenderer.flushGuiPass();
    }

}
