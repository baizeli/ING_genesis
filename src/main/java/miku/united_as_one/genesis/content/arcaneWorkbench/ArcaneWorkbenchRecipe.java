package miku.united_as_one.genesis.content.arcaneWorkbench;

import miku.united_as_one.genesis.content.workbenchs.ModRecipeSerializers;
import miku.united_as_one.genesis.content.workbenchs.ModRecipeTypes;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static miku.united_as_one.genesis.Genesis.MODID;

public class ArcaneWorkbenchRecipe implements Recipe<CraftingContainer> {
    
    static int MAX_WIDTH = 5;
    static int MAX_HEIGHT = 5;
    final int width;
    final int height;
    final NonNullList<Ingredient> recipeItems;
    final ItemStack result;
    private final ResourceLocation id;
    final String group;
    final CraftingBookCategory category;
    final boolean showNotification;

    public static void setCraftingSize(int width, int height) {
        if (MAX_WIDTH < width) {
            MAX_WIDTH = width;
        }

        if (MAX_HEIGHT < height) {
            MAX_HEIGHT = height;
        }
    }

    public ArcaneWorkbenchRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result, boolean showNotification) {
        this.id = id;
        this.group = group;
        this.category = category;
        this.width = width;
        this.height = height;
        this.recipeItems = recipeItems;
        this.result = result;
        this.showNotification = showNotification;
    }

    public ArcaneWorkbenchRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result) {
        this(id, group, category, width, height, recipeItems, result, true);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ARCANE_WORKBENCH_RECIPE_SERIALIZER.get();
    }

    
    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.ARCANE_WORKBENCH_RECIPE_TYPE.get();
    }

    public String getGroup() {
        return this.group;
    }

    public CraftingBookCategory category() {
        return this.category;
    }

    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    public boolean showNotification() {
        return this.showNotification;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    public boolean matches(CraftingContainer inv, Level level) {
        
        for(int i = 0; i <= inv.getWidth() - this.width; ++i) {
            for(int j = 0; j <= inv.getHeight() - this.height; ++j) {
                if (this.matches(inv, i, j, true)) {
                    return true;
                }

                if (this.matches(inv, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(CraftingContainer craftingInventory, int width, int height, boolean mirrored) {
        for(int i = 0; i < craftingInventory.getWidth(); ++i) {
            for(int j = 0; j < craftingInventory.getHeight(); ++j) {
                int k = i - width;
                int l = j - height;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (mirrored) {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(craftingInventory.getItem(i + j * craftingInventory.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return this.getResultItem(registryAccess).copy();
    }

    public int getWidth() {
        return this.width;
    }

    public int getRecipeWidth() {
        return this.getWidth();
    }

    public int getHeight() {
        return this.height;
    }

    public int getRecipeHeight() {
        return this.getHeight();
    }

    static NonNullList<Ingredient> dissolvePattern(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(keys.keySet());
        set.remove(" ");

        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length(); ++j) {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = keys.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + patternWidth * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... toShrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < toShrink.length; ++i1) {
            String s = toShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (toShrink.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[toShrink.length - l - k];

            for(int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = toShrink[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter((p_151277_) -> {
            return !p_151277_.isEmpty();
        }).anyMatch((p_151273_) -> {
            return ForgeHooks.hasNoElements(p_151273_);
        });
    }

    private static int firstNonSpace(String entry) {
        int i;
        for(i = 0; i < entry.length() && entry.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String entry) {
        int i;
        for(i = entry.length() - 1; i >= 0 && entry.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] patternFromJson(JsonArray patternArray) {
        String[] astring = new String[patternArray.size()];
        if (astring.length > MAX_HEIGHT) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = GsonHelper.convertToString(patternArray.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    static Map<String, Ingredient> keyFromJson(JsonObject keyEntry) {
        Map<String, Ingredient> map = Maps.newHashMap();
        Iterator var2 = keyEntry.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue(), false));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static ItemStack itemStackFromJson(JsonObject stackObject) {
        return CraftingHelper.getItemStack(stackObject, true, true);
    }

    public static Item itemFromJson(JsonObject itemObject) {
        String s = GsonHelper.getAsString(itemObject, "item");
        Item item = BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + s + "'");
        });
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Empty ingredient not allowed here");
        } else {
            return item;
        }
    }

    public static class Serializer implements RecipeSerializer<ArcaneWorkbenchRecipe> {
        
        private static final ResourceLocation NAME = new ResourceLocation(MODID, "arcane_workbench_recipe");

        public Serializer() {
        }

        public ArcaneWorkbenchRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = GsonHelper.getAsString(json, "group", "");
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
            Map<String, Ingredient> map = ArcaneWorkbenchRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] astring = ArcaneWorkbenchRecipe.shrink(ArcaneWorkbenchRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = ArcaneWorkbenchRecipe.dissolvePattern(astring, map, i, j);
            ItemStack itemstack = ArcaneWorkbenchRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            boolean flag = GsonHelper.getAsBoolean(json, "show_notification", true);
            return new ArcaneWorkbenchRecipe(recipeId, s, craftingbookcategory, i, j, nonnulllist, itemstack, flag);
        }

        public ArcaneWorkbenchRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            String s = buffer.readUtf();
            CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            boolean flag = buffer.readBoolean();
            return new ArcaneWorkbenchRecipe(recipeId, s, craftingbookcategory, i, j, nonnulllist, itemstack, flag);
        }

        public void toNetwork(FriendlyByteBuf buffer, ArcaneWorkbenchRecipe recipe) {
            buffer.writeVarInt(recipe.width);
            buffer.writeVarInt(recipe.height);
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);

            for(Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
            buffer.writeBoolean(recipe.showNotification);
        }
    }
}