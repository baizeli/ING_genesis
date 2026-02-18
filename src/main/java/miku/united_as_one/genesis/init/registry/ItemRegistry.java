package miku.united_as_one.genesis.init.registry;

import com.tterrag.registrate.providers.*;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.items.*;
import miku.united_as_one.genesis.common.items.spellbook.AEprospellbook;
import miku.united_as_one.genesis.common.items.spellbook.CelestialSourceSpellBook;
import miku.united_as_one.genesis.common.items.spellbook.ChaosSpellBook;
import miku.united_as_one.genesis.common.items.staff.*;
import miku.united_as_one.genesis.common.items.armor.*;
import miku.united_as_one.genesis.common.items.bow.*;
import miku.united_as_one.genesis.common.items.curios.*;
import miku.united_as_one.genesis.common.items.curios.rune_plus.*;
import miku.united_as_one.genesis.common.items.manuscript.*;
import miku.united_as_one.genesis.common.items.pickaxe.*;
import miku.united_as_one.genesis.common.items.sword.*;
import miku.united_as_one.genesis.client.tooltipParticleHandler.*;
import miku.united_as_one.genesis.common.spell.UpgradeOrbTypes;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.*;

import java.util.*;

@SuppressWarnings("removal")
public class ItemRegistry {
    public static final DeferredRegister<Item> REGISTRY_BLOCK_ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, Genesis.MODID);

    // 紫极锭
    public static final ItemEntry<EternisMaterial> PURPLEITE_GALAXY_INGOT;
    // 神圣金属锭
    public static final ItemEntry<Item> DIVINE_METAL_INGOT;
    // 扭曲混沌锭
    public static final ItemEntry<? extends Item> TWISTED_CHAOS_INGOT;
    // 扭曲之混沌
    public static final ItemEntry<ChaosBaseItem> TWISTED_CHAOS;
    // 星源珍珠
    public static final ItemEntry<CelestialSourceBaseItem> CELESTIAL_SOURCE_PEARL;
    // 星源锭
    public static final ItemEntry<CelestialSourceBaseItem> CELESTIAL_SOURCE_INGOT;
    // 寰宇大苹果
    public static final ItemEntry<EternisAppleItem> ETERNIS_APPLE;
    // good_cake
    public static final ItemEntry<GoodCake> GOOD_CAKE;
    // 天火圣裁 [伪]
    public static final ItemEntry<InfinitySword> INFINITY_SWORD;
    // 暮光极致者
    public static final ItemEntry<AvaritiaSword> AVARITIA_SWORD;
    // 秘银剑
    public static final ItemEntry<MithrilSword> MITHRIL_SWORD;
    // 秘银镐
    public static final ItemEntry<MithrilPickaxe> MITHRIL_PICKAXE;
    // 雷霆长弓
    public static final ItemEntry<ThunderLongbow> THUNDER_LONGBOW;
    // 冰霜长弓
    public static final ItemEntry<FrostLongbow> FROST_LONGBOW;
    // 巫术弓
    public static final ItemEntry<WitchcraftBow> WITCHCRAFT_BOW;
    // 火焰弓
    public static final ItemEntry<FlameBow> FLAME_BOW;
    // 创造之星
    public static final ItemEntry<CreateStar> CREATE_STAR;
    // 星源绘卷
    public static final ItemEntry<GalaxyScroll> GALAXY_SCROLL;
    // 奥术工作台
    public static final ItemEntry<BlockItem> ARCANE_WORKBENCH;
    // 神圣金属套
    public static final ItemEntry<DivineMetalArmor> DIVINE_METAL_HELMET;
    public static final ItemEntry<DivineMetalArmor> DIVINE_METAL_CHESTPLATE;
    public static final ItemEntry<DivineMetalArmor> DIVINE_METAL_LEGGINGS;
    public static final ItemEntry<DivineMetalArmor> DIVINE_METAL_BOOTS;
    // 星源法术套
    public static final ItemEntry<CelestialSourceSpellArmor> CELESTIAL_SOURCE_SPELL_HELMET;
    public static final ItemEntry<CelestialSourceSpellArmor> CELESTIAL_SOURCE_SPELL_CHESTPLATE;
    public static final ItemEntry<CelestialSourceSpellArmor> CELESTIAL_SOURCE_SPELL_LEGGINGS;
    public static final ItemEntry<CelestialSourceSpellArmor> CELESTIAL_SOURCE_SPELL_BOOTS;
    // 混沌法术套
    public static final ItemEntry<ChaosSpellArmor> CHAOS_SPELL_HELMET;
    public static final ItemEntry<ChaosSpellArmor> CHAOS_SPELL_CHESTPLATE;
    public static final ItemEntry<ChaosSpellArmor> CHAOS_SPELL_LEGGINGS;
    public static final ItemEntry<ChaosSpellArmor> CHAOS_SPELL_BOOTS;
    // 紫极战斗套
    public static final ItemEntry<VioletZenithArmor> VIOLET_ZENITH_HELMET;
    public static final ItemEntry<VioletZenithArmor> VIOLET_ZENITH_CHESTPLATE;
    public static final ItemEntry<VioletZenithArmor> VIOLET_ZENITH_LEGGINGS;
    public static final ItemEntry<VioletZenithArmor> VIOLET_ZENITH_BOOTS;
    // 混沌法术书
    public static final ItemEntry<ChaosSpellBook> CHAOS_SPELL_BOOK;
    // 星源法术书
    public static final ItemEntry<CelestialSourceSpellBook> CELESTIAL_SOURCE_SPELL_BOOK;
    //法术磁盘
    public static final ItemEntry<AEprospellbook> DISK_SPELL_BOOK;
    // 混沌法杖
    public static final ItemEntry<ChaosStaff> CHAOS_STAFF;
    // 星源法杖
    public static final ItemEntry<CelestialSourceStaff> CELESTIAL_SOURCE_STAFF;
    // 飞燕穿柳
    public static final ItemEntry<FlyingSwallowThroughWillow> FLYING_SWALLOW_THROUGH_Willow;
    // 混沌符文
    public static final ItemEntry<Item> CHAOS_RUNE;
    // 星源符文
    public static final ItemEntry<Item> CELESTIAL_SOURCE_RUNE;
    // 混沌升级法球
    public static final ItemEntry<UpgradeOrbItem> CHAOS_UPGRADE_ORB;
    // 星源升级法球
    public static final ItemEntry<UpgradeOrbItem> CELESTIAL_SOURCE_UPGRADE_ORB;
    // 邪术升级法球
    public static final ItemEntry<UpgradeOrbItem> ELDRITCH_UPGRADE_ORB;
    // 烈焰穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> FIRE_ORB_PRO;
    // 神圣穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> HOLY_ORB_PRO;
    //冰霜穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> ICE_ORB_PRO;
    // 猩红穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> BLOOD_ORB_PRO;
    // 末影穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> ENDER_ORB_PRO;
    // 雷霆穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> THUNDER_ORB_PRO;
    // 自然穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> NATURE_ORB_PRO;
    // 邪术穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> WARLOCK_ORB_PRO;
    // 混沌穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> CHAOS_ORB_PRO;
    // 星源穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> CELESTIAL_SOURCE_ORB_PRO;
    // 老王237
    public static final ItemEntry<LaoWang237Curios> LAO_WANG_237;
    // 创世之诅咒
    public static final ItemEntry<GenesisCurseItem> GENESIS_CURSE;
    // 无限忏悔石
    public static final ItemEntry<InfiniteShrivingStoneItem> INFINITE_SHRIVING_STONE;
    // 空白高级法球
    public static final ItemEntry<Item> UPGRADE_ORB_PRO;
    // 奥术水晶
    public static final ItemEntry<Item> ARCANE_CRYSTAL;
    // 猩红水晶
    public static final ItemEntry<Item> BLOOD_CRYSTAL;
    // 邪术水晶
    public static final ItemEntry<Item> ELDRITCH_CRYSTAL;
    // 末影水晶
    public static final ItemEntry<Item> ENDER_CRYSTAL;
    // 唤魔水晶
    public static final ItemEntry<Item> EVOCATION_CRYSTAL;
    // 炽焰水晶
    public static final ItemEntry<Item> FIRE_CRYSTAL;
    // 神圣水晶
    public static final ItemEntry<Item> HOLY_CRYSTAL;
    // 冰霜水晶
    public static final ItemEntry<Item> ICE_CRYSTAL;
    // 雷霆水晶
    public static final ItemEntry<Item> LIGHTNING_CRYSTAL;
    // 自然水晶
    public static final ItemEntry<Item> NATURE_CRYSTAL;
    // 混沌水晶
    public static final ItemEntry<Item> CHAOS_CRYSTAL;
    // 星源水晶
    public static final ItemEntry<CelestialSourceBaseItem> CELESTIAL_SOURCE_CRYSTAL;
    // 混沌手稿
    public static final ItemEntry<ChaosManuscript> CHAOS_MANUSCRIPT;
    // 星源手稿
    public static final ItemEntry<CelestialSourceManuscript> CELESTIAL_SOURCE_MANUSCRIPT;
    // 混沌手稿碎片
    public static final ItemEntry CHAOS_MANUSCRIPT_FRAGMENT;
    // 空白星源手稿碎片
    public static final ItemEntry BLANK_CELESTIAL_SOURCE_MANUSCRIPT;
    // 星源块
    public static final ItemEntry<BlockItem> CELESTIAL_SOURCE_BLOCK_ITEM;
    // 奥术水晶矿
    public static final ItemEntry<BlockItem> ARCANE_CRYSTAL_ORE_ITEM;
    // 深层奥术水晶矿
    public static final ItemEntry<BlockItem> ARCANE_CRYSTAL_ORE_DEEPSLATE_ITEM;
    // 下界奥术水晶矿
    public static final ItemEntry<BlockItem> NETHER_ARCANE_CRYSTAL_ORE_ITEM;
    // 末地奥术水晶矿
    public static final ItemEntry<BlockItem> END_ARCANE_CRYSTAL_ORE_ITEM;
    // 血肉魂铃
    public static final ItemEntry<Item> FLESH_SOUL_BELL;
    // 混沌原核
    public static final ItemEntry<ChaosCore> CHAOS_CORE;
    // 血肉灵魂碎片
    public static final ItemEntry<Item> FLESH_SOUL_FRAGMENT;
    // 恒久之戒
    public static final ItemEntry<EternalRing> ETERNAL_RING;
    // 雷霆胸饰
    public static final ItemEntry<LightningRunePlus> LIGHTNING_RUNE_PLUS;
    // 自然手镯
    public static final ItemEntry<NatureRunePlus> NATURE_RUNE_PLUS;
    // 末影指环
    public static final ItemEntry<EnderRunePlus> ENDER_RUNE_PLUS;
    // 神圣拳套
    public static final ItemEntry<HolyRunePlus> HOLY_RUNE_PLUS;
    // 冰霜脚链
    public static final ItemEntry<IceRunePlus> ICE_RUNE_PLUS;
    // 猩红之牙
    public static final ItemEntry<BloodRunePlus> BLOOD_RUNE_PLUS;
    // 赤焰手饰
    public static final ItemEntry<FireRunePlus> FIRE_RUNE_PLUS;
    // 邪术符文
    public static final ItemEntry<EldritchRunePlus> ELDRITCH_RUNE_PLUS;
    // 锻造模板
    public static final ItemEntry<Item> EVIOLET_ZENITH_TEMPLATE;
    // 锻造模板
    public static final ItemEntry<Item> DIVINE_TEMPLATE;

    //初始化
    static {
        PURPLEITE_GALAXY_INGOT = Genesis.L2_REGISTRATE
                .item("purpleite_galaxy_ingot", properties -> new EternisMaterial(properties, 0))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        DIVINE_METAL_INGOT = Genesis.L2_REGISTRATE
                .item("divine_metal_ingot", properties -> new Item(properties.rarity(Rarity.EPIC)))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        DIVINE_TEMPLATE = Genesis.L2_REGISTRATE
                .item("divine_template", properties -> new Item(properties.rarity(Rarity.EPIC)))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        EVIOLET_ZENITH_TEMPLATE = Genesis.L2_REGISTRATE
                .item("eviolet_smithing_template", properties -> new Item(properties.rarity(Rarity.EPIC)))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();


        TWISTED_CHAOS_INGOT = Genesis.L2_REGISTRATE
                .item("twisted_chaos_ingot", properties -> new ChaosBaseItem(properties.rarity(Rarity.EPIC)))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        TWISTED_CHAOS = Genesis.L2_REGISTRATE
                .item("twisted_chaos", properties -> new ChaosBaseItem(properties.rarity(Rarity.EPIC)))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CELESTIAL_SOURCE_PEARL = Genesis.L2_REGISTRATE
                .item("celestial_source_pearl", properties -> new CelestialSourceBaseItem(properties.rarity(Rarity.EPIC)))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CELESTIAL_SOURCE_INGOT = Genesis.L2_REGISTRATE
                .item("celestial_source_ingot", properties -> new CelestialSourceBaseItem(properties.rarity(Rarity.EPIC)))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ETERNIS_APPLE = Genesis.L2_REGISTRATE
                .item("eternis_apple", properties -> new EternisAppleItem(properties.stacksTo(64).rarity(Rarity.EPIC)))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        GOOD_CAKE = Genesis.L2_REGISTRATE
                .item("good_cake", properties -> new GoodCake())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        INFINITY_SWORD = Genesis.L2_REGISTRATE
                .item("infinity_sword", properties -> new InfinitySword(
                        Tiers.NETHERITE,
                        (int) (42 - Tiers.NETHERITE.getAttackDamageBonus()),
                        -2F,
                        properties.durability(Integer.MAX_VALUE)
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        AVARITIA_SWORD = Genesis.L2_REGISTRATE
                .item("avaritia_infinity_sword", properties -> new AvaritiaSword(
                        Integer.MAX_VALUE,
                        -2F,
                        properties.durability(Integer.MAX_VALUE)
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        MITHRIL_SWORD = Genesis.L2_REGISTRATE
                .item("mithril_sword", properties -> new MithrilSword(
                        TierRegistry.MITHRIL,
                        7,
                        -1.7F,
                        properties
                ))
                .model((ctx, prov) -> prov.handheld(ctx::getEntry))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        MITHRIL_PICKAXE = Genesis.L2_REGISTRATE
                .item("mithril_pickaxe", properties -> new MithrilPickaxe(
                        TierRegistry.MITHRIL,
                        0,
                        -1.6F,
                        properties
                ))
                .model((ctx, prov) -> prov.handheld(ctx::getEntry))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        THUNDER_LONGBOW = Genesis.L2_REGISTRATE
                .item("thunder_longbow", properties -> new ThunderLongbow(properties
                        .rarity(Rarity.EPIC)
                        .stacksTo(1)
                        .durability(2009)
                ))
                .model(ItemRegistry::createBowModel)
                /*.model((ctx, prov) -> {
                    prov.withExistingParent(ctx.getName(), new ResourceLocation(Genesis.MOD_ID, "item/bow/thunder_longbow"));
                })*/
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        FROST_LONGBOW = Genesis.L2_REGISTRATE
                .item("frost_longbow", properties -> new FrostLongbow(properties
                        .rarity(Rarity.EPIC)
                        .stacksTo(1)
                        .durability(2009)
                ))
                .model(ItemRegistry::createBowModel)
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        WITCHCRAFT_BOW = Genesis.L2_REGISTRATE
                .item("witchcraft_bow", properties -> new WitchcraftBow(properties
                        .rarity(Rarity.EPIC)
                        .stacksTo(1)
                        .durability(2009)
                ))
                .model(ItemRegistry::createBowModel)
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        FLAME_BOW = Genesis.L2_REGISTRATE
                .item("flame_bow", properties -> new FlameBow(properties
                        .rarity(Rarity.EPIC)
                        .stacksTo(1)
                        .durability(2009)
                ))
                .model(ItemRegistry::createBowModel)
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CREATE_STAR = Genesis.L2_REGISTRATE
                .item("create_star", properties -> new CreateStar(properties
                        .rarity(Rarity.COMMON)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        GALAXY_SCROLL = Genesis.L2_REGISTRATE
                .item("galaxy_scroll", properties -> new GalaxyScroll(properties
                        .rarity(Rarity.RARE)
                        .stacksTo(1)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ARCANE_WORKBENCH = Genesis.L2_REGISTRATE
                .item("arcane_workbench", properties -> new BlockItem(BlockRegistry.ARCANE_WORKBENCH.get(), properties))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                .register();

        DIVINE_METAL_HELMET = Genesis.L2_REGISTRATE
                .item("divine_metal_helmet", properties -> new DivineMetalArmor(
                        (IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.HELMET, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        DIVINE_METAL_CHESTPLATE = Genesis.L2_REGISTRATE
                .item("divine_metal_chestplate", properties -> new DivineMetalArmor(
                        (IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.CHESTPLATE, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        DIVINE_METAL_LEGGINGS = Genesis.L2_REGISTRATE
                .item("divine_metal_leggings", properties -> new DivineMetalArmor(
                        (IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.LEGGINGS, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        DIVINE_METAL_BOOTS = Genesis.L2_REGISTRATE
                .item("divine_metal_boots", properties -> new DivineMetalArmor(
                        (IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.BOOTS, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CELESTIAL_SOURCE_SPELL_HELMET = Genesis.L2_REGISTRATE
                .item("celestial_source_spell_helmet", properties -> new CelestialSourceSpellArmor(
                        ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.HELMET, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CELESTIAL_SOURCE_SPELL_CHESTPLATE = Genesis.L2_REGISTRATE
                .item("celestial_source_spell_chestplate", properties -> new CelestialSourceSpellArmor(
                        ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.CHESTPLATE, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CELESTIAL_SOURCE_SPELL_LEGGINGS = Genesis.L2_REGISTRATE
                .item("celestial_source_spell_leggings", properties -> new CelestialSourceSpellArmor(
                        ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.LEGGINGS, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CELESTIAL_SOURCE_SPELL_BOOTS = Genesis.L2_REGISTRATE
                .item("celestial_source_spell_boots", properties -> new CelestialSourceSpellArmor(
                        ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.BOOTS, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CHAOS_SPELL_HELMET = Genesis.L2_REGISTRATE
                .item("chaos_spell_helmet", properties -> new ChaosSpellArmor(
                        ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.HELMET, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CHAOS_SPELL_CHESTPLATE = Genesis.L2_REGISTRATE
                .item("chaos_spell_chestplate", properties -> new ChaosSpellArmor(
                        ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.CHESTPLATE, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CHAOS_SPELL_LEGGINGS = Genesis.L2_REGISTRATE
                .item("chaos_spell_leggings", properties -> new ChaosSpellArmor(
                        ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.LEGGINGS, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CHAOS_SPELL_BOOTS = Genesis.L2_REGISTRATE
                .item("chaos_spell_boots", properties -> new ChaosSpellArmor(
                        ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.BOOTS, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        VIOLET_ZENITH_HELMET = Genesis.L2_REGISTRATE
                .item("violet_zenith_helmet", properties -> new VioletZenithArmor(
                        ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.HELMET, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        VIOLET_ZENITH_CHESTPLATE = Genesis.L2_REGISTRATE
                .item("violet_zenith_chestplate", properties -> new VioletZenithArmor(
                        ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.CHESTPLATE, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        VIOLET_ZENITH_LEGGINGS = Genesis.L2_REGISTRATE
                .item("violet_zenith_leggings", properties -> new VioletZenithArmor(
                        ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.LEGGINGS, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        VIOLET_ZENITH_BOOTS = Genesis.L2_REGISTRATE
                .item("violet_zenith_boots", properties -> new VioletZenithArmor(
                        ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.BOOTS, properties
                ))
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CHAOS_SPELL_BOOK = Genesis.L2_REGISTRATE
                .item("chaos_spell_book", properties -> new ChaosSpellBook())
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CELESTIAL_SOURCE_SPELL_BOOK = Genesis.L2_REGISTRATE
                .item("celestial_source_spell_book", properties -> new CelestialSourceSpellBook())
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        DISK_SPELL_BOOK = Genesis.L2_REGISTRATE
                .item("disk_spell_book", properties -> new AEprospellbook())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CHAOS_STAFF = Genesis.L2_REGISTRATE
                .item("chaos_staff", properties -> new ChaosStaff())
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CELESTIAL_SOURCE_STAFF = Genesis.L2_REGISTRATE
                .item("celestial_source_staff", properties -> new CelestialSourceStaff())
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        FLYING_SWALLOW_THROUGH_Willow = Genesis.L2_REGISTRATE
                .item("flying_swallow_through_willow", properties -> new FlyingSwallowThroughWillow())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        CHAOS_RUNE = Genesis.L2_REGISTRATE
                .item("chaos_rune", properties -> new Item(ItemPropertiesHelper.material()))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CELESTIAL_SOURCE_RUNE = Genesis.L2_REGISTRATE
                .item("celestial_source_rune", properties -> new Item(ItemPropertiesHelper.material()))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CHAOS_UPGRADE_ORB = Genesis.L2_REGISTRATE
                .item("chaos_upgrade_orb", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.CHAOS_SPELL_POWER
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CELESTIAL_SOURCE_UPGRADE_ORB = Genesis.L2_REGISTRATE
                .item("celestial_source_upgrade_orb", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.CELESTIAL_SOURCE_SPELL_POWER
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ELDRITCH_UPGRADE_ORB = Genesis.L2_REGISTRATE
                .item("eldritch_upgrade_orb", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.ELDRITCH_SPELL_POWER
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        FIRE_ORB_PRO = Genesis.L2_REGISTRATE
                .item("fire_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.FLAME_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        HOLY_ORB_PRO = Genesis.L2_REGISTRATE
                .item("holy_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.HOLY_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ICE_ORB_PRO = Genesis.L2_REGISTRATE
                .item("ice_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.FROST_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        BLOOD_ORB_PRO = Genesis.L2_REGISTRATE
                .item("blood_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.SCARLET_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ENDER_ORB_PRO = Genesis.L2_REGISTRATE
                .item("ender_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.ENDER_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        THUNDER_ORB_PRO = Genesis.L2_REGISTRATE
                .item("thunder_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.THUNDER_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        NATURE_ORB_PRO = Genesis.L2_REGISTRATE
                .item("nature_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.NATURE_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        WARLOCK_ORB_PRO = Genesis.L2_REGISTRATE
                .item("warlock_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.WARLOCK_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CHAOS_ORB_PRO = Genesis.L2_REGISTRATE
                .item("chaos_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.CHAOS_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CELESTIAL_SOURCE_ORB_PRO = Genesis.L2_REGISTRATE
                .item("celestial_source_orb_pro", properties -> new UpgradeOrbItem(
                        ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                        UpgradeOrbTypes.CELESTIAL_SOURCE_SPELL_PENETRATION
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        LAO_WANG_237 = Genesis.L2_REGISTRATE
                .item("lao_wang_237", properties -> new LaoWang237Curios())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        GENESIS_CURSE = Genesis.L2_REGISTRATE
                .item("genesis_curse", properties -> new GenesisCurseItem())
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        INFINITE_SHRIVING_STONE = Genesis.L2_REGISTRATE
                .item("infinite_shriving_stone", properties -> new InfiniteShrivingStoneItem())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        UPGRADE_ORB_PRO = Genesis.L2_REGISTRATE
                .item("upgrade_orb_pro", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ARCANE_CRYSTAL = Genesis.L2_REGISTRATE
                .item("arcane_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        BLOOD_CRYSTAL = Genesis.L2_REGISTRATE
                .item("blood_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ELDRITCH_CRYSTAL = Genesis.L2_REGISTRATE
                .item("eldritch_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ENDER_CRYSTAL = Genesis.L2_REGISTRATE
                .item("ender_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        EVOCATION_CRYSTAL = Genesis.L2_REGISTRATE
                .item("evocation_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        FIRE_CRYSTAL = Genesis.L2_REGISTRATE
                .item("fire_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        HOLY_CRYSTAL = Genesis.L2_REGISTRATE
                .item("holy_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ICE_CRYSTAL = Genesis.L2_REGISTRATE
                .item("ice_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        LIGHTNING_CRYSTAL = Genesis.L2_REGISTRATE
                .item("lightning_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        NATURE_CRYSTAL = Genesis.L2_REGISTRATE
                .item("nature_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CHAOS_CRYSTAL = Genesis.L2_REGISTRATE
                .item("chaos_crystal", properties -> new Item(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CELESTIAL_SOURCE_CRYSTAL = Genesis.L2_REGISTRATE
                .item("celestial_source_crystal", properties -> new CelestialSourceBaseItem(properties
                        .stacksTo(16)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CHAOS_MANUSCRIPT = Genesis.L2_REGISTRATE
                .item("chaos_manuscript", properties -> new ChaosManuscript())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CELESTIAL_SOURCE_MANUSCRIPT = Genesis.L2_REGISTRATE
                .item("celestial_source_manuscript", properties -> new CelestialSourceManuscript())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CHAOS_MANUSCRIPT_FRAGMENT = Genesis.L2_REGISTRATE
                .item("chaos_manuscript_fragment", properties -> new ChaosBaseItem(properties
                        .rarity(Rarity.EPIC)) {
                    @Override
                    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
                        list.add(Component.translatable("item." + Genesis.MOD_ID + ".chaos_manuscript_fragment.hover"));
                    }
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        BLANK_CELESTIAL_SOURCE_MANUSCRIPT = Genesis.L2_REGISTRATE
                .item("blank_celestial_source_manuscript", properties -> new CelestialSourceBaseItem(properties
                        .fireResistant()
                        .rarity(Rarity.EPIC)) {
                    @Override
                    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
                        list.add(Component.translatable("item." + Genesis.MOD_ID + ".blank_celestial_source_manuscript.hover"));
                    }

                    @Override
                    public boolean isFoil(@NotNull ItemStack stack) {
                        return true;
                    }
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CELESTIAL_SOURCE_BLOCK_ITEM = Genesis.L2_REGISTRATE
                .item("celestial_source_block", properties -> new BlockItem(BlockRegistry.CELESTIAL_SOURCE_BLOCK.get(), properties))
                .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .register();

        ARCANE_CRYSTAL_ORE_ITEM = Genesis.L2_REGISTRATE
                .item("arcane_crystal_ore", properties -> new BlockItem(BlockRegistry.ARCANE_CRYSTAL_ORE.get(), properties))
                .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                .register();

        ARCANE_CRYSTAL_ORE_DEEPSLATE_ITEM = Genesis.L2_REGISTRATE
                .item("deepslate_arcane_crystal_ore", properties -> new BlockItem(BlockRegistry.ARCANE_CRYSTAL_ORE_DEEPSLATE.get(), properties))
                .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                .register();

        NETHER_ARCANE_CRYSTAL_ORE_ITEM = Genesis.L2_REGISTRATE
                .item("nether_arcane_crystal_ore", properties -> new BlockItem(BlockRegistry.NETHER_ARCANE_CRYSTAL_ORE.get(), properties))
                .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                .register();

        END_ARCANE_CRYSTAL_ORE_ITEM = Genesis.L2_REGISTRATE
                .item("end_arcane_crystal_ore", properties -> new BlockItem(BlockRegistry.END_ARCANE_CRYSTAL_ORE.get(), properties))
                .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
                .register();

        FLESH_SOUL_BELL = Genesis.L2_REGISTRATE
                .item("flesh_soul_bell", properties -> new Item(properties
                        .stacksTo(1)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        CHAOS_CORE = Genesis.L2_REGISTRATE
                .item("chaos_core", properties -> new ChaosCore())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        FLESH_SOUL_FRAGMENT = Genesis.L2_REGISTRATE
                .item("flesh_soul_fragment", properties -> new Item(properties
                        .stacksTo(1)
                        .rarity(Rarity.EPIC)
                ))
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
                .register();

        ETERNAL_RING = Genesis.L2_REGISTRATE
                .item("eternal_ring", properties -> new EternalRing())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        LIGHTNING_RUNE_PLUS = Genesis.L2_REGISTRATE
                .item("lightning_rune_plus", properties -> new LightningRunePlus())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        NATURE_RUNE_PLUS = Genesis.L2_REGISTRATE
                .item("nature_rune_plus", properties -> new NatureRunePlus())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        ENDER_RUNE_PLUS = Genesis.L2_REGISTRATE
                .item("ender_rune_plus", properties -> new EnderRunePlus())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        HOLY_RUNE_PLUS = Genesis.L2_REGISTRATE
                .item("holy_rune_plus", properties -> new HolyRunePlus())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        ICE_RUNE_PLUS = Genesis.L2_REGISTRATE
                .item("ice_rune_plus", properties -> new IceRunePlus())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        BLOOD_RUNE_PLUS = Genesis.L2_REGISTRATE
                .item("blood_rune_plus", properties -> new BloodRunePlus())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        FIRE_RUNE_PLUS = Genesis.L2_REGISTRATE
                .item("fire_rune_plus", properties -> new FireRunePlus())
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

        ELDRITCH_RUNE_PLUS = Genesis.L2_REGISTRATE
                .item("eldritch_rune_plus", properties -> new EldritchRunePlus())
                .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
                })
                .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
                .register();

    }

    public static void register(IEventBus modEventBus) {
        REGISTRY_BLOCK_ITEM.register(modEventBus);
    }

    public static <T extends Item> void createBowModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
        ItemModelBuilder builder = pvd.withExistingParent(ctx.getName(), "item/handheld");
        builder.texture("layer0", Genesis.MOD_ID + ":item/bow/" + ctx.getName() + "/bow");

        for (int i = 0; i < 3; i++) {
            String name = "item/bow/" + ctx.getName() + "/bow_pulling_" + i;
            pvd.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/bow_pulling_" + i))
                .texture("layer0", Genesis.MOD_ID + ":item/bow/" + ctx.getName() + "/bow_pulling_" + i);

            ItemModelBuilder.OverrideBuilder override = builder.override();
            override.predicate(new ResourceLocation("pulling"), 1);
            if (i == 1) {
                override.predicate(new ResourceLocation("pull"), 0.7f);
            } else if (i == 2) {
                override.predicate(new ResourceLocation("pull"), 0.9f);
            }
            override.model(new ModelFile.UncheckedModelFile(Genesis.MOD_ID + ":" + name));
        }
    }

    public static class ChaosBaseItem extends Item {
        public ChaosBaseItem(Properties properties) {
            super(properties);
        }
    }

    public static class CelestialSourceBaseItem extends Item implements ITooltipParticleItem {
        public CelestialSourceBaseItem(Properties properties) {
            super(properties);
        }

        @Override
        public TooltipParticleSystem.ParticleConfig getParticleConfig() {
            Minecraft mc = Minecraft.getInstance();
            double mouseX = mc.mouseHandler.xpos();
            double mouseY = mc.mouseHandler.ypos();

            // 转换为整数坐标
            int mousePosX = (int) mouseX;
            int mousePosY = (int) mouseY;
            return new TooltipParticleSystem.ParticleConfig()
                    // 纹理使用
                    .setTextures(
                            PTID.Star_0,
                            PTID.Star_1,
                            PTID.Star_2,
                            PTID.Star_3,
                            PTID.Star_4,
                            PTID.Star_5,
                            PTID.Star_6,
                            PTID.Star_7,
                            PTID.Star_8,
                            PTID.Star_9
                    )
                    .setParticleCount(1, 3) // 生成数量多少到多少
                    .setMaxTotalParticles(400) // 最大粒子数量
                    // 大小
                    .setSize(4.0f, 12.0f) // 基础大小
                    .setRandomSize(false) // 随机大小变化
                    // 生命周期
                    .setLife(3.0f, 4.0f) // 存活时间3-4秒
                    // 速度
                    .setSpeed(70.0f, 100.0f) // 基础速度
                    // 颜色
                    .setColors(0xFFffb800, 0xFFcd7231, 0xFFffeb00, 0xFFe0ae2d) // 基础颜色（彩虹模式下会被覆盖）
                    .setRainbowColors(true, 2.0f) // 开启彩虹渐变
                    .setColorTransitionSpeed(1.5f) // 颜色过渡速度
                    // 物理
                    .setGravity(false, 40.0f) // 关闭重力（RAIN模式自带下落效果）
                    .setWind(true, 0.0f, 110.0f) // 强风效果
                    .setAirResistance(0.1f) // 轻微空气阻力
                    .setBounciness(10.0f) // 弹性系数
                    // 旋转
                    .setRotation(true, 0.0f, 0.01f) // 旋转速度
                    .setInitialRotation(0.0f, 360.0f) // 随机初始旋转角度
                    // 运动类型
                    .setMotionType(TooltipParticleSystem.MotionType.RAIN) // 从屏幕上方下落
                    .setMotionProperties(20.0f, 1.5f) // 运动幅度和频率（对RAIN模式影响较小）
                    .setCenter((float) mousePosX / 3, (float) mousePosY / 3) // 运动中心点（对RAIN模式影响较小）
                    .setRadius(200.0f) // 运动半径（对RAIN模式影响较小）
                    // 淡入淡出
                    .setFadeIn(true, 0.05f) // 淡入，持续0.05秒
                    .setFadeOut(true, 0.25f) // 淡出，持续0.25秒
                    // 层次感
                    .setDepthLayers(true, 12, 0.08f) // 12层深度，每层变暗8%
                    // 贝塞尔曲线
                    .setSizeCurve(TooltipParticleSystem.BezierCurveType.STAR_EXPAND) // 星星膨胀曲线
                    .setAlphaCurve(TooltipParticleSystem.BezierCurveType.NONE) // 透明度曲线
                    .setSpeedCurve(TooltipParticleSystem.BezierCurveType.NONE) // 速度曲线
                    .setRotationCurve(TooltipParticleSystem.BezierCurveType.NONE); // 旋转曲线
        }
    }
}