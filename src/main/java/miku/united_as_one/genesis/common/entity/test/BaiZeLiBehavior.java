package miku.united_as_one.genesis.common.entity.test;

import miku.united_as_one.genesis.common.entity.test.data.BaiZeLiData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;

public class BaiZeLiBehavior {

    private final PathfinderMob entity;
    private final BaiZeLiData data;
    private long lastHurtTime = 0L;

    public BaiZeLiBehavior(PathfinderMob entity, BaiZeLiData data) {
        this.entity = entity;
        this.data = data;
    }

    public void registerGoals() {
        entity.goalSelector.addGoal(0, new FloatGoal(entity));
        entity.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(entity, 1.0D));
        entity.goalSelector.addGoal(2, new LookAtPlayerGoal(entity, Player.class, 8.0F));
        entity.goalSelector.addGoal(3, new RandomLookAroundGoal(entity));
    }

    public boolean handleHurt(DamageSource source, float amount) {
        if (entity.level().isClientSide || data.isRealDead()) return false;
        long t = entity.level().getGameTime();
        if (t - lastHurtTime < 30L) return false;
        lastHurtTime = t;

        float real = data.getRealHealth(entity);
        float max = data.getMaxHealth();
        if (!(source.getEntity() instanceof Player)) amount *= 0.01f;
        amount = Math.min(amount, max * 0.1f);

        float newFake = data.getFakeHealth() - amount;
        float newReal = real - amount;

        if (newFake <= 0 && !data.isDowned()) data.setDowned(true);
        if (newReal <= 0) { newReal = 0; data.setRealDead(true); }
        if (data.isDowned() && newFake > 0) data.setDowned(false);

        data.setFakeHealth(Math.max(newFake, 0));
        data.setRealHealth(entity, Math.max(newReal, 0));

        data.syncHealth(entity);
        return true;
    }

    public void tick() {
        float real = data.getRealHealth(entity);

        if (data.getFakeHealth() <= 0 && real > 0) {
            data.setDowned(true);
            entity.setPose(net.minecraft.world.entity.Pose.DYING);
            entity.setNoAi(false);
            entity.setHealth(1f);
        }

        if (real <= 0) {
            data.setRealDead(true);
            entity.setPose(net.minecraft.world.entity.Pose.DYING);
            entity.setNoAi(true);
            entity.setHealth(0.1f);
        }

        if (data.getFakeHealth() > 0 && real > 0) {
            data.setDowned(false);
            entity.setPose(net.minecraft.world.entity.Pose.STANDING);
            entity.setNoAi(false);
        }

        entity.removeAllEffects();
    }
}