package miku.united_as_one.genesis.init.mixin.ironsspellbooks.spells.nature;

import miku.united_as_one.genesis.common.items.curios.rune_plus.NatureRunePlus;
import miku.united_as_one.genesis.util.ModCurios;
import io.redspace.ironsspellbooks.spells.nature.AcidOrbSpell;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AcidOrbSpell.class, remap = false)
public class MixinAcidOrbSpell {

    @Inject(
            method = "getRadius",
            at = @At("RETURN"),
            cancellable = true
    )
    private void getRadius(int spellLevel, LivingEntity caster, CallbackInfoReturnable<Float> cir) {
        if(ModCurios.hasCurios(caster, NatureRunePlus::test)) {
            cir.setReturnValue(cir.getReturnValue() * 1.5f);
        }
    }
}
