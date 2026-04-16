package miku.united_as_one.genesis.init.config.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;

public class ConfigValue {
    public final String key;
    public final Font font;
    public int width, height = 20, x, y;
    public boolean visible = true;
    public AbstractWidget valueWidget;
    public final StringWidget label;
    public final Button resetBtn;
    public Consumer<ConfigValue> resetValue;
    public Component description;

    public ConfigValue(Object parent, String key, Font font) {
        this.key = key; this.font = font;
        this.label = new StringWidget(Component.translatable(key), font);
        this.resetBtn = Button.builder(Component.literal("⟲"), b -> this.reset(this))
                .bounds(0, 0, 20, 20).build();
    }

    public void reset(ConfigValue ignored) {
        if (this.resetValue != null) {
            this.resetValue.accept(this);
        }
    }

    public void draw(GuiGraphics g, int x, int y, int mx, int my) {
        this.x = x; this.y = y;
        label.setPosition(x + 10, y + 5);
        resetBtn.setPosition(x + width - 30, y);
        if (valueWidget != null) valueWidget.setPosition(x + width - 140, y);

        label.render(g, mx, my, 0);
        resetBtn.render(g, mx, my, 0);
        if (valueWidget != null) valueWidget.render(g, mx, my, 0);

        // 渲染描述信息的 Tooltip
        if (description != null && isHoveringLabel(mx, my)) {
            g.renderTooltip(font, description, mx, my);
        }
    }

    private boolean isHoveringLabel(int mx, int my) {
        return mx >= x + 10 && mx <= x + width - 150 && my >= y && my <= y + height;
    }

    public boolean click(double mx, double my) {
        if (resetBtn.mouseClicked(mx, my, 0)) return true;
        return valueWidget != null && valueWidget.mouseClicked(mx, my, 0);
    }
    public boolean matchesSearch(String query) {
        if (query == null || query.isEmpty()) return true;
        return Component.translatable(key).getString().toLowerCase().contains(query.toLowerCase());
    }
}