package miku.united_as_one.genesis.mixin.minecraft.world.damagesource;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.api.mixin.DamageSourceInterface;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements DamageSourceInterface {
    //源代码来自revelationfix，原作者mega32k
    @Shadow
    public Holder<DamageType> type;

    @Unique
    private static final TagKey<DamageType> ironSpellGenesis$CHAOS_MAGIC = TagKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Genesis.MODID, "chaos_magic")
    );

    @Unique
    private static final TagKey<DamageType> ironSpellGenesis$CELESTIAL_SOURCE_MAGIC = TagKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Genesis.MODID, "celestial_source_magic")
    );

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
        if (this.type.is(ironSpellGenesis$CELESTIAL_SOURCE_MAGIC) && ironSpellGenesis$isBypassAllTag(tagKey)) {
            cir.setReturnValue(true);
            return;
        }
        if (this.type.is(ironSpellGenesis$CHAOS_MAGIC) && tagKey == DamageTypeTags.BYPASSES_ARMOR) {
            cir.setReturnValue(true);
            return;
        }
        if (tagKey == DamageTypeTags.BYPASSES_ARMOR)
            if (this.ironSpellGenesis$bypassArmor()) cir.setReturnValue(true);
        if (ironSpellGenesis$isBypassAll()) {
            if (tagKey == DamageTypeTags.BYPASSES_ARMOR || tagKey == DamageTypeTags.BYPASSES_SHIELD || tagKey == DamageTypeTags.BYPASSES_INVULNERABILITY || tagKey == DamageTypeTags.BYPASSES_COOLDOWN || tagKey == DamageTypeTags.BYPASSES_RESISTANCE || tagKey == DamageTypeTags.BYPASSES_EFFECTS)
                cir.setReturnValue(true);
        }
    }

    @Unique
    private static boolean ironSpellGenesis$isBypassAllTag(TagKey<DamageType> tagKey) {
        return tagKey == DamageTypeTags.BYPASSES_ARMOR
                || tagKey == DamageTypeTags.BYPASSES_SHIELD
                || tagKey == DamageTypeTags.BYPASSES_INVULNERABILITY
                || tagKey == DamageTypeTags.BYPASSES_COOLDOWN
                || tagKey == DamageTypeTags.BYPASSES_RESISTANCE
                || tagKey == DamageTypeTags.BYPASSES_EFFECTS;
    }

}
