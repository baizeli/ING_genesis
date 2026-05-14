package miku.united_as_one.genesis.contents.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HammerMob extends Monster {
    private static final int AWAKEN_DELAY_TICKS = 30;

    private static final EntityDataAccessor<Boolean> AWAKEN_PLAYED = SynchedEntityData.defineId(HammerMob.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState awaken = new AnimationState();
    public final AnimationState idle = new AnimationState();
    public final AnimationState sprinting = new AnimationState();
    public final AnimationState death = new AnimationState();

    public HammerMob(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 100.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AWAKEN_PLAYED, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (!this.entityData.get(AWAKEN_PLAYED) && this.tickCount > AWAKEN_DELAY_TICKS) {
                this.entityData.set(AWAKEN_PLAYED, true);
            }
        }
        if (this.level().isClientSide) {
            if (!this.entityData.get(AWAKEN_PLAYED) && !this.awaken.isStarted()) {
                this.awaken.start(this.tickCount);
            } else if (this.getDeltaMovement().horizontalDistanceSqr() < 0.01 && !this.idle.isStarted() && !this.awaken.isStarted()) {
                this.idle.startIfStopped(this.tickCount);
            } else if (this.getDeltaMovement().horizontalDistanceSqr() >= 0.01) {
                this.idle.stop();
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("AwakenPlayed", this.entityData.get(AWAKEN_PLAYED));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(AWAKEN_PLAYED, compound.getBoolean("AwakenPlayed"));
    }
}