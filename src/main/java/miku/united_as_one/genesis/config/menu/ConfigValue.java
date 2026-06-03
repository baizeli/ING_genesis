package miku.united_as_one.genesis.config.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;

public class ConfigValue {
    public final String key;
    public final Font font;
    public int width, height = 24, x, y;
    public boolean visible = true;
    public AbstractWidget valueWidget;
    public final StringWidget label;
    public final Button resetBtn;

    public Consumer<ConfigValue> resetValue;
    public Runnable saveAction;
    public Component description;

    public ConfigValue(Object parent, String key, Font font) {
        this.key = key; this.font = font;
        this.label = new StringWidget(Component.translatable(key), font);
        this.resetBtn = Button.builder(Component.translatable("genesis_magic.config.reset"), b -> this.reset(this))
                .bounds(0, 0, 32, 18).build();
    }

    public void reset(ConfigValue ignored) {
        if (this.resetValue != null) this.resetValue.accept(this);
    }

    public void saveToConfig() {
        if (this.saveAction != null) this.saveAction.run();
    }

    public void draw(GuiGraphics g, int x, int y, int mx, int my) {
        this.x = x; this.y = y;
        label.setPosition(x + 10, y + 5);
        resetBtn.setPosition(x + width - 25, y + 1);
        if (valueWidget != null) {
            valueWidget.setPosition(x + width - 140, y + 1);
            try {
                java.lang.reflect.Field heightField = net.minecraft.client.gui.components.AbstractWidget.class.getDeclaredField("height");
                heightField.setAccessible(true);
                heightField.setInt(valueWidget, 18);
            } catch (Exception e) {
            }
        }

        label.render(g, mx, my, 0);
        resetBtn.render(g, mx, my, 0);
        if (valueWidget != null) valueWidget.render(g, mx, my, 0);

        if (description != null && isHoveringLabel(mx, my)) {
            g.renderTooltip(font, description, mx, my);
        }
    }

    private boolean isHoveringLabel(int mx, int my) {
        return mx >= x + 35 && mx <= x + width - 160 && my >= y && my <= y + height;
    }

    public boolean click(double mx, double my) {
        if (resetBtn.mouseClicked(mx, my, 0)) return true;
        if (valueWidget != null && valueWidget.mouseClicked(mx, my, 0)) {
            valueWidget.setFocused(true); // 确保点击后获得焦点，光标闪烁
            return true;
        }
        return false;
    }

    public boolean matchesSearch(String query) {
        if (query == null || query.isEmpty()) return true;
        return Component.translatable(key).getString().toLowerCase().contains(query.toLowerCase());
    }

    // ============ 新增：键盘事件向下路由 ============
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (valueWidget != null && valueWidget.isFocused()) {
            return valueWidget.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (valueWidget != null && valueWidget.isFocused()) {
            return valueWidget.charTyped(codePoint, modifiers);
        }
        return false;
    }

    public void setFocused(boolean focused) {
        if (valueWidget != null) {
            valueWidget.setFocused(focused);
        }
    }
}