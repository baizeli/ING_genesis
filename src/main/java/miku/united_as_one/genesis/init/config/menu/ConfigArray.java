package miku.united_as_one.genesis.init.config.menu;

import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraftforge.client.gui.widget.ScrollPanel;

import java.util.LinkedList;
import java.util.List;

public class ConfigArray extends ScrollPanel
{
    private final List<ConfigValue> allValues = new LinkedList<>();
    private final List<ConfigValue> filteredValues = new LinkedList<>();
    private int previousY = Integer.MIN_VALUE;
    private final int x;
    private String searchQuery = "";

    public ConfigArray(Minecraft client, int width, int height, int top, int left)
    {
        super(client, width, height, top, left);
        this.x = left;
    }

    @Override
    protected int getContentHeight()
    {
        int content = 0;
        for (ConfigValue value : this.filteredValues)
            if (value.visible)
                content += value.height + ConfigMenu.PADDING;
        return Math.max(content - ConfigMenu.PADDING, 0);
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY)
    {
        relativeY -= 4;
        if (this.height >= this.getContentHeight())
            relativeY = ConfigMenu.PADDING;
        this.previousY = relativeY;
        for (ConfigValue configValue : this.filteredValues)
        {
            if (!configValue.visible)
                continue;
            configValue.draw(guiGraphics, x, relativeY, mouseX, mouseY);
            relativeY += configValue.height + ConfigMenu.PADDING;
        }
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, Tesselator tess, float partialTick)
    {
        super.drawBackground(guiGraphics, tess, partialTick);
    }

    @Override
    public NarrationPriority narrationPriority()
    {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_)
    {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (this.click(mouseX, mouseY))
            return true;
        this.setFocused(null);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void push(ConfigValue value)
    {
        value.width = this.width;
        value.parent = this;
        this.allValues.add(value);
        this.applyFilter();
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
        this.applyFilter();
    }

    private void applyFilter() {
        this.filteredValues.clear();
        for (ConfigValue value : this.allValues) {
            if (value.matchesSearch(this.searchQuery)) {
                this.filteredValues.add(value);
            } else if (value instanceof GroupValue) {
                GroupValue group = (GroupValue) value;
                boolean hasMatch = false;
                for (ConfigValue item : group.group) {
                    if (item.matchesSearch(this.searchQuery)) {
                        hasMatch = true;
                        break;
                    }
                }
                if (hasMatch) {
                    this.filteredValues.add(value);
                }
            }
        }
    }

    public boolean click(double x, double y)
    {
        if (y < 6)
            return false;
        if (y > this.height + 6)
            return false;
        if (x < 6)
            return false;
        if (x > this.width + 6)
            return false;
        double y1 = y - this.previousY;
        int cy = 0;
        for (ConfigValue configValue : this.filteredValues)
        {
            if (!configValue.visible)
                continue;
            if (cy > y1)
                return false;
            if (cy + configValue.height >= y1)
            {
                return configValue.click(x, y);
            }
            cy += configValue.height + ConfigMenu.PADDING;
        }
        return false;
    }
}
