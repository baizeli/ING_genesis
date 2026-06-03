package miku.united_as_one.genesis.data.datagen;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.data.datagen.provider.recipe.RecipeGen;
import miku.united_as_one.genesis.registries.item.ItemRegistry;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;

public final class ModRegistrateData {
    private static boolean registered;

    private ModRegistrateData() {
    }

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;

        Genesis.L2_REGISTRATE.addDataGenerator(ProviderType.RECIPE, ModRegistrateData::recipes);
        Genesis.L2_REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, ModRegistrateData::itemTags);
    }

    private static void recipes(RegistrateRecipeProvider provider) {
        RecipeGen.genRecipe(provider);
    }

    private static void itemTags(RegistrateItemTagsProvider provider) {
        provider.addTag(SpellSchoolRegistry.CELESTIAL_SOURCE_FOCUS)
                .add(ItemRegistry.CELESTIAL_SOURCE_PEARL.get());
        provider.addTag(SpellSchoolRegistry.CHAOS_FOCUS)
                .add(ItemRegistry.TWISTED_CHAOS.get());
    }
}
