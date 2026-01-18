package com.baizeli.eternisstarrysky.Mixin.ironsspellbooks.spells.ice;

import com.baizeli.eternisstarrysky.Items.curios.rune_plus.IceRunePlus;
import com.baizeli.eternisstarrysky.util.ModCurios;
import io.redspace.ironsspellbooks.spells.ice.IcicleSpell;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = IcicleSpell.class, remap = false)
public class MixinIcicleSpell {
    @Inject(
            method = "getDamage",
            at = @At("RETURN"),
            cancellable = true
    )
    private void getDamage(int spellLevel, LivingEntity entity, CallbackInfoReturnable<Float> cir) {
        if(ModCurios.hasCurios(entity, IceRunePlus::test)) {
            cir.setReturnValue(cir.getReturnValue() + entity.getArmorValue() * 0.25f);
        }
    }
}
