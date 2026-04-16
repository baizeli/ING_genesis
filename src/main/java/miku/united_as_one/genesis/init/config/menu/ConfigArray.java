package miku.united_as_one.genesis.init.config.menu;

import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import java.util.LinkedList;
import java.util.List;

public class ConfigArray extends ScrollPanel {
    private final List<ConfigValue> allValues = new LinkedList<>();
    private final List<ConfigValue> filteredValues = new LinkedList<>();
    private int lastY = 0;
    private String searchQuery = "";

    public ConfigArray(Minecraft mc, int w, int h, int top, int left) {
        super(mc, w, h, top, left);
    }
    public void setSearchQuery(String query) {
        this.searchQuery = query;
        this.applyFilter();
    }

    private void applyFilter() {
        this.filteredValues.clear();
        for (ConfigValue v : allValues) {
            if (v instanceof GroupValue group) {
                boolean childMatch = false;
                for (ConfigValue child : group.group) {
                    if (child.matchesSearch(searchQuery)) { childMatch = true; break; }
                }
                if (v.matchesSearch(searchQuery) || childMatch) filteredValues.add(v);
            } else {
                if (v.matchesSearch(searchQuery)) filteredValues.add(v);
            }
        }
    }

    @Override protected int getContentHeight() {
        int h = 0;
        for (ConfigValue v : filteredValues) if (v.visible) h += v.height + 8;
        return Math.max(h, 0);
    }

    @Override protected void drawPanel(GuiGraphics g, int r, int y, Tesselator t, int mx, int my) {
        y -= 4; this.lastY = y;
        for (ConfigValue v : filteredValues) {
            if (!v.visible) continue;
            v.draw(g, this.left, y, mx, my);
            y += v.height + 8;
        }
    }

    @Override public boolean mouseClicked(double mx, double my, int b) {
        double ty = my - this.lastY;
        int curr = 0;
        for (ConfigValue v : filteredValues) {
            if (!v.visible) continue;
            if (curr <= ty && curr + v.height >= ty) return v.click(mx, my);
            curr += v.height + 8;
        }
        return super.mouseClicked(mx, my, b);
    }

    public void push(ConfigValue v) {
        v.width = this.width;
        allValues.add(v);
        applyFilter();
    }

    @Override public NarrationPriority narrationPriority() { return NarrationPriority.NONE; }
    @Override public void updateNarration(NarrationElementOutput o) {}
}