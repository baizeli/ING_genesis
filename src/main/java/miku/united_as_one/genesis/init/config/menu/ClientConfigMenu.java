package miku.united_as_one.genesis.init.config.menu;

import miku.united_as_one.genesis.init.config.Configuration;
import net.minecraft.client.gui.screens.Screen;

public class ClientConfigMenu extends GenesisConfigScreen {

    public ClientConfigMenu(Screen parent) {
        super(parent, "iron_spells_genesis.config.client.title", Configuration.CLIENT_SPEC);

        MajorCategory visual = createMajor("iron_spells_genesis.category.client.visual_main");

        visual.addSub("iron_spells_genesis.config.group.red")
                .add(Configuration.ENABLE_RED, "iron_spells_genesis.config.enable_red")
                .add(Configuration.WIDTH_RED, "iron_spells_genesis.config.width", 0, 10);

        visual.addSub("iron_spells_genesis.config.group.blue")
                .add(Configuration.ENABLE_BLUE, "iron_spells_genesis.config.enable_blue")
                .add(Configuration.WIDTH_BLUE, "iron_spells_genesis.config.width", 0, 10);

        visual.addSub("iron_spells_genesis.config.group.rainbow")
                .add(Configuration.ENABLE_RAINBOW, "iron_spells_genesis.config.enable_rainbow")
                .add(Configuration.WIDTH_RAINBOW, "iron_spells_genesis.config.width", 0, 10);
    }
}