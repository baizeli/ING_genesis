package com.baizeli.eternisstarrysky.fonts;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
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
    public static Font font = new FuckFont1(Minecraft.getInstance().font.fonts, false);
    static {
    }

    public FuckFont1(Function<ResourceLocation, FontSet> p_243253_, boolean p_243245_) {
        super(p_243253_, p_243245_);
    }

    public static FuckFont1 getFont() {
        return new FuckFont1(Minecraft.getInstance().font.fonts, false);
    }

    public int drawInBatch(@NotNull FormattedCharSequence formattedCharSequence, float x, float y, int rgb, boolean b1, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource multiBufferSource, @NotNull DisplayMode mode, int i, int i1) {
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

        List<int[]> keywordRanges = new ArrayList<>();
        addRange(text, "混沌", keywordRanges);
        addRange(text, "扭曲之混沌", keywordRanges);
        addRange(text, "Twisted Chaos", keywordRanges);
        addRange(text, "Chaos", keywordRanges);
        addRange(text, "chaos", keywordRanges);
        addRange(text, "CHAOS", keywordRanges);
        addRange(text, "星源", keywordRanges);
        addRange(text, "celestial source", keywordRanges);
        addRange(text, "Celestial Source", keywordRanges);

        if (keywordRanges.isEmpty()) {
            return (int) (x + super.drawInBatch(seq, x, y, baseRgb, dropShadow,
                    matrix, buffers, mode, light, overlay));
        }

        final float[] xBox = { x };
        final long time = Util.getMillis();
        final int[] index = { 0 };          // 当前字符在整个字符串中的下标

        seq.accept((i, style, codePoint) -> {
            final int idx = index[0]++;     // 先记录，再自增
            boolean isChaos = false;
            boolean isCelestial = false;

            for (int[] r : keywordRanges) {
                if (idx >= r[0] && idx < r[1]) {
                    String sub = text.substring(r[0], r[1]);
                    if (sub.equals("混沌") || sub.equals("扭曲之混沌") ||
                            sub.equalsIgnoreCase("chaos") || sub.equalsIgnoreCase("twisted chaos")) {
                        isChaos = true;
                    } else if (sub.equals("星源") || sub.equalsIgnoreCase("celestial source")) {
                        isCelestial = true;
                    }
                    break;
                }
            }

            Style outStyle = isChaos || isCelestial ? style.withColor((TextColor) null).withUnderlined(false) : style;

            int color;
            float amp = 0.35f;                 // 主字颤抖幅度（像素）
            float speed = 0.045f;              // 颤抖速度
            float dx;
            float dy;
            if (isChaos) {
                float progress = (time * 0.0009f + idx * 0.05f) % 1.0f;   // 0~1
                float hue   = 0.00f;                                   // 0=红
                float sat   = (1.0f - progress);                       // 1→0  深红→灰
                float bright= (1.0f - progress);                       // 1→0  灰→黑

                color = Mth.hsvToRgb(hue, sat, bright);
                dx = xBox[0] + (float) Math.cos(time * speed + idx * 3.7f) * amp;
                dy = y + (float) Math.sin(time * speed + idx * 2.9f) * amp;
            } else if (isCelestial) {
                float progress = (time * 0.0012F + idx * 0.03F) % 1F;
                color = Mth.hsvToRgb(progress, 0.7F, 1F) | 0xFF000000;
                dx = xBox[0];
                dy = y + (float) Math.cos(time / 200F + idx);
            } else {
                color = (style.getColor() != null) ? style.getColor().getValue() | 0xFF000000 : baseRgb;
                dx = xBox[0];
                dy = y;
            }

            if (isChaos || isCelestial) {
//                绘制背景
//                float charWidth = width(String.valueOf(ch));
//                RenderUtils.drawRenderTypeRect(xBox[0], y, charWidth, lineHeight - 1, COSMIC_FONT, matrix);
//                if (buffers instanceof MultiBufferSource.BufferSource source) source.endBatch();
                // 主字
                super.drawInBatch(FormattedCharSequence.forward(String.valueOf((char) codePoint), outStyle),
                        dx, dy, color, dropShadow,
                        matrix, buffers, mode, light, overlay);
                // 残影
                super.drawInBatch(FormattedCharSequence.forward(String.valueOf((char) codePoint), outStyle),
                        xBox[0] + 0.2F, y + 0.2F, (color & 0x00FFFFFF) | 0x33000000, dropShadow,
                        matrix, buffers, mode, light, overlay);
            } else {
                super.drawInBatch(FormattedCharSequence.forward(String.valueOf((char) codePoint), outStyle),
                        xBox[0], y, color, dropShadow,
                        matrix, buffers, mode, light, overlay);
            }

            xBox[0] += width(String.valueOf((char) codePoint));
            return true;
        });


        return (int) xBox[0];
    }

    private static void addRange(String text, String key, List<int[]> ranges) {
        for (int i = text.indexOf(key); i >= 0; i = text.indexOf(key, i + 1)) {
            ranges.add(new int[]{ i, i + key.length() });
        }
    }
