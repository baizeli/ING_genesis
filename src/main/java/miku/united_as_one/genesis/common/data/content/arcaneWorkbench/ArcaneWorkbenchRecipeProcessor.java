package miku.united_as_one.genesis.common.data.content.arcaneWorkbench;

import miku.united_as_one.genesis.common.data.content.workbenchs.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;


public class ArcaneWorkbenchRecipeProcessor implements IComponentProcessor {

    private ArcaneWorkbenchRecipe recipe;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
        recipe = (ArcaneWorkbenchRecipe) level.getRecipeManager()
                .byKey(id)
                .filter(r -> r.getType() == ModRecipeTypes.ARCANE_WORKBENCH_RECIPE_TYPE.get())
                .map(r -> (ArcaneWorkbenchRecipe) r)
                .orElse(null);
    }

    @Override
    public IVariable process(Level level, String key) {
        if (recipe == null) return null;

        switch (key) {
            
            case "input0": return getIngredientStack(0);
            case "input1": return getIngredientStack(1);
            case "input2": return getIngredientStack(2);
            case "input3": return getIngredientStack(3);
            case "input4": return getIngredientStack(4);
            case "input5": return getIngredientStack(5);
            case "input6": return getIngredientStack(6);
            case "input7": return getIngredientStack(7);
            case "input8": return getIngredientStack(8);
            case "input9": return getIngredientStack(9);
            case "input10": return getIngredientStack(10);
            case "input11": return getIngredientStack(11);
            case "input12": return getIngredientStack(12);
            case "input13": return getIngredientStack(13);
            case "input14": return getIngredientStack(14);
            case "input15": return getIngredientStack(15);
            case "input16": return getIngredientStack(16);
            case "input17": return getIngredientStack(17);
            case "input18": return getIngredientStack(18);
            case "input19": return getIngredientStack(19);
            case "input20": return getIngredientStack(20);
            case "input21": return getIngredientStack(21);
            case "input22": return getIngredientStack(22);
            case "input23": return getIngredientStack(23);
            case "input24": return getIngredientStack(24);

            
            case "output":
                return IVariable.from(recipe.getResultItem(level.registryAccess()));


            case "width":
                return IVariable.wrap(recipe.getWidth());

            case "height":
                return IVariable.wrap(recipe.getHeight());

            case "group":
                return IVariable.wrap(recipe.getGroup() == null ? "" : recipe.getGroup());

            
            case "arcaneEssenceCost":
                int cost = calculateArcaneEssenceCost(recipe.getIngredients());
                return IVariable.wrap(cost);

            
            case "recipeId":
                return IVariable.wrap(recipe.getId().toString());

            case "category":
                return IVariable.wrap(recipe.category().name());

            default:
                
                if (key.startsWith("input_")) {
                    try {
                        int index = Integer.parseInt(key.substring(6));
                        return getIngredientStack(index);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                return null;
        }
    }

    private IVariable getIngredientStack(int index) {
        List<Ingredient> ingredients = recipe.getIngredients();
        int recipeWidth = recipe.getWidth();
        int recipeHeight = recipe.getHeight();

        
        int gridX = index % 5;
        int gridY = index / 5;

        
        int offsetX = (5 - recipeWidth) / 2;
        int offsetY = (5 - recipeHeight) / 2;

        int recipeX = gridX - offsetX;
        int recipeY = gridY - offsetY;

        
        if (recipeX >= 0 && recipeX < recipeWidth && recipeY >= 0 && recipeY < recipeHeight) {
            int ingredientIndex = recipeY * recipeWidth + recipeX;

            if (ingredientIndex < ingredients.size()) {
                Ingredient ingredient = ingredients.get(ingredientIndex);
                if (!ingredient.isEmpty()) {
                    ItemStack[] items = ingredient.getItems();
                    if (items.length > 0) {
                        return IVariable.from(items[0]);
                    }
                }
            }
        }

        return IVariable.from(ItemStack.EMPTY);
    }

    
    private int calculateArcaneEssenceCost(List<Ingredient> ingredients) {
        int itemCount = 0;
        for (Ingredient ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                itemCount++;
            }
        }
        return itemCount * 2;
    }
}