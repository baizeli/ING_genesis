package com.baizeli.eternisstarrysky.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class TextUtil {
    public static final String ALL_TEXTS = "*ALL_TEXTS*";
    
    public static class FloatingFont extends Font {
        private final int color;
        private final float amplitude;
        private final float frequency;
        private final Set<String> targetTexts;
        private final boolean useFloating;
        private final boolean useGradient;
        private final int[] colors;
        private final float gradientSpeed;

        public FloatingFont(
            Function<ResourceLocation, FontSet> fonts, 
            boolean filterFishyGlyphs, 
            int color, 
            float amplitude, 
            float frequency, 
            String... targetTexts
        ) {
            super(fonts, filterFishyGlyphs);
            this.color = color;
            this.amplitude = amplitude;
            this.frequency = frequency;
            this.useFloating = true;
            this.useGradient = false;
            this.colors = new int[0];
            this.gradientSpeed = 0.05f;
            this.targetTexts = new HashSet<>(Arrays.asList(targetTexts));
        }

        public FloatingFont(
            Function<ResourceLocation, FontSet> fonts, 
            boolean filterFishyGlyphs, 
            int[] colors,
            float gradientSpeed,
            String... targetTexts
        ) {
            super(fonts, filterFishyGlyphs);
            this.color = 0xFFFFFFFF;
            this.amplitude = 0;
            this.frequency = 0;
            this.useFloating = false;
            this.useGradient = true;
            this.colors = colors != null ? colors.clone() : new int[0];
            this.gradientSpeed = gradientSpeed;
            this.targetTexts = new HashSet<>(Arrays.asList(targetTexts));
        }

        public FloatingFont(
            Function<ResourceLocation, FontSet> fonts, 
            boolean filterFishyGlyphs, 
            int[] colors,
            float amplitude, 
            float frequency,
            float gradientSpeed,
            String... targetTexts
        ) {
            super(fonts, filterFishyGlyphs);
            this.color = 0xFFFFFFFF;
            this.amplitude = amplitude;
            this.frequency = frequency;
            this.useFloating = true;
            this.useGradient = true;
            this.colors = colors != null ? colors.clone() : new int[0];
            this.gradientSpeed = gradientSpeed;
            this.targetTexts = new HashSet<>(Arrays.asList(targetTexts));
        }
        
        @Override
        public int drawInBatch(
            @NotNull FormattedCharSequence text, float x, float y, int color, boolean dropShadow, 
            @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, 
            @NotNull DisplayMode displayMode, int backgroundColor, int packedLight
        ) {
            StringBuilder stringBuilder = new StringBuilder();
            
            text.accept((index, style, codePoint) -> {
                stringBuilder.appendCodePoint(codePoint);

                return true;
            });
            
            String textStr = stringBuilder.toString();

            boolean shouldApply = targetTexts.contains(ALL_TEXTS) || 
            (!targetTexts.isEmpty() && targetTexts.contains(textStr));
            
            if (shouldApply) {
                long time = getTimeBase();
                float[] pos = {x};
                
                text.accept((index, style, codePoint) -> {
                    String charStr = new String(Character.toChars(codePoint));

                    float yOffset = 0;
                    if (useFloating) {
                        yOffset = (float) Math.sin(time * frequency + index * 0.3) * amplitude;
                    }

                    int currentColor = this.color;
                    if (useGradient && colors.length > 0) {
                        currentColor = getGradientColor(colors, time, gradientSpeed);
                    }
                    
                    super.drawInBatch(
                        charStr, pos[0], y + yOffset, currentColor, 
                        dropShadow, matrix, buffer, displayMode, backgroundColor, packedLight
                    );
                    
                    pos[0] += width(charStr);
                    return true;
                });
                
                return (int) pos[0];

            } else {
                return super.drawInBatch(
                    text, x, y, color, dropShadow, matrix, buffer, displayMode, backgroundColor, packedLight
                );
            }
        }

        private int getGradientColor(int[] colors, long time, float speed) {
            if (colors.length == 0) {
                return 0xFFFFFFFF;
            }
            
            if (colors.length == 1) {
                return colors[0];
            }

            float ratio = (float) (Math.sin(time * speed) + 1) / 2;

            float scaledRatio = ratio * (colors.length - 1);

            int index1 = (int) Math.floor(scaledRatio);
            int index2 = (int) Math.ceil(scaledRatio);

            index1 = Math.max(0, Math.min(index1, colors.length - 1));
            index2 = Math.max(0, Math.min(index2, colors.length - 1));

            float interRatio = scaledRatio - index1;
            
            int color1 = colors[index1];
            int color2 = colors[index2];
            
            int r1 = (color1 >> 16) & 0xFF;
            int g1 = (color1 >> 8) & 0xFF;
            int b1 = color1 & 0xFF;
            
            int r2 = (color2 >> 16) & 0xFF;
            int g2 = (color2 >> 8) & 0xFF;
            int b2 = color2 & 0xFF;
            
            int r = (int) (r1 + (r2 - r1) * interRatio);
            int g = (int) (g1 + (g2 - g1) * interRatio);
            int b = (int) (b1 + (b2 - b1) * interRatio);
            
            return (r << 16) | (g << 8) | b | 0xFF000000;
        }
    }

    public static FloatingFont createFloatingFont(
        Function<ResourceLocation, FontSet> fonts,
        boolean filterFishyGlyphs, 
        int color,
        float amplitude, 
        float frequency, 
        String... targetTexts
    ) {
        return new FloatingFont(fonts, filterFishyGlyphs, color, amplitude, frequency, targetTexts);
    }

    public static FloatingFont createGradientFont(
        Function<ResourceLocation, FontSet> fonts,
        boolean filterFishyGlyphs, 
        int[] colors,
        float gradientSpeed,
        String... targetTexts
    ) {
        return new FloatingFont(fonts, filterFishyGlyphs, colors, gradientSpeed, targetTexts);
    }

    public static FloatingFont createFloatingGradientFont(
        Function<ResourceLocation, FontSet> fonts,
        boolean filterFishyGlyphs, 
        int[] colors,
        float amplitude, 
        float frequency,
        float gradientSpeed,
        String... targetTexts
    ) {
        return new FloatingFont(fonts, filterFishyGlyphs, colors, amplitude, frequency, gradientSpeed, targetTexts);
    }

    public static FloatingFont createFloatingFontForAllTexts(
        Function<ResourceLocation, FontSet> fonts,
        boolean filterFishyGlyphs, 
        int color,
        float amplitude, 
        float frequency
    ) {
        return new FloatingFont(fonts, filterFishyGlyphs, color, amplitude, frequency, ALL_TEXTS);
    }

    public static FloatingFont createGradientFontForAllTexts(
        Function<ResourceLocation, FontSet> fonts,
        boolean filterFishyGlyphs, 
        int[] colors,
        float gradientSpeed
    ) {
        return new FloatingFont(fonts, filterFishyGlyphs, colors, gradientSpeed, ALL_TEXTS);
    }
    
    public static FloatingFont createFloatingGradientFontForAllTexts(
        Function<ResourceLocation, FontSet> fonts,
        boolean filterFishyGlyphs, 
        int[] colors,
        float amplitude, 
        float frequency,
        float gradientSpeed
    ) {
        return new FloatingFont(fonts, filterFishyGlyphs, colors, amplitude, frequency, gradientSpeed, ALL_TEXTS);
    }

    private static long getTimeBase() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) return minecraft.level.getGameTime();
        return System.currentTimeMillis() / 50;
    }
}