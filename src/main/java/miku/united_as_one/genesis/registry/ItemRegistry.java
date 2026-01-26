package miku.united_as_one.genesis.registry;

import com.tterrag.registrate.util.entry.ItemEntry;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.items.*;
import miku.united_as_one.genesis.items.staff.*;
import miku.united_as_one.genesis.items.armor.*;
import miku.united_as_one.genesis.items.curios.*;
import miku.united_as_one.genesis.items.curios.rune_plus.*;
import miku.united_as_one.genesis.items.manuscript.*;
import miku.united_as_one.genesis.tooltipParticleHandler.*;
import miku.united_as_one.genesis.spell.UpgradeOrbTypes;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.armor.IronsExtendedArmorMaterial;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.Supplier;

public class ItemRegistry {
    //加到创造标签页的用这个注册
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Genesis.MOD_ID);
    //不加到创造标签页的用这个注册
    public static final DeferredRegister<Item> PRE_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Genesis.MOD_ID);

    public static final ItemEntry<EternisMaterial> PURPLEITE_GALAXY_INGOT = Genesis.L2_REGISTRATE
            .item("purpleite_galaxy_ingot", properties -> new EternisMaterial(properties, 0))
            .defaultModel()
            .register();

    // 神圣金属锭
    public static final ItemEntry<Item> DIVINE_METAL_INGOT = Genesis.L2_REGISTRATE
            .item("divine_metal_ingot", properties -> new Item(properties.rarity(Rarity.EPIC)))
            .defaultModel()
            .register();

    // 扭曲混沌锭
    public static final ItemEntry<? extends Item> TWISTED_CHAOS_INGOT = Genesis.L2_REGISTRATE
            .item("twisted_chaos_ingot", properties -> new ItemRegistry.ChaosBaseItem(properties.rarity(Rarity.EPIC)))
            .defaultModel()
            .register();

    // 扭曲之混沌-[混沌]法术材料
    public static final ItemEntry<ChaosBaseItem> TWISTED_CHAOS = Genesis.L2_REGISTRATE
            .item("twisted_chaos", properties -> new ChaosBaseItem(properties.rarity(Rarity.EPIC)))
            .defaultModel()
            .register();

    // 星源珍珠-[星源]法术材料
    public static final ItemEntry<CelestialSourceBaseItem> CELESTIAL_SOURCE_PEARL = Genesis.L2_REGISTRATE
            .item("celestial_source_pearl", properties -> new CelestialSourceBaseItem(properties.rarity(Rarity.EPIC)))
            .defaultModel()
            .register();

    // 星源锭-[星源]法术材料
    public static final ItemEntry<CelestialSourceBaseItem> CELESTIAL_SOURCE_INGOT = Genesis.L2_REGISTRATE
            .item("celestial_source_ingot", properties -> new CelestialSourceBaseItem(properties.rarity(Rarity.EPIC)))
            .defaultModel()
            .register();

    public static final ItemEntry<EternisAppleItem> ETERNIS_APPLE = Genesis.L2_REGISTRATE
            .item("eternis_apple", properties -> new EternisAppleItem(properties.stacksTo(64).rarity(Rarity.EPIC)))
            .defaultModel()
            .register();

    public static final ItemEntry<GoodCake> GOOD_CAKE = Genesis.L2_REGISTRATE
            .item("good_cake", properties -> new GoodCake())
            .defaultModel()
            .register();

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

    public static final ItemEntry<CreateStar> CREATE_STAR = Genesis.L2_REGISTRATE
            .item("create_star", properties -> new CreateStar(
                    properties.rarity(Rarity.COMMON)
            ))
            .defaultModel()
            .register();

    public static final ItemEntry<GalaxyScroll> GALAXY_SCROLL = Genesis.L2_REGISTRATE
            .item("galaxy_scroll", properties -> new GalaxyScroll(
                    properties.rarity(Rarity.RARE)
                            .stacksTo(1)
            ))
            .defaultModel()
            .register();

    // 奥术工作台
    public static final RegistryObject<Item> ARCANE_WORKBENCH = ITEMS.register("arcane_workbench",
        () -> new BlockItem(BlockRegistry.ARCANE_WORKBENCH.get(), new Item.Properties()));

