package miku.united_as_one.genesis.client.renderer.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import miku.united_as_one.genesis.common.entity.projectile.ThrownIron;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

public class ThrownIronRenderer extends EntityRenderer<ThrownIron> {
    public ThrownIronRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ThrownIron entity, float entityYaw, float partialTicks, PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        poseStack.scale(1.25f, 1.25f, 1.25f);

        Minecraft.getInstance().getItemRenderer().renderStatic(
            new ItemStack(Items.IRON_INGOT),
            ItemDisplayContext.GROUND,
            packedLight,
            OverlayTexture.NO_OVERLAY,
            poseStack,
            buffer,
            entity.level(),
            0
        );

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ThrownIron entity) {
        return null;
    }
}