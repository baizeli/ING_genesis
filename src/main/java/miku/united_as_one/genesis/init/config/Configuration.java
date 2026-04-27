package miku.united_as_one.genesis.init.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;

    // 服务端
    public static final ForgeConfigSpec.DoubleValue WHISPER_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_HARD_MODE;

    // 客户端 - 蓝色特效
    public static final ForgeConfigSpec.BooleanValue ENABLE_BLUE;
    public static final ForgeConfigSpec.DoubleValue WIDTH_BLUE;

    // 客户端 - 黑红特效
    public static final ForgeConfigSpec.BooleanValue ENABLE_RED;
    public static final ForgeConfigSpec.DoubleValue WIDTH_RED;

    // 客户端 - 彩色特效
    public static final ForgeConfigSpec.BooleanValue ENABLE_RAINBOW;
    public static final ForgeConfigSpec.DoubleValue WIDTH_RAINBOW;

    static {
        ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();
        serverBuilder.push("combat_settings");
        WHISPER_DAMAGE = serverBuilder.comment("武器基础伤害").defineInRange("whisper_damage", 60.0, 0.0, 1000.0);
        ENABLE_HARD_MODE = serverBuilder.comment("是否开启困难模式").define("enable_hard_mode", false);
        serverBuilder.pop();
        SERVER_SPEC = serverBuilder.build();

        ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();

        // 统一所有默认宽度为 1.0
        clientBuilder.push("blue_effect");
        ENABLE_BLUE = clientBuilder.comment("开启特效").define("enable_blue", true);
        WIDTH_BLUE = clientBuilder.comment("勾边宽度").defineInRange("width_blue", 1.0, 0.0, 10.0);
        clientBuilder.pop();

        clientBuilder.push("red_effect");
        ENABLE_RED = clientBuilder.comment("开启特效").define("enable_red", true);
        WIDTH_RED = clientBuilder.comment("勾边宽度").defineInRange("width_red", 1.0, 0.0, 10.0);
        clientBuilder.pop();

        clientBuilder.push("rainbow_effect");
        ENABLE_RAINBOW = clientBuilder.comment("开启特效").define("enable_rainbow", true);
        WIDTH_RAINBOW = clientBuilder.comment("勾边宽度").defineInRange("width_rainbow", 1.0, 0.0, 10.0);
        clientBuilder.pop();

        CLIENT_SPEC = clientBuilder.build();
    }

    public static void setup() {}
}