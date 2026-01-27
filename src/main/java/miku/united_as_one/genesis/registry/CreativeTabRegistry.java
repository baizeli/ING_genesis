package miku.united_as_one.genesis.registry;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

@SuppressWarnings("removal")
public class CreativeTabRegistry {
    public static final ResourceKey<CreativeModeTab> IRON_SPELLS_GENESIS_BLOCK = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB, new ResourceLocation(Genesis.MOD_ID, "block"));

    public static final ResourceKey<CreativeModeTab> IRON_SPELLS_GENESIS_MATERIAL = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB, new ResourceLocation(Genesis.MOD_ID, "material"));

    public static final ResourceKey<CreativeModeTab> IRON_SPELLS_GENESIS_EQUIPMENT = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB, new ResourceLocation(Genesis.MOD_ID, "equipment"));

    static {
        Genesis.L2_REGISTRATE
            .buildModCreativeTab("block", "itemGroup." + Genesis.MOD_ID, builder -> builder
                .icon(() -> ItemRegistry.CELESTIAL_SOURCE_BLOCK_ITEM.get().getDefaultInstance())
            );
            
        Genesis.L2_REGISTRATE
            .buildModCreativeTab("material", "itemGroup." + Genesis.MOD_ID, builder -> builder
                .icon(() -> ItemRegistry.GALAXY_SCROLL.get().getDefaultInstance())
            );

        Genesis.L2_REGISTRATE
            .buildModCreativeTab("equipment", "itemGroup." + Genesis.MOD_ID, builder -> builder
                .icon(() -> ItemRegistry.AVARITIA_SWORD.get().getDefaultInstance())
            );

        Genesis.L2_REGISTRATE.defaultCreativeTab(CreativeModeTabs.SEARCH);
    }

    public static void register() {}
}