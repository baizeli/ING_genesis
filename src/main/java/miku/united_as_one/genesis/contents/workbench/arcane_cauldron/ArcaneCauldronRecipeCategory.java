package miku.united_as_one.genesis.contents.workbench.arcane_cauldron;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.block.BlockRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import static io.redspace.ironsspellbooks.registries.ItemRegistry.ARCANE_ESSENCE;

public class ArcaneCauldronRecipeCategory implements IRecipeCategory<ArcaneCauldronRecipe> {
    public static final RecipeType<ArcaneCauldronRecipe> ARCANE_CAULDRON_RECIPE_TYPE =
            RecipeType.create(Genesis.MOD_ID, "arcane_cauldron", ArcaneCauldronRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable cauldron;
    private final IDrawable arrow;
    private final IDrawable plus;

    public ArcaneCauldronRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(170, 92);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.ARCANE_CAULDRON.get()));
        this.cauldron = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.ARCANE_CAULDRON.get()));
        this.arrow = guiHelper.getRecipeArrow();
        this.plus = guiHelper.getRecipePlusSign();
    }

    @Override
    public RecipeType<ArcaneCauldronRecipe> getRecipeType() {
        return ARCANE_CAULDRON_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.genesis_magic.arcane_cauldron");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ArcaneCauldronRecipe recipe, IFocusGroup focuses) {
        int fluidX = 6;
        int fluidY = 10;
        for (int i = 0; i < recipe.getFluids().size(); i++) {
            FluidStack fluid = recipe.getFluids().get(i);
            var slot = builder.addSlot(RecipeIngredientRole.INPUT, fluidX, fluidY + i * 18)
                    .setStandardSlotBackground()
                    .setFluidRenderer(Math.max(1000, fluid.getAmount()), false, 16, 16)
                    .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("jei.genesis_magic.arcane_cauldron.fluid", fluid.getAmount())));

            if (fluid.hasTag()) {
                slot.addFluidStack(fluid.getFluid(), fluid.getAmount(), fluid.getTag());
            } else {
                slot.addFluidStack(fluid.getFluid(), fluid.getAmount());
            }
        }

        int ingredientX = 42;
        int ingredientY = 16;
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, ingredientX + (i % 3) * 18, ingredientY + (i / 3) * 18)
                    .setStandardSlotBackground()
                    .addIngredients(ingredient);
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 100, 52)
                .setStandardSlotBackground()
                .addItemStack(new ItemStack(ARCANE_ESSENCE.get(), Math.min(recipe.getEssenceCost(), 64)))
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("jei.genesis_magic.arcane_cauldron.essence", recipe.getEssenceCost())));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 142, 41)
                .setOutputSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(ArcaneCauldronRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        cauldron.draw(guiGraphics, 100, 24);
        plus.draw(guiGraphics, 84, 56);
        arrow.draw(guiGraphics, 116, 42);
    }
}
