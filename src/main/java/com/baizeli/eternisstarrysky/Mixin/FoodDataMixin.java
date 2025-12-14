package com.baizeli.eternisstarrysky.Mixin;

import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
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