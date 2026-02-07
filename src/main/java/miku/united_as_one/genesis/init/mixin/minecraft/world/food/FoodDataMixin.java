package miku.united_as_one.genesis.init.mixin.minecraft.world.food;

import miku.united_as_one.genesis.init.registry.EffectRegistry;
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
        if (player.hasEffect(EffectRegistry.UNPARALLELED.get())) {
            tickTimer += 1;
        }
    }
}