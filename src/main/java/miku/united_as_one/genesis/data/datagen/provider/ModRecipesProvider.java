package miku.united_as_one.genesis.data.datagen.provider;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipesProvider extends RecipeProvider {
    public ModRecipesProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        test(Items.WHITE_WOOL, Tags.Items.DYES_WHITE, ItemTags.WOOL, writer);
        test(Items.BLACK_WOOL, Tags.Items.DYES_BLACK, ItemTags.WOOL, writer);
        test(Items.RED_WOOL, Tags.Items.DYES_RED, ItemTags.WOOL, writer);
        test(Items.BLUE_WOOL, Tags.Items.DYES_BLUE, ItemTags.WOOL, writer);
        test(Items.PINK_WOOL, Tags.Items.DYES_PINK, ItemTags.WOOL, writer);
        //……

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.ARCANE_CRYSTAL, 1)
                .define('A', Items.END_CRYSTAL)
                .define('B', ItemTags.WOOL)
                .define('C', Tags.Items.INGOTS_IRON)
                .pattern("ABC")
                .unlockedBy("has_end_crystal", has(Items.END_CRYSTAL))
                .save(writer);
        //如果要自定义配方id
        //        .save(writer, ResourceLocation.fromNamespaceAndPath("abc", "abc"));


        //锻造模板
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ItemRegistry.CHAOS_RUNE),
                Ingredient.of(ItemRegistry.CHAOS_SPELL_BOOTS),
                Ingredient.of(ItemRegistry.TWISTED_CHAOS_INGOT),
                RecipeCategory.COMBAT,
                ItemRegistry.CELESTIAL_SOURCE_SPELL_BOOTS.get()
        )
       .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
       .save(writer, ResourceLocation.fromNamespaceAndPath(Genesis.MODID, "celestial_armor_smithing"));
        //你不写modid的

    }

    private static void test(Item result, TagKey<Item> dye, TagKey<Item> ingredient, Consumer<FinishedRecipe> writer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 1)
                .requires(ingredient)
                .requires(dye)
                .unlockedBy("has_test_ingredient", has(ingredient))
                .save(writer);
    }
}