//
//    public int drawInternal(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords) {
//        color = adjustColor(color);
//        Matrix4f matrix4f = new Matrix4f(matrix);
//        if (dropShadow) {
//            this.renderText(text, x, y, color, true, matrix, buffer, displayMode, backgroundColor, packedLightCoords);
//            matrix4f.translate(SHADOW_OFFSET);
//        }
//
//        x = this.renderText(text, x, y, color, false, matrix4f, buffer, displayMode, backgroundColor, packedLightCoords);
//        return (int)x + (dropShadow ? 1 : 0);
//    }
//
//    public float renderText(String text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords) {
//        StringRenderOutput font$stringrenderoutput = new StringRenderOutput(buffer, x, y, color, dropShadow, matrix, displayMode, packedLightCoords);
//        StringDecomposer.iterateFormatted(text, Style.EMPTY, font$stringrenderoutput);
//        return finish(backgroundColor, x, font$stringrenderoutput);
//    }
//
//    public float renderText(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords) {
//        StringRenderOutput font$stringrenderoutput = new StringRenderOutput(buffer, x, y, color, dropShadow, matrix, displayMode, packedLightCoords);
//        text.accept(font$stringrenderoutput);
//        return finish(backgroundColor, x, font$stringrenderoutput);
//    }
//
//    public float finish(int backgroundColor, float x,  StringRenderOutput font$stringrenderoutput) {
//        if (backgroundColor != 0) {
//            float f = (float)(backgroundColor >> 24 & 255) / 255.0F;
//            float f1 = (float)(backgroundColor >> 16 & 255) / 255.0F;
//            float f2 = (float)(backgroundColor >> 8 & 255) / 255.0F;
//            float f3 = (float)(backgroundColor & 255) / 255.0F;
//            font$stringrenderoutput.addEffect(new BakedGlyph.Effect(x - 1.0F, font$stringrenderoutput.y + 9.0F, font$stringrenderoutput.x + 1.0F, font$stringrenderoutput.y - 1.0F, 0.01F, f1, f2, f3, f));
//        }
//
//        if (font$stringrenderoutput.effects != null) {
//            BakedGlyph bakedglyph = this.getFontSet(Style.DEFAULT_FONT).whiteGlyph();
//            VertexConsumer vertexconsumer = font$stringrenderoutput.bufferSource.getBuffer(COSMIC_FONT);
//
//            for(BakedGlyph.Effect bakedglyph$effect : font$stringrenderoutput.effects) {
//                bakedglyph.renderEffect(bakedglyph$effect, font$stringrenderoutput.pose, vertexconsumer, font$stringrenderoutput.packedLightCoords);
//            }
//        }
//
//        return font$stringrenderoutput.x;
//    }
}