package miku.united_as_one.genesis.init.config.menu;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.config.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ServerConfigMenu extends Screen {
    public static final int PADDING = 8;
    public static final int HEADER_HEIGHT = 24;
    private final Screen parent;

    public final GroupValue GROUP_COMBAT;
    public final DoubleValue WHISPER_DAMAGE_UI;

    private EditBox searchBox;
    private String searchQuery = "";
    private ConfigArray configArray;

    public ServerConfigMenu(Screen parent) {
        super(Component.translatable(Genesis.MOD_ID + ".config.server.title"));
        this.parent = parent;
        Minecraft mc = Minecraft.getInstance();

        Configuration.setup();
        this.GROUP_COMBAT = new GroupValue(this, Genesis.MOD_ID + ".config.group.combat", mc.font);
        this.GROUP_COMBAT.setExpanded(true); // 默认展开

        this.WHISPER_DAMAGE_UI = new DoubleValue(this, Genesis.MOD_ID + ".config.whisper_damage", mc.font);
        this.WHISPER_DAMAGE_UI.min = 0.0;
        this.WHISPER_DAMAGE_UI.max = 1000.0;
        this.WHISPER_DAMAGE_UI.value(Configuration.WHISPER_DAMAGE.get());
        this.WHISPER_DAMAGE_UI.resetValue = (val) -> ((DoubleValue) val).value(Configuration.WHISPER_DAMAGE.getDefault());
        this.WHISPER_DAMAGE_UI.description = Component.translatable(Genesis.MOD_ID + ".config.whisper_damage.desc");

        this.GROUP_COMBAT.group.add(this.WHISPER_DAMAGE_UI);
    }

    @Override
    public void init() {
        this.clearWidgets();

        int buttonWidth = 150;
        int buttonHeight = 20;
        int buttonY = this.height - PADDING - buttonHeight;

        // 底部功能按钮
        this.addRenderableWidget(Button.builder(Component.translatable(Genesis.MOD_ID + ".config.save"), b -> this.save())
                .bounds(PADDING, buttonY, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(Component.translatable(Genesis.MOD_ID + ".config.done"), b -> this.close())
                .bounds(this.width - PADDING - buttonWidth, buttonY, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(Component.translatable(Genesis.MOD_ID + ".config.reset_all"), b -> this.resetAll())
                .bounds((this.width - buttonWidth) / 2, buttonY, buttonWidth, buttonHeight).build());

        // 滚动面板设置
        int arrayTop = HEADER_HEIGHT + PADDING * 2;
        int arrayHeight = this.height - arrayTop - buttonHeight - PADDING * 2;
        this.configArray = new ConfigArray(this.minecraft, this.width - PADDING * 2, arrayHeight, arrayTop, PADDING);

        // --- 将配置分组推送到滚动面板 ---
        this.configArray.push(this.GROUP_COMBAT);

        this.addRenderableWidget(this.configArray);

        // 搜索框设置
        int searchBoxWidth = 200;
        this.searchBox = new EditBox(this.font, (this.width - searchBoxWidth) / 2, PADDING + 2, searchBoxWidth, 18, Component.empty());
        this.searchBox.setValue(this.searchQuery);
        this.searchBox.setResponder(query -> {
            this.searchQuery = query;
            if (this.configArray != null) this.configArray.setSearchQuery(query);
        });
        this.addWidget(this.searchBox);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, PADDING, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) this.minecraft.setScreen(this.parent);
    }

    public void save() {
        // --- 将 UI 的值写回后台 Configuration 对象 ---
        Configuration.WHISPER_DAMAGE.set(this.WHISPER_DAMAGE_UI.value.doubleValue());

        // 保存到磁盘文件 (.toml)
        Configuration.SPECIFICATION.save();
    }

    public void resetAll() {
        // 触发 UI 控件的重置
        this.WHISPER_DAMAGE_UI.reset(null);
        this.save();
    }

    public void close() {
        this.save();
        this.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}