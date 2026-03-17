package miku.united_as_one.genesis.common.entity.spell.fire;

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
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SummonedKeeperEntity extends KeeperEntity implements IMagicSummon {
    public SummonedKeeperEntity(EntityType<? extends KeeperEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.ATTACK_DAMAGE, 10)
            .add(Attributes.MAX_HEALTH, 60)
            .add(Attributes.FOLLOW_RANGE, 25)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
            .add(Attributes.ATTACK_KNOCKBACK, 2)
            .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1)
            .add(ForgeMod.ENTITY_REACH.get(), 3.5)
            .add(Attributes.MOVEMENT_SPEED, .19);
    }
    
    public SummonedKeeperEntity(Level level) {
        this(EntityRegistry.SUMMONED_KEEPER.get(), level);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner == null) return;
        SummonManager.setOwner(this, owner);
    }

    @Override
    public void onUnSummon() {
        if (this.level().isClientSide) return;
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public void die(@NotNull DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void onRemovedFromWorld() {
        this.onRemovedHelper(this);
        super.onRemovedFromWorld();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() == getSummoner()) return false;
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity instanceof IMagicSummon summon)
            return summon.getSummoner() != null && getSummoner() != null && getSummoner() == summon.getSummoner();
        if (getSummoner() != null && pEntity.isAlliedTo(getSummoner()))
            return true;
        if (pEntity instanceof Mob mob && (mob.getType().getCategory().isFriendly() || !mob.isAggressive()))
            return true;
        return false;
    }

    @Override
    protected void registerGoals() {
        // 傻逼子类继承fwllllllll
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