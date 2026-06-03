package miku.united_as_one.genesis.data.datagen.provider.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.item.ItemRegistry;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
import miku.united_as_one.genesis.registries.workbench.ModRecipeSerializers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public final class RecipeGen {
    private static final String IRONS_SPELLBOOKS = "irons_spellbooks";
    private static final int INK_BOTTLE_AMOUNT = 250;

    private RecipeGen() {
    }

    public static void genRecipe(RegistrateRecipeProvider provider) {
        alchemistCauldron(provider);
        arcaneWorkbench(provider);
        materials(provider);
        crystals(provider);
        runes(provider);
        orbs(provider);
        bows(provider);
        equipment(provider);
        spellbooks(provider);
    }

    private static void alchemistCauldron(RegistrateRecipeProvider provider) {
        ResourceLocation arcaneCrystal = Genesis.rl("arcane_crystal");

        alchemistCauldronBrew()
                .baseFluid(Genesis.rl("blackwater_fluid"), 1000)
                .input(arcaneCrystal)
                .resultFluid(ironsLocation("uncommon_ink"), INK_BOTTLE_AMOUNT)
                .save(provider, Genesis.rl("alchemist_cauldron/brew_uncommon_ink_from_blackwater"));

        alchemistCauldronBrew()
                .baseFluid(ironsLocation("common_ink"), INK_BOTTLE_AMOUNT * 3)
                .input(arcaneCrystal)
                .resultFluid(ironsLocation("uncommon_ink"), INK_BOTTLE_AMOUNT)
                .save(provider, Genesis.rl("alchemist_cauldron/brew_uncommon_ink_from_common_ink"));

        alchemistCauldronBrew()
                .baseFluid(ironsLocation("uncommon_ink"), INK_BOTTLE_AMOUNT * 3)
                .input(arcaneCrystal)
                .resultFluid(ironsLocation("rare_ink"), INK_BOTTLE_AMOUNT)
                .save(provider, Genesis.rl("alchemist_cauldron/brew_rare_ink_from_uncommon_ink"));

        alchemistCauldronBrew()
                .baseFluid(ironsLocation("rare_ink"), INK_BOTTLE_AMOUNT * 3)
                .input(arcaneCrystal)
                .resultFluid(ironsLocation("epic_ink"), INK_BOTTLE_AMOUNT)
                .save(provider, Genesis.rl("alchemist_cauldron/brew_epic_ink_from_rare_ink"));

        alchemistCauldronBrew()
                .baseFluid(ironsLocation("epic_ink"), INK_BOTTLE_AMOUNT * 3)
                .input(arcaneCrystal)
                .resultFluid(ironsLocation("legendary_ink"), INK_BOTTLE_AMOUNT)
                .save(provider, Genesis.rl("alchemist_cauldron/brew_legendary_ink_from_epic_ink"));
    }

    private static void arcaneWorkbench(RegistrateRecipeProvider provider) {
        arcane(Items.NETHERITE_INGOT)
                .pattern("  A  ")
                .pattern(" ABA ")
                .pattern("ACBCA")
                .pattern(" ABA ")
                .pattern("  A  ")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.DIAMOND)
                .define('C', Items.EMERALD)
                .save(provider, Genesis.rl("arcane_workbench/test_recipe"));
    }

    private static void materials(RegistrateRecipeProvider provider) {
        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CELESTIAL_SOURCE_INGOT.get())::unlockedBy, ItemRegistry.CELESTIAL_SOURCE_PEARL.get())
                .pattern("FFF")
                .pattern("FRF")
                .pattern("FFF")
                .define('F', ItemRegistry.CELESTIAL_SOURCE_PEARL.get())
                .define('R', irons("mithril_ingot"))
                .save(provider, Genesis.rl("item/items/celestial_source_ingot"));

        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CELESTIAL_SOURCE_PEARL.get(), 5)::unlockedBy, ItemRegistry.CELESTIAL_SOURCE_CRYSTAL.get())
                .pattern("YXY")
                .pattern("XYX")
                .pattern("YXY")
                .define('X', ItemRegistry.CELESTIAL_SOURCE_CRYSTAL.get())
                .define('Y', irons("divine_pearl"))
                .save(provider, Genesis.rl("item/items/celestial_source_pearl"));

        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.TWISTED_CHAOS_INGOT.get())::unlockedBy, ItemRegistry.TWISTED_CHAOS.get())
                .pattern("YXY")
                .pattern("XZX")
                .pattern("YXY")
                .define('X', ItemRegistry.TWISTED_CHAOS.get())
                .define('Y', irons("mithril_scrap"))
                .define('Z', irons("pyrium_ingot"))
                .save(provider, Genesis.rl("item/items/twisted_chaos"));

        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.TWISTED_CHAOS.get())::unlockedBy, ItemRegistry.ARCANE_CRYSTAL.get())
                .pattern(" A ")
                .pattern("DBD")
                .pattern(" C ")
                .define('A', ItemRegistry.ARCANE_CRYSTAL.get())
                .define('B', Items.ECHO_SHARD)
                .define('C', irons("ancient_knowledge_fragment"))
                .define('D', irons("blood_vial"))
                .save(provider, Genesis.rl("item/items/twisted_chaos_ingot"));
    }

    private static void crystals(RegistrateRecipeProvider provider) {
        crystal(provider, "crystal/blood_crystal", ItemRegistry.BLOOD_CRYSTAL.get(), irons("blood_vial"));
        crystal(provider, "crystal/chaos_crystal", ItemRegistry.CHAOS_CRYSTAL.get(), ItemRegistry.TWISTED_CHAOS.get());
        crystal(provider, "crystal/eldritch_crystal", ItemRegistry.ELDRITCH_CRYSTAL.get(), Items.ECHO_SHARD);
        crystal(provider, "crystal/ender_crystal", ItemRegistry.ENDER_CRYSTAL.get(), Items.ENDER_PEARL);
        crystal(provider, "crystal/evocation_crystal", ItemRegistry.EVOCATION_CRYSTAL.get(), Items.EMERALD);
        crystal(provider, "crystal/fire_crystal", ItemRegistry.FIRE_CRYSTAL.get(), Items.BLAZE_ROD);
        crystal(provider, "crystal/holy_crystal", ItemRegistry.HOLY_CRYSTAL.get(), irons("divine_pearl"));
        crystal(provider, "crystal/ice_crystal", ItemRegistry.ICE_CRYSTAL.get(), irons("frozen_bone"));
        crystal(provider, "crystal/lightning_crystal", ItemRegistry.LIGHTNING_CRYSTAL.get(), irons("lightning_bottle"));
        crystal(provider, "crystal/nature_crystal", ItemRegistry.NATURE_CRYSTAL.get(), Items.POISONOUS_POTATO);

        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CELESTIAL_SOURCE_CRYSTAL.get())::unlockedBy, ItemRegistry.ARCANE_CRYSTAL.get())
                .pattern("ABC")
                .pattern("DEF")
                .pattern("GHI")
                .define('A', ItemRegistry.BLOOD_CRYSTAL.get())
                .define('B', ItemRegistry.ELDRITCH_CRYSTAL.get())
                .define('C', ItemRegistry.EVOCATION_CRYSTAL.get())
                .define('D', ItemRegistry.FIRE_CRYSTAL.get())
                .define('E', ItemRegistry.HOLY_CRYSTAL.get())
                .define('F', ItemRegistry.ICE_CRYSTAL.get())
                .define('G', ItemRegistry.LIGHTNING_CRYSTAL.get())
                .define('H', ItemRegistry.NATURE_CRYSTAL.get())
                .define('I', ItemRegistry.ENDER_CRYSTAL.get())
                .save(provider, Genesis.rl("crystal/celestial_crystal"));
    }

    private static void runes(RegistrateRecipeProvider provider) {
        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CELESTIAL_SOURCE_RUNE.get(), 5)::unlockedBy, ItemRegistry.CELESTIAL_SOURCE_PEARL.get())
                .pattern("RFR")
                .pattern("FRF")
                .pattern("RFR")
                .define('F', SpellSchoolRegistry.CELESTIAL_SOURCE_FOCUS)
                .define('R', irons("blank_rune"))
                .save(provider, Genesis.rl("rune/celestial_source_rune"));

        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CHAOS_RUNE.get())::unlockedBy, ItemRegistry.TWISTED_CHAOS.get())
                .pattern("FFF")
                .pattern("FRF")
                .pattern("FFF")
                .define('F', SpellSchoolRegistry.CHAOS_FOCUS)
                .define('R', irons("blank_rune"))
                .save(provider, Genesis.rl("rune/chaos_rune"));
    }

    private static void orbs(RegistrateRecipeProvider provider) {
        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.UPGRADE_ORB_PRO.get())::unlockedBy, ItemRegistry.ARCANE_CRYSTAL.get())
                .pattern("YXY")
                .pattern("XOX")
                .pattern("YXY")
                .define('O', irons("mithril_ingot"))
                .define('X', irons("arcane_essence"))
                .define('Y', ItemRegistry.ARCANE_CRYSTAL.get())
                .save(provider, Genesis.rl("orb/upgrade_orb_pro"));

        orb(provider, "orb/blood_orb_pro", ItemRegistry.BLOOD_ORB_PRO.get(), ItemRegistry.BLOOD_CRYSTAL.get(), irons("blood_rune"));
        orb(provider, "orb/celestial_source_orb_pro", ItemRegistry.CELESTIAL_SOURCE_ORB_PRO.get(), ItemRegistry.CELESTIAL_SOURCE_CRYSTAL.get(), ItemRegistry.CELESTIAL_SOURCE_RUNE.get());
        orb(provider, "orb/chaos_orb_pro", ItemRegistry.CHAOS_ORB_PRO.get(), ItemRegistry.CHAOS_CRYSTAL.get(), ItemRegistry.CHAOS_RUNE.get());
        orb(provider, "orb/ender_orb_pro", ItemRegistry.ENDER_ORB_PRO.get(), ItemRegistry.ENDER_CRYSTAL.get(), irons("ender_rune"));
        orb(provider, "orb/fire_orb_pro", ItemRegistry.FIRE_ORB_PRO.get(), ItemRegistry.FIRE_CRYSTAL.get(), irons("fire_rune"));
        orb(provider, "orb/holy_orb_pro", ItemRegistry.HOLY_ORB_PRO.get(), ItemRegistry.HOLY_CRYSTAL.get(), irons("holy_rune"));
        orb(provider, "orb/ice_orb_pro", ItemRegistry.ICE_ORB_PRO.get(), ItemRegistry.ICE_CRYSTAL.get(), irons("ice_rune"));
        orb(provider, "orb/nature_orb_pro", ItemRegistry.NATURE_ORB_PRO.get(), ItemRegistry.NATURE_CRYSTAL.get(), irons("nature_rune"));
        orb(provider, "orb/thunder_orb_pro", ItemRegistry.THUNDER_ORB_PRO.get(), ItemRegistry.LIGHTNING_CRYSTAL.get(), irons("lightning_rune"));
    }

    private static void bows(RegistrateRecipeProvider provider) {
        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.FLAME_BOW.get())::unlockedBy, ItemRegistry.FIRE_CRYSTAL.get())
                .pattern(" CB")
                .pattern("CAB")
                .pattern(" CB")
                .define('A', irons("pyrium_ingot"))
                .define('B', irons("dragonskin"))
                .define('C', ItemRegistry.FIRE_CRYSTAL.get())
                .save(provider, Genesis.rl("item/bow/flame_bow"));

        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.FROST_LONGBOW.get())::unlockedBy, ItemRegistry.ICE_CRYSTAL.get())
                .pattern(" CB")
                .pattern("CAB")
                .pattern(" CB")
                .define('A', Items.NETHER_STAR)
                .define('B', irons("dragonskin"))
                .define('C', ItemRegistry.ICE_CRYSTAL.get())
                .save(provider, Genesis.rl("item/bow/frost_longbow"));

        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.THUNDER_LONGBOW.get())::unlockedBy, ItemRegistry.LIGHTNING_CRYSTAL.get())
                .pattern(" CB")
                .pattern("CAB")
                .pattern(" CB")
                .define('A', irons("energized_core"))
                .define('B', irons("dragonskin"))
                .define('C', ItemRegistry.LIGHTNING_CRYSTAL.get())
                .save(provider, Genesis.rl("item/bow/thunder_longbow"));

        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.WITCHCRAFT_BOW.get())::unlockedBy, ItemRegistry.FLAME_BOW.get())
                .pattern(" CB")
                .pattern("DAB")
                .pattern(" EB")
                .define('A', irons("divine_soulshard"))
                .define('B', Items.ECHO_SHARD)
                .define('C', ItemRegistry.FROST_LONGBOW.get())
                .define('D', ItemRegistry.FLAME_BOW.get())
                .define('E', ItemRegistry.THUNDER_LONGBOW.get())
                .save(provider, Genesis.rl("item/bow/witchcraft_bow"));
    }

    private static void equipment(RegistrateRecipeProvider provider) {
        smithing(provider, "item/equipment/divine_metal_helmet", ItemRegistry.DIVINE_UPGRADE_SMITHING_TEMPLATE.get(), irons("netherite_mage_helmet"), ItemRegistry.DIVINE_METAL_INGOT.get(), ItemRegistry.DIVINE_METAL_HELMET.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/divine_metal_chestplate", ItemRegistry.DIVINE_UPGRADE_SMITHING_TEMPLATE.get(), irons("netherite_mage_chestplate"), ItemRegistry.DIVINE_METAL_INGOT.get(), ItemRegistry.DIVINE_METAL_CHESTPLATE.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/divine_metal_leggings", ItemRegistry.DIVINE_UPGRADE_SMITHING_TEMPLATE.get(), irons("netherite_mage_leggings"), ItemRegistry.DIVINE_METAL_INGOT.get(), ItemRegistry.DIVINE_METAL_LEGGINGS.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/divine_metal_boots", ItemRegistry.DIVINE_UPGRADE_SMITHING_TEMPLATE.get(), irons("netherite_mage_boots"), ItemRegistry.DIVINE_METAL_INGOT.get(), ItemRegistry.DIVINE_METAL_BOOTS.get(), RecipeCategory.COMBAT);

        smithing(provider, "item/equipment/violet_zenith_helmet", ItemRegistry.VIOLET_UPGRADE_SMITHING_TEMPLATE.get(), irons("netherite_mage_helmet"), ItemRegistry.VIOLET_GALAXY_INGOT.get(), ItemRegistry.VIOLET_ZENITH_HELMET.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/violet_zenith_chestplate", ItemRegistry.VIOLET_UPGRADE_SMITHING_TEMPLATE.get(), irons("netherite_mage_chestplate"), ItemRegistry.VIOLET_GALAXY_INGOT.get(), ItemRegistry.VIOLET_ZENITH_CHESTPLATE.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/violet_zenith_leggings", ItemRegistry.VIOLET_UPGRADE_SMITHING_TEMPLATE.get(), irons("netherite_mage_leggings"), ItemRegistry.VIOLET_GALAXY_INGOT.get(), ItemRegistry.VIOLET_ZENITH_LEGGINGS.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/violet_zenith_boots", ItemRegistry.VIOLET_UPGRADE_SMITHING_TEMPLATE.get(), irons("netherite_mage_boots"), ItemRegistry.VIOLET_GALAXY_INGOT.get(), ItemRegistry.VIOLET_ZENITH_BOOTS.get(), RecipeCategory.COMBAT);

        smithing(provider, "item/equipment/chaos_spell_helmet", ItemRegistry.FLESH_SOUL_FRAGMENT.get(), ItemRegistry.DIVINE_METAL_HELMET.get(), ItemRegistry.TWISTED_CHAOS_INGOT.get(), ItemRegistry.CHAOS_SPELL_HELMET.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/chaos_spell_chestplate", ItemRegistry.FLESH_SOUL_FRAGMENT.get(), ItemRegistry.DIVINE_METAL_CHESTPLATE.get(), ItemRegistry.TWISTED_CHAOS_INGOT.get(), ItemRegistry.CHAOS_SPELL_CHESTPLATE.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/chaos_spell_leggings", ItemRegistry.FLESH_SOUL_FRAGMENT.get(), ItemRegistry.DIVINE_METAL_LEGGINGS.get(), ItemRegistry.TWISTED_CHAOS_INGOT.get(), ItemRegistry.CHAOS_SPELL_LEGGINGS.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/chaos_spell_boots", ItemRegistry.FLESH_SOUL_FRAGMENT.get(), ItemRegistry.DIVINE_METAL_BOOTS.get(), ItemRegistry.TWISTED_CHAOS_INGOT.get(), ItemRegistry.CHAOS_SPELL_BOOTS.get(), RecipeCategory.COMBAT);

        smithing(provider, "item/equipment/celestial_source_spell_helmet", ItemRegistry.CREATE_STAR.get(), ItemRegistry.VIOLET_ZENITH_HELMET.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_HELMET.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/celestial_source_spell_helmet2", ItemRegistry.CREATE_STAR.get(), ItemRegistry.CHAOS_SPELL_HELMET.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_HELMET.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/celestial_source_spell_chestplate", ItemRegistry.CREATE_STAR.get(), ItemRegistry.VIOLET_ZENITH_CHESTPLATE.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_CHESTPLATE.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/celestial_source_spell_chestplate2", ItemRegistry.CREATE_STAR.get(), ItemRegistry.CHAOS_SPELL_CHESTPLATE.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_CHESTPLATE.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/celestial_source_spell_leggings", ItemRegistry.CREATE_STAR.get(), ItemRegistry.VIOLET_ZENITH_LEGGINGS.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_LEGGINGS.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/celestial_source_spell_leggings2", ItemRegistry.CREATE_STAR.get(), ItemRegistry.CHAOS_SPELL_LEGGINGS.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_LEGGINGS.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/celestial_source_spell_boots", ItemRegistry.CREATE_STAR.get(), ItemRegistry.CHAOS_SPELL_BOOTS.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_BOOTS.get(), RecipeCategory.COMBAT);
        smithing(provider, "item/equipment/celestial_source_spell_boots2", ItemRegistry.CREATE_STAR.get(), ItemRegistry.VIOLET_ZENITH_BOOTS.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_BOOTS.get(), RecipeCategory.COMBAT);

        smithing(provider, "item/equipment/mithril_pickaxe", irons("mithril_ingot"), Items.NETHERITE_PICKAXE, irons("mithril_ingot"), ItemRegistry.MITHRIL_PICKAXE.get(), RecipeCategory.TOOLS);
        smithing(provider, "item/equipment/mithril_sword", irons("mithril_ingot"), Items.NETHERITE_SWORD, irons("mithril_ingot"), ItemRegistry.MITHRIL_SWORD.get(), RecipeCategory.COMBAT);
    }

    private static void spellbooks(RegistrateRecipeProvider provider) {
        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.CHAOS_SPELL_BOOK.get())::unlockedBy, ItemRegistry.TWISTED_CHAOS.get())
                .pattern("YBY")
                .pattern("ACA")
                .pattern("YAY")
                .define('A', irons("dragonskin"))
                .define('B', Items.NETHER_STAR)
                .define('C', irons("ruined_book"))
                .define('Y', ItemRegistry.TWISTED_CHAOS.get())
                .save(provider, Genesis.rl("spellbooks/chaos_spell_book"));

        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.DISK_SPELL_BOOK.get())::unlockedBy, ItemRegistry.ARCANE_CRYSTAL.get())
                .pattern("YXY")
                .pattern("ABC")
                .pattern("YXY")
                .define('A', irons("netherite_spell_book"))
                .define('B', Items.NETHER_STAR)
                .define('C', irons("dragonskin_spell_book"))
                .define('X', ItemRegistry.ARCANE_CRYSTAL.get())
                .define('Y', irons("mithril_ingot"))
                .save(provider, Genesis.rl("spellbooks/disk_spell_book"));

        smithing(provider, "spellbooks/celestial_source_spell_book", ItemRegistry.CREATE_STAR.get(), ItemRegistry.DISK_SPELL_BOOK.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_BOOK.get(), RecipeCategory.COMBAT);
        smithing(provider, "spellbooks/celestial_source_spell_book2", ItemRegistry.CREATE_STAR.get(), ItemRegistry.CHAOS_SPELL_BOOK.get(), ItemRegistry.CELESTIAL_SOURCE_INGOT.get(), ItemRegistry.CELESTIAL_SOURCE_SPELL_BOOK.get(), RecipeCategory.COMBAT);
    }

    private static void crystal(RegistrateRecipeProvider provider, String path, ItemLike result, ItemLike catalyst) {
        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 2)::unlockedBy, ItemRegistry.ARCANE_CRYSTAL.get())
                .pattern("YXY")
                .pattern("XYX")
                .pattern("YXY")
                .define('X', ItemRegistry.ARCANE_CRYSTAL.get())
                .define('Y', catalyst)
                .save(provider, Genesis.rl(path));
    }

    private static void orb(RegistrateRecipeProvider provider, String path, ItemLike result, ItemLike crystal, ItemLike rune) {
        unlock(provider, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)::unlockedBy, ItemRegistry.UPGRADE_ORB_PRO.get())
                .pattern("YXY")
                .pattern("XOX")
                .pattern("YXY")
                .define('O', ItemRegistry.UPGRADE_ORB_PRO.get())
                .define('X', crystal)
                .define('Y', rune)
                .save(provider, Genesis.rl(path));
    }

    private static void smithing(
            RegistrateRecipeProvider provider,
            String path,
            ItemLike template,
            ItemLike base,
            ItemLike addition,
            ItemLike result,
            RecipeCategory category
    ) {
        unlock(provider, SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(template),
                Ingredient.of(base),
                Ingredient.of(addition),
                category,
                result.asItem()
        )::unlocks, addition).save(provider, Genesis.rl(path));
    }

    private static <T> T unlock(RegistrateRecipeProvider provider, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, ItemLike item) {
        Item trigger = item.asItem();
        return func.apply("has_" + provider.safeName(trigger), DataIngredient.items(trigger).getCritereon(provider));
    }

    private static ArcaneWorkbenchRecipeBuilder arcane(ItemLike result) {
        return new ArcaneWorkbenchRecipeBuilder(result);
    }

    private static AlchemistCauldronBrewRecipeBuilder alchemistCauldronBrew() {
        return new AlchemistCauldronBrewRecipeBuilder();
    }

    private static Item irons(String path) {
        return item(IRONS_SPELLBOOKS, path);
    }

    private static Item item(String namespace, String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, path);
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(id), "Missing item: " + id);
    }

    private static ResourceLocation ironsLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(IRONS_SPELLBOOKS, path);
    }

    private static final class AlchemistCauldronBrewRecipeBuilder {
        private ResourceLocation baseFluid;
        private int baseAmount;
        private ResourceLocation inputItem;
        private ResourceLocation resultFluid;
        private int resultAmount;

        private AlchemistCauldronBrewRecipeBuilder baseFluid(ResourceLocation fluid, int amount) {
            this.baseFluid = fluid;
            this.baseAmount = amount;
            return this;
        }

        private AlchemistCauldronBrewRecipeBuilder input(ResourceLocation item) {
            this.inputItem = item;
            return this;
        }

        private AlchemistCauldronBrewRecipeBuilder resultFluid(ResourceLocation fluid, int amount) {
            this.resultFluid = fluid;
            this.resultAmount = amount;
            return this;
        }

        private void save(Consumer<FinishedRecipe> provider, ResourceLocation id) {
            if (baseFluid == null || inputItem == null || resultFluid == null) {
                throw new IllegalStateException("Incomplete alchemist cauldron brew recipe: " + id);
            }
            provider.accept(new AlchemistCauldronBrewRecipe(
                    id,
                    baseFluid,
                    baseAmount,
                    inputItem,
                    resultFluid,
                    resultAmount
            ));
        }
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

    private static final class ArcaneWorkbenchRecipeBuilder {
        private final ItemStack result;
        private final List<String> pattern = new ArrayList<>();
        private final Map<Character, Ingredient> key = new LinkedHashMap<>();
        private CraftingBookCategory category = CraftingBookCategory.MISC;
        private boolean showNotification = true;

        private ArcaneWorkbenchRecipeBuilder(ItemLike result) {
            this.result = new ItemStack(result);
        }

        private ArcaneWorkbenchRecipeBuilder pattern(String line) {
            pattern.add(line);
            return this;
        }

        private ArcaneWorkbenchRecipeBuilder define(char symbol, ItemLike item) {
            key.put(symbol, Ingredient.of(item));
            return this;
        }

        @SuppressWarnings("unused")
        private ArcaneWorkbenchRecipeBuilder category(CraftingBookCategory category) {
            this.category = category;
            return this;
        }

        @SuppressWarnings("unused")
        private ArcaneWorkbenchRecipeBuilder showNotification(boolean showNotification) {
            this.showNotification = showNotification;
            return this;
        }

        private void save(Consumer<FinishedRecipe> provider, ResourceLocation id) {
            provider.accept(new Result(id, result, List.copyOf(pattern), Map.copyOf(key), category, showNotification));
        }

        private record Result(
                ResourceLocation id,
                ItemStack result,
                List<String> pattern,
                Map<Character, Ingredient> key,
                CraftingBookCategory category,
                boolean showNotification
        ) implements FinishedRecipe {
            @Override
            public void serializeRecipeData(JsonObject json) {
                json.addProperty("category", category.getSerializedName());

                JsonArray patternJson = new JsonArray();
                pattern.forEach(patternJson::add);
                json.add("pattern", patternJson);

                JsonObject keyJson = new JsonObject();
                key.forEach((symbol, ingredient) -> keyJson.add(String.valueOf(symbol), ingredient.toJson()));
                json.add("key", keyJson);

                JsonObject resultJson = new JsonObject();
                ResourceLocation itemId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.getItem()), "Missing result item id");
                resultJson.addProperty("item", itemId.toString());
                resultJson.addProperty("count", result.getCount());
                json.add("result", resultJson);
                json.addProperty("show_notification", showNotification);
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ModRecipeSerializers.ARCANE_WORKBENCH_RECIPE_SERIALIZER.get();
            }

            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        }
    }
}
