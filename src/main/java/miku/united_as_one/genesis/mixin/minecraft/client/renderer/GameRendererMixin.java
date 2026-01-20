package miku.united_as_one.genesis.mixin.minecraft.client.renderer;

import miku.united_as_one.genesis.items.armor.VioletZenithArmor;
import net.minecraft.client.renderer.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static miku.united_as_one.genesis.client.TrailRender.renderTrail;
import static miku.united_as_one.genesis.client.renderer.DistortWorldRender.*;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

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


}