package miku.united_as_one.genesis.common.data.content.arcaneWorkbench;

import miku.united_as_one.genesis.common.data.content.workbenchs.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class ArcaneWorkbenchMenu extends AbstractContainerMenu {
    private final ArcaneWorkbenchBlockEntity blockEntity;
    private final ContainerLevelAccess access;
    private static final int SLOT_COUNT = 5 * 5;

    public ArcaneWorkbenchMenu(MenuType<?> type, int containerId, Inventory playerInventory, ArcaneWorkbenchBlockEntity blockEntity) {
        super(type, containerId);
        this.blockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());


        this.addSlot(new ResultSlot(playerInventory.player, blockEntity, blockEntity.resultSlots, 0, 139, 54));


        for(int j = 0; j < 5; ++j) {
            for(int k = 0; k < 5; ++k) {
                this.addSlot(new Slot(blockEntity, k + j * 5, 8 + k * 18, 18 + j * 18));
            }
        }


        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + 18));
            }
        }


        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + 18));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, blockEntity.getBlockState().getBlock());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot == null || !slot.hasItem()) {
            return itemstack;
        }

        ItemStack itemstack1 = slot.getItem();
        itemstack = itemstack1.copy();


        int RESULT_SLOT = 0;
        int CRAFTING_START = 1;
        int CRAFTING_END = SLOT_COUNT;
        int INVENTORY_START = CRAFTING_END + 1;
        int INVENTORY_END = INVENTORY_START + 27;
        int HOTBAR_START = INVENTORY_END;
        int HOTBAR_END = HOTBAR_START + 9;


        if (index == RESULT_SLOT) {
            if (!this.moveItemStackTo(itemstack1, INVENTORY_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(itemstack1, itemstack);
        }

        else if (index >= CRAFTING_START && index <= CRAFTING_END) {
            if (!this.moveItemStackTo(itemstack1, INVENTORY_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
        }

        else if (index >= INVENTORY_START && index <= HOTBAR_END) {
            if (!this.moveItemStackTo(itemstack1, CRAFTING_START, CRAFTING_END + 1, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (itemstack1.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (itemstack1.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, itemstack1);
        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.blockEntity.stopOpen(player);
    }

    public ArcaneWorkbenchBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public void slotsChanged(Container container) {
        super.slotsChanged(container);

    }

    public class ResultSlot extends Slot {
        private final CraftingContainer craftSlots;
        private final Player player;
        private int removeCount;

        public ResultSlot(Player player, CraftingContainer craftSlots, Container container, int slot, int xPosition, int yPosition) {
            super(container, slot, xPosition, yPosition);
            this.player = player;
            this.craftSlots = craftSlots;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack remove(int amount) {
            if (this.hasItem()) {
                this.removeCount += Math.min(amount, this.getItem().getCount());
            }
            return super.remove(amount);
        }

        @Override
        protected void onQuickCraft(ItemStack stack, int amount) {
            this.removeCount += amount;
            this.checkTakeAchievements(stack);
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            this.checkTakeAchievements(stack);


            NonNullList<ItemStack> remainingItems = player.level().getRecipeManager()
                    .getRemainingItemsFor(ModRecipeTypes.ARCANE_WORKBENCH_RECIPE_TYPE.get(), this.craftSlots, player.level());

            for (int i = 0; i < remainingItems.size(); ++i) {
                ItemStack currentStack = this.craftSlots.getItem(i);
                ItemStack remainingStack = remainingItems.get(i);

                if (!currentStack.isEmpty()) {
                    this.craftSlots.removeItem(i, 1);
                    currentStack = this.craftSlots.getItem(i);
                }

                if (!remainingStack.isEmpty()) {
                    if (currentStack.isEmpty()) {
                        this.craftSlots.setItem(i, remainingStack);
                    } else if (ItemStack.isSameItemSameTags(currentStack, remainingStack)) {
                        remainingStack.grow(currentStack.getCount());
                        this.craftSlots.setItem(i, remainingStack);
                    } else if (!player.getInventory().add(remainingStack)) {
                        player.drop(remainingStack, false);
                    }
                }
            }
        }

        @Override
        protected void onSwapCraft(int numItemsCrafted) {
            super.onSwapCraft(numItemsCrafted);
        }
    }
}