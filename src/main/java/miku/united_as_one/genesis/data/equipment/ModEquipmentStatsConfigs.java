package miku.united_as_one.genesis.data.equipment;

import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import miku.united_as_one.genesis.Genesis;

public final class ModEquipmentStatsConfigs {
    public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(Genesis.rl("config"), 1);
    public static final ConfigTypeEntry<EquipmentStatsConfig> EQUIPMENT_STATS =
            new ConfigTypeEntry<>(HANDLER, "equipment_stats", EquipmentStatsConfig.class);

    static {
        HANDLER.addAfterReloadListener(EquipmentStatsManager::rebuildFromConfig);
    }

    private ModEquipmentStatsConfigs() {
    }

    public static void init() {
    }
}
