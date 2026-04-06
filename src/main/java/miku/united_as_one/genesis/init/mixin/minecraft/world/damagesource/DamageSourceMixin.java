package miku.united_as_one.genesis.init.mixin.minecraft.world.damagesource;

import miku.united_as_one.genesis.util.mixinutil.DamageSourceInterface;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements DamageSourceInterface {
    @Unique
    private boolean revelationfix$bypassArmor = false;
    @Unique
    private boolean revelationfix$bypassAll = false;

    @Override
    public void revelationfix$setBypassArmor(boolean z) {
        this.revelationfix$bypassArmor = z;
    }

    @Override
    public boolean revelationfix$bypassArmor() {
        return this.revelationfix$bypassArmor;
    }

    @Override
    public void revelationfix$setBypassAll(boolean z) {
        revelationfix$bypassAll = z;
    }

    @Override
    public boolean revelationfix$isBypassAll() {
        return revelationfix$bypassAll;
    }

    @Inject(method = "is(Lnet/minecraft/tags/TagKey;)Z", at = @At("HEAD"), cancellable = true)
    private void is(TagKey<DamageType> tagKey, CallbackInfoReturnable<Boolean> cir) {
        if (tagKey == DamageTypeTags.BYPASSES_ARMOR)
            if (this.revelationfix$bypassArmor()) cir.setReturnValue(true);
        if (revelationfix$isBypassAll()) {
            if (tagKey == DamageTypeTags.BYPASSES_ARMOR || tagKey == DamageTypeTags.BYPASSES_SHIELD || tagKey == DamageTypeTags.BYPASSES_INVULNERABILITY || tagKey == DamageTypeTags.BYPASSES_COOLDOWN || tagKey == DamageTypeTags.BYPASSES_RESISTANCE || tagKey == DamageTypeTags.BYPASSES_EFFECTS)
                cir.setReturnValue(true);
        }
    }

}