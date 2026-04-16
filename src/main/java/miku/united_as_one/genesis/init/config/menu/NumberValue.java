package miku.united_as_one.genesis.init.config.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public abstract class NumberValue extends ConfigValue {
    public Number value, min, max;
    public NumberValue(Object p, String k, Font f) {
        super(p, k, f);
        EditBox box = new EditBox(f, 0, 0, 100, 20, Component.empty());
        box.setResponder(s -> { try {
            Number v = parse(s);
            if(v.doubleValue() >= min.doubleValue() && v.doubleValue() <= max.doubleValue()) value = v;
        } catch(Exception e){} });
        this.valueWidget = box;
    }
    public abstract Number parse(String s);
    public void value(Number n) { this.value = n; ((EditBox)valueWidget).setValue(String.valueOf(n)); }
}