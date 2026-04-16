package miku.united_as_one.genesis.init.config;

import miku.united_as_one.genesis.init.config.menu.ConfigMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import java.util.function.BiFunction;

public class ConfigurationFactory implements BiFunction<Minecraft, Screen, Screen> {
    @Override
    public Screen apply(Minecraft mc, Screen lastScreen) {
        return new ConfigMenu(lastScreen);
    }
}