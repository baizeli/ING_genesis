package miku.united_as_one.genesis.init.config.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import java.util.LinkedList;
import java.util.List;

public class GroupValue extends ConfigValue {
    public final List<ConfigValue> group = new LinkedList<>();
    private boolean expanded = false;

    public GroupValue(Object parent, String key, Font font) {
        super(parent, key, font);
        this.resetValue = null;
        this.height = 24;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        for (ConfigValue v : group) {
            v.visible = expanded;
        }
    }

    public boolean isExpanded() {
        return this.expanded;
    }
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        graphics.fill(x, y, x + width, y + height, 0x40FFFFFF);

        graphics.fill(x, y + height - 1, x + width, y + height, 0xFF888888);

        String prefix = this.expanded ? "▼ " : "▶ ";
        Component titleText = Component.literal(prefix).append(Component.translatable(this.key));

        graphics.drawString(this.font, titleText, x + 5, y + (height - 8) / 2, 0xFFFFFF);
    }

    @Override
    public boolean click(double mouseX, double mouseY) {
        // 如果点击位置在分组标题的高度范围内
        this.setExpanded(!this.expanded);
        return true;
    }

    @Override
    public boolean matchesSearch(String query) {
        if (super.matchesSearch(query)) {
            return true;
        }

        for (ConfigValue v : group) {
            if (v.matchesSearch(query)) {
                return true;
            }
        }
        return false;
    }
}