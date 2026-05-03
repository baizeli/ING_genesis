package miku.united_as_one.genesis.init.config;

import miku.united_as_one.genesis.Genesis;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Configuration {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final List<ConfigEntry<?>> SERVER_ENTRIES = new ArrayList<>();
    public static final List<ConfigEntry<?>> CLIENT_ENTRIES = new ArrayList<>();

    public static double whisperDamage = 60.0;
    public static boolean enableHardMode = false;

    public static boolean enableBlue = true;
    public static float widthBlue = 1.0f;
    public static boolean enableRed = true;
    public static float widthRed = 1.0f;
    public static boolean enableRainbow = true;
    public static float widthRainbow = 1.0f;

    static {
        ForgeConfigSpec.Builder serverBuf = new ForgeConfigSpec.Builder();
        serverBuf.push("server_settings");
        register(serverBuf, SERVER_ENTRIES, "combat_main", "combat_stats", "whisper_damage", 60.0, 0.0, 1000.0, "武器基础伤害");
        register(serverBuf, SERVER_ENTRIES, "combat_main", "combat_stats", "enable_hard_mode", false, "是否开启困难模式");
        serverBuf.pop();
        SERVER_SPEC = serverBuf.build();

        ForgeConfigSpec.Builder clientBuf = new ForgeConfigSpec.Builder();
        clientBuf.push("render_settings");
        register(clientBuf, CLIENT_ENTRIES, "visual_main", "red", "enable_red", true, "开启黑红特效");
        register(clientBuf, CLIENT_ENTRIES, "visual_main", "red", "width_red", 1.0, 0.0, 10.0, "黑红勾边宽度");

        register(clientBuf, CLIENT_ENTRIES, "visual_main", "blue", "enable_blue", true, "开启蓝色特效");
        register(clientBuf, CLIENT_ENTRIES, "visual_main", "blue", "width_blue", 1.0, 0.0, 10.0, "蓝色勾边宽度");

        register(clientBuf, CLIENT_ENTRIES, "visual_main", "rainbow", "enable_rainbow", true, "开启彩色特效");
        register(clientBuf, CLIENT_ENTRIES, "visual_main", "rainbow", "width_rainbow", 1.0, 0.0, 10.0, "彩色勾边宽度");
        clientBuf.pop();
        CLIENT_SPEC = clientBuf.build();
    }

    public static class ConfigEntry<T> {
        public final String major, sub, key;
        public final ForgeConfigSpec.ConfigValue<T> specValue;
        public final T defaultValue;
        public final double min, max;

        public ConfigEntry(String major, String sub, String key, ForgeConfigSpec.ConfigValue<T> val, T def, double min, double max) {
            this.major = major; this.sub = sub; this.key = "iron_spells_genesis.config." + key;
            this.specValue = val; this.defaultValue = def;
            this.min = min; this.max = max;
        }
    }

    private static void register(ForgeConfigSpec.Builder builder, List<ConfigEntry<?>> list, String major, String sub, String key, boolean def, String comment) {
        list.add(new ConfigEntry<>(major, sub, key, builder.comment(comment).define(key, def), def, 0, 0));
    }

    private static void register(ForgeConfigSpec.Builder builder, List<ConfigEntry<?>> list, String major, String sub, String key, double def, double min, double max, String comment) {
        list.add(new ConfigEntry<>(major, sub, key, builder.comment(comment).defineInRange(key, def, min, max), def, min, max));
    }

    public static void setup() {}

    public static void updateCache() {
        try {
            whisperDamage = (Double) find("whisper_damage", SERVER_ENTRIES).specValue.get();
            enableHardMode = (Boolean) find("enable_hard_mode", SERVER_ENTRIES).specValue.get();
            enableRed = (Boolean) find("enable_red", CLIENT_ENTRIES).specValue.get();
            widthRed = ((Double) find("width_red", CLIENT_ENTRIES).specValue.get()).floatValue();
            enableBlue = (Boolean) find("enable_blue", CLIENT_ENTRIES).specValue.get();
            widthBlue = ((Double) find("width_blue", CLIENT_ENTRIES).specValue.get()).floatValue();
            enableRainbow = (Boolean) find("enable_rainbow", CLIENT_ENTRIES).specValue.get();
            widthRainbow = ((Double) find("width_rainbow", CLIENT_ENTRIES).specValue.get()).floatValue();
        } catch (IllegalStateException ignored) {
        }
    }

    @SubscribeEvent
    public static void onConfigLoad(final ModConfigEvent.Loading event) { updateCache(); }

    @SubscribeEvent
    public static void onConfigReload(final ModConfigEvent.Reloading event) { updateCache(); }

    private static ConfigEntry<?> find(String key, List<ConfigEntry<?>> list) {
        return list.stream().filter(e -> e.key.endsWith(key)).findFirst().orElse(null);
    }
}