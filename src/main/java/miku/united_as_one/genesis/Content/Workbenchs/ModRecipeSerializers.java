package miku.united_as_one.genesis.Content.Workbenchs;

import miku.united_as_one.genesis.Content.ArcaneWorkbench.ArcaneWorkbenchRecipe;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Genesis.MOD_ID);

    public static final RegistryObject<RecipeSerializer<ArcaneWorkbenchRecipe>> ARCANE_WORKBENCH_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("arcane_workbench_recipe", ArcaneWorkbenchRecipe.Serializer::new);
    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
