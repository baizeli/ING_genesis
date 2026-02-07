package miku.united_as_one.genesis.common.data.datagen;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.data.damage.DamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGenerator extends TagsProvider<DamageType> {
    public static final TagKey<DamageType> CHAOS_MAGIC = create("chaos_magic");
    public static final TagKey<DamageType> CELESTIAL_SOURCE_MAGIC = create("celestial_source_magic");

    public DamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, Genesis.MODID, existingFileHelper);
    }

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Genesis.MODID, name));
    }

    @Override
    protected void addTags(@NotNull HolderLookup.@NotNull Provider provider) {
        this.tag(CELESTIAL_SOURCE_MAGIC).add(
                DamageTypes.CELESTIAL_SOURCE_MAGIC
        );

        this.tag(CHAOS_MAGIC).add(
                DamageTypes.CHAOS_MAGIC
        );
    }
}