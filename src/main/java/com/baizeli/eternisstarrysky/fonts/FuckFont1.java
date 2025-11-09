package com.baizeli.eternisstarrysky.fonts;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FuckFont1 extends Font {
    public static List<String> tips = new ArrayList<>();
    public static Font font = new FuckFont1(Minecraft.getInstance().font.fonts, false);
    static {
        tips.add("混沌");
    }

    public FuckFont1(Function<ResourceLocation, FontSet> p_243253_, boolean p_243245_) {
        super(p_243253_, p_243245_);
    }

    public static FuckFont1 getFont() {
        return new FuckFont1(Minecraft.getInstance().font.fonts, false);
    }

    public int drawInBatch(@NotNull FormattedCharSequence formattedCharSequence, float x, float y, int rgb, boolean b1, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource multiBufferSource, @NotNull DisplayMode mode, int i, int i1) {
//        StringBuilder builder = new StringBuilder();
//        formattedCharSequence.accept((p_13746_, p_13747_, p_13748_) -> {
//            builder.appendCodePoint(p_13748_);
//            return true;
//        });
        return renderFont(formattedCharSequence, x, y, rgb, b1, matrix4f, multiBufferSource, mode, i, i1, this.isBidirectional());
    }

    public int drawInBatch(@NotNull String text, float x, float y, int rgb, boolean b, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource source, @NotNull DisplayMode mode, int i, int i1, boolean isText) {
        return renderFont(FormattedCharSequence.forward(text, Style.EMPTY), x, y, rgb, b, matrix4f, source, mode, i, i1, isText);
    }

    public int drawInBatch(@NotNull Component component, float x, float y, int rgb, boolean b, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource source, @NotNull DisplayMode mode, int i, int i1) {
        return renderFont(FormattedCharSequence.forward(component.getString(), Style.EMPTY), x, y, rgb, b, matrix4f, source, mode, i, i1, this.isBidirectional());
    }

    public int renderFont(FormattedCharSequence seq, float x, float y, int baseRgb, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffers, DisplayMode mode, int light, int overlay, boolean isText) {
        StringBuilder plain = new StringBuilder();
        seq.accept((i, st, cp) -> { plain.appendCodePoint(cp); return true; });
        final String text = plain.toString();
        final boolean hasKeyword = text.contains("混沌");

        if (!hasKeyword) {   // 没有关键字，一次性画完
            return (int) (x + super.drawInBatch(seq, x, y, baseRgb, dropShadow, matrix, buffers, mode, light, overlay));
        }

        final float[] xBox = { x };
        final long time = Util.getMillis();

        seq.accept((index, style, codePoint) -> {
            char ch = (char) codePoint;
            boolean isKeyword = (ch == '混' || ch == '沌');

            // .withUnderlined(false) 我觉得去掉下划线会有点突兀
            Style outStyle = isKeyword ? style.withColor((TextColor) null) : style;// 非关键字保持原样

            // 计算颜色
            int color;
            if (isKeyword) {          // 关键字：走渐变
                float progress = (time * 0.0009F + index * 0.05F) % 1F;
                color = Mth.hsvToRgb(0.83F, 0.6F * (1F - progress), 1F) | 0xFF000000;
            } else {                  // 非关键字：用原样式或 baseRgb
                color = (style.getColor() != null) ? style.getColor().getValue() | 0xFF000000 : baseRgb;
            }

            // 画字
            if (isKeyword) {
                float yOffset = (float) Math.cos(time / 200F + index);
                // 主字
                super.drawInBatch(FormattedCharSequence.forward(String.valueOf(ch), outStyle), xBox[0], y + yOffset, color, dropShadow, matrix, buffers, mode, light, overlay);
                // 残影
                super.drawInBatch(FormattedCharSequence.forward(String.valueOf(ch), outStyle), xBox[0] + 0.2F, y + 0.2F, (color & 0x00FFFFFF) | 0x33000000, dropShadow, matrix, buffers, mode, light, overlay);
            } else {
                super.drawInBatch(FormattedCharSequence.forward(String.valueOf(ch), outStyle), xBox[0], y, color, dropShadow, matrix, buffers, mode, light, overlay);
            }

            xBox[0] += width(String.valueOf(ch));
            return true;
        });

        return (int) xBox[0];
    }
}