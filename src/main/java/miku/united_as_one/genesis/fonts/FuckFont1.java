package miku.united_as_one.genesis.fonts;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import java.util.List;
import java.util.function.Function;

public class FuckFont1 extends Font {
    public static Font font = new FuckFont1(Minecraft.getInstance().font.fonts, false);

    public FuckFont1(Function<ResourceLocation, FontSet> fonts, boolean filterFishyGlyphs) {
        super(fonts, filterFishyGlyphs);
    }

    public static FuckFont1 getFont() {
        return new FuckFont1(Minecraft.getInstance().font.fonts, false);
    }

    @Override
    public int drawInBatch(@NotNull FormattedCharSequence formattedCharSequence, float x, float y, int rgb, boolean b1, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource multiBufferSource, @NotNull DisplayMode mode, int i, int i1) {
        return renderFont(formattedCharSequence, x, y, rgb, b1, matrix4f, multiBufferSource, mode, i, i1, this.isBidirectional());
    }

    @Override
    public int drawInBatch(@NotNull String text, float x, float y, int rgb, boolean b, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource source, @NotNull DisplayMode mode, int i, int i1, boolean isText) {
        return renderFont(FormattedCharSequence.forward(text, Style.EMPTY), x, y, rgb, b, matrix4f, source, mode, i, i1, isText);
    }

    @Override
    public int drawInBatch(@NotNull Component component, float x, float y, int rgb, boolean b, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource source, @NotNull DisplayMode mode, int i, int i1) {
        return renderFont(FormattedCharSequence.forward(component.getString(), Style.EMPTY), x, y, rgb, b, matrix4f, source, mode, i, i1, this.isBidirectional());
    }

    public int renderFont(FormattedCharSequence seq, float x, float y, int baseRgb, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffers, DisplayMode mode, int light, int overlay, boolean ignoredIsText) {
        final int NORMAL = 0, AFTER_HASH = 1, AFTER_HASH_EXIT = 2, CHAOS = 3, CELESTIAL = 4;

        final int[] state = {NORMAL};
        final Style[] hashStyle = {null};
        final float[] currentX = {x};
        final long time = net.minecraft.Util.getMillis();
        final int[] charIndex = {0};

        seq.accept((index, style, codePoint) -> {
            char ch = (char) codePoint;
            boolean shouldRender = true;
            Style outStyle = style;
            int color = baseRgb;
            float dx = 0, dy = 0;
            boolean isSpecial = false;

            if (state[0] == AFTER_HASH || state[0] == AFTER_HASH_EXIT) {
                if (ch == '1') {
                    state[0] = CHAOS;
                    shouldRender = false;
                } else if (ch == '2') {
                    state[0] = CELESTIAL;
                    shouldRender = false;
                } else if (ch == '#') {
                    if (state[0] == AFTER_HASH) {
                        renderChar('#', hashStyle[0], currentX[0], y, baseRgb, dropShadow, matrix, buffers, mode, light, overlay, charIndex[0], false);
                        currentX[0] += width("#");
                        charIndex[0]++;
                        shouldRender = false;
                    }
                    else {
                        state[0] = AFTER_HASH;
                        hashStyle[0] = style;
                        shouldRender = false;
                    }
                } else {
                    if (state[0] == AFTER_HASH) {
                        renderChar('#', hashStyle[0], currentX[0], y, baseRgb, dropShadow, matrix, buffers, mode, light, overlay, charIndex[0], false);
                        currentX[0] += width("#");
                        charIndex[0]++;
                    }

                    color = (style.getColor() != null) ? style.getColor().getValue() | 0xFF000000 : baseRgb;
                    renderChar(ch, style, currentX[0], y, color, dropShadow, matrix, buffers, mode, light, overlay, charIndex[0], false);
                    currentX[0] += width(String.valueOf(ch));
                    charIndex[0]++;

                    state[0] = NORMAL;
                    shouldRender = false;
                }
            } else if (state[0] == CHAOS) {
                if (ch == '#') {
                    state[0] = AFTER_HASH_EXIT;
                    hashStyle[0] = style;
                    shouldRender = false;
                } else {
                    isSpecial = true;
                    color = 1;
                    dx = (float) Math.cos(time * 0.045 + charIndex[0] * 3.7f) * 0.35f;
                    dy = (float) Math.sin(time * 0.045 + charIndex[0] * 2.9f) * 0.35f;
                    outStyle = style.withColor((TextColor) null).withUnderlined(false);
                }
            } else if (state[0] == CELESTIAL) {
                if (ch == '#') {
                    state[0] = AFTER_HASH_EXIT;
                    hashStyle[0] = style;
                    shouldRender = false;
                } else {
                    isSpecial = true;
                    color = 2;
                    dy = (float) Math.cos(time / 200F + charIndex[0]);
                    outStyle = style.withColor((TextColor) null).withUnderlined(false);
                }
            } else if (ch == '#') {
                state[0] = AFTER_HASH;
                hashStyle[0] = style;
                shouldRender = false;
            } else {
                color = (style.getColor() != null) ? style.getColor().getValue() | 0xFF000000 : baseRgb;
            }

            if (shouldRender) {
                renderChar(ch, outStyle, currentX[0] + dx, y + dy, color, dropShadow, matrix, buffers, mode, light, overlay, charIndex[0], isSpecial);
                currentX[0] += width(String.valueOf(ch));
                charIndex[0]++;
            }
            return true;
        });

        if (state[0] == AFTER_HASH) {
            renderChar('#', hashStyle[0], currentX[0], y, baseRgb, dropShadow, matrix, buffers, mode, light, overlay, charIndex[0], false);
            currentX[0] += width("#");
        }

        return (int) currentX[0];
    }

