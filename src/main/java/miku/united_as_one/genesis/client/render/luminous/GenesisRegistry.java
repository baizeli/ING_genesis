package miku.united_as_one.genesis.client.render.luminous;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.item.Item;
import miku.united_as_one.genesis.registries.ItemRegistry;
import java.util.Map;

public final class GenesisRegistry {
    private static final Object2ObjectOpenHashMap<Item, GenesisEffect> ASSIGNMENTS;
    private static final GenesisEffect FALLBACK_EFFECT = null;

    private static volatile boolean initialized = false;

    private static final int INITIAL_CAPACITY = 16;

    static {
        ASSIGNMENTS = new Object2ObjectOpenHashMap<>(INITIAL_CAPACITY);
        ASSIGNMENTS.defaultReturnValue(FALLBACK_EFFECT);
    }

    public static void init() {
        if (initialized) return;

        synchronized (GenesisRegistry.class) {
            if (initialized) return;
            assignDirect(ItemRegistry.MITHRIL_SWORD.get(), GenesisEffect.BLUE_WHITE);
            assignDirect(ItemRegistry.MITHRIL_PICKAXE.get(), GenesisEffect.BLUE_WHITE);
            assignDirect(ItemRegistry.DISK_SPELL_BOOK.get(), GenesisEffect.BLUE_WHITE);
            assignDirect(ItemRegistry.TWISTED_CHAOS_INGOT.get(), GenesisEffect.BLACK_RED);
            assignDirect(ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), GenesisEffect.RAINBOW);

            initialized = true;
        }
    }

    private static void assignDirect(Item item, GenesisEffect effect) {
        ASSIGNMENTS.put(item, effect);
    }
    public static void assign(Item item, GenesisEffect effect) {
        if (item == null || effect == null) return;
        ASSIGNMENTS.put(item, effect);
    }
    public static GenesisEffect getTargetEffect(Item item) {
        return item == null ? FALLBACK_EFFECT : ASSIGNMENTS.get(item);
    }
    public static void assignAll(Map<Item, GenesisEffect> assignments) {
        if (assignments != null && !assignments.isEmpty()) {
            assignments.forEach(GenesisRegistry::assign);
        }
    }
    public static void clear() {
        ASSIGNMENTS.clear();
        initialized = false;
    }
    public static int getRegisteredItemCount() {
        return ASSIGNMENTS.size();
    }

    public static boolean isRegistered(Item item) {
        return item != null && ASSIGNMENTS.containsKey(item);
    }
}