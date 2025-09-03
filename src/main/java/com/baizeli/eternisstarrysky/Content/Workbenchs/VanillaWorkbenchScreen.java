package com.baizeli.eternisstarrysky.Content.Workbenchs;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class VanillaWorkbenchScreen extends AbstractContainerScreen<VanillaWorkbenchMenu> {

    public VanillaWorkbenchScreen(VanillaWorkbenchMenu menu, Inventory inventory, Component component)
    {
        super(menu, inventory, component);
        this.imageWidth = 230;
        this.imageHeight = 280;
    }

    @Override
    protected void init()
    {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.inventoryLabelY = this.imageHeight - 92;
        this.inventoryLabelX = 30;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // 背景
        guiGraphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFFcccccc);

        // 边框
        renderFineBorder(guiGraphics, x, y, imageWidth, imageHeight);

        // 绘制9x9合成槽
        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                int slotX = x + 11 + col * 18;
                int slotY = y + 17 + row * 18;
                renderFineSlot(guiGraphics, slotX, slotY);
            }
        }

        // 结果槽
        renderResultSlot(guiGraphics, x + 187, y + 89);

        int ya = -5;
        // 背包槽
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                int slotX = x + 29 + col * 18;
                int slotY = y + 199 + ya + row * 18;
                renderFineSlot(guiGraphics, slotX, slotY);
            }
        }

        // 快捷栏
        for (int col = 0; col < 9; col++)
        {
            int slotX = x + 29 + col * 18;
            int slotY = y + 257 + ya;
            renderFineSlot(guiGraphics, slotX, slotY);
        }
    }

    private void renderFineBorder(GuiGraphics guiGraphics, int x, int y, int width, int height)
    {
        guiGraphics.fill(x, y, x + width, y + 1, 0xFF8B8B8B);
        guiGraphics.fill(x, y, x + 1, y + height, 0xFF8B8B8B);
        guiGraphics.fill(x + width - 1, y, x + width, y + height, 0xFF373737);
        guiGraphics.fill(x, y + height - 1, x + width, y + height, 0xFF373737);
    }

    private void renderFineSlot(GuiGraphics guiGraphics, int x, int y)
    {
        guiGraphics.fill(x + 1, y + 1, x + 17, y + 17, 0xFF8B8B8B);

        guiGraphics.fill(x, y, x + 18, y + 1, 0xFF373737);
        guiGraphics.fill(x, y, x + 1, y + 18, 0xFF373737);
        guiGraphics.fill(x + 17, y + 1, x + 18, y + 18, 0xFFFFFFFF);
        guiGraphics.fill(x + 1, y + 17, x + 18, y + 18, 0xFFFFFFFF);
    }

    private void renderResultSlot(GuiGraphics guiGraphics, int x, int y)
    {
        guiGraphics.fill(x + 1, y + 1, x + 17, y + 17, 0xFF9B9B6B);

        guiGraphics.fill(x, y, x + 18, y + 1, 0xFFB8860B);
        guiGraphics.fill(x, y, x + 1, y + 18, 0xFFB8860B);
        guiGraphics.fill(x + 17, y + 1, x + 18, y + 18, 0xFFFFD700);
        guiGraphics.fill(x + 1, y + 17, x + 18, y + 18, 0xFFFFD700);
    }

    @Override protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, 6, 0x404040, false);// 绘制标题
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY - 5, 0x404040, false);// 绘制背包名
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta)
    {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}