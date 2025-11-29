package com.baizeli.eternisstarrysky.Content.Workbenchs;

import com.baizeli.eternisstarrysky.Content.ArcaneWorkbench.ArcaneWorkbenchRecipe;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EternisStarrySky.MOD_ID);

    public static final RegistryObject<RecipeSerializer<VanillaWorkbenchRecipe>> VANILLA_WORKBENCH_RECIPE = RECIPE_SERIALIZERS.register("vanilla_workbench_s", VanillaWorkbenchRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<ArcaneWorkbenchRecipe>> ARCANE_WORKBENCH_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("arcane_workbench_recipe", ArcaneWorkbenchRecipe.Serializer::new);
    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
