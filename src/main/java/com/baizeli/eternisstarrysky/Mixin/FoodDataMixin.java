package com.baizeli.eternisstarrysky.mixin;

import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {
    
    @Shadow
    private int tickTimer;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTickHead(Player player, CallbackInfo ci) {
        if (player.hasEffect(ModEffect.UNPARALLELED.get())) {
            tickTimer += 1;
        }
    }
}