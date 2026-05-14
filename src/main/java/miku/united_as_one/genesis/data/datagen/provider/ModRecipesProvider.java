package miku.united_as_one.genesis.data.datagen.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipesProvider extends RecipeProvider {
    private static final String IRONS_SPELLBOOKS = "irons_spellbooks";
    private static final int INK_BOTTLE_AMOUNT = 250;

    public ModRecipesProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        ResourceLocation arcaneCrystal = Genesis.rl("arcane_crystal");

        alchemistCauldronBrew(
                writer,
                "alchemist_cauldron/brew_uncommon_ink_from_blackwater",
                Genesis.rl("blackwater_fluid"),
                1000,
                arcaneCrystal,
                ironsSpellbooks("uncommon_ink"),
                INK_BOTTLE_AMOUNT
        );

        alchemistCauldronBrew(
                writer,
                "alchemist_cauldron/brew_uncommon_ink_from_common_ink",
                ironsSpellbooks("common_ink"),
                INK_BOTTLE_AMOUNT * 3,
                arcaneCrystal,
                ironsSpellbooks("uncommon_ink"),
                INK_BOTTLE_AMOUNT
        );

        alchemistCauldronBrew(
                writer,
                "alchemist_cauldron/brew_rare_ink_from_uncommon_ink",
                ironsSpellbooks("uncommon_ink"),
                INK_BOTTLE_AMOUNT * 3,
                arcaneCrystal,
                ironsSpellbooks("rare_ink"),
                INK_BOTTLE_AMOUNT
        );

        alchemistCauldronBrew(
                writer,
                "alchemist_cauldron/brew_epic_ink_from_rare_ink",
                ironsSpellbooks("rare_ink"),
                INK_BOTTLE_AMOUNT * 3,
                arcaneCrystal,
                ironsSpellbooks("epic_ink"),
                INK_BOTTLE_AMOUNT
        );

        alchemistCauldronBrew(
                writer,
                "alchemist_cauldron/brew_legendary_ink_from_epic_ink",
                ironsSpellbooks("epic_ink"),
                INK_BOTTLE_AMOUNT * 3,
                arcaneCrystal,
                ironsSpellbooks("legendary_ink"),
                INK_BOTTLE_AMOUNT
        );
    }

    private static void alchemistCauldronBrew(
            Consumer<FinishedRecipe> writer,
            String path,
            ResourceLocation baseFluid,
            int baseAmount,
            ResourceLocation inputItem,
            ResourceLocation resultFluid,
            int resultAmount
    ) {
        writer.accept(new AlchemistCauldronBrewRecipe(
                Genesis.rl(path),
                baseFluid,
                baseAmount,
                inputItem,
                resultFluid,
                resultAmount
        ));
    }

    private static ResourceLocation ironsSpellbooks(String path) {
        return ResourceLocation.fromNamespaceAndPath(IRONS_SPELLBOOKS, path);
    }

    private record AlchemistCauldronBrewRecipe(
            ResourceLocation id,
            ResourceLocation baseFluid,
            int baseAmount,
            ResourceLocation inputItem,
            ResourceLocation resultFluid,
            int resultAmount
    ) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("base_fluid", fluidStack(baseFluid, baseAmount));

            JsonObject input = new JsonObject();
            input.addProperty("item", inputItem.toString());
            json.add("input", input);

            JsonArray results = new JsonArray();
            results.add(fluidStack(resultFluid, resultAmount));
            json.add("results", results);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeRegistry.ALCHEMIST_CAULDRON_BREW_SERIALIZER.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }

        private static JsonObject fluidStack(ResourceLocation fluid, int amount) {
            JsonObject stack = new JsonObject();
            stack.addProperty("Amount", amount);
            stack.addProperty("FluidName", fluid.toString());
            return stack;
        }
    }
}
