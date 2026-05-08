package miku.united_as_one.genesis.contents.entity.boss;

import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;

public class TrailComponent {
    private final Vec3[][] trailPositions;
    private int trailPointer = 0;
    private int currentSize = 0; // 当前有效采样点的数量
    private boolean hasTrail = false;
    private final int maxLength;

    public TrailComponent(int maxLength) {
        this.maxLength = maxLength;
        this.trailPositions = new Vec3[maxLength][2];
    }

    /**
     * 更新轨迹。应在模型动画的 setCustomAnimations 中调用。
     */
    public void updateTrail(Vec3 startPos, Vec3 endPos) {
        if (!hasTrail) {
            // 重新开启轨迹时，清空计数确保不会读取到旧数据
            this.currentSize = 0;
            this.trailPointer = 0;
            this.hasTrail = true;
        }

        // 存入当前点
        this.trailPositions[this.trailPointer] = new Vec3[]{startPos, endPos};

        // 指针后移
        this.trailPointer = (this.trailPointer + 1) % this.maxLength;

        // 增加有效计数，直到填满缓冲区
        if (this.currentSize < this.maxLength) {
            this.currentSize++;
        }
    }

    /**
     * 获取用于渲染的插值后的轨迹点。
     * @param pointer 历史偏移量（0 为最新点，1 为前一个点，以此类推）
     * @param partialTick 客户端部分刻
     */
    @Nullable
    public Vec3[] getTrailPosition(int pointer, float partialTick) {

        if (!hasTrail || currentSize < 2 || pointer >= currentSize - 1) {
            return null;
        }


        int latestIdx = (this.trailPointer - 1 + this.maxLength) % this.maxLength;

        int i = (latestIdx - pointer + this.maxLength) % this.maxLength;
        int j = (latestIdx - pointer - 1 + this.maxLength) % this.maxLength;

        Vec3[] currFrame = trailPositions[i];
        Vec3[] prevFrame = trailPositions[j];

        if (currFrame == null || prevFrame == null) {
            return null;
        }

        Vec3 interpolatedStart = prevFrame[0].lerp(currFrame[0], partialTick);
        Vec3 interpolatedEnd = prevFrame[1].lerp(currFrame[1], partialTick);

        return new Vec3[]{interpolatedStart, interpolatedEnd};
    }

    public boolean hasTrail() {
        return hasTrail;
    }

    public void setHasTrail(boolean hasTrail) {
        if (this.hasTrail != hasTrail) {
            this.hasTrail = hasTrail;
            if (!hasTrail) {
                this.currentSize = 0;
                this.trailPointer = 0;
            }
        }
    }

    public int getMaxLength() {
        return maxLength;
    }

    /**
     * 获取当前可渲染的有效段数（点数 - 1）
     */
    public int getAvailableSegments() {
        return Math.max(0, currentSize - 1);
    }
}