package com.baizeli.eternisstarrysky.fonts;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Mth;
import net.minecraft.util.StringDecomposer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FuckFont1 extends Font {
    public static Font font = new FuckFont1(Minecraft.getInstance().font.fonts, false);
    private static final Map<String, KeywordType> KEYWORDS = Map.ofEntries(
            Map.entry("混沌", KeywordType.CHAOS),
            Map.entry("扭曲之混沌", KeywordType.CHAOS),
            Map.entry("星源珍珠", KeywordType.CELESTIAL),
            Map.entry("星源锭", KeywordType.CELESTIAL),
            Map.entry("Twisted Chaos", KeywordType.CHAOS),
            Map.entry("twisted_chaos", KeywordType.CHAOS),
            Map.entry("Celestial Source Pearl", KeywordType.CELESTIAL),
            Map.entry("celestial_source_pearl", KeywordType.CELESTIAL),
            Map.entry("Celestial Source Ingot", KeywordType.CELESTIAL),
            Map.entry("celestial_source_ingot", KeywordType.CELESTIAL),
            Map.entry("Chaos", KeywordType.CHAOS),
            Map.entry("chaos", KeywordType.CHAOS),
            Map.entry("CHAOS", KeywordType.CHAOS),
            Map.entry("星源", KeywordType.CELESTIAL),
            Map.entry("celestial_source", KeywordType.CELESTIAL),
            Map.entry("celestial source", KeywordType.CELESTIAL),
            Map.entry("Celestial Source", KeywordType.CELESTIAL)
    );
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

        List<Map.Entry<int[], KeywordType>> keywords = scanKeywords(text);

        final float[] xBox = { x };
        final long time = net.minecraft.Util.getMillis();
        final int[] index = { 0 };          // 当前字符在整个字符串中的下标

        seq.accept((i, style, codePoint) -> {
            int idx = index[0]++;
            KeywordType type = getTypeAt(idx, keywords);

            boolean isChaos = (type == KeywordType.CHAOS);
            boolean isCelestial = (type == KeywordType.CELESTIAL);

            Style outStyle = isChaos || isCelestial ? style.withColor((TextColor) null).withUnderlined(false) : style;

            int color;
            float amp = 0.35f;                 // 主字颤抖幅度（像素）
            float speed = 0.045f;              // 颤抖速度
            float dx;
            float dy;
            if (isChaos) {
                color = 1;
                dx = xBox[0] + (float) Math.cos(time * speed + idx * 3.7f) * amp;
                dy = y + (float) Math.sin(time * speed + idx * 2.9f) * amp;
            } else if (isCelestial) {
                color = 2;
                dx = xBox[0];
                dy = y + (float) Math.cos(time / 200F + idx);
            } else {
                color = (style.getColor() != null) ? style.getColor().getValue() | 0xFF000000 : baseRgb;
                dx = xBox[0];
                dy = y;
            }

            if (isChaos || isCelestial) {
                // 主字
                drawInternal(FormattedCharSequence.forward(String.valueOf((char) codePoint), outStyle),
                        dx, dy, color, dropShadow,
                        matrix, buffers, mode, light, overlay, idx);
                // 残影
                super.drawInternal(FormattedCharSequence.forward(String.valueOf((char) codePoint), outStyle),
                        xBox[0] + 0.2F, y + 0.2F, (color & 0x00FFFFFF) | 0x33000000, dropShadow,
                        matrix, buffers, mode, light, overlay);
            } else {
                super.drawInternal(FormattedCharSequence.forward(String.valueOf((char) codePoint), outStyle),
                        xBox[0], y, color, dropShadow,
                        matrix, buffers, mode, light, overlay);
            }

            xBox[0] += width(String.valueOf((char) codePoint));
            return true;
        });


        return (int) xBox[0];
    }

    private static List<Map.Entry<int[], KeywordType>> scanKeywords(String text) {
        List<Map.Entry<int[], KeywordType>> result = new ArrayList<>();
        for (var entry : KEYWORDS.entrySet()) {
            String key = entry.getKey();
            KeywordType type = entry.getValue();
            for (int i = text.indexOf(key); i >= 0; i = text.indexOf(key, i + 1)) {
                result.add(Map.entry(new int[]{i, i + key.length()}, type));
            }
        }
        // 按起始位置排序，重叠时优先长的
        result.sort((a, b) -> {
            int[] r1 = a.getKey();
            int[] r2 = b.getKey();
            if (r1[0] != r2[0]) return Integer.compare(r1[0], r2[0]);
            return Integer.compare(r2[1], r1[1]); // 长的优先
        });
        // 去重重叠区间
        List<Map.Entry<int[], KeywordType>> filtered = new ArrayList<>();
        int lastEnd = -1;
        for (var e : result) {
            int[] r = e.getKey();
            if (r[0] >= lastEnd) {
                filtered.add(e);
                lastEnd = r[1];
            }
        }
        return filtered;
    }

    private static KeywordType getTypeAt(int idx, List<Map.Entry<int[], KeywordType>> keywords) {
        for (var e : keywords) {
            int[] r = e.getKey();
            if (idx >= r[0] && idx < r[1]) return e.getValue();
        }
        return null;
    }

    enum KeywordType {
        CHAOS,
        CELESTIAL
    }

    public int drawInternal(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords, int index) {
        Matrix4f matrix4f = new Matrix4f(matrix);
        if (dropShadow) {
            this.renderText(text, x, y, color, true, matrix, buffer, displayMode, backgroundColor, packedLightCoords, index);
            matrix4f.translate(SHADOW_OFFSET);
        }

        x = this.renderText(text, x, y, color, false, matrix4f, buffer, displayMode, backgroundColor, packedLightCoords, index);
        return (int)x + (dropShadow ? 1 : 0);
    }

    public float renderText(String text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords, int index) {
        MyStringRenderOutput font$stringrenderoutput = new MyStringRenderOutput(buffer, x, y, color, dropShadow, matrix, displayMode, packedLightCoords, index);
        StringDecomposer.iterateFormatted(text, Style.EMPTY, font$stringrenderoutput);
        return font$stringrenderoutput.finish(backgroundColor, x);
    }

    public float renderText(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords, int index) {
        MyStringRenderOutput font$stringrenderoutput = new MyStringRenderOutput(buffer, x, y, color, dropShadow, matrix, displayMode, packedLightCoords, index);
        text.accept(font$stringrenderoutput);
        return font$stringrenderoutput.finish(backgroundColor, x);
    }

    @OnlyIn(Dist.CLIENT)
    public final class MyStringRenderOutput implements FormattedCharSink {
        public final MultiBufferSource bufferSource;
        public final boolean dropShadow;
        public final float dimFactor;
        public final Matrix4f pose;
        public final DisplayMode mode;
        public final int packedLightCoords;
        public float x, y;
        public int color;
        @Nullable public List<BakedGlyph.Effect> effects;

        private final int index;                         // 第几个字（用于相位偏移）

        public MyStringRenderOutput(MultiBufferSource bufferSource, float x, float y,
                                    int color, boolean dropShadow, Matrix4f pose,
                                    DisplayMode mode, int packedLightCoords, int index) {
            this.bufferSource = bufferSource;
            this.x = x;
            this.y = y;
            this.color = color;
            this.dropShadow = dropShadow;
            this.dimFactor = dropShadow ? 0.25F : 1.0F;
            this.pose = pose;
            this.mode = mode;
            this.packedLightCoords = packedLightCoords;
            this.index = index;
        }

        private int calcColor(float localPhase, int color) {
            long time = Util.getMillis();
            if (color == 1) {
                float progress = (time * 0.0009f + (index + localPhase) * 0.05f) % 1.0f; // 0~1
                float hue = 0.00f;          // 固定红色
                float sat = 1.0f - progress; // 1→0  深红→灰
                float bri = 1.0f - progress; // 1→0  灰→黑
                return Mth.hsvToRgb(hue, sat, bri) | 0xFF_000000; // 强制不透明
            } else {
                float hue = (time * 0.0012f + (index + localPhase) * 0.03f) % 1.0f;            // 全色域循环
                return Mth.hsvToRgb(hue, 1, 1) | 0xFF_000000;
            }
        }

        @Override
        public boolean accept(int pos, Style style, int codePoint) {
            FontSet fontSet = FuckFont1.this.getFontSet(style.getFont());
            GlyphInfo glyphInfo = fontSet.getGlyphInfo(codePoint, FuckFont1.this.filterFishyGlyphs);
            BakedGlyph baked = style.isObfuscated() && codePoint != ' '
                    ? fontSet.getRandomGlyph(glyphInfo)
                    : fontSet.getGlyph(codePoint);

            boolean bold = style.isBold();
            float shadowOff = dropShadow ? glyphInfo.getShadowOffset() : 0F;
            float advance = glyphInfo.getAdvance(bold);

            int cLeft = calcColor(0.0f, this.color);
            int cRight = calcColor(1.0f, this.color);

            float[] colorLeft = unpack(cLeft);
            float[] colorRight = unpack(cRight);

            float x0 = this.x + shadowOff;
            float y0 = this.y + shadowOff;

            float lineY = dropShadow ? 1F : 0F;

            VertexConsumer vc = bufferSource.getBuffer(baked.renderType(mode));
            if (!(baked instanceof EmptyGlyph)) {
                render(baked, style.isItalic(), this.x + shadowOff, this.y + shadowOff, this.pose, vc, colorLeft, colorRight, this.packedLightCoords);
            }

            if (style.isStrikethrough()) {
                addEffect(new BakedGlyph.Effect(x0 + lineY - 1F, y0 + 4.5F, x0 + lineY + advance, y0 + 4.5F - 1F,
                        0.01F, colorLeft[0], colorLeft[1], colorLeft[2], colorLeft[3]));
            }

            if (style.isUnderlined()) {
                addEffect(new BakedGlyph.Effect(x0 + lineY - 1F, y0 + 9F, x0 + lineY + advance, y0 + 9F - 1F,
                            0.01F, colorLeft[0], colorLeft[1], colorLeft[2], colorLeft[3]));
            }

            this.x += advance;
            return true;
        }

        private float[] unpack(int c) {
            return new float[]{
                    (((c >> 16) & 0xFF) / 255F) * dimFactor,
                    (((c >> 8)  & 0xFF) / 255F) * dimFactor,
                    (((c)       & 0xFF) / 255F) * dimFactor,
                    (((c >> 24) & 0xFF) / 255F)
            };
        }

        public float finish(int backgroundColor, float x) {
            if (backgroundColor != 0) {
                float f = (float)(backgroundColor >> 24 & 255) / 255.0F;
                float f1 = (float)(backgroundColor >> 16 & 255) / 255.0F;
                float f2 = (float)(backgroundColor >> 8 & 255) / 255.0F;
                float f3 = (float)(backgroundColor & 255) / 255.0F;
                this.addEffect(new BakedGlyph.Effect(x - 1.0F, this.y + 9.0F, this.x + 1.0F, this.y - 1.0F, 0.01F, f1, f2, f3, f));
            }

            if (this.effects != null) {
                BakedGlyph bakedglyph = FuckFont1.this.getFontSet(Style.DEFAULT_FONT).whiteGlyph();
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(bakedglyph.renderType(this.mode));

                for(BakedGlyph.Effect bakedglyph$effect : this.effects) {
                    bakedglyph.renderEffect(bakedglyph$effect, this.pose, vertexconsumer, this.packedLightCoords);
                }
            }

            return this.x;
        }

        public void render(BakedGlyph glyph, boolean italic, float x, float y,
                           Matrix4f matrix, VertexConsumer buffer, float[] colLeft, float[] colRight, int packedLight) {
            float f = x + glyph.left;
            float f1 = x + glyph.right;
            float f2 = glyph.up - 3.0F;
            float f3 = glyph.down - 3.0F;
            float f4 = y + f2;
            float f5 = y + f3;
            float f6 = italic ? 1.0F - 0.25F * f2 : 0.0F;
            float f7 = italic ? 1.0F - 0.25F * f3 : 0.0F;
            buffer.vertex(matrix, f + f6, f4, 0.0F).color(colLeft[0],  colLeft[1],  colLeft[2],  colLeft[3]).uv(glyph.u0, glyph.v0).uv2(packedLight).endVertex();
            buffer.vertex(matrix, f + f7, f5, 0.0F).color(colLeft[0],  colLeft[1],  colLeft[2],  colLeft[3]).uv(glyph.u0, glyph.v1).uv2(packedLight).endVertex();
            buffer.vertex(matrix, f1 + f7, f5, 0.0F).color(colRight[0], colRight[1], colRight[2], colRight[3]).uv(glyph.u1, glyph.v1).uv2(packedLight).endVertex();
            buffer.vertex(matrix, f1 + f6, f4, 0.0F).color(colRight[0], colRight[1], colRight[2], colRight[3]).uv(glyph.u1, glyph.v0).uv2(packedLight).endVertex();
        }

        public void addEffect(BakedGlyph.Effect effect) {
            if (this.effects == null) {
                this.effects = Lists.newArrayList();
            }
            this.effects.add(effect);
        }
    }
}