package miku.united_as_one.genesis.Content.Workbenchs;

import miku.united_as_one.genesis.Content.ArcaneWorkbench.ArcaneWorkbenchRecipe;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes
{
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Genesis.MOD_ID);

    public static final RegistryObject<RecipeType<ArcaneWorkbenchRecipe>> ARCANE_WORKBENCH_RECIPE_TYPE = RECIPE_TYPES.register("arcane_workbench", () -> RecipeType.simple(Genesis.rl("arcane_workbench")));

    public static void register(IEventBus eventBus) {RECIPE_TYPES.register(eventBus);}
}

