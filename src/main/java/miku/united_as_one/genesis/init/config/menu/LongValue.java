package miku.united_as_one.genesis.init.config.menu;
import net.minecraft.client.gui.Font;
public class LongValue extends NumberValue {
    public LongValue(Object p, String k, Font f) { super(p, k, f); }
    @Override public Number parse(String s) { return Long.parseLong(s); }
}
