package com.baizeli.eternisstarrysky.Mixin.ironsspellbooks.spells.nature;

import com.baizeli.eternisstarrysky.Items.curios.rune_plus.NatureRunePlus;
import com.baizeli.eternisstarrysky.util.ModCurios;
import io.redspace.ironsspellbooks.entity.spells.poison_arrow.PoisonArrow;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = PoisonArrow.class, remap = false)
public class MixinPoisonArrow {
    @Inject(
            method = "createPoisonCloud",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void arrowEnhanced(Vec3 location, CallbackInfo ci, PoisonCloud poisonCloud) {
        PoisonArrow arrow = PoisonArrow.class.cast(this);
        if(arrow.getOwner() instanceof LivingEntity entity) {
            if(ModCurios.hasCurios(entity, NatureRunePlus::test)) {
                poisonCloud.setRadius(poisonCloud.getRadius() * 2);
            }
        }
    }
}
