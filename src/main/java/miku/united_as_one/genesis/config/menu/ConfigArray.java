package miku.united_as_one.genesis.config.menu;

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

    public void clear() {
        this.allValues.clear();
        this.applyFilter();
    }

    public void applyFilter() {
        this.filteredValues.clear();
        boolean hasSearch = searchQuery != null && !searchQuery.isEmpty();

        for (ConfigValue v : allValues) {
            if (v instanceof GroupValue group) {
                boolean childMatch = false;
                for (ConfigValue child : group.group) {
                    if (child.matchesSearch(searchQuery)) {
                        childMatch = true;
                        break;
                    }
                }

                if (v.matchesSearch(searchQuery) || childMatch) {
                    group.width = this.width - 15;
                    filteredValues.add(group);

                    boolean shouldExpand = group.isExpanded() || (hasSearch && childMatch);

                    if (shouldExpand) {
                        for (ConfigValue child : group.group) {
                            if (child.matchesSearch(searchQuery)) {
                                child.width = this.width - 15;
                                filteredValues.add(child);
                            }
                        }
                    }
                }
            } else {
                if (v.matchesSearch(searchQuery)) {
                    v.width = this.width - 15;
                    filteredValues.add(v);
                }
            }
        }
    }

    @Override protected int getContentHeight() {
        int h = 0;
        for (ConfigValue v : filteredValues) if (v.visible) h += v.height + 4;
        return Math.max(h, 0);
    }

    protected void drawBackground() {}
    protected void drawBackground(GuiGraphics graphics) {}
    protected void drawBackground(GuiGraphics graphics, Tesselator tessellator) {}

    @Override protected void drawPanel(GuiGraphics g, int r, int y, Tesselator t, int mx, int my) {
        y += 5;
        this.lastY = y;
        for (ConfigValue v : filteredValues) {
            if (!v.visible) continue;
            v.draw(g, this.left, y, mx, my);
            y += v.height + 4;
        }
    }

    @Override public boolean mouseClicked(double mx, double my, int b) {
        double ty = my - this.lastY;
        int curr = 0;
        boolean handled = false;
        boolean needsRefilter = false;
        for (ConfigValue v : filteredValues) {
            if (!v.visible) continue;
            if (!handled && curr <= ty && curr + v.height >= ty) {
                if (v.click(mx, my)) {
                    handled = true;
                    if (v instanceof GroupValue) {
                        needsRefilter = true;
                    }
                } else {
                    v.setFocused(false);
                }
            } else {
                v.setFocused(false);
            }
            curr += v.height + 4;
        }
        if (needsRefilter) {
            this.applyFilter();
        }

        if (handled) return true;
        return super.mouseClicked(mx, my, b);
    }

    public void push(ConfigValue v) {
        v.width = this.width - 15;
        allValues.add(v);
        this.applyFilter();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ConfigValue v : filteredValues) {
            if (v.visible && v.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for (ConfigValue v : filteredValues) {
            if (v.visible && v.charTyped(codePoint, modifiers)) return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    public void unfocusAll() {
        for (ConfigValue v : allValues) v.setFocused(false);
    }

    @Override public NarrationPriority narrationPriority() { return NarrationPriority.NONE; }
    @Override public void updateNarration(NarrationElementOutput o) {}
}