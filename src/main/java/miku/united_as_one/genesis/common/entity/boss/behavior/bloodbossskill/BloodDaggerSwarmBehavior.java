package miku.united_as_one.genesis.common.entity.boss.behavior.bloodbossskill;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
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

public class BloodDaggerSwarmBehavior extends AnimatedActionBehavior<BloodBoss> {
    public static final int ANIM_DURATION = 25;
    public static final int ACTION_TIMESTAMP = 17;

    public BloodDaggerSwarmBehavior() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull BloodBoss entity, long gameTime) {
        this.abilityTimer = 0;
    }

    @Override
    protected boolean canStartAction(BloodBoss entity) {
        return true;
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
        return Utils.random.nextIntBetweenInclusive(100, 160);
    }

    @Override
    protected String getAnimationId() {
        return "";
    }

    @Override
    protected void doAction(BloodBoss entity) {
        LivingEntity target = entity.getTarget();
        if (target != null) {
            entity.playSound(SoundRegistry.FIRE_CAST.get(), 2.0F, (float)Utils.random.nextIntBetweenInclusive(80, 110) * 0.01F);
            Vec3 pos = entity.position();
            int count = 7;
            int delay = Utils.random.nextIntBetweenInclusive(30, 70);
            float yAngle = -Utils.getAngle(target.getX(), target.getZ(), entity.getX(), entity.getZ()) + ((float)Math.PI / 2F);

            for(int i = 0; i < count; ++i) {
                Vec3 offset = new Vec3((double) 1.5F * (double) entity.getScale(), 0.0, 0.0).zRot(Mth.lerp((float) i / ((float) count - 1.0F), 0.0F, -(float) Math.PI)).yRot(yAngle).add(0.0, entity.getEyeHeight(), 0.0);
                BloodDaggerEntity dagger = new BloodDaggerEntity(entity.level);
                dagger.setOwner(entity);
                dagger.ownerTrack = offset;
                dagger.setTarget(entity.getTarget());
                dagger.setPos(pos.add(offset.yRot(entity.getYRot())));
                dagger.delay = delay + i * 2;
                float damage = (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
                dagger.setDamage(damage);
                entity.level.addFreshEntity(dagger);
            }
        }
    }
}
