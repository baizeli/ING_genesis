package miku.united_as_one.genesis.client.render.luminous;

public enum GenesisEffect {
    // 定义顺序=内部索引顺序，性能最优
    BLACK_RED("black_red"),      // ordinal=0
    BLUE_WHITE("blue_white"),    // ordinal=1
    RAINBOW("rainbow");          // ordinal=2

    private static final int CACHE_SIZE = 3;
    private static final GenesisEffect[] ORDINAL_CACHE = values();
    private static final String[] ID_CACHE = new String[CACHE_SIZE];

    private final String id;

    GenesisEffect(String id) {
        this.id = id.intern();
    }

    public String getId() {
        return id;
    }
    public static GenesisEffect fromOrdinal(int ordinal) {
        return (ordinal >= 0 && ordinal < CACHE_SIZE) ? ORDINAL_CACHE[ordinal] : null;
    }

    public static int getEffectCount() {
        return CACHE_SIZE;
    }

    public static GenesisEffect[] getCachedValues() {
        return ORDINAL_CACHE;
    }

    public boolean isId(String testId) {
        return this.id == testId || this.id.equals(testId);
    }
}