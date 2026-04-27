package miku.united_as_one.genesis.init.config.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class BooleanValue extends ConfigValue {
    public boolean state;

    public BooleanValue(Object parent, String key, Font font) {
        super(parent, key, font);
        this.valueWidget = Button.builder(Component.empty(), b -> this.toggle())
                .bounds(0, 0, 100, 20).build();
    }

    public void toggle() {
        this.value(!this.state);
    }

    public void value(boolean val) {
        this.state = val;
        Component text = Component.translatable(val ? "gui.genesis.on" : "gui.genesis.off");
        ((Button) this.valueWidget).setMessage(text);
    }
}