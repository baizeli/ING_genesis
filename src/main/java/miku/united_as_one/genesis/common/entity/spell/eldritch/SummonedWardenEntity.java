package miku.united_as_one.genesis.common.entity.spell.eldritch;

import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.*;

import java.util.*;

public class SummonedWardenEntity extends Warden implements IMagicSummon, OwnableEntity {
    private int despawnDelay = -1;

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

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (player == getSummoner() && !this.level().isClientSide) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    public void onUnSummon() {
        if (this.level().isClientSide) return;
        this.setPose(Pose.DIGGING);
        this.playSound(SoundEvents.WARDEN_DIG, 5, 1);
        this.despawnDelay = 100;
    }

    protected void customServerAiStep() {
        if (this.despawnDelay >= 0) {
            if (--this.despawnDelay <= 0) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }
            this.getNavigation().stop();
            return;
        }

        if (this.hasPose(Pose.EMERGING)) this.getNavigation().stop();

        Optional.ofNullable(getSummoner())
            .filter(s -> s instanceof LivingEntity)
            .filter(s -> !this.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
            .map(s -> (LivingEntity) s)
            .ifPresent(livingSummoner -> {
                LivingEntity t = livingSummoner.getLastHurtByMob();
                if (t != null && !this.isAlliedTo(t) && this.canTargetEntity(t)) {
                    this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, t);
                    this.increaseAngerAt(t, 150, false);
                    return;
                }
                LivingEntity t2 = livingSummoner.getLastHurtMob();
                if (t2 != null && !this.isAlliedTo(t2) && this.canTargetEntity(t2)) {
                    this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, t2);
                    this.increaseAngerAt(t2, 150, false);
                }
            });
        super.customServerAiStep();
    }

    public void die(@NotNull DamageSource pDamageSource) {
        super.die(pDamageSource);
    }

    public void onRemovedFromWorld() {
        this.onRemovedHelper(this);
        super.onRemovedFromWorld();
    }

    public boolean canTargetEntity(@Nullable Entity pEntity) {
        if (pEntity instanceof LivingEntity livingentity) {
            return this.level() == pEntity.level() &&
                    EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(pEntity) &&
                    !this.isAlliedTo(pEntity) &&
                    livingentity.getType() != EntityType.ARMOR_STAND &&
                    !livingentity.isInvulnerable() &&
                    !livingentity.isDeadOrDying() &&
                    this.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
        }
        return false;
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
        if (pEntity.getType() == EntityType.WARDEN) return false;
        if (pEntity.getType() == EntityType.SLIME || pEntity.getType() == EntityType.MAGMA_CUBE) return false;
        if (pEntity.getType().is(Tags.EntityTypes.BOSSES)) return false;
        if (pEntity instanceof IMagicSummon summon)
            return summon.getSummoner() != null && getSummoner() != null && getSummoner() == summon.getSummoner();
        if (getSummoner() != null && pEntity.isAlliedTo(getSummoner())) 
            return true;
        if (pEntity instanceof Mob mob && (mob.getType().getCategory().isFriendly() || !mob.isAggressive()))
            return true;
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 15, 5, false, 25));
    }
}