package com.baizeli.eternisstarrysky.Content.Workbenchs;

import com.baizeli.eternisstarrysky.Content.ModBlock;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class VanillaWorkbenchRecipeCategory implements IRecipeCategory<VanillaWorkbenchRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(EternisStarrySky.MOD_ID, "vanilla_workbench");
    public static final RecipeType<VanillaWorkbenchRecipe> RECIPE_TYPE = new RecipeType<>(UID, VanillaWorkbenchRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public VanillaWorkbenchRecipeCategory(IGuiHelper helper) {
        // 增大背景以容纳更多槽位或使用滚动
        this.background = helper.createBlankDrawable(200, 120);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlock.workbench.get()));
    }

    @Override
    public RecipeType<VanillaWorkbenchRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.category.eternisstarrysky.vanilla_workbench");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, VanillaWorkbenchRecipe recipe, IFocusGroup focuses) {
        if (recipe.shaped) {
            int maxDisplaySize = Math.min(9, WorkbenchConfig.GRID_SIZE);
            int displayWidth = Math.min(recipe.width, maxDisplaySize);
            int displayHeight = Math.min(recipe.height, maxDisplaySize);

            for (int row = 0; row < displayHeight; row++) {
                for (int col = 0; col < displayWidth; col++) {
                    int recipeIndex = col + row * recipe.width;
                    if (recipeIndex < recipe.getIngredients().size()) {
                        Ingredient ingredient = recipe.getIngredients().get(recipeIndex);
                        if (!ingredient.isEmpty()) {
                            builder.addSlot(RecipeIngredientRole.INPUT, 1 + col * 18, 1 + row * 18)
                                    .addIngredients(ingredient);
                        }
                    }
                }
            }
        } else {
            int cols = Math.min(9, (int) Math.ceil(Math.sqrt(recipe.getIngredients().size())));
            for (int i = 0; i < recipe.getIngredients().size() && i < 81; i++) { // 最多显示25个
                Ingredient ingredient = recipe.getIngredients().get(i);
                if (!ingredient.isEmpty()) {
                    int col = i % cols;
                    int row = i / cols;
                    builder.addSlot(RecipeIngredientRole.INPUT, 1 + col * 18, 1 + row * 18)
                            .addIngredients(ingredient);
                }
            }
        }

        // 添加结果槽位
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 40).addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(VanillaWorkbenchRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // 绘制背景
        guiGraphics.fill(0, 0, 200, 120, 0xFFF0F0F0);

        // 绘制网格背景
        int maxDisplaySize = Math.min(5, WorkbenchConfig.GRID_SIZE);
        if (recipe.shaped) {
            int displayWidth = Math.min(recipe.width, maxDisplaySize);
            int displayHeight = Math.min(recipe.height, maxDisplaySize);

            for (int row = 0; row < displayHeight; row++) {
                for (int col = 0; col < displayWidth; col++) {
                    drawSlotBackground(guiGraphics, col * 18, row * 18);
                }
            }
        } else {
            int cols = Math.min(5, (int) Math.ceil(Math.sqrt(recipe.getIngredients().size())));
            int rows = (int) Math.ceil((double) Math.min(recipe.getIngredients().size(), 25) / cols);

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    drawSlotBackground(guiGraphics, col * 18, row * 18);
                }
            }
        }

        // 绘制结果槽位背景
        drawSlotBackground(guiGraphics, 139, 39);

        // 如果配方太大，显示提示文本
        if (recipe.shaped && (recipe.width > maxDisplaySize || recipe.height > maxDisplaySize)) {
            guiGraphics.drawString(Minecraft.getInstance().font,
                    "Full recipe: " + recipe.width + "x" + recipe.height,
                    1, 110, 0xFF666666, false);
        }
    }

    private void drawSlotBackground(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + 18, y + 18, 0xFF8B8B8B);
        guiGraphics.fill(x, y, x + 18, y + 1, 0xFF373737);
        guiGraphics.fill(x, y, x + 1, y + 18, 0xFF373737);
        guiGraphics.fill(x + 17, y + 1, x + 18, y + 18, 0xFFFFFFFF);
        guiGraphics.fill(x + 1, y + 17, x + 18, y + 18, 0xFFFFFFFF);
    }
}
