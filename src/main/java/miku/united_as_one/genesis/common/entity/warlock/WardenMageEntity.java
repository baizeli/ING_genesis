package miku.united_as_one.genesis.common.entity.warlock;

import miku.united_as_one.genesis.client.animation.warlock.WardenMageAnimation;
import miku.united_as_one.genesis.util.entity.WarlockParticleUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;
import vzling.lib.animation.VzlingAnimation;
import vzling.lib.entity.BossEntity;
import vzling.lib.entity.ISkillEntity;
import vzling.lib.entity.ai.VzlingPathNavigateGround;
import vzling.lib.entity.skill.SkillManager;

import java.util.List;

public class WardenMageEntity extends BossEntity implements ISkillEntity {
    public static final String MOD_ID = "iron_spells_genesis";

    public final VzlingAnimation IDLE = new VzlingAnimation(this, anim("idle"));
    public final VzlingAnimation START = new VzlingAnimation(this, anim("start"));
    public final VzlingAnimation WALK = new VzlingAnimation(this, anim("walk"));

    private final SkillManager<WardenMageEntity> skillManager = new SkillManager<>(this, List.of());

    public WardenMageEntity(EntityType<? extends BossEntity> type, Level world) {
        super(type, world);
        if (FMLLoader.getDist().isClient()) {
            this.IDLE.setDefinition(WardenMageAnimation.IDLE);
            this.START.setDefinition(WardenMageAnimation.START);
            this.WALK.setDefinition(WardenMageAnimation.WALK);
        }
    }

    private static ResourceLocation anim(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "warden_warlock_" + name);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)//血量
                .add(Attributes.ATTACK_DAMAGE, 30.0D)//攻击力
                .add(Attributes.MOVEMENT_SPEED, 0.3D)//移速
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }
    //傻逼漂移LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new VzlingPathNavigateGround(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false) {
            @Override
            public boolean canUse() {
                return isSpawned() && !isUsingSkill() && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return isSpawned() && super.canUse();
            }
        });

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public int getMaxHeadYRot() {
        return 5;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (!this.isSpawned()) {
                this.getNavigation().stop();
                Vec3 movement = this.getDeltaMovement();
                this.setDeltaMovement(0, movement.y, 0);
                this.hasImpulse = true;
            } else {
                // 平滑身体转向
                if (this.getTarget() != null && !this.isUsingSkill()) {
                    this.yBodyRot = Mth.approachDegrees(this.yBodyRot, this.yHeadRot, 5.0F);
                }
            }
        } else {
            if (this.isSpawned() && this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6D) {
                this.playAnimationIfStopped(this, this.IDLE);
            }
        }
    }
    @Override
    public void spawn() {
        if (!this.isSpawned()) {
            this.spawnTick++;
            if (this.level().isClientSide) {
                if (this.spawnTick == 1) {
                    this.playAnimationIfStopped(this, this.START);
                }
                if (this.spawnTick >= 132) {
                    this.stopAnimation(this, this.START);
                }
            }
            else {
                this.setInvisible(this.spawnTick < 5);
                if (this.spawnTick == 1) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.WARDEN_EMERGE, SoundSource.HOSTILE, 1.0F, 1.0F);
                }
                if (this.spawnTick < 100 && this.spawnTick % 2 == 0) {
                    WarlockParticleUtils.spawnEmergingParticles(this);
                }
                if (this.spawnTick == 100) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.WARDEN_ROAR, SoundSource.HOSTILE, 1.5F, 0.7F);
                    WarlockParticleUtils.spawnSoulSphereBlast(this, 0.35F);
                }
                if (this.spawnTick >= 132) {
                    this.setSpawned(true);
                }
            }
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }
    }
    @Override
    public void updatePhase() {
    }

    @Override
    public boolean hasSpawnProcess() {
        return true;
    }

    @Override
    public SkillManager<? extends ISkillEntity> getSkillManager() {
        return this.skillManager;
    }

    @Override
    public List<Integer> getSkillPool() {
        return List.of();
    }
}