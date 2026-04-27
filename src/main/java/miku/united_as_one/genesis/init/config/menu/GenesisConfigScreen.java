package miku.united_as_one.genesis.init.config.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class GenesisConfigScreen extends Screen {
    protected final Screen parent;
    protected final ForgeConfigSpec spec;
    protected final String modid = "iron_spells_genesis";
    protected final int leftWidth = 80;
    protected final int bottomHeight = 30;

    protected EditBox searchBox;
    protected ConfigArray configArray;
    protected List<MajorCategory> majorCategories = new ArrayList<>();
    protected MajorCategory currentMajor;

    public GenesisConfigScreen(Screen parent, String titleKey, ForgeConfigSpec spec) {
        super(Component.translatable(titleKey));
        this.parent = parent;
        this.spec = spec;
    }

    public class MajorCategory {
        public String nameKey;
        public List<GroupValue> subCategories = new ArrayList<>();
        public MajorCategory(String nameKey) { this.nameKey = nameKey; }

        public GroupValue addSub(String subNameKey) {
            GroupValue group = new GroupValue(GenesisConfigScreen.this, subNameKey, Minecraft.getInstance().font);
            group.setExpanded(false);
            subCategories.add(group);
            return group;
        }
    }

    public MajorCategory createMajor(String nameKey) {
        MajorCategory mc = new MajorCategory(nameKey);
        majorCategories.add(mc);
        if (currentMajor == null) currentMajor = mc;
        return mc;
    }

    @Override
    public void init() {
        this.clearWidgets();

        this.searchBox = new EditBox(this.font, 4, 8, leftWidth - 8, 14, Component.empty());
        this.searchBox.setResponder(s -> { if(configArray != null) configArray.setSearchQuery(s); });
        this.addRenderableWidget(this.searchBox);

        this.configArray = new ConfigArray(this.minecraft, this.width - leftWidth, this.height - bottomHeight - 5, 5, leftWidth);
        loadMajor(currentMajor);
        this.addRenderableWidget(this.configArray);
    }

    protected void loadMajor(MajorCategory mc) {
        this.currentMajor = mc;
        if (this.configArray != null && mc != null) {
            this.configArray.clear();
            for (GroupValue sub : mc.subCategories) this.configArray.push(sub);
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        // 搜索框焦点排他性管理
        if (this.searchBox != null) {
            if (mx >= 4 && mx <= leftWidth - 4 && my >= 8 && my <= 22) {
                this.searchBox.setFocused(true);
                if (this.configArray != null) this.configArray.unfocusAll();
            } else {
                this.searchBox.setFocused(false);
            }
        }

        if (mx > 0 && mx < leftWidth && my > 30 && my < this.height - bottomHeight) {
            int idx = (int) ((my - 30) / 20);
            if (idx >= 0 && idx < majorCategories.size()) {
                loadMajor(majorCategories.get(idx));
                return true;
            }
        }

        int y = this.height - 20;
        int doneX = this.width - font.width(Component.translatable(modid + ".config.done")) - 20;
        int resetX = doneX - font.width(Component.translatable(modid + ".config.reset_all")) - 20;
        int saveX = resetX - font.width(Component.translatable(modid + ".config.save")) - 20;

        if (my >= y - 5 && my <= y + 15) {
            if (mx >= doneX - 5) { this.close(); return true; }
            if (mx >= resetX - 5 && mx < doneX - 5) { this.resetAll(); return true; }
            if (mx >= saveX - 5 && mx < resetX - 5) { this.save(); return true; }
        }
        return super.mouseClicked(mx, my, button);
    }

    // ============ 新增：捕捉并向下传递键盘输入 ============
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox != null && this.searchBox.isFocused()) {
            if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        if (this.configArray != null && this.configArray.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.searchBox != null && this.searchBox.isFocused()) {
            if (this.searchBox.charTyped(codePoint, modifiers)) return true;
        }
        if (this.configArray != null && this.configArray.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public void render(@NotNull GuiGraphics g, int mx, int my, float pt) {
        g.fill(0, 0, leftWidth, this.height, 0xCC000000);
        g.fill(leftWidth, this.height - bottomHeight, this.width, this.height, 0x88000000);

        int catY = 30;
        for (MajorCategory mc : majorCategories) {
            int color = (mc == currentMajor) ? 0xFFFFFF : 0x888888;
            if (mx > 0 && mx < leftWidth && my > catY - 2 && my < catY + 12) color = 0xFFFFAA;
            g.drawString(this.font, Component.translatable(mc.nameKey), 6, catY, color, false);
            catY += 20;
        }

        int y = this.height - 20;
        String doneT = Component.translatable(modid + ".config.done").getString();
        String resetT = Component.translatable(modid + ".config.reset_all").getString();
        String saveT = Component.translatable(modid + ".config.save").getString();

        int doneX = this.width - font.width(doneT) - 20;
        int resetX = doneX - font.width(resetT) - 20;
        int saveX = resetX - font.width(saveT) - 20;

        g.drawString(font, saveT, saveX, y, (mx >= saveX - 5 && mx < resetX - 5 && my >= y - 5 && my <= y + 15) ? 0xFFFFAA : 0xFFFFFF);
        g.drawString(font, resetT, resetX, y, (mx >= resetX - 5 && mx < doneX - 5 && my >= y - 5 && my <= y + 15) ? 0xFFFFAA : 0xFFFFFF);
        g.drawString(font, doneT, doneX, y, (mx >= doneX - 5 && my >= y - 5 && my <= y + 15) ? 0xFFFFAA : 0xFFFFFF);

        super.render(g, mx, my, pt);
    }

    @Override public void renderBackground(GuiGraphics g) {}
    public void save() {
        for (MajorCategory mc : majorCategories) {
            for (GroupValue sub : mc.subCategories) {
                for (ConfigValue v : sub.group) v.saveToConfig();
            }
        }
        spec.save();
    }
    public void resetAll() {
        for (MajorCategory mc : majorCategories) {
            for (GroupValue sub : mc.subCategories) {
                for (ConfigValue v : sub.group) v.reset(null);
            }
        }
        this.save();
    }
    public void close() { this.save(); this.minecraft.setScreen(this.parent); }
}