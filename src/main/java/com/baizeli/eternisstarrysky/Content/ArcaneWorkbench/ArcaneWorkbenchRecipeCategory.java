package com.baizeli.eternisstarrysky.Content.ArcaneWorkbench;

import com.baizeli.eternisstarrysky.Items.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import static com.baizeli.eternisstarrysky.Content.ArcaneWorkbench.ArcaneWorkbenchBlockEntity.ARCANE_WORKBENCH_COMPONENT;
import static com.baizeli.eternisstarrysky.EternisStarrySky.MODID;

public class ArcaneWorkbenchRecipeCategory implements IRecipeCategory<ArcaneWorkbenchRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MODID, "arcane_workbench");
    private static final ResourceLocation CRAFTING_TABLE_LOCATION = new ResourceLocation(MODID, "textures/gui/container/arcane_workbench.png");
    public static final RecipeType<ArcaneWorkbenchRecipe> ARCANE_WORKBENCH_RECIPE_TYPE = new RecipeType<>(UID, ArcaneWorkbenchRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ArcaneWorkbenchRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(CRAFTING_TABLE_LOCATION, 0, 0, 176, 119);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.AVARITIA_SWORD.get()));
    }

    @Override
    public RecipeType<ArcaneWorkbenchRecipe> getRecipeType() {
        return ARCANE_WORKBENCH_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return ARCANE_WORKBENCH_COMPONENT;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Nullable
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ArcaneWorkbenchRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int recipeWidth = recipe.getWidth();
        int recipeHeight = recipe.getHeight();
        ItemStack result = recipe.getResultItem(null);

        
        create5x5RecipeLayout(builder, ingredients, recipeWidth, recipeHeight, result);
    }

    
    private void create5x5RecipeLayout(IRecipeLayoutBuilder builder,
                                       NonNullList<Ingredient> ingredients,
                                       int recipeWidth, int recipeHeight,
                                       ItemStack result) {
        
        int offsetX = (5 - recipeWidth) / 2;
        int offsetY = (5 - recipeHeight) / 2;

        
        for (int recipeY = 0; recipeY < recipeHeight; recipeY++) {
            for (int recipeX = 0; recipeX < recipeWidth; recipeX++) {
                int ingredientIndex = recipeY * recipeWidth + recipeX;

                if (ingredientIndex < ingredients.size()) {
                    Ingredient ingredient = ingredients.get(ingredientIndex);

                    if (!ingredient.isEmpty()) {
                        
                        int slotX = 8 + (offsetX + recipeX) * 18;
                        int slotY = 18 + (offsetY + recipeY) * 18;

                        builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                                .addIngredients(ingredient);
                    }
                }
            }
        }

        
        int outputSlotX = 139;
        int outputSlotY = 54;

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlotX, outputSlotY)
                .addItemStack(result)
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    
                    tooltip.add(Component.translatable("jei.tooltip.recipe.arcane_workbench"));
                });
    }
}