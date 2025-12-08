package com.baizeli.eternisstarrysky.Content.ArcaneWorkbench;

import com.baizeli.eternisstarrysky.network.NetworkHandler;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.common.transfer.RecipeTransferOperationsResult;
import mezz.jei.common.transfer.RecipeTransferUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.baizeli.eternisstarrysky.Content.Workbenchs.ModMenuTypes.ARCANE_WORKBENCH_MENU;

public class ArcaneWorkbenchRecipeTransferHandler implements IRecipeTransferHandler<ArcaneWorkbenchMenu, ArcaneWorkbenchRecipe> {

    private final IStackHelper stackHelper;
    private final IRecipeTransferHandlerHelper handlerHelper;

    public ArcaneWorkbenchRecipeTransferHandler(IStackHelper stackHelper, IRecipeTransferHandlerHelper handlerHelper) {
        this.stackHelper = stackHelper;
        this.handlerHelper = handlerHelper;
    }

    @Override
    public Class<ArcaneWorkbenchMenu> getContainerClass() {
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

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(ArcaneWorkbenchMenu menu, ArcaneWorkbenchRecipe recipe,
                                               IRecipeSlotsView recipeSlots, Player player,
                                               boolean maxTransfer, boolean doTransfer) {

        
        if (recipe.getWidth() > 5 || recipe.getHeight() > 5) {
            return createError("配方尺寸过大，无法在工作台中制作");
        }

        
        List<IRecipeSlotView> inputSlots = recipeSlots.getSlotViews(RecipeIngredientRole.INPUT);

        
        List<Slot> craftingSlots = getCraftingSlots(menu);
        List<Slot> inventorySlots = getInventorySlots(menu);

        
        if (inputSlots.size() > craftingSlots.size()) {
            return handlerHelper.createInternalError();
        }

        
        InventoryState inventoryState = getInventoryState(craftingSlots, inventorySlots, player);
        if (inventoryState == null) {
            return handlerHelper.createInternalError();
        }

        
        if (!inventoryState.hasRoom(inputSlots.size())) {
            return handlerHelper.createUserErrorWithTooltip(Component.translatable("jei.tooltip.error.recipe.transfer.inventory.full"));
        }

        
        RecipeTransferOperationsResult transferOperations = RecipeTransferUtil.getRecipeTransferOperations(
                stackHelper,
                inventoryState.availableItemStacks,
                inputSlots,
                craftingSlots
        );

        
        if (!transferOperations.missingItems.isEmpty()) {
            return handlerHelper.createUserErrorForMissingSlots(
                    Component.translatable("jei.tooltip.error.recipe.transfer.missing"),
                    transferOperations.missingItems
            );
        }

        
        if (!RecipeTransferUtil.validateSlots(player, transferOperations.results, craftingSlots, inventorySlots)) {
            return handlerHelper.createInternalError();
        }

        if (doTransfer) {
            
            sendTransferPacket(menu, recipe, player);
        }

        return null;
    }

    private List<Slot> getCraftingSlots(ArcaneWorkbenchMenu menu) {
        
        List<Slot> slots = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            slots.add(menu.getSlot(i));
        }
        return slots;
    }

    private List<Slot> getInventorySlots(ArcaneWorkbenchMenu menu) {
        
        List<Slot> slots = new ArrayList<>();
        for (int i = 26; i < menu.slots.size(); i++) {
            slots.add(menu.getSlot(i));
        }
        return slots;
    }

    @Nullable
    private InventoryState getInventoryState(Collection<Slot> craftingSlots, Collection<Slot> inventorySlots, Player player) {
        Map<Slot, ItemStack> availableItemStacks = new HashMap<>();
        int filledCraftSlotCount = 0;
        int emptySlotCount = 0;

        for (Slot slot : craftingSlots) {
            ItemStack stack = slot.getItem();
            if (!stack.isEmpty()) {
                if (!slot.mayPickup(player)) {
                    return null;
                }
                filledCraftSlotCount++;
                availableItemStacks.put(slot, stack.copy());
            }
        }

        for (Slot slot : inventorySlots) {
            ItemStack stack = slot.getItem();
            if (!stack.isEmpty()) {
                if (!slot.mayPickup(player)) {
                    return null;
                }
                availableItemStacks.put(slot, stack.copy());
            } else {
                emptySlotCount++;
            }
        }

        return new InventoryState(availableItemStacks, filledCraftSlotCount, emptySlotCount);
    }

    private void sendTransferPacket(ArcaneWorkbenchMenu menu, ArcaneWorkbenchRecipe recipe, Player player) {
        
        ArcaneWorkbenchRecipeTransferPacket packet = new ArcaneWorkbenchRecipeTransferPacket(
                recipe.getId(),
                menu.containerId
        );

        NetworkHandler.INSTANCE.sendToServer(packet);
    }

    private IRecipeTransferError createError(String message) {
        return new IRecipeTransferError() {
            @Override
            public Type getType() {
                return Type.USER_FACING;
            }


        };
    }

    private static class InventoryState {
        public final Map<Slot, ItemStack> availableItemStacks;
        public final int filledCraftSlotCount;
        public final int emptySlotCount;

        public InventoryState(Map<Slot, ItemStack> availableItemStacks, int filledCraftSlotCount, int emptySlotCount) {
            this.availableItemStacks = availableItemStacks;
            this.filledCraftSlotCount = filledCraftSlotCount;
            this.emptySlotCount = emptySlotCount;
        }

        public boolean hasRoom(int inputCount) {
            return filledCraftSlotCount - inputCount <= emptySlotCount;
        }
    }
}