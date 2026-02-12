package miku.united_as_one.genesis.common.data.datagen.provider;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ModItemModelProvider extends ItemModelProvider {

    public static final String GENERATED = "item/generated";

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Genesis.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<RegistryObject<Item>> items = new HashSet<>();
        items.addAll(ItemRegistry.REGISTRY_BLOCK_ITEM.getEntries());
        items.forEach(item -> itemGenerateModel(
                item.get(), resourceItem(item)
        ));
    }

    public void itemGenerateModel(Item item, ResourceLocation location){
        withExistingParent(itemName(item), GENERATED).texture("layer0", location);
    }

    public String itemName(Item item){
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();
    }

    public ResourceLocation resourceItem(RegistryObject<Item> item){
        String prefix = "item/";
        if(item.get() instanceof BlockItem) prefix = "block/";
        if(item.getId() != null) return ResourceLocation.fromNamespaceAndPath(Genesis.MODID, prefix + item.getId().getPath());
        else throw new IllegalArgumentException("Unknown item id.");
    }
}