    private void renderChar(char ch, Style style, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffers, DisplayMode mode, int light, int overlay, int index, boolean isSpecial) {
        FormattedCharSequence seq = FormattedCharSequence.forward(String.valueOf(ch), style);
        if (isSpecial) {
            drawInternal(seq, x, y, color, dropShadow, matrix, buffers, mode, light, overlay, index);
            super.drawInternal(seq, x + 0.2F, y + 0.2F, (color & 0x00FFFFFF) | 0x33000000, dropShadow, matrix, buffers, mode, light, overlay);
        } else {
            super.drawInternal(seq, x, y, color, dropShadow, matrix, buffers, mode, light, overlay);
        }
    }

    public void drawInternal(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords, int index) {
        Matrix4f matrix4f = new Matrix4f(matrix);
        if (dropShadow) {
            this.renderText(text, x, y, color, true, matrix, buffer, displayMode, backgroundColor, packedLightCoords, index);
            matrix4f.translate(SHADOW_OFFSET);
        }

        this.renderText(text, x, y, color, false, matrix4f, buffer, displayMode, backgroundColor, packedLightCoords, index);
    }

    public float renderText(String text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords, int index) {
        MyStringRenderOutput font$stringrenderoutput = new MyStringRenderOutput(buffer, x, y, color, dropShadow, matrix, displayMode, packedLightCoords, index);
        StringDecomposer.iterateFormatted(text, Style.EMPTY, font$stringrenderoutput);
        return font$stringrenderoutput.finish(backgroundColor, x);
    }

    public void renderText(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords, int index) {
        MyStringRenderOutput font$stringrenderoutput = new MyStringRenderOutput(buffer, x, y, color, dropShadow, matrix, displayMode, packedLightCoords, index);
        text.accept(font$stringrenderoutput);
        font$stringrenderoutput.finish(backgroundColor, x);
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

        private final int index; // 第几个字（用于相位偏移）
        private static final int SEGMENTS = 256; // 每个字符的分段数

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

            // 生成多段颜色
            int[] segmentColors = new int[SEGMENTS + 1];
            for (int i = 0; i <= SEGMENTS; i++) {
                float phase = (float) i / SEGMENTS;
                segmentColors[i] = calcColor(phase, this.color);
            }

            // 多段渲染
            VertexConsumer vc = bufferSource.getBuffer(baked.renderType(mode));
            if (!(baked instanceof EmptyGlyph)) {
                renderMultiSegment(baked, style.isItalic(), this.x + shadowOff, this.y + shadowOff,
                        this.pose, vc, segmentColors, this.packedLightCoords);
            }

            // 下划线和删除线效果
            float x0 = this.x + shadowOff;
            float y0 = this.y + shadowOff;
            float lineY = dropShadow ? 1F : 0F;

            if (style.isStrikethrough()) {
                addEffect(new BakedGlyph.Effect(x0 + lineY - 1F, y0 + 4.5F, x0 + lineY + advance, y0 + 4.5F - 1F,
                        0.01F, 0F, 0F, 0F, 1F)); // 颜色在finish时统一处理
            }

            if (style.isUnderlined()) {
                addEffect(new BakedGlyph.Effect(x0 + lineY - 1F, y0 + 9F, x0 + lineY + advance, y0 + 9F - 1F,
                        0.01F, 0F, 0F, 0F, 1F));
            }

            this.x += advance;
            return true;
        }

