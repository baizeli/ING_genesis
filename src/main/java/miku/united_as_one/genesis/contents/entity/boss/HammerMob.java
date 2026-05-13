package miku.united_as_one.genesis.contents.entity.boss;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.level.Level;

public class HammerMob extends Monster {
    public final AnimationState awaken = new AnimationState();
    public final AnimationState idle = new AnimationState();
    public final AnimationState sprinting = new AnimationState();

    public HammerMob(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 100.0D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (!this.awaken.isStarted()) {
                this.awaken.start(this.tickCount);
            } else if (this.isSprinting() && !this.sprinting.isStarted()) {
                this.sprinting.startIfStopped(this.tickCount);
                this.idle.stop();
            } else if (!this.isSprinting() && this.getDeltaMovement().horizontalDistanceSqr() < 0.01 && !this.idle.isStarted() && !this.awaken.isStarted()) {
                this.idle.startIfStopped(this.tickCount);
                this.sprinting.stop();
            } else if (this.getDeltaMovement().horizontalDistanceSqr() >= 0.01 && !this.isSprinting()) {
                this.idle.stop();
                this.sprinting.stop();
            }
        }
    }
}