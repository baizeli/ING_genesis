package com.baizeli.eternisstarrysky.Mixin.client;

import com.baizeli.eternisstarrysky.Items.armor.VioletZenithArmor;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionScale(LivingEntity livingEntity, float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (livingEntity instanceof Player player) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

            if (helmet.getItem() instanceof VioletZenithArmor) {
                MobEffectInstance effect = player.getEffect(MobEffects.NIGHT_VISION);

                if (effect != null) {
                    cir.setReturnValue(1f);
                    /*cir.cancel();*/
                }
            }
        }
    }
}