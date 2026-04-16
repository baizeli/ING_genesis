package miku.united_as_one.genesis.init.config.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.network.chat.Component;

import java.util.LinkedList;
import java.util.List;

public class GroupValue extends ConfigValue
{
    public List<ConfigValue> group = new LinkedList<>();
    private boolean expanded = false;

    public GroupValue(AbstractContainerEventHandler parent, String key, Font font)
    {
        super(parent, key, font);
        this.resetEnable = false;
        this.height = 28; // Slightly taller for group headers
    }

    @Override
    public void update()
    {
        this.text.setWidth(this.width - 40);
        this.text.alignLeft();
        String prefix = this.expanded ? "▼ " : "▶ ";
        this.text.setMessage(Component.literal(prefix).append(Component.translatable(this.key)));
    }

    @Override
    public void clickText()
    {
        this.expanded = !this.expanded;
        for (ConfigValue value : this.group)
            value.visible(this.expanded);
    }

    @Override
    public void visible(boolean visible)
    {
        super.visible(visible);
        for (ConfigValue value : this.group)
            value.visible(visible && this.expanded);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        for (ConfigValue value : this.group)
            value.visible(expanded);
        update();
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    @Override
    public boolean matchesSearch(String query) {
        if (super.matchesSearch(query)) {
            return true;
        }
        for (ConfigValue value : this.group) {
            if (value.matchesSearch(query)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(GuiGraphics graphics, int x, int y, int mouseX, int mouseY)
    {
        // Draw group header with background
        graphics.fill(x, y, x + this.width, y + this.height, 0x40FFFFFF);

        // Draw separator line
        graphics.fill(x, y + this.height - 1, x + this.width, y + this.height, 0xFF888888);

        super.draw(graphics, x, y, mouseX, mouseY);
    }
}
