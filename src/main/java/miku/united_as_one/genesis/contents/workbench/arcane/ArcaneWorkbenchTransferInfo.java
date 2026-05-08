package miku.united_as_one.genesis.contents.workbench.arcane;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static miku.united_as_one.genesis.registries.workbench.ModMenuTypes.ARCANE_WORKBENCH_MENU;

public class ArcaneWorkbenchTransferInfo implements IRecipeTransferInfo<ArcaneWorkbenchMenu, ArcaneWorkbenchRecipe> {

    @Override
    public Class<? extends ArcaneWorkbenchMenu> getContainerClass() {
        return ArcaneWorkbenchMenu.class;
    }

    @Override
    public Optional<MenuType<ArcaneWorkbenchMenu>> getMenuType() {
        return Optional.of(ARCANE_WORKBENCH_MENU.get());
    }

    @Override
    public RecipeType<ArcaneWorkbenchRecipe> getRecipeType() {
        return ArcaneWorkbenchRecipeCategory.ARCANE_WORKBENCH_RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(ArcaneWorkbenchMenu container, ArcaneWorkbenchRecipe recipe) {
        return recipe.getWidth() <= 5 && recipe.getHeight() <= 5;
    }

    @Override
    public List<Slot> getRecipeSlots(ArcaneWorkbenchMenu container, ArcaneWorkbenchRecipe recipe) {
        
        List<Slot> slots = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(ArcaneWorkbenchMenu container, ArcaneWorkbenchRecipe recipe) {

        List<Slot> slots = new ArrayList<>();
        for (int i = 26; i < container.slots.size(); i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public boolean requireCompleteSets(ArcaneWorkbenchMenu container, ArcaneWorkbenchRecipe recipe) {
        return true;
    }

    @Nullable
    @Override
    public IRecipeTransferError getHandlingError(ArcaneWorkbenchMenu container, ArcaneWorkbenchRecipe recipe) {
        
        if (recipe.getWidth() > 5 || recipe.getHeight() > 5) {
            
            return null; 
        }
        return null;
    }
}