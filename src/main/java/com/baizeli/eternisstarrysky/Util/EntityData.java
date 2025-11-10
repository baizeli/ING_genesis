package com.baizeli.eternisstarrysky.Util;

import net.minecraft.world.phys.Vec3;

public class EntityData {
    public long time;
    public Vec3 pos;

    public EntityData(long time, Vec3 pos) {
        this.time = time;
        this.pos = pos;
    }

    public EntityData(long time) {
        this.time = time;
        this.pos = Vec3.ZERO;
    }
}
