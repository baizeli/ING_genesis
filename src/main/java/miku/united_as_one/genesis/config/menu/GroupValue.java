package miku.united_as_one.genesis.config.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.LinkedList;
import java.util.List;

public class GroupValue extends ConfigValue {
    public final List<ConfigValue> group = new LinkedList<>();
    private boolean expanded = false;

    public GroupValue(Object parent, String key, Font font) {
        super(parent, key, font);
        this.height = 20; // 缩小折叠标题的高度
    }

    public GroupValue add(ForgeConfigSpec.DoubleValue spec, String key, double min, double max) {
        DoubleValue v = new DoubleValue(null, key, Minecraft.getInstance().font);
        v.min = min; v.max = max; v.value(spec.get());
        v.resetValue = val -> ((DoubleValue)val).value(spec.getDefault());
        v.saveAction = () -> spec.set(v.value.doubleValue());
        v.description = Component.translatable(key + ".desc");
        group.add(v);
        return this;
    }

    public GroupValue add(ForgeConfigSpec.BooleanValue spec, String key) {
        BooleanValue v = new BooleanValue(null, key, Minecraft.getInstance().font);
        v.value(spec.get());
        v.resetValue = val -> ((BooleanValue)val).value(spec.getDefault());
        v.saveAction = () -> spec.set(v.state);
        v.description = Component.translatable(key + ".desc");
        group.add(v);
        return this;
    }

    public GroupValue add(ForgeConfigSpec.LongValue spec, String key, long min, long max) {
        LongValue v = new LongValue(null, key, Minecraft.getInstance().font);
        v.min = min; v.max = max; v.value(spec.get());
        v.resetValue = val -> ((LongValue)val).value(spec.getDefault());
        v.saveAction = () -> spec.set(v.value.longValue());
        v.description = Component.translatable(key + ".desc");
        group.add(v);
        return this;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() { return this.expanded; }

    @Override
    public void draw(GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        this.x = x; this.y = y;

        String prefix = this.expanded ? "-  " : "+  ";
        Component titleText = Component.literal(prefix).append(Component.translatable(this.key));

        int color = (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) ? 0xFFFFAA : 0xDDDDDD;
        if (this.expanded) color = 0xFFFFFF;

        graphics.drawString(this.font, titleText, x + 5, y + 6, color, false);
    }

    @Override
    public boolean click(double mouseX, double mouseY) {
        this.setExpanded(!this.expanded);
        return true;
    }
}