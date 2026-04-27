package miku.united_as_one.genesis.init.config.menu;

import miku.united_as_one.genesis.init.config.Configuration;
import net.minecraft.client.gui.screens.Screen;

public class ServerConfigMenu extends GenesisConfigScreen {

    public ServerConfigMenu(Screen parent) {
        super(parent, "iron_spells_genesis.config.server.title", Configuration.SERVER_SPEC);

        MajorCategory combat = createMajor("iron_spells_genesis.category.server.combat_main");

        combat.addSub("iron_spells_genesis.config.group.combat")
                .add(Configuration.WHISPER_DAMAGE, "iron_spells_genesis.config.whisper_damage", 0, 1000)
                .add(Configuration.ENABLE_HARD_MODE, "iron_spells_genesis.config.enable_hard_mode");
    }
}