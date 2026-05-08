package miku.united_as_one.genesis.contents.entity.spell.fire;

import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericCopyOwnerTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericFollowOwnerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericProtectOwnerTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import miku.united_as_one.genesis.registries.EntityRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import javax.annotation.Nullable;

public class SummonedKeeperEntity extends KeeperEntity implements IMagicSummon, OwnableEntity {
    public SummonedKeeperEntity(EntityType<? extends KeeperEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return KeeperEntity.prepareAttributes();
    }
    
    public SummonedKeeperEntity(Level level) {
        this(EntityRegistry.SUMMONED_KEEPER.get(), level);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner == null) return;
        SummonManager.setOwner(this, owner);
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

    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity == getSummoner()) return true;
        if (pEntity.getType() == EntityType.SLIME || pEntity.getType() == EntityType.MAGMA_CUBE) return false;
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
        this.goalSelector.addGoal(1, new KeeperAnimatedWarlockAttackGoal(this, 1f, 10, 30));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1d));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 15, 5, false, 25));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }
}