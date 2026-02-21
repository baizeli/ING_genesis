package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import miku.united_as_one.genesis.common.entity.ai.ModMemoryModuleType;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.entity.boss.behavior.AnimatedActionBehavior;
import miku.united_as_one.genesis.common.entity.spells.blood_boss.blood_dagger.BloodDaggerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BloodDaggerZoneBehavior extends AnimatedActionBehavior<BloodBoss> {
    public static final int ANIM_DURATION = 5;
    public static final int ACTION_TIMESTAMP = 1;

    public BloodDaggerZoneBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleType.IS_CASTING_SKILL.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        return entity.getTarget() != null && entity.distanceToSqr(entity.getTarget()) > 9.0;
    }

    @Override
    protected int getActionTimestamp() {
        return ACTION_TIMESTAMP;
    }

    @Override
    protected int getActionDuration() {
        return ANIM_DURATION;
    }

    @Override
    protected int getCooldown() {
        return Utils.random.nextIntBetweenInclusive(50, 90);
    }

    @Override
    protected String getAnimationId() {
        return "instant_slash";
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull BloodBoss owner, long gameTime) {
        super.tick(level, owner, gameTime);
        if (owner.getTarget() != null)
            owner.lookAt(owner.getTarget(), 30.0F, 30.0F);
    }

    @Override
    protected void doAction(BloodBoss entity) {
        LivingEntity target = entity.getTarget();
        if (target != null) {
            entity.playSound(SoundRegistry.FIERY_DAGGER_THROW.get(), 2.0F, (float) Utils.random.nextIntBetweenInclusive(80, 110) * 0.01F);
            Vec3 start = entity.getEyePosition();
            Vec3 targetPos = target.position();
            Vec3 deltaAim = targetPos.subtract(start);

            for(int i = 0; i < 3; ++i) {
                Vec3 aim = start.add(deltaAim.yRot((float) ((Math.PI / 4) * (i - 1))));
                int delay = Utils.random.nextIntBetweenInclusive(10, 40);
                BloodDaggerEntity dagger = new BloodDaggerEntity(entity.level);
                dagger.setOwner(entity);
                dagger.setPos(start);
                dagger.delay = delay;
                dagger.setDamage((float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE));
                dagger.setExplosionRadius(4.0F + Utils.random.nextFloat() * 2.0F);
                dagger.setNoGravity(false);
                Vec3 horizontal = aim.subtract(start).multiply(1.0, 0.0, 1.0);
                double horizontalSpeed = Mth.cos((float) (Math.PI / 4)) + 0.5;
                double distance = horizontal.length();
                double ticks = distance / horizontalSpeed;
                double y1 = aim.y - start.y;
                double g = 0.05;
                double verticalSpeed = (y1 + 0.5 * g * ticks * ticks) / ticks;
                Vec3 trajectory = horizontal.normalize().scale(horizontalSpeed).add(0.0, verticalSpeed, 0.0);
                dagger.setDeltaMovement(trajectory);
                entity.level.addFreshEntity(dagger);
            }
        }
    }
}