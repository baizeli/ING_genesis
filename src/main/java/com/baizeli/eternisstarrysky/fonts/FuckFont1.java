package com.baizeli.eternisstarrysky.fonts;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class FuckFont1 extends Font {
    public static Font font = new FuckFont1(Minecraft.getInstance().font.fonts, false);

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

    public int renderFont(FormattedCharSequence seq, float x, float y, int baseRgb, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffers, DisplayMode mode, int light, int overlay, boolean ignoredIsText) {
        final int NORMAL = 0, AFTER_HASH = 1, CHAOS = 2, CELESTIAL = 3;

        final int[] state = {NORMAL};
        final Style[] hashStyle = {null};
        final float[] currentX = {x};
        final long time = net.minecraft.Util.getMillis();
        final int[] charIndex = {0};

        final int[] prevState = {NORMAL};
        final int[] hashCharIndex = {-1};

        seq.accept((index, style, codePoint) -> {
            char ch = (char) codePoint;
            boolean shouldRender = true;
            Style outStyle = style;
            int color = baseRgb;
            float dx = 0, dy = 0;
            boolean isSpecial = false;

            if (state[0] == AFTER_HASH) {
                if (ch == '1') {
                    state[0] = CHAOS;
                    shouldRender = false;
                } else if (ch == '2') {
                    state[0] = CELESTIAL;
                    shouldRender = false;
                } else if (ch == '#') {
                    // 转义的##，渲染单个#
                    state[0] = NORMAL;
                } else {
                    // 命令不匹配时的处理
                    if (prevState[0] == NORMAL) {
                        // 来自普通状态的#，渲染它
                        renderChar('#', hashStyle[0], currentX[0], y, baseRgb, dropShadow, matrix, buffers, mode, light, overlay, charIndex[0], false);
                        currentX[0] += width("#");
                        charIndex[0]++;
                    } else {
                        // 来自特殊状态的结束标记，不渲染#
                        state[0] = NORMAL;
                    }

                    // 渲染当前字符（普通模式）
                    color = (style.getColor() != null) ? style.getColor().getValue() | 0xFF000000 : baseRgb;
                    renderChar(ch, style, currentX[0], y, color, dropShadow, matrix, buffers, mode, light, overlay, charIndex[0], false);
                    currentX[0] += width(String.valueOf(ch));
                    charIndex[0]++;

                    shouldRender = false; // 已经处理完毕
                }
            } else if (state[0] == CHAOS) {
                if (ch == '#') {
                    prevState[0] = CHAOS;
                    state[0] = AFTER_HASH;
                    hashStyle[0] = style;
                    hashCharIndex[0] = charIndex[0];
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
                    prevState[0] = CELESTIAL;
                    state[0] = AFTER_HASH;
                    hashStyle[0] = style;
                    hashCharIndex[0] = charIndex[0];
                    shouldRender = false;
                } else {
                    isSpecial = true;
                    color = 2;
                    dy = (float) Math.cos(time / 200F + charIndex[0]);
                    outStyle = style.withColor((TextColor) null).withUnderlined(false);
                }
            } else if (ch == '#') {
                prevState[0] = NORMAL;
                state[0] = AFTER_HASH;
                hashStyle[0] = style;
                hashCharIndex[0] = charIndex[0];
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

        // 处理末尾的#（仅当是普通状态未匹配的#时才渲染）
        if (state[0] == AFTER_HASH && prevState[0] == NORMAL) {
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

//    public float renderText(String text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode displayMode, int backgroundColor, int packedLightCoords, int index) {
//        MyStringRenderOutput font$stringrenderoutput = new MyStringRenderOutput(buffer, x, y, color, dropShadow, matrix, displayMode, packedLightCoords, index);
//        StringDecomposer.iterateFormatted(text, Style.EMPTY, font$stringrenderoutput);
//        return font$stringrenderoutput.finish(backgroundColor, x);
//    }

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

        public void finish(int backgroundColor, float x) {
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