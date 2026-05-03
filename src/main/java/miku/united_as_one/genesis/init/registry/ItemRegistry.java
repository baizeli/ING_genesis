package miku.united_as_one.genesis.init.registry;

import com.tterrag.registrate.providers.*;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.boss.BloodBoss;
import miku.united_as_one.genesis.common.items.*;
import miku.united_as_one.genesis.common.items.spell.spellbook.*;
import miku.united_as_one.genesis.common.items.spell.staff.*;
import miku.united_as_one.genesis.common.items.armor.*;
import miku.united_as_one.genesis.common.items.weapon.bow.*;
import miku.united_as_one.genesis.common.items.weapon.sword.*;
import miku.united_as_one.genesis.common.items.curios.*;
import miku.united_as_one.genesis.common.items.curios.rune_plus.*;
import miku.united_as_one.genesis.common.items.spell.manuscript.*;
import miku.united_as_one.genesis.common.items.tool.pickaxe.*;
import miku.united_as_one.genesis.common.items.tool.axe.*;
import miku.united_as_one.genesis.common.items.tool.shovel.*;
import miku.united_as_one.genesis.common.items.tool.hoe.*;
import miku.united_as_one.genesis.common.spell.UpgradeOrbTypes;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.generators.*;
import org.jetbrains.annotations.*;

import java.util.*;

