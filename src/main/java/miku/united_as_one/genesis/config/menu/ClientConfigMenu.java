package miku.united_as_one.genesis.config.menu;
import miku.united_as_one.genesis.config.Configuration;
import net.minecraft.client.gui.screens.Screen;

public class ClientConfigMenu extends GenesisConfigScreen {
    public ClientConfigMenu(Screen parent) {
        super(parent, "iron_spells_genesis.config.client.title", Configuration.CLIENT_SPEC);
        buildAutoConfig(Configuration.CLIENT_ENTRIES);
    }
}