package com.baizeli.eternisstarrysky.Items;

import com.baizeli.eternisstarrysky.Content.ModBlock;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EternisStarrySky.MOD_ID);

    public static final RegistryObject<Item> BAG = ITEMS.register("bag", () -> new BagItem(new Item.Properties(), 0));
    public static final RegistryObject<Item> primogem = ITEMS.register("primogem", () -> new BagItem(new Item.Properties(), 1));

    public static final RegistryObject<Item> ETERNIS_APPLE = ITEMS.register("eternis_apple",
            () -> new EternisAppleItem(new Item.Properties()
                    .stacksTo(64)
                    .rarity(Rarity.EPIC)
            ));

    public static final RegistryObject<Item> PEACH = ITEMS.register("peach",
            () -> new Peach(new Item.Properties()
                    .stacksTo(64)
                    .rarity(Rarity.RARE)
            ));

    public static final RegistryObject<Item> infinity_sword = ITEMS.register("infinity_sword",
            () -> new InfinitySwordItem(
                    Tiers.NETHERITE,
                    1,
                    -2F,
                    new Item.Properties().durability(Integer.MAX_VALUE), false
            )
    );

    public static final RegistryObject<Item> infinity_sword_true = ITEMS.register("infinity_sword_true",
            () -> new InfinitySwordItem(
                    Tiers.NETHERITE,
                    Integer.MAX_VALUE,
                    -2F,
                    new Item.Properties().durability(Integer.MAX_VALUE), true
            )
    );

    public static final RegistryObject<Item> BOW = ITEMS.register("bow_pr",
            () -> new NewBowItem(
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .stacksTo(1)
                            .durability(384 * 3)
            )
    );

    public static final RegistryObject<Item> create_star = ITEMS.register("create_star",
            () -> new CreateStar(
                    new Item.Properties()
                            .rarity(Rarity.COMMON)
            )
    );

    public static final RegistryObject<Item> cjzg = ITEMS.register("cjzg",
            () -> new Item(
                    new Item.Properties()
                            .rarity(Rarity.COMMON)
                            .stacksTo(64)
            )
    );

    public static final RegistryObject<Item> dragon_book = ITEMS.register("galaxy_scroll",
            () -> new GalaxyScroll(
                    new Item.Properties()
                            .rarity(Rarity.RARE)
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<Item> bjzg = ITEMS.register("bjzg",
            () -> new BJZGItem(
                    new Item.Properties()
                            .rarity(Rarity.RARE)
                            .stacksTo(64)
                            .food(new FoodProperties.Builder()
                                    .nutrition(0)
                                    .saturationMod(0.0f)
                                    .alwaysEat()
                                    .build())
            )
    );

    public static final RegistryObject<Item> workbench_item = ITEMS.register("workbench", () -> new BlockItem(ModBlock.workbench.get(), new Item.Properties()));

    // 无尽永恒盔甲套装
    public static final RegistryObject<Item> INFINITY_ETERNAL_HELMET = ITEMS.register("infinity_eternal_helmet",
            () -> new InfinityEternalArmorItem(ModArmorMaterials.INFINITY_ETERNAL, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> INFINITY_ETERNAL_CHESTPLATE = ITEMS.register("infinity_eternal_chestplate",
            () -> new InfinityEternalArmorItem(ModArmorMaterials.INFINITY_ETERNAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> INFINITY_ETERNAL_LEGGINGS = ITEMS.register("infinity_eternal_leggings",
            () -> new InfinityEternalArmorItem(ModArmorMaterials.INFINITY_ETERNAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> INFINITY_ETERNAL_BOOTS = ITEMS.register("infinity_eternal_boots",
            () -> new InfinityEternalArmorItem(ModArmorMaterials.INFINITY_ETERNAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.RARE)));
}