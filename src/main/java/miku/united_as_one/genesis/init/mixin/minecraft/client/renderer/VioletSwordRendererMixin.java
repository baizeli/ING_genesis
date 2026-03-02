package miku.united_as_one.genesis.init.mixin.minecraft.client.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import miku.united_as_one.genesis.common.items.tool.VioletGalaxyingotTool;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class VioletSwordRendererMixin {

    @Inject(
            method = "renderArmWithItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V", shift = At.Shift.AFTER)
    )
    private void violet$applyFirstPersonBlocking(
            AbstractClientPlayer player,
            float partialTicks,
            float pitch,
            InteractionHand hand,
            float swingProgress,
            ItemStack stack,
            float equipProgress,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int combinedLight,
            CallbackInfo ci
    ) {
        if (player.isUsingItem() && player.getUseItem() == stack && stack.getItem() instanceof VioletGalaxyingotTool.Sword) {
            if (stack.getUseAnimation() == UseAnim.BLOCK) {
                boolean isMainHand = hand == InteractionHand.MAIN_HAND;
                float sideSign = isMainHand ? 1.0F : -1.0F;

                // 1. 微调位移：
                // X: sideSign * -0.15F (稍微向中心靠拢，但保持手柄在右下角/左下角)
                // Y: 0.15F (略微抬高，防止被挡住)
                // Z: -0.05F (稍微拉远一点点)
                poseStack.translate(sideSign * -0.15F, 0.15F, -0.05F);

                // 2. 核心旋转逻辑：

                // 绕 X 轴：-105.0F。让剑尖向下压低，使整把剑显得更“横”。
                poseStack.mulPose(Axis.XP.rotationDegrees(-105.0F));

                // 绕 Y 轴：sideSign * 55.0F。关键旋转！
                // 这会将剑尖从身体外侧大幅度旋转到屏幕中心。
                poseStack.mulPose(Axis.YP.rotationDegrees(sideSign * 20.0F));

                // 绕 Z 轴：sideSign * 60.0F。
                // 配合 Y 轴旋转，确保剑刃的平面（宽面）是斜对着玩家的，让你看清材质。
                poseStack.mulPose(Axis.ZP.rotationDegrees(sideSign * 90.0F));
            }
        }
    }
}