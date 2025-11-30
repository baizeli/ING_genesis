package com.baizeli.eternisstarrysky.Content.Workbenchs;

import com.baizeli.eternisstarrysky.Content.ArcaneWorkbench.ArcaneWorkbenchRecipe;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes
{
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, EternisStarrySky.MOD_ID);

    public static final RegistryObject<RecipeType<VanillaWorkbenchRecipe>> VANILLA_WORKBENCH_TYPE = RECIPE_TYPES.register("vanilla_workbench", () -> RecipeType.simple(EternisStarrySky.rl("vanilla_workbench")));
    public static final RegistryObject<RecipeType<ArcaneWorkbenchRecipe>> ARCANE_WORKBENCH_RECIPE_TYPE = RECIPE_TYPES.register("arcane_workbench", () -> RecipeType.simple(EternisStarrySky.rl("arcane_workbench")));

    public static void register(IEventBus eventBus) {RECIPE_TYPES.register(eventBus);}
}

