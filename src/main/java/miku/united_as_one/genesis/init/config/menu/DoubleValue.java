package miku.united_as_one.genesis.init.config.menu;
import net.minecraft.client.gui.Font;
public class DoubleValue extends NumberValue {
    public DoubleValue(Object p, String k, Font f) { super(p, k, f); }
    @Override public Number parse(String s) { return Double.parseDouble(s); }
}