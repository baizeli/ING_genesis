package miku.united_as_one.genesis.common.entity;

import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class ControlledAnimation {
    private int timer;
    private int duration;

    public ControlledAnimation(int d) {
        this.timer = 0;
        this.duration = d;
    }

    public void setDuration(int d) {
        this.timer = 0;
        this.duration = d;
    }

    public int getTimer() {
        return this.timer;
    }

    public void increaseTimer() {
        if (this.timer < this.duration) {
            this.timer++;
        }
    }

    public void decreaseTimer() {
        if (this.timer > 0.0d) {
            this.timer--;
        }
    }
}