        // 多段渲染方法
        private void renderMultiSegment(BakedGlyph glyph, boolean italic, float x, float y,
                                        Matrix4f matrix, VertexConsumer buffer, int[] colors,
                                        int packedLight) {
            float left = x + glyph.left;
            float right = x + glyph.right;
            float top = y + glyph.up - 3.0F;
            float bottom = y + glyph.down - 3.0F;

            // 斜体偏移
            float italicTop = italic ? 1.0F - 0.25F * glyph.up : 0.0F;
            float italicBottom = italic ? 1.0F - 0.25F * glyph.down : 0.0F;

            // 每段的宽度和UV跨度
            float segmentWidth = (right - left) / SEGMENTS;
            float segmentU = (glyph.u1 - glyph.u0) / SEGMENTS;

            // 为每段生成四边形
            for (int i = 0; i < SEGMENTS; i++) {
                float x0 = left + i * segmentWidth;
                float x1 = left + (i + 1) * segmentWidth;
                float u0 = glyph.u0 + i * segmentU;
                float u1 = glyph.u0 + (i + 1) * segmentU;

                float[] colLeft = unpack(colors[i]);
                float[] colRight = unpack(colors[i + 1]);

                // 四个顶点
                buffer.vertex(matrix, x0 + italicTop, top, 0.0F).color(colLeft[0], colLeft[1], colLeft[2], colLeft[3]).uv(u0, glyph.v0).uv2(packedLight).endVertex();
                buffer.vertex(matrix, x0 + italicBottom, bottom, 0.0F).color(colLeft[0], colLeft[1], colLeft[2], colLeft[3]).uv(u0, glyph.v1).uv2(packedLight).endVertex();
                buffer.vertex(matrix, x1 + italicBottom, bottom, 0.0F).color(colRight[0], colRight[1], colRight[2], colRight[3]).uv(u1, glyph.v1).uv2(packedLight).endVertex();
                buffer.vertex(matrix, x1 + italicTop, top, 0.0F).color(colRight[0], colRight[1], colRight[2], colRight[3]).uv(u1, glyph.v0).uv2(packedLight).endVertex();
            }
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
                float alpha = (float)(backgroundColor >> 24 & 255) / 255.0F;
                float r = (float)(backgroundColor >> 16 & 255) / 255.0F;
                float g = (float)(backgroundColor >> 8 & 255) / 255.0F;
                float b = (float)(backgroundColor & 255) / 255.0F;
                this.addEffect(new BakedGlyph.Effect(x - 1.0F, this.y + 9.0F, this.x + 1.0F, this.y - 1.0F, 0.01F, r, g, b, alpha));
            }

            if (this.effects != null) {
                BakedGlyph bakedglyph = FuckFont1.this.getFontSet(Style.DEFAULT_FONT).whiteGlyph();
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(bakedglyph.renderType(this.mode));

                for(BakedGlyph.Effect bakedglyph$effect : this.effects) {
                    bakedglyph.renderEffect(bakedglyph$effect, this.pose, vertexconsumer, this.packedLightCoords);
                }
            }

            return x;
        }

        public void addEffect(BakedGlyph.Effect effect) {
            if (this.effects == null) {
                this.effects = Lists.newArrayList();
            }
            this.effects.add(effect);
        }
    }
}