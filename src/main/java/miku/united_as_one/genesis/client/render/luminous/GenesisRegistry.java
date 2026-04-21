package miku.united_as_one.genesis.client.render.luminous;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import miku.united_as_one.genesis.init.registry.ItemRegistry;
import java.util.HashMap;
import java.util.Map;

public class GenesisRegistry {
    private static final Map<Item, GenesisEffect> ASSIGNMENTS = new HashMap<>();
    private static final GenesisEffect FALLBACK = null;

    //这里添加物品
    public static void init() {
        assign(ItemRegistry.MITHRIL_SWORD.get(), GenesisEffect.BLUE_WHITE);
        assign(ItemRegistry.MITHRIL_PICKAXE.get(), GenesisEffect.BLUE_WHITE);
        assign(ItemRegistry.TWISTED_CHAOS_INGOT.get(), GenesisEffect.BLACK_RED);
        assign(ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), GenesisEffect.RAINBOW);
    }

    public static void assign(Item item, GenesisEffect effect) {
        if (item != null && effect != null) {
            ASSIGNMENTS.put(item, effect);
        }
    }

    public static GenesisEffect getTargetEffect(Item item) {
        return ASSIGNMENTS.getOrDefault(item, FALLBACK);
    }
}