package miku.united_as_one.genesis.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.contents.workbench.arcane_cauldron.ArcaneCauldronRecipeCategory;
import miku.united_as_one.genesis.registries.block.BlockRegistry;
import miku.united_as_one.genesis.registries.workbench.ModRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

@JeiPlugin
public class GenesisJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return Genesis.rl("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ArcaneCauldronRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            registration.addRecipes(
                    ArcaneCauldronRecipeCategory.ARCANE_CAULDRON_RECIPE_TYPE,
                    level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.ARCANE_CAULDRON_RECIPE_TYPE.get())
            );
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(BlockRegistry.ARCANE_CAULDRON.get(), ArcaneCauldronRecipeCategory.ARCANE_CAULDRON_RECIPE_TYPE);
    }
}
