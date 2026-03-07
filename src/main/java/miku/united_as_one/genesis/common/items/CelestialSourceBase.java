package miku.united_as_one.genesis.common.items;

import miku.united_as_one.genesis.client.tooltipParticleHandler.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.*;

import java.util.*;

public class CelestialSourceBase extends Item implements ITooltipParticleItem {
    public CelestialSourceBase(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("item." + miku.united_as_one.genesis.Genesis.MOD_ID + ".celestial_source_base_item.hover"));
    }

    @Override
    public TooltipParticleSystem.ParticleConfig getParticleConfig() {
        Minecraft mc = Minecraft.getInstance();
        double mouseX = mc.mouseHandler.xpos();
        double mouseY = mc.mouseHandler.ypos();

        // 转换为整数坐标
        int mousePosX = (int) mouseX;
        int mousePosY = (int) mouseY;
        return new TooltipParticleSystem.ParticleConfig()
                // 纹理使用
                .setTextures(
                        PTID.Star_0,
                        PTID.Star_1,
                        PTID.Star_2,
                        PTID.Star_3,
                        PTID.Star_4,
                        PTID.Star_5,
                        PTID.Star_6,
                        PTID.Star_7,
                        PTID.Star_8,
                        PTID.Star_9
                )
                .setParticleCount(1, 3) // 生成数量多少到多少
                .setMaxTotalParticles(400) // 最大粒子数量
                // 大小
                .setSize(4.0f, 12.0f) // 基础大小
                .setRandomSize(false) // 随机大小变化
                // 生命周期
                .setLife(3.0f, 4.0f) // 存活时间3-4秒
                // 速度
                .setSpeed(70.0f, 100.0f) // 基础速度
                // 颜色
                .setColors(0xFFffb800, 0xFFcd7231, 0xFFffeb00, 0xFFe0ae2d) // 基础颜色（彩虹模式下会被覆盖）
                .setRainbowColors(true, 2.0f) // 开启彩虹渐变
                .setColorTransitionSpeed(1.5f) // 颜色过渡速度
                // 物理
                .setGravity(false, 40.0f) // 关闭重力（RAIN模式自带下落效果）
                .setWind(true, 0.0f, 110.0f) // 强风效果
                .setAirResistance(0.1f) // 轻微空气阻力
                .setBounciness(10.0f) // 弹性系数
                // 旋转
                .setRotation(true, 0.0f, 0.01f) // 旋转速度
                .setInitialRotation(0.0f, 360.0f) // 随机初始旋转角度
                // 运动类型
                .setMotionType(TooltipParticleSystem.MotionType.RAIN) // 从屏幕上方下落
                .setMotionProperties(20.0f, 1.5f) // 运动幅度和频率（对RAIN模式影响较小）
                .setCenter((float) mousePosX / 3, (float) mousePosY / 3) // 运动中心点（对RAIN模式影响较小）
                .setRadius(200.0f) // 运动半径（对RAIN模式影响较小）
                // 淡入淡出
                .setFadeIn(true, 0.05f) // 淡入，持续0.05秒
                .setFadeOut(true, 0.25f) // 淡出，持续0.25秒
                // 层次感
                .setDepthLayers(true, 12, 0.08f) // 12层深度，每层变暗8%
                // 贝塞尔曲线
                .setSizeCurve(TooltipParticleSystem.BezierCurveType.STAR_EXPAND) // 星星膨胀曲线
                .setAlphaCurve(TooltipParticleSystem.BezierCurveType.NONE) // 透明度曲线
                .setSpeedCurve(TooltipParticleSystem.BezierCurveType.NONE) // 速度曲线
                .setRotationCurve(TooltipParticleSystem.BezierCurveType.NONE); // 旋转曲线
    }
}