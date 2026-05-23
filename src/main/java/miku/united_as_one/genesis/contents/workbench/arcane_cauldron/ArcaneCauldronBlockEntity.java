package miku.united_as_one.genesis.contents.workbench.arcane_cauldron;

import io.redspace.ironsspellbooks.api.backwards_compat.FluidHelper;
import io.redspace.ironsspellbooks.fluids.PotionFluid;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.EmptyAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.FillAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.workbench.ModBlockEntities;
import miku.united_as_one.genesis.registries.workbench.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.redspace.ironsspellbooks.registries.ItemRegistry.ARCANE_ESSENCE;

public class ArcaneCauldronBlockEntity extends BlockEntity implements WorldlyContainer {
    public static final int INPUT_SIZE = 9;
    public static final int COOK_TIME = 100;
    private static final int TOTAL_FLUID_CAPACITY = 1000;
    private static final int[] SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};

    public final NonNullList<ItemStack> inputItems = NonNullList.withSize(INPUT_SIZE, ItemStack.EMPTY);
    private final int[] cookTimes = new int[INPUT_SIZE];
    private final boolean[] cooked = new boolean[INPUT_SIZE];
    public final ArcaneCauldronFluidHandler fluidInventory;
    private final LazyOptional<IFluidHandler> fluidHandlerLazyOptional;

    public ArcaneCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ARCANE_CAULDRON.get(), pos, blockState);
        this.fluidInventory = new ArcaneCauldronFluidHandler();
        this.fluidHandlerLazyOptional = LazyOptional.of(() -> fluidInventory);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState blockState, ArcaneCauldronBlockEntity cauldron) {
        boolean dataChanged = false;
        boolean completedCook = false;
        boolean boiling = cauldron.isBoiling();

        for (int i = 0; i < cauldron.inputItems.size(); i++) {
            ItemStack itemStack = cauldron.inputItems.get(i);
            if (itemStack.isEmpty()) {
                if (cauldron.cookTimes[i] != 0 || cauldron.cooked[i]) {
                    cauldron.resetCookState(i);
                    dataChanged = true;
                }
            } else if (boiling && !cauldron.cooked[i]) {
                cauldron.cookTimes[i]++;
                dataChanged = true;
                if (cauldron.cookTimes[i] >= COOK_TIME) {
                    cauldron.cookTimes[i] = COOK_TIME;
                    cauldron.cooked[i] = true;
                    completedCook = true;
                }
            } else if (!boiling && !cauldron.cooked[i] && cauldron.cookTimes[i] != 0) {
                cauldron.cookTimes[i] = 0;
                dataChanged = true;
            }
        }

        if (boiling && level instanceof ServerLevel serverLevel) {
            float waterLevel = Mth.lerp(cauldron.getFluidAmount() / 1000f, .25f, .9f);
            serverLevel.sendParticles(
                    ParticleTypes.BUBBLE_POP,
                    pos.getX() + Mth.randomBetween(level.random, .2f, .8f),
                    pos.getY() + waterLevel,
                    pos.getZ() + Mth.randomBetween(level.random, .2f, .8f),
                    1,
                    0, 0, 0, 0
            );
        }

        if (completedCook) {
            level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.7f, 1.2f);
            cauldron.setChanged();
        } else if (dataChanged && level.getGameTime() % 20 == 0) {
            cauldron.markDataDirty();
        }
    }

    public InteractionResult handleUse(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.is(ARCANE_ESSENCE.get())) {
            if (canCraftWith(itemStack)) {
                if (!level.isClientSide) {
                    craftWith(itemStack, player);
                }
                return InteractionResult.SUCCESS;
            }
        }

        ItemStack recipeResult = tryExecuteRecipeInteractions(level, itemStack);
        if (!recipeResult.isEmpty()) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(player.getItemInHand(hand), player, recipeResult));
            return InteractionResult.SUCCESS;
        }

        if (isValidInput(itemStack)) {
            if (!level.isClientSide) {
                insertInput(itemStack, player);
            }
            return InteractionResult.SUCCESS;
        }

        if ((itemStack.isEmpty() || player.isCrouching()) && hand == InteractionHand.MAIN_HAND) {
            for (int i = 0; i < inputItems.size(); i++) {
                ItemStack stack = inputItems.get(i);
                if (!stack.isEmpty()) {
                    if (!level.isClientSide) {
                        takeInput(i, player, hand);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.CONSUME;
    }

    public ItemStack tryExecuteRecipeInteractions(Level level, ItemStack itemStack) {
        SimpleContainer fillRecipeInput = new SimpleContainer(itemStack);
        var recipeManager = level.getRecipeManager();
        Optional<FillAlchemistCauldronRecipe> fillRecipe = recipeManager.getRecipeFor(RecipeRegistry.ALCHEMIST_CAULDRON_FILL_TYPE.get(), fillRecipeInput, level);
        if (fillRecipe.isEmpty() && FluidHelper.hasPotionContents(itemStack)) {
            FluidStack fluid = FluidHelper.isWater(itemStack) ? new FluidStack(Fluids.WATER, 250) : PotionFluid.from(itemStack);
            fillRecipe = Optional.of(new FillAlchemistCauldronRecipe(
                    Genesis.rl("generated"),
                    Ingredient.of(itemStack),
                    new ItemStack(Items.GLASS_BOTTLE),
                    fluid,
                    true,
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.BOTTLE_EMPTY)
            ));
        }
        if (fillRecipe.isPresent()) {
            FillAlchemistCauldronRecipe recipe = fillRecipe.get();
            int amountThatCanFit = fluidInventory.fill(recipe.result(), IFluidHandler.FluidAction.SIMULATE);
            if ((!recipe.mustFitAll() || amountThatCanFit == recipe.result().getAmount()) && amountThatCanFit != 0) {
                fluidInventory.fill(recipe.result(), IFluidHandler.FluidAction.EXECUTE);
                setChanged();
                level.playSound(null, getBlockPos(), recipe.fillSound().value(), SoundSource.BLOCKS);
                return recipe.assemble(fillRecipeInput, level.registryAccess());
            }
        }

        FluidStack topFluid = fluidInventory.drain(TOTAL_FLUID_CAPACITY, IFluidHandler.FluidAction.SIMULATE);
        EmptyAlchemistCauldronRecipe.Input emptyRecipeInput = new EmptyAlchemistCauldronRecipe.Input(itemStack, topFluid);
        Optional<EmptyAlchemistCauldronRecipe> emptyRecipe = recipeManager.getRecipeFor(RecipeRegistry.ALCHEMIST_CAULDRON_EMPTY_TYPE.get(), emptyRecipeInput, level);
        if (emptyRecipe.isEmpty() && itemStack.is(Items.GLASS_BOTTLE)) {
            ItemStack potionStack = PotionFluid.from(topFluid);
            if (!potionStack.isEmpty()) {
                emptyRecipe = Optional.of(new EmptyAlchemistCauldronRecipe(
                        Genesis.rl("generated"),
                        Ingredient.EMPTY,
                        potionStack,
                        FluidHelper.copyWithAmount(topFluid, 250),
                        BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.BOTTLE_FILL)
                ));
            }
        }
        if (emptyRecipe.isPresent()) {
            EmptyAlchemistCauldronRecipe recipe = emptyRecipe.get();
            fluidInventory.drain(recipe.fluid(), IFluidHandler.FluidAction.EXECUTE);
            level.playSound(null, getBlockPos(), recipe.emptySound().value(), SoundSource.BLOCKS);
            setChanged();
            return recipe.assemble(emptyRecipeInput, level.registryAccess());
        }
        return ItemStack.EMPTY;
    }

    public boolean isValidInput(ItemStack itemStack) {
        if (itemStack.isEmpty() || itemStack.is(ARCANE_ESSENCE.get()) || level == null) {
            return false;
        }
        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.ARCANE_CAULDRON_RECIPE_TYPE.get())
                .stream()
                .flatMap(recipe -> recipe.getIngredients().stream())
                .anyMatch(ingredient -> ingredient.test(itemStack));
    }

    public boolean canCraftWith(ItemStack catalyst) {
        return getCraftableRecipe(catalyst).isPresent();
    }

    private Optional<ArcaneCauldronRecipe> getCraftableRecipe(ItemStack catalyst) {
        if (level == null || !areAllInputItemsCooked()) {
            return Optional.empty();
        }

        ArcaneCauldronRecipe.Input input = new ArcaneCauldronRecipe.Input(copyInputItems(), fluidInventory.fluids(), catalyst);
        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.ARCANE_CAULDRON_RECIPE_TYPE.get())
                .stream()
                .filter(recipe -> recipe.matches(input, level))
                .findFirst();
    }

    private void craftWith(ItemStack catalyst, Player player) {
        if (level == null) {
            return;
        }

        Optional<ArcaneCauldronRecipe> recipeOptional = getCraftableRecipe(catalyst);
        if (recipeOptional.isEmpty()) {
            return;
        }

        ArcaneCauldronRecipe recipe = recipeOptional.get();
        Optional<int[]> matchingSlots = recipe.findMatchingSlots(inputItems);
        if (matchingSlots.isEmpty()) {
            return;
        }

        ItemStack result = recipe.assemble(new ArcaneCauldronRecipe.Input(copyInputItems(), fluidInventory.fluids(), catalyst), level.registryAccess());
        if (result.isEmpty()) {
            return;
        }

        if (!player.getAbilities().instabuild) {
            catalyst.shrink(recipe.getEssenceCost());
        }

        for (FluidStack fluid : recipe.getFluids()) {
            fluidInventory.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
        }

        int[] slots = matchingSlots.get();
        int outputSlot = slots[0];
        for (int slot : slots) {
            inputItems.set(slot, ItemStack.EMPTY);
            resetCookState(slot);
        }

        inputItems.set(outputSlot, result.copy());
        resetCookState(outputSlot);
        setChanged();
        level.playSound(null, getBlockPos(), SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0f, 1.0f);
    }

    private boolean areAllInputItemsCooked() {
        boolean hasItem = false;
        for (int i = 0; i < inputItems.size(); i++) {
            if (!inputItems.get(i).isEmpty()) {
                hasItem = true;
                if (!cooked[i]) {
                    return false;
                }
            }
        }
        return hasItem;
    }

    private NonNullList<ItemStack> copyInputItems() {
        NonNullList<ItemStack> copy = NonNullList.withSize(inputItems.size(), ItemStack.EMPTY);
        for (int i = 0; i < inputItems.size(); i++) {
            copy.set(i, inputItems.get(i).copy());
        }
        return copy;
    }

    private void insertInput(ItemStack itemStack, Player player) {
        for (int i = 0; i < inputItems.size(); i++) {
            if (inputItems.get(i).isEmpty()) {
                ItemStack input = itemStack.copy();
                input.setCount(1);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                inputItems.set(i, input);
                resetCookState(i);
                setChanged();
                return;
            }
        }
    }

    private void takeInput(int slot, Player player, InteractionHand hand) {
        ItemStack take = inputItems.get(slot).split(1);
        if (inputItems.get(slot).isEmpty()) {
            inputItems.set(slot, ItemStack.EMPTY);
            resetCookState(slot);
        }

        if (player.getItemInHand(hand).isEmpty()) {
            player.setItemInHand(hand, take);
        } else if (!player.getInventory().add(take)) {
            player.drop(take, false);
        }
        setChanged();
    }

    private void resetCookState(int slot) {
        cookTimes[slot] = 0;
        cooked[slot] = false;
    }

    private void clearItems() {
        for (int i = 0; i < inputItems.size(); i++) {
            inputItems.set(i, ItemStack.EMPTY);
            resetCookState(i);
        }
    }

    private void markDataDirty() {
        super.setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        clearItems();
        ContainerHelper.loadAllItems(tag, inputItems);
        fluidInventory.clear();
        fluidInventory.load("Results", tag);

        int[] savedCookTimes = tag.getIntArray("CookTimes");
        for (int i = 0; i < Math.min(savedCookTimes.length, cookTimes.length); i++) {
            cookTimes[i] = savedCookTimes[i];
        }

        byte[] savedCooked = tag.getByteArray("Cooked");
        for (int i = 0; i < Math.min(savedCooked.length, cooked.length); i++) {
            cooked[i] = savedCooked[i] != 0;
        }
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, inputItems);
        fluidInventory.save("Results", tag);
        tag.putIntArray("CookTimes", cookTimes);

        byte[] savedCooked = new byte[cooked.length];
        for (int i = 0; i < cooked.length; i++) {
            savedCooked[i] = (byte) (cooked[i] ? 1 : 0);
        }
        tag.putByteArray("Cooked", savedCooked);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            load(tag);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandlerLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidHandlerLazyOptional.invalidate();
    }

    public void drops() {
        SimpleContainer simpleContainer = new SimpleContainer(inputItems.size());
        for (int i = 0; i < inputItems.size(); i++) {
            simpleContainer.setItem(i, inputItems.get(i));
        }
        if (level != null) {
            Containers.dropContents(level, worldPosition, simpleContainer);
        }
    }

    public boolean isBoiling() {
        return getFluidAmount() >= 1;
    }

    public int getFluidAmount() {
        return fluidInventory.fluidAmount();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return direction != Direction.DOWN && index >= 0 && index < inputItems.size() && getItem(index).isEmpty() && isValidInput(itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN;
    }

    @Override
    public int getContainerSize() {
        return INPUT_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return inputItems.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < inputItems.size() ? inputItems.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack removed = ContainerHelper.removeItem(inputItems, slot, amount);
        if (!removed.isEmpty()) {
            if (getItem(slot).isEmpty()) {
                resetCookState(slot);
            }
            setChanged();
        }
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot < 0 || slot >= inputItems.size()) {
            return ItemStack.EMPTY;
        }
        ItemStack removed = ContainerHelper.takeItem(inputItems, slot);
        resetCookState(slot);
        return removed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < inputItems.size()) {
            ItemStack oldStack = inputItems.get(slot);
            inputItems.set(slot, stack);
            if (!ItemStack.isSameItemSameTags(oldStack, stack)) {
                resetCookState(slot);
            }
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null
                && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        clearItems();
        fluidInventory.clear();
        setChanged();
    }

    public class ArcaneCauldronFluidHandler implements IFluidHandler {
        public class CallbackFluidTank extends FluidTank {
            public CallbackFluidTank(int capacity) {
                super(capacity);
            }

            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                ArcaneCauldronFluidHandler.this.onContentsChanged();
            }
        }

        private IFluidTank[] tanks = new IFluidTank[]{
                new CallbackFluidTank(TOTAL_FLUID_CAPACITY),
                new CallbackFluidTank(TOTAL_FLUID_CAPACITY),
                new CallbackFluidTank(TOTAL_FLUID_CAPACITY),
                new CallbackFluidTank(TOTAL_FLUID_CAPACITY)
        };

        @Override
        public int getTanks() {
            return tanks.length;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return tank < 0 || tank >= tanks.length || tanks[tank].getFluidAmount() == 0 ? FluidStack.EMPTY : tanks[tank].getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return TOTAL_FLUID_CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return tank >= 0 && tank < tanks.length && tanks[tank].isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || resource.getFluid().is(ModTags.CAULDRON_FLUID_DISALLOW)) {
                return 0;
            }

            int resourceLocation = -1;
            int emptyLocation = -1;
            int remainingCapacity = TOTAL_FLUID_CAPACITY - fluidAmount();
            if (remainingCapacity == 0) {
                return 0;
            }

            for (int i = 0; i < tanks.length; i++) {
                if (isTankCompatible(tanks[i], resource)) {
                    resourceLocation = i;
                    break;
                } else if (emptyLocation == -1 && tanks[i].getFluid().isEmpty()) {
                    emptyLocation = i;
                }
            }

            FluidStack copy = FluidHelper.copyWithAmount(resource, Math.min(remainingCapacity, resource.getAmount()));
            if (resourceLocation >= 0) {
                return tanks[resourceLocation].fill(copy, action);
            } else if (emptyLocation >= 0) {
                return tanks[emptyLocation].fill(copy, action);
            }
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) {
                return FluidStack.EMPTY;
            }
            for (int i = 0; i < tanks.length; i++) {
                IFluidTank tank = tanks[i];
                if (isTankCompatible(tank, resource)) {
                    FluidStack result = tank.drain(resource, action);
                    bubbleEmptyTanks();
                    return result;
                }
            }
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            for (int i = tanks.length - 1; i >= 0; i--) {
                IFluidTank tank = tanks[i];
                if (!tank.getFluid().isEmpty()) {
                    FluidStack result = tank.drain(maxDrain, action);
                    bubbleEmptyTanks();
                    return result;
                }
            }
            return FluidStack.EMPTY;
        }

        public int fluidAmount() {
            return fluids().stream().mapToInt(FluidStack::getAmount).sum();
        }

        public List<FluidStack> fluids() {
            return Arrays.stream(tanks)
                    .map(IFluidTank::getFluid)
                    .filter(fluid -> !fluid.isEmpty())
                    .map(FluidStack::copy)
                    .toList();
        }

        public boolean contains(FluidStack stack, int minAmount) {
            for (IFluidTank tank : tanks) {
                if (isTankCompatible(tank, stack) && tank.getFluidAmount() >= minAmount) {
                    return true;
                }
            }
            return false;
        }

        public boolean contains(Fluid fluid, int minAmount) {
            for (IFluidTank tank : tanks) {
                if (tank.getFluid().getFluid() == fluid && tank.getFluidAmount() >= minAmount) {
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            for (IFluidTank tank : tanks) {
                tank.drain(tank.getCapacity(), FluidAction.EXECUTE);
            }
        }

        public void save(String name, CompoundTag tag) {
            ListTag fluids = new ListTag();
            for (IFluidTank tank : tanks) {
                if (!tank.getFluid().isEmpty()) {
                    fluids.add(tank.getFluid().writeToNBT(new CompoundTag()));
                }
            }
            tag.put(name, fluids);
        }

        public void load(String name, CompoundTag tag) {
            if (tag.contains(name, Tag.TAG_LIST)) {
                ListTag fluids = tag.getList(name, Tag.TAG_COMPOUND);
                int i = 0;
                for (Tag fluidTag : fluids) {
                    if (i >= tanks.length) {
                        break;
                    }
                    FluidStack stack = FluidStack.loadFluidStackFromNBT((CompoundTag) fluidTag);
                    tanks[i++].fill(stack, FluidAction.EXECUTE);
                }
            }
        }

        private boolean isTankCompatible(IFluidTank tank, FluidStack stack) {
            return tank.isFluidValid(stack) && FluidHelper.isSameFluidSameComponents(tank.getFluid(), stack);
        }

        private void onContentsChanged() {
            ArcaneCauldronBlockEntity.this.setChanged();
        }

        private void bubbleEmptyTanks() {
            for (int j = 0; j < tanks.length - 1; j++) {
                for (int k = j + 1; k < tanks.length; k++) {
                    if (tanks[j].getFluid().isEmpty() && !tanks[k].getFluid().isEmpty()) {
                        IFluidTank tmp = tanks[j];
                        tanks[j] = tanks[k];
                        tanks[k] = tmp;
                    }
                }
            }
        }
    }
}
