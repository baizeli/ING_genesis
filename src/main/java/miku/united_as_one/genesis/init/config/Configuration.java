package miku.united_as_one.genesis.init.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {
    public static final ForgeConfigSpec SPECIFICATION;

    // 在此声明变量
    public static final ForgeConfigSpec.DoubleValue WHISPER_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_HARD_MODE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        // 【添加配置项：第二步】在此定义默认值和范围
        builder.push("combat_settings");
        WHISPER_DAMAGE = builder.comment("武器基础伤害").defineInRange("whisper_damage", 60.0, 0.0, 1000.0);
        ENABLE_HARD_MODE = builder.comment("是否开启困难模式").define("enable_hard_mode", false);
        builder.pop();

        SPECIFICATION = builder.build();
    }

    public static void setup() {} // 确保类加载
}