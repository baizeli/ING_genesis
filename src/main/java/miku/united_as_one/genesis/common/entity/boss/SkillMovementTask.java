package miku.united_as_one.genesis.common.entity.boss;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public abstract class SkillMovementTask {

    protected int tick;
    protected final int duration;

    public SkillMovementTask(int duration) {
        this.duration = duration;
    }

    public void start(Mob mob) {}


    public abstract Vec3 compute(Mob mob, float progress);

    public void tick(Mob mob) {
        tick++;
    }

    public void end(Mob mob) {}

    public boolean finished() {
        return tick >= duration;
    }

    public float progress() {
        return Mth.clamp((float) tick / duration, 0.0F, 1.0F);
    }
}
