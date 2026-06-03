package miku.united_as_one.genesis.contents.entity.arrow;

import miku.united_as_one.genesis.Genesis;
import miku.bai_ze_li.genesis.api.text.GenesisColor;
import miku.bai_ze_li.genesis.api.render.TrailRenderStyle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Random;

public enum ArrowRenderDefinition {
    THUNDER(0, "thunder", "textures/images/trail_thunder.png", GenesisColor.THUNDER_THEME, 0x87CEEB, false, 4, 1),
    HOLY(1, "holy", "textures/images/trail_holy.png", GenesisColor.HOLY_THEME, 0xFFD700, false, 4, 1),
    BLOOD(2, "blood", "textures/images/trail_blood.png", GenesisColor.BLOOD_THEME, 0xFF0000, false, 4, 1),
    STELLAR(3, "stellar", "textures/images/trail_stellar.png", GenesisColor.RAINBOW, 0xFFFFFF, true, 2, 2);

    private final int id;
    private final String key;
    private final ResourceLocation trailTexture;
    private final List<Integer> colors;
    private final int bodyColor;
    private final boolean fixedEntityBodyColor;
    private final int flightParticleInterval;
    private final int flightParticleCount;

    private static final ResourceLocation OVERLAY_TRAIL_TEXTURE = Genesis.rl("textures/images/map_3.png");
    private static final ResourceLocation CONE_TEXTURE = Genesis.rl("textures/images/gr.png");

    ArrowRenderDefinition(int id, String key, String trailTexture, List<Integer> colors, int bodyColor,
                          boolean fixedEntityBodyColor, int flightParticleInterval, int flightParticleCount) {
        this.id = id;
        this.key = key;
        this.trailTexture = Genesis.rl(trailTexture);
        this.colors = colors;
        this.bodyColor = bodyColor;
        this.fixedEntityBodyColor = fixedEntityBodyColor;
        this.flightParticleInterval = flightParticleInterval;
        this.flightParticleCount = flightParticleCount;
    }

    public int id() {
        return id;
    }

    public String key() {
        return key;
    }

    public ResourceLocation trailTexture() {
        return trailTexture;
    }

    public int flightParticleInterval() {
        return flightParticleInterval;
    }

    public int flightParticleCount() {
        return flightParticleCount;
    }

    public float[] bodyColor(int entityId) {
        if (!fixedEntityBodyColor) {
            return rgb(bodyColor);
        }
        Random random = new Random(entityId * 31L);
        return new float[]{
                0.35F + random.nextFloat() * 0.65F,
                0.35F + random.nextFloat() * 0.65F,
                0.35F + random.nextFloat() * 0.65F
        };
    }

    public float[] trailColor(float progress, float time, int entityId) {
        float timeOffset = time * 0.1F;
        float colorPosition = (progress * 2.0F + timeOffset) % 1.0F;
        if (colorPosition < 0.0F) {
            colorPosition += 1.0F;
        }
        return interpolateColor(colorPosition, colors);
    }

    public float[] particleColor(float time, int entityId) {
        if (fixedEntityBodyColor) {
            return trailColor((time * 0.071F) % 1.0F, time, entityId);
        }
        return bodyColor(entityId);
    }

    public TrailRenderStyle trailStyle() {
        return TrailRenderStyle.builder(trailTexture, this::trailColor)
                .overlayTexture(OVERLAY_TRAIL_TEXTURE)
                .coneTexture(CONE_TEXTURE)
                .width(1.0F)
                .alphaMultiplier(0.6F)
                .emissive(true)
                .build();
    }

    public static ArrowRenderDefinition byId(int id) {
        for (ArrowRenderDefinition definition : values()) {
            if (definition.id == id) {
                return definition;
            }
        }
        return THUNDER;
    }

    public static ArrowRenderDefinition byKey(String key) {
        for (ArrowRenderDefinition definition : values()) {
            if (definition.key.equals(key)) {
                return definition;
            }
        }
        return THUNDER;
    }

    public static ArrowRenderDefinition random(RandomSource random) {
        return weighted(random,
                weighted(THUNDER, 1),
                weighted(HOLY, 1),
                weighted(BLOOD, 1),
                weighted(STELLAR, 1)
        );
    }

    public static ArrowRenderDefinition weighted(RandomSource random, WeightedEntry... entries) {
        int totalWeight = 0;
        for (WeightedEntry entry : entries) {
            totalWeight += Math.max(0, entry.weight());
        }
        if (totalWeight <= 0) {
            return THUNDER;
        }

        int roll = random.nextInt(totalWeight);
        for (WeightedEntry entry : entries) {
            int weight = Math.max(0, entry.weight());
            if (roll < weight) {
                return entry.definition();
            }
            roll -= weight;
        }
        return THUNDER;
    }

    public static WeightedEntry weighted(ArrowRenderDefinition definition, int weight) {
        return new WeightedEntry(definition, weight);
    }

    public record WeightedEntry(ArrowRenderDefinition definition, int weight) {
    }

    private static float[] interpolateColor(float position, List<Integer> colors) {
        if (colors.isEmpty()) {
            return new float[]{1.0F, 1.0F, 1.0F};
        }
        if (colors.size() == 1) {
            return rgb(colors.get(0));
        }

        float scaled = Mth.clamp(position, 0.0F, 1.0F) * (colors.size() - 1);
        int index = Mth.clamp((int) Math.floor(scaled), 0, colors.size() - 2);
        float fraction = scaled - index;
        float[] first = rgb(colors.get(index));
        float[] second = rgb(colors.get(index + 1));
        return new float[]{
                Mth.lerp(fraction, first[0], second[0]),
                Mth.lerp(fraction, first[1], second[1]),
                Mth.lerp(fraction, first[2], second[2])
        };
    }

    private static float[] rgb(int color) {
        return new float[]{
                ((color >> 16) & 0xFF) / 255.0F,
                ((color >> 8) & 0xFF) / 255.0F,
                (color & 0xFF) / 255.0F
        };
    }
}
