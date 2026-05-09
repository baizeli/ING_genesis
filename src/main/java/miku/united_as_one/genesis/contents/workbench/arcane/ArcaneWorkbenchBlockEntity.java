package miku.united_as_one.genesis.contents.workbench.arcane;

import miku.united_as_one.genesis.registries.workbench.ModBlockEntities;
import miku.united_as_one.genesis.registries.workbench.ModMenuTypes;
import miku.united_as_one.genesis.registries.workbench.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static miku.united_as_one.genesis.api.text.i18nUtil.translatableContainerName;
import static io.redspace.ironsspellbooks.registries.ItemRegistry.ARCANE_ESSENCE;

public class ArcaneWorkbenchBlockEntity extends BaseContainerBlockEntity implements CraftingContainer, RecipeHolder, StackedContentsCompatible {

    private static final int CONTAINER_SIZE = 5 * 5;
    private static final int ESSENCE_SLOT = CONTAINER_SIZE; 
    public static final int TOTAL_SLOTS = CONTAINER_SIZE + 1; 

    public static final Component ARCANE_WORKBENCH_COMPONENT = translatableContainerName("arcane_workbench");
    private final NonNullList<ItemStack> items;
    final ResultContainer resultSlots = new ResultContainer();
    private Recipe<?> recipeUsed;

    public ArcaneWorkbenchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
    }

    public ArcaneWorkbenchBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ARCANE_WORKBENCH.get(), pos, blockState);
        this.items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
    }

    @Override
    public int getWidth() {
        return 5;
    }

    @Override
    public int getHeight() {
        return 5;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    
    public ItemStack getEssenceItem() {
        return items.get(ESSENCE_SLOT);
    }

    
    public void setEssenceItem(ItemStack stack) {
        items.set(ESSENCE_SLOT, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
        onInventoryChanged();
    }

    public int getContainerSize() {
        return TOTAL_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : items) {
            if(!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getItem(int slot) {
        if(slot >= TOTAL_SLOTS) return items.get(TOTAL_SLOTS - 1);
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        if (!result.isEmpty()) {
            onInventoryChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
        onInventoryChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D,
                (double)this.worldPosition.getY() + 0.5D,
                (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    protected Component getDefaultName() {
        return ARCANE_WORKBENCH_COMPONENT;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ArcaneWorkbenchMenu(ModMenuTypes.ARCANE_WORKBENCH_MENU.get(), containerId, inventory, this);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            load(tag);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
        setChanged();
        onInventoryChanged();
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        this.recipeUsed = recipe;
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return recipeUsed;
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        
        for(int i = 0; i < CONTAINER_SIZE; i++) {
            stackedContents.accountStack(items.get(i));
        }
    }

    private void onInventoryChanged() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            checkCraftingRecipe();
        }
    }

    
    public boolean hasEnoughEssence(int consumedSlots) {
        ItemStack essenceStack = getEssenceItem();
        if (essenceStack.isEmpty() || essenceStack.getItem() != ARCANE_ESSENCE.get()) {
            return false;
        }
        int requiredEssence = consumedSlots * 2;
        return essenceStack.getCount() >= requiredEssence;
    }

    
    public void consumeEssence(int consumedSlots) {
        ItemStack essenceStack = getEssenceItem();
        if (!essenceStack.isEmpty() && essenceStack.getItem() == ARCANE_ESSENCE.get()) {
            int requiredEssence = consumedSlots * 2;
            essenceStack.shrink(requiredEssence);
            if (essenceStack.isEmpty()) {
                setEssenceItem(ItemStack.EMPTY);
            }
            setChanged();
        }
    }

    
    private int getConsumedSlotsCount() {
        int count = 0;
        for (int i = 0; i < CONTAINER_SIZE; i++) {
            if (!items.get(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private void checkCraftingRecipe() {
        if (level == null || level.isClientSide) return;

        RecipeManager recipeManager = level.getRecipeManager();
        Optional<ArcaneWorkbenchRecipe> optional = recipeManager.getRecipeFor(
                ModRecipeTypes.ARCANE_WORKBENCH_RECIPE_TYPE.get(),
                this,
                level
        );

        if (optional.isPresent()) {
            ArcaneWorkbenchRecipe recipe = optional.get();
            int consumedSlots = getConsumedSlotsCount();

            
            if (hasEnoughEssence(consumedSlots)) {
                ItemStack result = recipe.assemble(this, level.registryAccess());
                resultSlots.setItem(0, result);
                setRecipeUsed(recipe);
            } else {
                resultSlots.setItem(0, ItemStack.EMPTY);
                setRecipeUsed(null);
            }
        } else {
            resultSlots.setItem(0, ItemStack.EMPTY);
            setRecipeUsed(null);
        }
    }
}