package miku.united_as_one.genesis.contents.workbench.arcane;

import miku.united_as_one.genesis.registries.ItemRegistry;
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

import static miku.united_as_one.genesis.contents.workbench.arcane.ArcaneWorkbenchBlockEntity.ARCANE_WORKBENCH_COMPONENT;
import static miku.united_as_one.genesis.Genesis.MODID;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.ARCANE_ESSENCE;

public class ArcaneWorkbenchRecipeCategory implements IRecipeCategory<ArcaneWorkbenchRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MODID, "arcane_workbench");
    private static final ResourceLocation CRAFTING_TABLE_LOCATION = new ResourceLocation(MODID, "textures/gui/jei/arcane_workbench_jei.png");
    public static final RecipeType<ArcaneWorkbenchRecipe> ARCANE_WORKBENCH_RECIPE_TYPE = new RecipeType<>(UID, ArcaneWorkbenchRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ArcaneWorkbenchRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(CRAFTING_TABLE_LOCATION, 0, 0, 176, 119);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemRegistry.AVARITIA_SWORD.get()));
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

        
        int arcaneEssenceCost = calculateArcaneEssenceCost(ingredients);

        create5x5RecipeLayout(builder, ingredients, recipeWidth, recipeHeight, result, arcaneEssenceCost);
    }

    
    private int calculateArcaneEssenceCost(NonNullList<Ingredient> ingredients) {
        int itemCount = 0;
        for (Ingredient ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                itemCount++;
            }
        }
        return itemCount * 2;
    }

    private void create5x5RecipeLayout(IRecipeLayoutBuilder builder,
                                       NonNullList<Ingredient> ingredients,
                                       int recipeWidth, int recipeHeight,
                                       ItemStack result, int arcaneEssenceCost) {

        int offsetX = (5 - recipeWidth) / 2;
        int offsetY = (5 - recipeHeight) / 2;
        int x_shifted = 3;
        int y_shifted = -4;

        
        for (int recipeY = 0; recipeY < recipeHeight; recipeY++) {
            for (int recipeX = 0; recipeX < recipeWidth; recipeX++) {
                int ingredientIndex = recipeY * recipeWidth + recipeX;

                if (ingredientIndex < ingredients.size()) {
                    Ingredient ingredient = ingredients.get(ingredientIndex);

                    if (!ingredient.isEmpty()) {
                        int slotX = 8 + (offsetX + recipeX) * 18 + x_shifted;
                        int slotY = 18 + (offsetY + recipeY) * 18 + y_shifted;

                        builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                                .addIngredients(ingredient);
                    }
                }
            }
        }

        
        int outputSlotX = 139 + x_shifted;
        int outputSlotY = 54 + y_shifted;

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputSlotX, outputSlotY)
                .addItemStack(result)
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    tooltip.add(Component.translatable("jei.tooltip.recipe.arcane_workbench"));
                });

        
        int arcaneEssenceSlotX = outputSlotX; 
        int arcaneEssenceSlotY = outputSlotY + 24; 

        
        ItemStack arcaneEssenceStack = new ItemStack(ARCANE_ESSENCE.get(), arcaneEssenceCost);

        builder.addSlot(RecipeIngredientRole.INPUT, arcaneEssenceSlotX, arcaneEssenceSlotY)
                .addItemStack(arcaneEssenceStack)
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    tooltip.add(Component.translatable("jei.tooltip.arcane_essence_consumption", arcaneEssenceCost));
                });
    }
}