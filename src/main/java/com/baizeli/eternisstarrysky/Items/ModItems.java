package com.baizeli.eternisstarrysky.Items;

import com.baizeli.eternisstarrysky.Content.ModBlock;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Items.Staff.CelestialSourceStaff;
import com.baizeli.eternisstarrysky.Items.Staff.ChaosStaff;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EternisStarrySky.MOD_ID);

    public static final RegistryObject<Item> PURPLEITE_GALAXY_INGOT = ITEMS.register("purpleite_galaxy_ingot", () -> new EternisMaterial(new Item.Properties(), 0));

    // 神圣金属锭
    public static final RegistryObject<Item> DIVINE_METAL_INGOT = ITEMS.register("divine_metal_ingot",
            () -> new Item(new Item.Properties()
                    .rarity(Rarity.EPIC)
            ));

    // 扭曲之混沌-[混沌]法术材料
    public static final RegistryObject<Item> TWISTED_CHAOS = ITEMS.register("twisted_chaos",
            () -> new Item(new Item.Properties()
                    .rarity(Rarity.EPIC)
            ));

    public static final RegistryObject<Item> ETERNIS_APPLE = ITEMS.register("eternis_apple",
            () -> new EternisAppleItem(new Item.Properties()
                    .stacksTo(64)
                    .rarity(Rarity.EPIC)
            ));

    public static final RegistryObject<Item> GOOD_CAKE = ITEMS.register("good_cake",GoodCake :: new);

    public static final RegistryObject<Item> INFINITY_SWORD = ITEMS.register("infinity_sword",
        () -> new InfinitySword(
            Tiers.NETHERITE,
            (int) (42 - Tiers.NETHERITE.getAttackDamageBonus()),
            -2F,
            new Item.Properties().durability(Integer.MAX_VALUE)
        )
    );

    public static final RegistryObject<Item> AVARITIA_SWORD = ITEMS.register("avaritia_infinity_sword",
            () -> new AvaritiaSword(
                    Integer.MAX_VALUE,
                    -2F,
                    new Item.Properties().durability(Integer.MAX_VALUE)
            )
    );
    public static final RegistryObject<Item> WHISPER_OF_THE_PAST = ITEMS.register("whisper_of_the_past",
            () -> new NewBowItem(
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .stacksTo(1)
                            .durability(384 * 3)
            )
    );

    public static final RegistryObject<Item> CREATE_STAR = ITEMS.register("create_star",
            () -> new CreateStar(
                    new Item.Properties()
                            .rarity(Rarity.COMMON)
            )
    );

    public static final RegistryObject<Item> GALAXY_SCROLL = ITEMS.register("galaxy_scroll",
            () -> new GalaxyScroll(
                    new Item.Properties()
                            .rarity(Rarity.RARE)
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<Item> WORKBENCH = ITEMS.register("workbench", () -> new BlockItem(ModBlock.workbench.get(), new Item.Properties()));

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

    // 神圣金属盔甲套装
    public static final RegistryObject<Item> DIVINE_METAL_HELMET = ITEMS.register("divine_metal_helmet",
            () -> new DivineMetalArmorItem((IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DIVINE_METAL_CHESTPLATE = ITEMS.register("divine_metal_chestplate",
            () -> new DivineMetalArmorItem((IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DIVINE_METAL_LEGGINGS = ITEMS.register("divine_metal_leggings",
            () -> new DivineMetalArmorItem((IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DIVINE_METAL_BOOTS = ITEMS.register("divine_metal_boots",
            () -> new DivineMetalArmorItem((IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> CHAOS_SPELL_BOOK = ITEMS.register("chaos_spell_book", ChaosSpellBook::new);

    public static final RegistryObject<Item> CELESTIAL_SOURCE_SPELL_BOOK = ITEMS.register("celestial_source_spell_book", CelestialSourceSpellBook::new);

    // 混沌法杖
    public static final RegistryObject<Item> CHAOS_STAFF = ITEMS.register("chaos_staff", ChaosStaff::new);

    // 星源法杖
    public static final RegistryObject<Item> CELESTIAL_SOURCE_STAFF = ITEMS.register("celestial_source_staff", CelestialSourceStaff::new);
}