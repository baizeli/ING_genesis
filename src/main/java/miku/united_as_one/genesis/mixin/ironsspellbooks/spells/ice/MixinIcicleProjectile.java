package miku.united_as_one.genesis.mixin.ironsspellbooks.spells.ice;

import miku.united_as_one.genesis.contents.items.curios.rune_plus.IceRunePlus;
import miku.united_as_one.genesis.api.curios.ModCurios;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = IcicleProjectile.class, remap = false)
public abstract class MixinIcicleProjectile extends AbstractMagicProjectile {
    public MixinIcicleProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(
            method = "getSpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void getSpeed(CallbackInfoReturnable<Float> cir) {
        Entity owner = getOwner();
        if(owner instanceof LivingEntity entity) {
            if(ModCurios.hasCurios(entity, IceRunePlus::test)) {
                cir.setReturnValue(cir.getReturnValue() * 4);
            }
        }
    }
}
