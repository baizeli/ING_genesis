package com.baizeli.eternisstarrysky.Mixin.ironsspellbooks.spells.nature;

import com.baizeli.eternisstarrysky.Items.curios.rune_plus.NatureRunePlus;
import com.baizeli.eternisstarrysky.util.ModCurios;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonCloud;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonSplash;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = PoisonSplash.class, remap = false)
public class MixinPoisonSplash {
    @Inject(
            method = "createPoisonCloud",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void createPoisonCloud(CallbackInfo ci, PoisonCloud poisonCloud) {
        PoisonSplash splash = PoisonSplash.class.cast(this);
        if(splash.getOwner() instanceof LivingEntity entity) {
            if(ModCurios.hasCurios(entity, NatureRunePlus::test)) {
                poisonCloud.setRadius(poisonCloud.getRadius() * 2);
                poisonCloud.setDamage(poisonCloud.getDamage() * 2);
            }
        }
    }
}
