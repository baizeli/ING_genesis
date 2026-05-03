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
        super(Component.translatable(Genesis.MOD_ID + ".config.title"));
        this.parent = parent;
    }

    @Override
    public void init() {
        this.clearWidgets();
        int buttonWidth = 150;
        int bottomButtonWidth = 200;
        int buttonHeight = 20;
        int gap = 8;
        int startY = this.height / 2 + 10;

        this.addRenderableWidget(Button.builder(Component.translatable(Genesis.MOD_ID + ".config.client"), b -> this.minecraft.setScreen(new ClientConfigMenu(this)))
                .bounds(this.width / 2 - buttonWidth - (gap / 2), startY, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(Component.translatable(Genesis.MOD_ID + ".config.server"), b -> this.minecraft.setScreen(new ServerConfigMenu(this)))
                .bounds(this.width / 2 + (gap / 2), startY, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(Component.translatable(Genesis.MOD_ID + ".config.github"), b -> {
            this.minecraft.setScreen(new ConfirmLinkScreen(open -> {
                if (open) Util.getPlatform().openUri(GITHUB_LINK);
                this.minecraft.setScreen(this);
            }, GITHUB_LINK, true));
        }).bounds(this.width / 2 - (bottomButtonWidth / 2), startY + buttonHeight + gap, bottomButtonWidth, buttonHeight).build());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, this.width, this.height, 0x60000000);
        int titleY = 50;

        graphics.pose().pushPose();
        graphics.pose().scale(2.0F, 2.0F, 2.0F);
        graphics.drawCenteredString(this.font, this.title, this.width / 4, titleY / 2, 0xFFFFFF);
        graphics.pose().popPose();

        graphics.drawCenteredString(this.font, Component.translatable(Genesis.MOD_ID + ".config.subtitle"), this.width / 2, titleY + 25, 0xFFFFFF);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {}

    @Override
    public void onClose() {
        if (this.minecraft != null) this.minecraft.setScreen(this.parent);
    }
}