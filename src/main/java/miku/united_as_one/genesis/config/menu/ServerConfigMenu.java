package miku.united_as_one.genesis.config.menu;
import miku.united_as_one.genesis.config.Configuration;
import net.minecraft.client.gui.screens.Screen;

public class ServerConfigMenu extends GenesisConfigScreen {
    public ServerConfigMenu(Screen parent) {
        super(parent, "iron_spells_genesis.config.server.title", Configuration.SERVER_SPEC);
        buildAutoConfig(Configuration.SERVER_ENTRIES);
    }
}