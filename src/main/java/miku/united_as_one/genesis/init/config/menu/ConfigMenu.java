package miku.united_as_one.genesis.init.config.menu;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ConfigMenu extends Screen {
    private final Screen parent;

    private static final String GITHUB_LINK = "https://github.com/baizeli/ING_genesis";

    public ConfigMenu(Screen parent) {
        // 主标题
        super(Component.translatable(Genesis.MOD_ID + ".config.title"));
        this.parent = parent;
    }

    @Override
    public void init() {
        this.clearWidgets();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 30;

        int startY = (this.height / 2) - ((buttonHeight * 3 + spacing * 2) / 2);
        int centerX = (this.width - buttonWidth) / 2;

        this.addRenderableWidget(Button.builder(
                Component.translatable(Genesis.MOD_ID + ".config.client"),
                b -> {
                }
        ).bounds(centerX, startY, buttonWidth, buttonHeight).build());

        // 2. 服务端配置按钮 (中间)
        this.addRenderableWidget(Button.builder(
                Component.translatable(Genesis.MOD_ID + ".config.server"),
                b -> {
                    // 跳转到原先写好的详细配置界面
                    this.minecraft.setScreen(new ServerConfigMenu(this));
                }
        ).bounds(centerX, startY + spacing, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable(Genesis.MOD_ID + ".config.github"),
                b -> {
                    ConfirmLinkScreen confirmLinkScreen = new ConfirmLinkScreen(
                            (open) -> {
                                if (open) {
                                    Util.getPlatform().openUri(GITHUB_LINK);
                                }
                                this.minecraft.setScreen(this);
                            }, GITHUB_LINK, true
                    );
                    this.minecraft.setScreen(confirmLinkScreen);
                }
        ).bounds(centerX, startY + spacing * 2, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        } else {
            super.onClose();
        }
    }
}