package miku.united_as_one.genesis.registries.workbench;

import miku.united_as_one.genesis.contents.workbench.arcane.ArcaneWorkbenchRecipe;
import miku.united_as_one.genesis.contents.workbench.arcane_cauldron.ArcaneCauldronRecipe;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Genesis.MOD_ID);

    // 奥术工作台配方 JSON 的读写序列化器。
    public static final RegistryObject<RecipeSerializer<ArcaneWorkbenchRecipe>> ARCANE_WORKBENCH_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("arcane_workbench_recipe", ArcaneWorkbenchRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<ArcaneCauldronRecipe>> ARCANE_CAULDRON_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("arcane_cauldron", ArcaneCauldronRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
