package miku.united_as_one.genesis.api.helper;

import java.util.Arrays;
import java.util.List;

public class ColorHelper {
    // 基础颜色列表
    public static final List<Integer> NULL = List.of(16777215);

    // 雷霆主题 - 蓝紫色渐变
    public static final List<Integer> THUNDER_THEME = Arrays.asList(
            65280, 65407, 65535, 32767, 255, 4129023, 8323327, 16711935
    );

    // 神圣主题 - 金黄色渐变
    public static final List<Integer> HOLY_THEME = Arrays.asList(
            16764672, 16764672, 16777215, 16764672
    );

    // 鲜血主题 - 红色渐变
    public static final List<Integer> BLOOD_THEME = Arrays.asList(
            16711680, 16729344, 16747520, 16766720, 16711680
    );

    // 星辰主题 - 全颜色随机（使用特殊标记，渲染时处理）
    public static final List<Integer> STELLAR_THEME = Arrays.asList(
            16711680, 16744192, 16776960, 8388352, 65280, 65407, 65535, 32767, 255, 4129023, 8323327, 16711935, 16711807, 16711680
    );

    // 其他预设颜色主题
    public static final List<Integer> PASTEL_CANDY_THEME = Arrays.asList(
            16763110, 16767153, 16645526, 13303743, 10221311, 12432127, 16762623, 16763110
    );

    public static final List<Integer> FIRE_THEME = Arrays.asList(
            16711680, 16729344, 16747520, 16766720, 16711680
    );

    public static final List<Integer> ICE_THEME = Arrays.asList(
            65535, 49151, 2003199, 8900346, 11393254, 65535
    );

    public static final List<Integer> NATURE_THEME = Arrays.asList(
            3329330, 2263842, 8190976, 11403055, 3329330
    );

    public static final List<Integer> PURPLE_DREAM = Arrays.asList(
            6948210, 10904524, 13211340, 15250635, 16764117, 6948210
    );

    public static final List<Integer> RAINBOW = Arrays.asList(
            16711680, 16744192, 16776960, 8388352, 65280, 65407, 65535, 32767, 255, 4129023, 8323327, 16711935, 16711807, 16711680
    );
}
