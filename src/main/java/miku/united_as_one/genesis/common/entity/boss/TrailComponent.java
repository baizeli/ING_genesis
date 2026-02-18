package miku.united_as_one.genesis.common.entity.boss;

import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;

public class TrailComponent {
    private final Vec3[][] trailPositions;
    private int trailPointer = -1;
    private boolean hasTrail = false;
    private final int maxLength;

    public TrailComponent(int maxLength) {
        this.maxLength = maxLength;
        this.trailPositions = new Vec3[maxLength][2];
    }

    /**
     * 更新轨迹。应在模型动画的 setCustomAnimations 中调用，传入两个骨骼的世界坐标。
     */
    public void updateTrail(Vec3 startPos, Vec3 endPos) {
        if (!hasTrail) {
            // 初始化轨迹数组
            for (int i = 0; i < maxLength; i++) {
                trailPositions[i] = new Vec3[]{startPos, endPos};
            }
            hasTrail = true;
        }

        if (++this.trailPointer >= this.maxLength) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = new Vec3[]{startPos, endPos};
    }

    /**
     * 获取用于渲染的插值后的轨迹点。
     * @param pointer 历史索引（0为最新，越大越旧）
     * @param partialTick 部分刻时间，用于平滑插值
     * @return 包含[startPos, endPos]的数组，如果轨迹未初始化或索引无效返回 null
     */
    @Nullable
    public Vec3[] getTrailPosition(int pointer, float partialTick) {

        if (!hasTrail || trailPointer == -1) {
            return null;
        }
        if (pointer < 0 || pointer >= maxLength) {
            return null;
        }

        int i = (this.trailPointer - pointer) & (maxLength - 1);
        int j = (this.trailPointer - pointer - 1) & (maxLength - 1);

        Vec3[] prevFrame = trailPositions[j];
        Vec3[] currFrame = trailPositions[i];

        if (prevFrame == null || currFrame == null ||
                prevFrame[0] == null || prevFrame[1] == null ||
                currFrame[0] == null || currFrame[1] == null) {
            return null;
        }

        Vec3 interpolatedStart = prevFrame[0].add(currFrame[0].subtract(prevFrame[0]).scale(partialTick));
        Vec3 interpolatedEnd = prevFrame[1].add(currFrame[1].subtract(prevFrame[1]).scale(partialTick));

        return new Vec3[]{interpolatedStart, interpolatedEnd};
    }
    public boolean hasTrail() {
        return hasTrail;
    }

    public void setHasTrail(boolean hasTrail) {
        this.hasTrail = hasTrail;
        if (!hasTrail) {
            trailPointer = -1; // 重置指针
        }
    }

    public int getMaxLength() {
        return maxLength;
    }
}