/*    // 无尽永恒盔甲套装
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
                    new Item.Properties().rarity(Rarity.RARE)));*/

    // 神圣金属套
    public static final RegistryObject<Item> DIVINE_METAL_HELMET = ITEMS.register("divine_metal_helmet",
            () -> new DivineMetalArmor((IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DIVINE_METAL_CHESTPLATE = ITEMS.register("divine_metal_chestplate",
            () -> new DivineMetalArmor((IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DIVINE_METAL_LEGGINGS = ITEMS.register("divine_metal_leggings",
            () -> new DivineMetalArmor((IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DIVINE_METAL_BOOTS = ITEMS.register("divine_metal_boots",
            () -> new DivineMetalArmor((IronsExtendedArmorMaterial) ModArmorMaterials.DIVINE_METAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    // 星源法术套
    public static final RegistryObject<Item> CELESTIAL_SOURCE_SPELL_HELMET = ITEMS.register("celestial_source_spell_helmet",
            () -> new CelestialSourceSpellArmor((IronsExtendedArmorMaterial) ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> CELESTIAL_SOURCE_SPELL_CHESTPLATE = ITEMS.register("celestial_source_spell_chestplate",
            () -> new CelestialSourceSpellArmor((IronsExtendedArmorMaterial) ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> CELESTIAL_SOURCE_SPELL_LEGGINGS = ITEMS.register("celestial_source_spell_leggings",
            () -> new CelestialSourceSpellArmor((IronsExtendedArmorMaterial) ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> CELESTIAL_SOURCE_SPELL_BOOTS = ITEMS.register("celestial_source_spell_boots",
            () -> new CelestialSourceSpellArmor((IronsExtendedArmorMaterial) ModArmorMaterials.CELESTIAL_SOURCE_SPELL, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    // 混沌法术套
    public static final RegistryObject<Item> CHAOS_SPELL_HELMET = ITEMS.register("chaos_spell_helmet",
            () -> new ChaosSpellArmor((IronsExtendedArmorMaterial) ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> CHAOS_SPELL_CHESTPLATE = ITEMS.register("chaos_spell_chestplate",
            () -> new ChaosSpellArmor((IronsExtendedArmorMaterial) ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> CHAOS_SPELL_LEGGINGS = ITEMS.register("chaos_spell_leggings",
            () -> new ChaosSpellArmor((IronsExtendedArmorMaterial) ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> CHAOS_SPELL_BOOTS = ITEMS.register("chaos_spell_boots",
            () -> new ChaosSpellArmor((IronsExtendedArmorMaterial) ModArmorMaterials.CHAOS_SPELL, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    // 紫极战斗套
    public static final RegistryObject<Item> VIOLET_ZENITH_HELMET = ITEMS.register("violet_zenith_helmet",
            () -> new VioletZenithArmor((IronsExtendedArmorMaterial) ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> VIOLET_ZENITH_CHESTPLATE = ITEMS.register("violet_zenith_chestplate",
            () -> new VioletZenithArmor((IronsExtendedArmorMaterial) ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> VIOLET_ZENITH_LEGGINGS = ITEMS.register("violet_zenith_leggings",
            () -> new VioletZenithArmor((IronsExtendedArmorMaterial) ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> VIOLET_ZENITH_BOOTS = ITEMS.register("violet_zenith_boots",
            () -> new VioletZenithArmor((IronsExtendedArmorMaterial) ModArmorMaterials.VIOLET_ZENITH, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    // 混沌法术书
    public static final RegistryObject<Item> CHAOS_SPELL_BOOK = ITEMS.register("chaos_spell_book", ChaosSpellBook::new);

    // 星源法术书
    public static final RegistryObject<Item> CELESTIAL_SOURCE_SPELL_BOOK = ITEMS.register("celestial_source_spell_book", CelestialSourceSpellBook::new);

    // 混沌法杖
    public static final RegistryObject<Item> CHAOS_STAFF = ITEMS.register("chaos_staff", ChaosStaff::new);

    // 星源法杖
    public static final RegistryObject<Item> CELESTIAL_SOURCE_STAFF = ITEMS.register("celestial_source_staff", CelestialSourceStaff::new);

    public static final RegistryObject<Item> CHAOS_RUNE = ITEMS.register("chaos_rune", () -> new Item(ItemPropertiesHelper.material()));

    public static final RegistryObject<Item> CELESTIAL_SOURCE_RUNE = ITEMS.register("celestial_source_rune", () -> new Item(ItemPropertiesHelper.material()));

    public static final RegistryObject<Item> CHAOS_UPGRADE_ORB = ITEMS.register("chaos_upgrade_orb", () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON), UpgradeOrbTypes.CHAOS_SPELL_POWER));

    public static final RegistryObject<Item> CELESTIAL_SOURCE_UPGRADE_ORB = ITEMS.register("celestial_source_upgrade_orb", () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON), UpgradeOrbTypes.CELESTIAL_SOURCE_SPELL_POWER));

    // 老王237
    public static final RegistryObject<Item> LAO_WANG_237 = ITEMS.register("lao_wang_237", LaoWang237Curios::new);

    // 创世之诅咒
    public static final RegistryObject<Item> GENESIS_CURSE = ITEMS.register("genesis_curse", GenesisCurseItem::new);


    // 无限忏悔石
    public static final RegistryObject<Item> INFINITE_SHRIVING_STONE = ITEMS.register(
        "infinite_shriving_stone", InfiniteShrivingStoneItem::new
    );

    // 飞燕穿柳
    public static final RegistryObject<Item> FLYING_SWALLOW_THROUGH_Willow = ITEMS.register("flying_swallow_through_willow",
            FlyingSwallowThroughWillow::new);

    // 奥术水晶
    public static final ItemEntry<Item> ARCANE_CRYSTAL = Genesis.L2_REGISTRATE
            .item("arcane_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 猩红水晶
    public static final ItemEntry<Item> BLOOD_CRYSTAL = Genesis.L2_REGISTRATE
            .item("blood_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 邪术水晶
    public static final ItemEntry<Item> ELDRITCH_CRYSTAL = Genesis.L2_REGISTRATE
            .item("eldritch_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 末影水晶
    public static final ItemEntry<Item> ENDER_CRYSTAL = Genesis.L2_REGISTRATE
            .item("ender_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 唤魔水晶
    public static final ItemEntry<Item> EVOCATION_CRYSTAL = Genesis.L2_REGISTRATE
            .item("evocation_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 炽焰水晶
    public static final ItemEntry<Item> FIRE_CRYSTAL = Genesis.L2_REGISTRATE
            .item("fire_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 神圣水晶
    public static final ItemEntry<Item> HOLY_CRYSTAL = Genesis.L2_REGISTRATE
            .item("holy_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 冰霜水晶
    public static final ItemEntry<Item> ICE_CRYSTAL = Genesis.L2_REGISTRATE
            .item("ice_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 雷霆水晶
    public static final ItemEntry<Item> LIGHTNING_CRYSTAL = Genesis.L2_REGISTRATE
            .item("lightning_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 自然水晶
    public static final ItemEntry<Item> NATURE_CRYSTAL = Genesis.L2_REGISTRATE
            .item("nature_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 混沌水晶
    public static final ItemEntry<Item> CHAOS_CRYSTAL = Genesis.L2_REGISTRATE
            .item("chaos_crystal", properties -> new Item(
                    properties.stacksTo(16)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 星源水晶
    public static final RegistryObject<Item> CELESTIAL_SOURCE_CRYSTAL = ITEMS.register("celestial_source_crystal",
            () -> new CelestialSourceBaseItem(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.EPIC)
            ));

    public static final RegistryObject<Item> CHAOS_MANUSCRIPT = ITEMS.register("chaos_manuscript",
            ChaosManuscript::new);

    public static final RegistryObject<Item> CELESTIAL_SOURCE_MANUSCRIPT = ITEMS.register("celestial_source_manuscript",
            CelestialSourceManuscript::new);

    // 混沌手稿碎片
    public static final RegistryObject<Item> CHAOS_MANUSCRIPT_FRAGMENT = ITEMS.register("chaos_manuscript_fragment",
            () -> new ChaosBaseItem(new Item.Properties()
                    .rarity(Rarity.EPIC)) {
                @Override
                public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
                    list.add(Component.translatable(
                            "item." + Genesis.MOD_ID + ".chaos_manuscript_fragment.hover"
                    ));
                }
            });

    // 星源手稿碎片
    public static final RegistryObject<Item> BLANK_CELESTIAL_SOURCE_MANUSCRIPT = ITEMS.register("blank_celestial_source_manuscript",
            () -> new CelestialSourceBaseItem(new Item.Properties()
                    .fireResistant()
                    .rarity(Rarity.EPIC)) {
                @Override
                public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
                    list.add(Component.translatable(
                            "item." + Genesis.MOD_ID + ".blank_celestial_source_manuscript.hover"
                    ));
                }

                @Override
                public boolean isFoil(@NotNull ItemStack stack) {
                    return true;
                }
            });

    // 奥术水晶矿
    public static final RegistryObject<BlockItem> ARCANE_CRYSTAL_ORE_ITEM =
            ItemRegistry.ITEMS.register("arcane_crystal_ore", () -> new BlockItem(BlockRegistry.ARCANE_CRYSTAL_ORE.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> ARCANE_CRYSTAL_ORE_DEEPSLATE_ITEM =
            ItemRegistry.ITEMS.register("deepslate_arcane_crystal_ore", () -> new BlockItem(BlockRegistry.ARCANE_CRYSTAL_ORE_DEEPSLATE.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> NETHER_ARCANE_CRYSTAL_ORE_ITEM =
            ItemRegistry.ITEMS.register("nether_arcane_crystal_ore", () -> new BlockItem(BlockRegistry.NETHER_ARCANE_CRYSTAL_ORE.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> END_ARCANE_CRYSTAL_ORE_ITEM =
            ItemRegistry.ITEMS.register("end_arcane_crystal_ore", () -> new BlockItem(BlockRegistry.END_ARCANE_CRYSTAL_ORE.get(), new Item.Properties()));

    // 血肉魂铃
    public static final ItemEntry<Item> FLESH_SOUL_BELL = Genesis.L2_REGISTRATE
            .item("flesh_soul_bell", properties -> new Item(
                    properties.stacksTo(1)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    // 混沌原核
    public static final RegistryObject<Item> CHAOS_CORE = ItemRegistry.ITEMS.register("chaos_core", ChaosCore::new);

    // 血肉灵魂碎片
    public static final ItemEntry<Item> FLESH_SOUL_FRAGMENT = Genesis.L2_REGISTRATE
            .item("flesh_soul_fragment", properties -> new Item(
                    properties.stacksTo(1)
                            .rarity(Rarity.EPIC)
            ))
            .defaultModel()
            .register();

    public static final Map<TagKey<Item>, Set<RegistryObject<Item>>> itemTagMap = new HashMap<>();
    public static final RegistryObject<Item> ETERNAL_RING = registerCurios("eternal_ring", TagRegistry.RING, EternalRing::new);
    public static final RegistryObject<Item> LIGHTNING_RUNE_PLUS = registerCurios("lightning_rune_plus", TagRegistry.BODY, LightningRunePlus::new);
    public static final RegistryObject<Item> NATURE_RUNE_PLUS = registerCurios("nature_rune_plus", TagRegistry.BRACELET, NatureRunePlus::new);
    public static final RegistryObject<Item> ENDER_RUNE_PLUS = registerCurios("ender_rune_plus", TagRegistry.RING, EnderRunePlus::new);
    public static final RegistryObject<Item> HOLY_RUNE_PLUS = registerCurios("holy_rune_plus", TagRegistry.HANDS, HolyRunePlus::new);
    public static final RegistryObject<Item> ICE_RUNE_PLUS = registerCurios("ice_rune_plus", TagRegistry.HANDS, IceRunePlus::new);
    public static final RegistryObject<Item> BLOOD_RUNE_PLUS = registerCurios("blood_rune_plus", TagRegistry.CHARM, BloodRunePlus::new);
    public static final RegistryObject<Item> FIRE_RUNE_PLUS = registerCurios("fire_rune_plus", TagRegistry.HANDS, FireRunePlus::new);
    public static final RegistryObject<Item> ELDRITCH_RUNE_PLUS = registerCurios("eldritch_rune_plus", TagRegistry.HANDS, EldritchRunePlus::new);

    private static RegistryObject<Item> registerCurios(String name, TagKey<Item> tagKey, Supplier<Item> item) {
        RegistryObject<Item> register = ITEMS.register(name, item);
        Set<RegistryObject<Item>> set = itemTagMap.getOrDefault(tagKey, new HashSet<>());
        set.add(register);
        itemTagMap.put(tagKey, set);
        return register;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        PRE_ITEMS.register(eventBus);
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