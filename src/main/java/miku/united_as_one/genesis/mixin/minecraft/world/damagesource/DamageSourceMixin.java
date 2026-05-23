package miku.united_as_one.genesis.mixin.minecraft.world.damagesource;

import miku.united_as_one.genesis.api.mixin.DamageSourceInterface;
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
    //源代码来自revelationfix，原作者mega32k
    @Unique
    private boolean revelationfix$bypassArmor = false;
    @Unique
    private boolean revelationfix$bypassAll = false;

    @Override
    public void ironSpellGenesis$setBypassArmor(boolean z) {
        this.revelationfix$bypassArmor = z;
    }

    @Override
    public boolean ironSpellGenesis$bypassArmor() {
        return this.revelationfix$bypassArmor;
    }

    @Override
    public void ironSpellGenesis$setBypassAll(boolean z) {
        revelationfix$bypassAll = z;
    }

    @Override
    public boolean ironSpellGenesis$isBypassAll() {
        return revelationfix$bypassAll;
    }

    @Inject(method = "is(Lnet/minecraft/tags/TagKey;)Z", at = @At("HEAD"), cancellable = true)
    private void is(TagKey<DamageType> tagKey, CallbackInfoReturnable<Boolean> cir) {
        if (tagKey == DamageTypeTags.BYPASSES_ARMOR)
            if (this.ironSpellGenesis$bypassArmor()) cir.setReturnValue(true);
        if (ironSpellGenesis$isBypassAll()) {
            if (tagKey == DamageTypeTags.BYPASSES_ARMOR || tagKey == DamageTypeTags.BYPASSES_SHIELD || tagKey == DamageTypeTags.BYPASSES_INVULNERABILITY || tagKey == DamageTypeTags.BYPASSES_COOLDOWN || tagKey == DamageTypeTags.BYPASSES_RESISTANCE || tagKey == DamageTypeTags.BYPASSES_EFFECTS)
                cir.setReturnValue(true);
        }
    }

}