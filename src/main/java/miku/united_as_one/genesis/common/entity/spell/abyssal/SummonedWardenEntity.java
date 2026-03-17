package miku.united_as_one.genesis.common.entity.spell.abyssal;

import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SummonedWardenEntity extends Warden implements IMagicSummon, OwnableEntity {
    public SummonedWardenEntity(EntityType<? extends Warden> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Warden.createAttributes();
    }
    
    public SummonedWardenEntity(Level level) {
        this(EntityRegistry.SUMMONED_WARDEN.get(), level);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner == null) return;
        SummonManager.setOwner(this, owner);
    }
    
    public void setIsSummoned() {}

    protected void customServerAiStep() {
        if (this.hasPose(Pose.EMERGING)) this.getNavigation().stop();
        this.getBrain().setMemoryWithExpiry(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, Long.MAX_VALUE);
        super.customServerAiStep();
    }

    public void onUnSummon() {
        if (this.level().isClientSide) return;
        this.remove(RemovalReason.DISCARDED);
    }

    public void die(@NotNull DamageSource pDamageSource) {
        super.die(pDamageSource);
    }

    public void onRemovedFromWorld() {
        this.onRemovedHelper(this);
        super.onRemovedFromWorld();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() == getSummoner()) return false;
        return super.hurt(pSource, pAmount);
    }

    public LivingEntity getOwner() {
        return (LivingEntity) getSummoner();
    }

    public UUID getOwnerUUID() {
        if (getSummoner() != null) return getSummoner().getUUID();
        return null;
    }

    public boolean isAlliedTo(@NotNull Entity pEntity) {
        if (pEntity == getSummoner()) return true;
        if (pEntity instanceof IMagicSummon summon)
            return summon.getSummoner() != null && getSummoner() != null && getSummoner() == summon.getSummoner();
        if (getSummoner() != null && pEntity.isAlliedTo(getSummoner())) 
            return true;
        if (pEntity instanceof Mob mob && (mob.getType().getCategory().isFriendly() || !mob.isAggressive()))
            return true;
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1d));
        this.goalSelector.addGoal(2, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 15, 5, false, 25));
    }
}