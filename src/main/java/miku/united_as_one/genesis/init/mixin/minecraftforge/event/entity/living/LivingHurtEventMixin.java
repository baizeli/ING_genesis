package miku.united_as_one.genesis.init.mixin.minecraftforge.event.entity.living;

import miku.united_as_one.genesis.util.mixinutil.LivingEventEC;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingDamageEvent.class, remap = false)
public abstract class LivingHurtEventMixin implements LivingEventEC {
    @Shadow
    private float amount;
    @Unique
    private boolean revelationfix$onlyAmountUp;

    public boolean revelationfix$isHackedOnlyAmountUp() {
        return revelationfix$onlyAmountUp;
    }

    public void revelationfix$hackedOnlyAmountUp(boolean target) {
        revelationfix$onlyAmountUp = target;
    }

    @Inject(method = "setAmount", at = @At("HEAD"), cancellable = true)
    private void setAmount(float amount, CallbackInfo ci) {
        if (revelationfix$onlyAmountUp) {
            ci.cancel();
            this.amount = Math.max(this.amount, amount);
        }
    }
}
