package miku.united_as_one.genesis.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import miku.united_as_one.genesis.common.items.sword.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class RightSwordAnimation {
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        ItemStack item = event.getItemStack();
        LocalPlayer player = mc.player;
        if (player == null) return;

        if (item.getItem() instanceof DivineMetalSword || item.getItem() instanceof MithrilSword) {
            HumanoidArm humanoidarm = event.getHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            boolean flag = humanoidarm == HumanoidArm.RIGHT;
            PoseStack poseStack = event.getPoseStack();
            int side = flag ? 1 : -1;
            
            if (!player.isUsingItem()) return;
            poseStack.pushPose();

            if (item.getItem() instanceof MithrilSword) poseStack.translate(side * 0.65, -0.45, -0.7);
            if (item.getItem() instanceof DivineMetalSword) poseStack.translate(side * 0.7, -0.5, -0.8);
            poseStack.mulPose(Axis.XP.rotationDegrees(-100));
            poseStack.mulPose(Axis.YP.rotationDegrees(side * -120));
            poseStack.mulPose(Axis.ZP.rotationDegrees(side * -80));

            mc.getEntityRenderDispatcher().getItemInHandRenderer().renderItem(
                player, item, flag ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                !flag, poseStack, event.getMultiBufferSource(), event.getPackedLight()
            );

            poseStack.popPose();
            event.setCanceled(true);
        }
    }
}