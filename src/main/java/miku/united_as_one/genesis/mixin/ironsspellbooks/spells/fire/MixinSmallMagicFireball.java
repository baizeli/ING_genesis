package miku.united_as_one.genesis.mixin.ironsspellbooks.spells.fire;

import miku.united_as_one.genesis.contents.items.curios.rune_plus.FireRunePlus;
import miku.united_as_one.genesis.api.mixin.IMixinSmallMagicFireball;
import miku.united_as_one.genesis.api.curios.ModCurios;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.fireball.SmallMagicFireball;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = SmallMagicFireball.class, remap = false)
public abstract class MixinSmallMagicFireball extends AbstractMagicProjectile implements IMixinSmallMagicFireball {

    @Unique
    private boolean eternisStarrySky$needReborn = true;

    public MixinSmallMagicFireball(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Unique
    public void eternisStarrySky$setNotNeedReborn() {
        eternisStarrySky$needReborn = false;
    }

    @Override
    public void tick() {
        if(!this.level.isClientSide && this.tickCount == 40 && eternisStarrySky$needReborn) {
            Entity owner = getOwner();
            if(owner instanceof LivingEntity entity) {
                if(ModCurios.hasCurios(entity, FireRunePlus::test)) {
                    Vec3 origin = entity.getEyePosition().add(entity.getForward().normalize().scale(0.2F)).subtract(0.0F, 0.15, 0.0F);
                    SmallMagicFireball fireball = new SmallMagicFireball(level, entity);
                    fireball.setPos(origin.subtract(0.0F, fireball.getBbHeight(), 0.0F));
                    float inaccuracy = 0.4F;
                    Vec3 vec = entity.getForward().add(0.0F, 0.2, 0.0F).normalize();
                    fireball.shoot(vec.scale(0.5F), inaccuracy);
                    fireball.setDamage(getDamage() * 1.8f);
                    fireball.setCursorHoming(true);
                    ((IMixinSmallMagicFireball)fireball).eternisStarrySky$setNotNeedReborn();
                    level.addFreshEntity(fireball);
                    this.discard();
                }
            }
        }
        super.tick();
    }
}
