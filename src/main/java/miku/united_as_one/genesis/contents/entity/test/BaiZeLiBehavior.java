package miku.united_as_one.genesis.contents.entity.test;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;

public class BaiZeLiBehavior {

    private final PathfinderMob entity;

    public BaiZeLiBehavior(PathfinderMob entity) {
        this.entity = entity;
    }

    public void registerGoals() {
        entity.goalSelector.addGoal(0, new FloatGoal(entity));
        entity.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(entity, 1.0D));
        entity.goalSelector.addGoal(2, new LookAtPlayerGoal(entity, Player.class, 8.0F));
        entity.goalSelector.addGoal(3, new RandomLookAroundGoal(entity));
    }

    public void tick() {
    }
}