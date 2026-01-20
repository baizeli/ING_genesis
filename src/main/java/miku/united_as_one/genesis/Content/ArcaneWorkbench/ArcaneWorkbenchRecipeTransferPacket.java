package miku.united_as_one.genesis.Content.ArcaneWorkbench;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ArcaneWorkbenchRecipeTransferPacket {
    private final ResourceLocation recipeId;
    private final int containerId;

    public ArcaneWorkbenchRecipeTransferPacket(ResourceLocation recipeId, int containerId) {
        this.recipeId = recipeId;
        this.containerId = containerId;
    }

    public ArcaneWorkbenchRecipeTransferPacket(FriendlyByteBuf buffer) {
        this.recipeId = buffer.readResourceLocation();
        this.containerId = buffer.readVarInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(recipeId);
        buffer.writeVarInt(containerId);
    }

    public static void handle(ArcaneWorkbenchRecipeTransferPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            
            if (context.getSender() != null) {
                handleOnServer(packet, context.getSender());
            }
        });
        context.setPacketHandled(true);
    }

    private static void handleOnServer(ArcaneWorkbenchRecipeTransferPacket packet, net.minecraft.server.level.ServerPlayer player) {
        
        if (player.containerMenu != null && player.containerMenu.containerId == packet.containerId) {
            if (player.containerMenu instanceof ArcaneWorkbenchMenu menu) {
                
                Recipe<?> recipe = player.level().getRecipeManager().byKey(packet.recipeId).orElse(null);
                if (recipe instanceof ArcaneWorkbenchRecipe arcaneRecipe) {
                    
                    transferRecipeToMenu(menu, arcaneRecipe, player);
                }
            }
        }
    }

    private static void transferRecipeToMenu(ArcaneWorkbenchMenu menu, ArcaneWorkbenchRecipe recipe, net.minecraft.server.level.ServerPlayer player) {
        
        if (recipe.getWidth() > 5 || recipe.getHeight() > 5) {
            return; 
        }

        
        if (!hasEnoughMaterials(menu, recipe, player)) {
            return;
        }

        
        clearCraftingGrid(menu);

        
        placeIngredients(menu, recipe, player);
    }

    private static boolean hasEnoughMaterials(ArcaneWorkbenchMenu menu, ArcaneWorkbenchRecipe recipe, net.minecraft.server.level.ServerPlayer player) {
        var ingredients = recipe.getIngredients();

        
        net.minecraft.core.NonNullList<ItemStack> availableStacks = net.minecraft.core.NonNullList.withSize(player.getInventory().getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < availableStacks.size(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            availableStacks.set(i, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }

        
        for (net.minecraft.world.item.crafting.Ingredient ingredient : ingredients) {
            if (ingredient.isEmpty()) continue;

            boolean found = false;
            for (int i = 0; i < availableStacks.size(); i++) {
                ItemStack available = availableStacks.get(i);
                if (!available.isEmpty() && ingredient.test(available)) {
                    available.shrink(1);
                    if (available.getCount() <= 0) {
                        availableStacks.set(i, ItemStack.EMPTY);
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    private static void clearCraftingGrid(ArcaneWorkbenchMenu menu) {
        
        for (int i = 2; i <= 26; i++) {
            net.minecraft.world.inventory.Slot slot = menu.slots.get(i);
            if (slot.hasItem()) {
                ItemStack stack = slot.getItem().copy();
                slot.set(ItemStack.EMPTY);

                
                net.minecraft.world.entity.player.Player player = menu.getBlockEntity().getLevel().getNearestPlayer(
                        menu.getBlockEntity().getBlockPos().getX(),
                        menu.getBlockEntity().getBlockPos().getY(),
                        menu.getBlockEntity().getBlockPos().getZ(),
                        5, 
                        false
                );

                if (player != null) {
                    
                    if (!player.getInventory().add(stack)) {
                        
                        player.drop(stack, false);
                    }
                } else {
                    
                    net.minecraft.world.level.Level level = menu.getBlockEntity().getLevel();
                    net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(
                            level,
                            menu.getBlockEntity().getBlockPos().getX() + 0.5,
                            menu.getBlockEntity().getBlockPos().getY() + 1.0,
                            menu.getBlockEntity().getBlockPos().getZ() + 0.5,
                            stack
                    );
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    private static void placeIngredients(ArcaneWorkbenchMenu menu, ArcaneWorkbenchRecipe recipe, net.minecraft.server.level.ServerPlayer player) {
        var ingredients = recipe.getIngredients();
        int recipeWidth = recipe.getWidth();
        int recipeHeight = recipe.getHeight();

        
        int offsetX = (5 - recipeWidth) / 2;
        int offsetY = (5 - recipeHeight) / 2;

        
        for (int y = 0; y < recipeHeight; y++) {
            for (int x = 0; x < recipeWidth; x++) {
                int ingredientIndex = y * recipeWidth + x;
                if (ingredientIndex < ingredients.size()) {
                    var ingredient = ingredients.get(ingredientIndex);
                    if (!ingredient.isEmpty()) {
                        
                        int slotIndex = (offsetY + y) * 5 + (offsetX + x) + 2; 

                        
                        for (int invSlot = 0; invSlot < player.getInventory().getContainerSize(); invSlot++) {
                            ItemStack stack = player.getInventory().getItem(invSlot);
                            if (!stack.isEmpty() && ingredient.test(stack)) {
                                
                                ItemStack toPlace = stack.copy();
                                toPlace.setCount(1);

                                net.minecraft.world.inventory.Slot craftingSlot = menu.slots.get(slotIndex);
                                craftingSlot.set(toPlace);

                                
                                stack.shrink(1);
                                if (stack.getCount() <= 0) {
                                    player.getInventory().setItem(invSlot, ItemStack.EMPTY);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}