@SuppressWarnings("removal")
public class ItemRegistry {
    // 紫极锭
    public static final ItemEntry<EternisMaterial> VIOLET_GALAXY_INGOT = Genesis.L2_REGISTRATE
            .item("violet_galaxy_ingot", properties -> new EternisMaterial(properties, 0))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 紫极碎片
    public static final ItemEntry<Item> VIOLET_FRAGMENTS = Genesis.L2_REGISTRATE
            .item("violet_fragments", properties -> new Item(properties.rarity(Rarity.EPIC)))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();
    //破灭神格
    public static final ItemEntry<Item> DESTROY_GODHEAD = Genesis.L2_REGISTRATE
            .item("destroy_godhead", properties -> new Item(properties.rarity(Rarity.EPIC)))
            .model((ctx, prov) -> {
                prov.withExistingParent(ctx.getName(), "item/generated")
                        .texture("layer0", prov.modLoc("item/" + ctx.getName()))
                        .texture("layer1", prov.modLoc("item/misc/red_lightning"));
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();
    // 神圣金属锭
    public static final ItemEntry<Item> DIVINE_METAL_INGOT = Genesis.L2_REGISTRATE
            .item("divine_metal_ingot", properties -> new Item(properties.rarity(Rarity.EPIC)))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 神圣金属碎片
    public static final ItemEntry<Item> DIVINE_METAL_FRAGMENT = Genesis.L2_REGISTRATE
            .item("divine_metal_fragment", properties -> new Item(properties.rarity(Rarity.EPIC)))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 扭曲混沌锭
    public static final ItemEntry<? extends Item> TWISTED_CHAOS_INGOT = Genesis.L2_REGISTRATE
            .item("twisted_chaos_ingot", properties -> new ChaosBase(properties.rarity(Rarity.EPIC)))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 扭曲之混沌
    public static final ItemEntry<ChaosBase> TWISTED_CHAOS = Genesis.L2_REGISTRATE
            .item("twisted_chaos", properties -> new ChaosBase(properties.rarity(Rarity.EPIC)))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 星源珍珠
    public static final ItemEntry<CelestialSourceBase> CELESTIAL_SOURCE_PEARL = Genesis.L2_REGISTRATE
            .item("celestial_source_pearl", properties -> new CelestialSourceBase(properties.rarity(Rarity.EPIC)))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 星源锭
    public static final ItemEntry<CelestialSourceBase> CELESTIAL_SOURCE_INGOT = Genesis.L2_REGISTRATE
            .item("celestial_source_ingot", properties -> new CelestialSourceBase(properties.rarity(Rarity.EPIC)))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // good_cake
    public static final ItemEntry<GoodCake> GOOD_CAKE = Genesis.L2_REGISTRATE
            .item("good_cake", properties -> new GoodCake())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 天火圣裁 [伪]
    public static final ItemEntry<InfinitySword> INFINITY_SWORD = Genesis.L2_REGISTRATE
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

    // 暮光极致者
    public static final ItemEntry<AvaritiaSword> AVARITIA_SWORD = Genesis.L2_REGISTRATE
            .item("avaritia_infinity_sword", properties -> new AvaritiaSword(
                Integer.MAX_VALUE,
                -2F,
                properties.durability(Integer.MAX_VALUE)
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 秘银剑
    public static final ItemEntry<MithrilSword> MITHRIL_SWORD = Genesis.L2_REGISTRATE
            .item("mithril_sword", properties -> new MithrilSword(
                TierRegistry.MITHRIL,
                7,
                -1.7f,
                properties
            ))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 神圣金属剑
    public static final ItemEntry<DivineMetalSword> DIVINE_METAL_SWORD = Genesis.L2_REGISTRATE
            .item("divine_metal_sword", properties -> new DivineMetalSword(
                TierRegistry.DIVINE_METAL,
                15,
                -2.7f,
                properties
            ))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 神圣金属斧
    public static final ItemEntry<DivineMetalAxe> DIVINE_METAL_AXE = Genesis.L2_REGISTRATE
            .item("divine_metal_axe", properties -> new DivineMetalAxe(
                TierRegistry.DIVINE_METAL,
                19,
                -3.3f,
                properties
            ))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 神圣金属镐
    public static final ItemEntry<DivineMetalPickaxe> DIVINE_METAL_PICKAXE = Genesis.L2_REGISTRATE
            .item("divine_metal_pickaxe", properties -> new DivineMetalPickaxe(
                TierRegistry.DIVINE_METAL,
                4,
                -1.0f,
                properties
            ))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 神圣金属铲
    public static final ItemEntry<DivineMetalShovel> DIVINE_METAL_SHOVEL = Genesis.L2_REGISTRATE
            .item("divine_metal_shovel", properties -> new DivineMetalShovel(
                TierRegistry.DIVINE_METAL,
                4,
                -1.0f,
                properties
            ))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 神圣金属锄
    public static final ItemEntry<DivineMetalHoe> DIVINE_METAL_HOE = Genesis.L2_REGISTRATE
            .item("divine_metal_hoe", properties -> new DivineMetalHoe(
                TierRegistry.DIVINE_METAL,
                0,
                96,
                properties
            ))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 秘银镐
    public static final ItemEntry<MithrilPickaxe> MITHRIL_PICKAXE = Genesis.L2_REGISTRATE
            .item("mithril_pickaxe", properties -> new MithrilPickaxe(
                TierRegistry.MITHRIL,
                0,
                -1.6f,
                properties
            ))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 紫极剑
    public static final ItemEntry<VioletSword> VIOLET_SWORD = Genesis.L2_REGISTRATE
            .item("violet_sword", VioletSword::new)
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();
    // 紫极斧
    public static final ItemEntry<VioletAxe> VIOLET_AXE = Genesis.L2_REGISTRATE
            .item("violet_axe", VioletAxe::new)
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 紫极稿
    public static final ItemEntry<VioletPickaxe> VIOLET_PICKAXE = Genesis.L2_REGISTRATE
            .item("violet_pickaxe", properties -> new VioletPickaxe(
                TierRegistry.VIOLET_GALAXY_INGOT,
                0,
                -1.6f,
                properties
            ))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 紫极铲
    public static final ItemEntry<VioletShovel> VIOLET_SHOVEL = Genesis.L2_REGISTRATE
            .item("violet_shovel", VioletShovel::new)
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 紫极锄
    public static final ItemEntry<VioletHoe> VIOLET_HOE = Genesis.L2_REGISTRATE
            .item("violet_hoe", VioletHoe::new)
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 雷霆长弓
    public static final ItemEntry<ThunderLongBow> THUNDER_LONGBOW = Genesis.L2_REGISTRATE
            .item("thunder_longbow", properties -> new ThunderLongBow(properties
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

    // 冰霜长弓
    public static final ItemEntry<FrostLongBow> FROST_LONGBOW = Genesis.L2_REGISTRATE
            .item("frost_longbow", properties -> new FrostLongBow(properties
                .rarity(Rarity.EPIC)
                .stacksTo(1)
                .durability(2009)
            ))
            .model(ItemRegistry::createBowModel)
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 巫术弓
    public static final ItemEntry<WitchcraftBow> WITCHCRAFT_BOW = Genesis.L2_REGISTRATE
            .item("witchcraft_bow", properties -> new WitchcraftBow(properties
                .rarity(Rarity.EPIC)
                .stacksTo(1)
                .durability(2009)
            ))
            .model(ItemRegistry::createBowModel)
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 火焰弓
    public static final ItemEntry<FlameBow> FLAME_BOW = Genesis.L2_REGISTRATE
            .item("flame_bow", properties -> new FlameBow(properties
                .rarity(Rarity.EPIC)
                .stacksTo(1)
                .durability(2009)
            ))
            .model(ItemRegistry::createBowModel)
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 创造之星
    public static final ItemEntry<CreateStar> CREATE_STAR = Genesis.L2_REGISTRATE
            .item("create_star", properties -> new CreateStar(properties
                .rarity(Rarity.COMMON)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 星源绘卷
    public static final ItemEntry<GalaxyScroll> GALAXY_SCROLL = Genesis.L2_REGISTRATE
            .item("galaxy_scroll", properties -> new GalaxyScroll(properties
                .rarity(Rarity.RARE)
                .stacksTo(1)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 神圣金属套
    public static final ItemEntry<DivineMetalArmor> DIVINE_METAL_HELMET = Genesis.L2_REGISTRATE
            .item("divine_metal_helmet", properties -> new DivineMetalArmor(
                (IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.HELMET, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<DivineMetalArmor> DIVINE_METAL_CHESTPLATE = Genesis.L2_REGISTRATE
            .item("divine_metal_chestplate", properties -> new DivineMetalArmor(
                (IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.CHESTPLATE, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<DivineMetalArmor> DIVINE_METAL_LEGGINGS = Genesis.L2_REGISTRATE
            .item("divine_metal_leggings", properties -> new DivineMetalArmor(
                (IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.LEGGINGS, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<DivineMetalArmor> DIVINE_METAL_BOOTS = Genesis.L2_REGISTRATE
            .item("divine_metal_boots", properties -> new DivineMetalArmor(
                (IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.BOOTS, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 星源法术套
    public static final ItemEntry<CelestialSourceSpellArmor> CELESTIAL_SOURCE_SPELL_HELMET = Genesis.L2_REGISTRATE
            .item("celestial_source_spell_helmet", properties -> new CelestialSourceSpellArmor(
                ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.HELMET, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<CelestialSourceSpellArmor> CELESTIAL_SOURCE_SPELL_CHESTPLATE = Genesis.L2_REGISTRATE
            .item("celestial_source_spell_chestplate", properties -> new CelestialSourceSpellArmor(
                ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.CHESTPLATE, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<CelestialSourceSpellArmor> CELESTIAL_SOURCE_SPELL_LEGGINGS = Genesis.L2_REGISTRATE
            .item("celestial_source_spell_leggings", properties -> new CelestialSourceSpellArmor(
                ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.LEGGINGS, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<CelestialSourceSpellArmor> CELESTIAL_SOURCE_SPELL_BOOTS = Genesis.L2_REGISTRATE
            .item("celestial_source_spell_boots", properties -> new CelestialSourceSpellArmor(
                ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.BOOTS, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 混沌法术套
    public static final ItemEntry<ChaosSpellArmor> CHAOS_SPELL_HELMET = Genesis.L2_REGISTRATE
            .item("chaos_spell_helmet", properties -> new ChaosSpellArmor(
                ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.HELMET, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<ChaosSpellArmor> CHAOS_SPELL_CHESTPLATE = Genesis.L2_REGISTRATE
            .item("chaos_spell_chestplate", properties -> new ChaosSpellArmor(
                ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.CHESTPLATE, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<ChaosSpellArmor> CHAOS_SPELL_LEGGINGS = Genesis.L2_REGISTRATE
            .item("chaos_spell_leggings", properties -> new ChaosSpellArmor(
                ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.LEGGINGS, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<ChaosSpellArmor> CHAOS_SPELL_BOOTS = Genesis.L2_REGISTRATE
            .item("chaos_spell_boots", properties -> new ChaosSpellArmor(
                ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.BOOTS, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 紫极战斗套
    public static final ItemEntry<VioletZenithArmor> VIOLET_ZENITH_HELMET = Genesis.L2_REGISTRATE
            .item("violet_zenith_helmet", properties -> new VioletZenithArmor(
                ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.HELMET, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<VioletZenithArmor> VIOLET_ZENITH_CHESTPLATE = Genesis.L2_REGISTRATE
            .item("violet_zenith_chestplate", properties -> new VioletZenithArmor(
                ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.CHESTPLATE, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<VioletZenithArmor> VIOLET_ZENITH_LEGGINGS = Genesis.L2_REGISTRATE
            .item("violet_zenith_leggings", properties -> new VioletZenithArmor(
                ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.LEGGINGS, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<VioletZenithArmor> VIOLET_ZENITH_BOOTS = Genesis.L2_REGISTRATE
            .item("violet_zenith_boots", properties -> new VioletZenithArmor(
                ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.BOOTS, properties
            ))
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {})
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 混沌法术书
    public static final ItemEntry<ChaosSpellBook> CHAOS_SPELL_BOOK = Genesis.L2_REGISTRATE
            .item("chaos_spell_book", properties -> new ChaosSpellBook())
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {})
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 星源法术书
    public static final ItemEntry<CelestialSourceSpellBook> CELESTIAL_SOURCE_SPELL_BOOK = Genesis.L2_REGISTRATE
            .item("celestial_source_spell_book", properties -> new CelestialSourceSpellBook())
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {})
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 雷霆法术书
    public static final ItemEntry<LightningSpellBook> LIGHTNING_SPELL_BOOK = Genesis.L2_REGISTRATE
            .item("lightning_spell_book", properties -> new LightningSpellBook())
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {})
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    //法术磁盘
    public static final ItemEntry<AEprospellbook> DISK_SPELL_BOOK = Genesis.L2_REGISTRATE
            .item("disk_spell_book", properties -> new AEprospellbook())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 混沌法杖
    public static final ItemEntry<ChaosStaff> CHAOS_STAFF = Genesis.L2_REGISTRATE
            .item("chaos_staff", properties -> new ChaosStaff())
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 星源法杖
    public static final ItemEntry<CelestialSourceStaff> CELESTIAL_SOURCE_STAFF = Genesis.L2_REGISTRATE
            .item("celestial_source_staff", properties -> new CelestialSourceStaff())
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 飞燕穿柳
    public static final ItemEntry<FlyingSwallowThroughWillow> FLYING_SWALLOW_THROUGH_Willow = Genesis.L2_REGISTRATE
            .item("flying_swallow_through_willow", properties -> new FlyingSwallowThroughWillow())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 混沌符文
    public static final ItemEntry<Item> CHAOS_RUNE = Genesis.L2_REGISTRATE
            .item("chaos_rune", properties -> new Item(ItemPropertiesHelper.material()))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 星源符文
    public static final ItemEntry<Item> CELESTIAL_SOURCE_RUNE = Genesis.L2_REGISTRATE
            .item("celestial_source_rune", properties -> new Item(ItemPropertiesHelper.material()))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 邪术符文
    public static final ItemEntry<Item> ELDRITCH_RUNE = Genesis.L2_REGISTRATE
            .item("eldritch_rune", properties -> new Item(ItemPropertiesHelper.material()))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    //亵渎钥匙
    public static final ItemEntry<Item> BLASPHHEMY_KEY = Genesis.L2_REGISTRATE
            .item("blasphemy_key", properties -> new Item(ItemPropertiesHelper.material()))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 混沌升级法球
    public static final ItemEntry<UpgradeOrbItem> CHAOS_UPGRADE_ORB = Genesis.L2_REGISTRATE
            .item("chaos_upgrade_orb", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.CHAOS_SPELL_POWER
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 星源升级法球
    public static final ItemEntry<UpgradeOrbItem> CELESTIAL_SOURCE_UPGRADE_ORB = Genesis.L2_REGISTRATE
            .item("celestial_source_upgrade_orb", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.CELESTIAL_SOURCE_SPELL_POWER
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 邪术升级法球
    public static final ItemEntry<UpgradeOrbItem> ELDRITCH_UPGRADE_ORB = Genesis.L2_REGISTRATE
            .item("eldritch_upgrade_orb", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.ELDRITCH_SPELL_POWER
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 烈焰穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> FIRE_ORB_PRO = Genesis.L2_REGISTRATE
            .item("fire_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.FIRE_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 神圣穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> HOLY_ORB_PRO = Genesis.L2_REGISTRATE
            .item("holy_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.HOLY_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 冰霜穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> ICE_ORB_PRO = Genesis.L2_REGISTRATE
            .item("ice_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.IEC_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 猩红穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> BLOOD_ORB_PRO = Genesis.L2_REGISTRATE
            .item("blood_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.BLOOD_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 末影穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> ENDER_ORB_PRO = Genesis.L2_REGISTRATE
            .item("ender_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.ENDER_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 雷霆穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> THUNDER_ORB_PRO = Genesis.L2_REGISTRATE
            .item("thunder_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.THUNDER_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 自然穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> NATURE_ORB_PRO = Genesis.L2_REGISTRATE
            .item("nature_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.NATURE_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 邪术穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> ELDRITCH_ORB_PRO = Genesis.L2_REGISTRATE
            .item("eldritch_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.ELDRITCH_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 混沌穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> CHAOS_ORB_PRO = Genesis.L2_REGISTRATE
            .item("chaos_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.CHAOS_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 星源穿透升级法球
    public static final ItemEntry<UpgradeOrbItem> CELESTIAL_SOURCE_ORB_PRO = Genesis.L2_REGISTRATE
            .item("celestial_source_orb_pro", properties -> new UpgradeOrbItem(
                ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON),
                UpgradeOrbTypes.CELESTIAL_SOURCE_SPELL_PENETRATION
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 老王237
    public static final ItemEntry<LaoWang237Curios> LAO_WANG_237 = Genesis.L2_REGISTRATE
            .item("lao_wang_237", properties -> new LaoWang237Curios())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 创世之诅咒
    public static final ItemEntry<GenesisCurseItem> GENESIS_CURSE = Genesis.L2_REGISTRATE
            .item("genesis_curse", properties -> new GenesisCurseItem())
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {})
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 无限忏悔石
    public static final ItemEntry<InfiniteShrivingStoneItem> INFINITE_SHRIVING_STONE = Genesis.L2_REGISTRATE
            .item("infinite_shriving_stone", properties -> new InfiniteShrivingStoneItem())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 空白高级法球
    public static final ItemEntry<Item> UPGRADE_ORB_PRO = Genesis.L2_REGISTRATE
            .item("upgrade_orb_pro", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 奥术水晶
    public static final ItemEntry<Item> ARCANE_CRYSTAL = Genesis.L2_REGISTRATE
            .item("arcane_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 猩红水晶
    public static final ItemEntry<Item> BLOOD_CRYSTAL = Genesis.L2_REGISTRATE
            .item("blood_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 邪术水晶
    public static final ItemEntry<Item> ELDRITCH_CRYSTAL = Genesis.L2_REGISTRATE
            .item("eldritch_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 末影水晶
    public static final ItemEntry<Item> ENDER_CRYSTAL = Genesis.L2_REGISTRATE
            .item("ender_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 唤魔水晶
    public static final ItemEntry<Item> EVOCATION_CRYSTAL = Genesis.L2_REGISTRATE
            .item("evocation_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 炽焰水晶
    public static final ItemEntry<Item> FIRE_CRYSTAL = Genesis.L2_REGISTRATE
            .item("fire_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 神圣水晶
    public static final ItemEntry<Item> HOLY_CRYSTAL = Genesis.L2_REGISTRATE
            .item("holy_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 冰霜水晶
    public static final ItemEntry<Item> ICE_CRYSTAL = Genesis.L2_REGISTRATE
            .item("ice_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 雷霆水晶
    public static final ItemEntry<Item> LIGHTNING_CRYSTAL = Genesis.L2_REGISTRATE
            .item("lightning_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 自然水晶
    public static final ItemEntry<Item> NATURE_CRYSTAL = Genesis.L2_REGISTRATE
            .item("nature_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 混沌水晶
    public static final ItemEntry<Item> CHAOS_CRYSTAL = Genesis.L2_REGISTRATE
            .item("chaos_crystal", properties -> new Item(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 星源水晶
    public static final ItemEntry<CelestialSourceBase> CELESTIAL_SOURCE_CRYSTAL = Genesis.L2_REGISTRATE
            .item("celestial_source_crystal", properties -> new CelestialSourceBase(properties
                .stacksTo(16)
                .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 混沌手稿
    public static final ItemEntry<ChaosManuscript> CHAOS_MANUSCRIPT = Genesis.L2_REGISTRATE
            .item("chaos_manuscript", properties -> new ChaosManuscript())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 星源手稿
    public static final ItemEntry<CelestialSourceManuscript> CELESTIAL_SOURCE_MANUSCRIPT = Genesis.L2_REGISTRATE
            .item("celestial_source_manuscript", properties -> new CelestialSourceManuscript())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 混沌手稿碎片
    public static final ItemEntry<?> CHAOS_MANUSCRIPT_FRAGMENT = Genesis.L2_REGISTRATE
            .item("chaos_manuscript_fragment", properties -> new ChaosBase(properties
                    .rarity(Rarity.EPIC)) {
                @Override
                public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
                    list.add(Component.translatable("item." + Genesis.MOD_ID + ".chaos_manuscript_fragment.hover"));
                }
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 空白星源手稿碎片
    public static final ItemEntry<?> BLANK_CELESTIAL_SOURCE_MANUSCRIPT = Genesis.L2_REGISTRATE
            .item("blank_celestial_source_manuscript", properties -> new CelestialSourceBase(properties
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

    // 混沌传送门框架
    public static final ItemEntry<BlockItem> CHAOS_PORTAL_FRAME = Genesis.L2_REGISTRATE
            .item("chaos_portal_frame", properties -> new BlockItem(BlockRegistry.CHAOS_PORTAL_FRAME.get(), properties))
            .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<BlockItem> ARCANE_WORKBENCH = Genesis.L2_REGISTRATE
            .item("arcane_workbench", properties -> new BlockItem(BlockRegistry.ARCANE_WORKBENCH.get(), properties))
            .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .register();

    // 星源块
    public static final ItemEntry<BlockItem> CELESTIAL_SOURCE_BLOCK = Genesis.L2_REGISTRATE
            .item("celestial_source_block", properties -> new BlockItem(BlockRegistry.CELESTIAL_SOURCE_BLOCK.get(), properties))
            .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .register();

    // 受火者的匕首
    public static final ItemEntry<FireBossDagger> FIRE_BOSS_DAGGER = Genesis.L2_REGISTRATE
            .item("fire_boss_dagger", properties -> new FireBossDagger(TierRegistry.DARGGET, 0, -2.4F, properties.rarity(Rarity.EPIC)))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 受血者的匕首
    public static final ItemEntry<BloodBossDagger> BLOOD_BOSS_DAGGER = Genesis.L2_REGISTRATE
            .item("blood_boss_dagger", properties -> new BloodBossDagger(TierRegistry.DARGGET, 2, -2.2F, properties.rarity(Rarity.EPIC)))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    public static final ItemEntry<Gungnir> GUNGNIR = Genesis.L2_REGISTRATE
            .item("gungnir", properties -> new Gungnir(TierRegistry.LEGENDARY, 2, -1.9F, properties.rarity(Rarity.create("Frustration", style ->
                    style.withColor(0x660000)
                            .withItalic(true)
                            .withStrikethrough(true)
            ))))
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 血肉魂铃
    public static final ItemEntry<?> FLESH_SOUL_BELL = Genesis.L2_REGISTRATE
            .item("flesh_soul_bell", properties -> new Item(properties
                .stacksTo(1)
                .rarity(Rarity.EPIC)
            ) {
                @Override
                public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
                    if (player.isShiftKeyDown()) {
                        BlockPos playerPos = player.blockPosition();

                        int radius = 15;
                        AABB area = new AABB(
                                playerPos.getX() - radius, playerPos.getY() - radius, playerPos.getZ() - radius,
                                playerPos.getX() + radius, playerPos.getY() + radius, playerPos.getZ() + radius
                        );

                        level.getEntities(player, area).forEach(e -> {
                            if (e instanceof BloodBoss bloodBoss) {
                                bloodBoss.remove(Entity.RemovalReason.KILLED);
                            }
                        });
                    }
                    return super.use(level, player, usedHand);
                }
            })
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 混沌原核
    public static final ItemEntry<ChaosCore> CHAOS_CORE = Genesis.L2_REGISTRATE
            .item("chaos_core", properties -> new ChaosCore())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 血肉灵魂碎片
    public static final ItemEntry<Item> FLESH_SOUL_FRAGMENT = Genesis.L2_REGISTRATE
            .item("flesh_soul_fragment", properties -> new Item(properties
                    .stacksTo(1)
                    .rarity(Rarity.EPIC)
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    // 恒久之戒
    public static final ItemEntry<EternalRing> ETERNAL_RING = Genesis.L2_REGISTRATE
            .item("eternal_ring", properties -> new EternalRing())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 雷霆胸饰
    public static final ItemEntry<LightningRunePlus> LIGHTNING_RUNE_PLUS = Genesis.L2_REGISTRATE
            .item("lightning_rune_plus", properties -> new LightningRunePlus())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 自然手镯
    public static final ItemEntry<NatureRunePlus> NATURE_RUNE_PLUS = Genesis.L2_REGISTRATE
            .item("nature_rune_plus", properties -> new NatureRunePlus())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 末影指环
    public static final ItemEntry<EnderRunePlus> ENDER_RUNE_PLUS = Genesis.L2_REGISTRATE
            .item("ender_rune_plus", properties -> new EnderRunePlus())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 神圣拳套
    public static final ItemEntry<HolyRunePlus> HOLY_RUNE_PLUS = Genesis.L2_REGISTRATE
            .item("holy_rune_plus", properties -> new HolyRunePlus())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 冰霜脚链
    public static final ItemEntry<IceRunePlus> ICE_RUNE_PLUS = Genesis.L2_REGISTRATE
            .item("ice_rune_plus", properties -> new IceRunePlus())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 猩红之牙
    public static final ItemEntry<BloodRunePlus> BLOOD_RUNE_PLUS = Genesis.L2_REGISTRATE
            .item("blood_rune_plus", properties -> new BloodRunePlus())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 赤焰手饰
    public static final ItemEntry<FireRunePlus> FIRE_RUNE_PLUS = Genesis.L2_REGISTRATE
            .item("fire_rune_plus", properties -> new FireRunePlus())
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 邪术符文
    public static final ItemEntry<EldritchRunePlus> ELDRITCH_RUNE_PLUS = Genesis.L2_REGISTRATE
            .item("eldritch_rune_plus", properties -> new EldritchRunePlus())
            .setData(ProviderType.ITEM_MODEL, (ctx, prov) -> {})
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_EQUIPMENT)
            .register();

    // 锻造模板
    public static final ItemEntry<ModSmithingTemplateItem> VIOLET_UPGRADE_SMITHING_TEMPLATE = Genesis.L2_REGISTRATE
            .item("violet_upgrade_smithing_template", properties -> new ModSmithingTemplateItem(
                    Component.translatable("item.iron_spells_genesis.smithing_template.violet_upgrade.applies_to").withStyle(SmithingTemplateItem.DESCRIPTION_FORMAT),
                    Component.translatable("item.iron_spells_genesis.smithing_template.violet_upgrade.ingredients").withStyle(SmithingTemplateItem.DESCRIPTION_FORMAT),
                    Component.translatable("upgrade.iron_spells_genesis.violet_upgrade").withStyle(SmithingTemplateItem.TITLE_FORMAT),
                    Component.translatable("item.iron_spells_genesis.smithing_template.violet_upgrade.base_slot_description"),
                    Component.translatable("item.iron_spells_genesis.smithing_template.violet_upgrade.additions_slot_description"),
                    SmithingTemplateItem.createTrimmableArmorIconList(), SmithingTemplateItem.createNetheriteUpgradeMaterialList(),
                    "item." + Genesis.MOD_ID + ".violet_upgrade_smithing_template"
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .lang(SmithingTemplateItem::getDescriptionId)
            .register();

    // 锻造模板
    public static final ItemEntry<ModSmithingTemplateItem> DIVINE_UPGRADE_SMITHING_TEMPLATE = Genesis.L2_REGISTRATE
            .item("divine_upgrade_smithing_template", properties -> new ModSmithingTemplateItem(
                    Component.translatable("item.iron_spells_genesis.smithing_template.divine_upgrade.applies_to").withStyle(SmithingTemplateItem.DESCRIPTION_FORMAT),
                    Component.translatable("item.iron_spells_genesis.smithing_template.divine_upgrade.ingredients").withStyle(SmithingTemplateItem.DESCRIPTION_FORMAT),
                    Component.translatable("upgrade.iron_spells_genesis.divine_upgrade").withStyle(SmithingTemplateItem.TITLE_FORMAT),
                    Component.translatable("item.iron_spells_genesis.smithing_template.divine_upgrade.base_slot_description"),
                    Component.translatable("item.iron_spells_genesis.smithing_template.divine_upgrade.additions_slot_description"),
                    SmithingTemplateItem.createTrimmableArmorIconList(), SmithingTemplateItem.createNetheriteUpgradeMaterialList(),
                    "item." + Genesis.MOD_ID + ".divine_upgrade_smithing_template"
            ))
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_MATERIAL)
            .register();

    public static void register() {}

    // 咕咕嘎嘎的弓模型生成
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
}