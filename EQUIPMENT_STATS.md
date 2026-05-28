# 装备属性数据包维护说明

代码里只保留物品行为、材质、稀有度、特效、提示文本等逻辑；数值本身统一从 `equipment_stats` 数据读取。

一句话版本：新增装备时，先正常注册物品，再到 `EquipmentStatsDefaults` 写默认数据，最后跑 `runData` 生成 JSON。

## 关键文件

- `src/main/java/miku/united_as_one/genesis/data/equipment/EquipmentStatsDefaults.java`
  默认数值都写在这里。这里也是给维护者看的总表，中文注释尽量写清楚这个属性是做什么的

- `src/main/java/miku/united_as_one/genesis/data/datagen/provider/ModEquipmentStatsProvider.java`
  datagen 提供器，会把 `EquipmentStatsDefaults.all()` 写成数据包 JSON

- `src/generated/resources/data/iron_spells_genesis/iron_spells_genesis_config/equipment_stats/`
  datagen 生成的默认数据。不要手改这里，改了下次 `runData` 会被覆盖

- `src/main/java/miku/united_as_one/genesis/data/equipment/EquipmentStatsManager.java`
  运行时加载和注入属性的地方。正常加装备不用动它。

- `src/main/java/miku/united_as_one/genesis/mixin/minecraft/world/item/ItemStackMixin.java`
  让耐久也可以从数据里接管。正常加装备不用动它。

## 数据文件路径

本模组物品默认生成在：

```text
data/iron_spells_genesis/iron_spells_genesis_config/equipment_stats/<物品名>.json
```

例如：

```text
data/iron_spells_genesis/iron_spells_genesis_config/equipment_stats/violet_sword.json
```

如果以后要在数据包里控制别的命名空间的物品，路径里的 `data/<namespace>` 要跟物品 ID 的命名空间一致：

```text
data/<物品命名空间>/iron_spells_genesis_config/equipment_stats/<物品路径>.json
```

文件名对应完整物品 ID。比如 `iron_spells_genesis:chaos_staff` 对应 `data/iron_spells_genesis/.../chaos_staff.json`。

## JSON 格式

一个文件可以只写需要接管的部分。可用字段如下：

```json
{
  "durability": 8000,
  "weapon": {
    "attack_damage": 20.0,
    "attack_speed": -2.7
  },
  "armor": {
    "armor": 8.0,
    "armor_toughness": 3.0,
    "knockback_resistance": 0.0
  },
  "curio": {},
  "attributes": [
    {
      "attribute": "irons_spellbooks:cooldown_reduction",
      "amount": 0.15,
      "operation": "multiply_total"
    }
  ]
}
```

字段含义：

- `durability`：耐久。写 `0` 表示这个物品不可损坏；不写则不接管耐久。
- `weapon`：武器主手属性，只包含 `attack_damage` 和 `attack_speed`。
- `armor`：盔甲基础属性，包含护甲、韧性、击退抗性。
- `curio`：写成 `{}` 表示这个物品在 Curios 槽位由数据系统接管属性。
- `attributes`：额外属性列表，可以给武器、盔甲、饰品使用。

`operation` 支持：

- `addition`
- `multiply_base`
- `multiply_total`

也兼容 `add`、`multiply`、`0`、`1`、`2` 这些写法，但默认数据里建议统一写上面三个名字。

## 新增武器

先正常在 `ItemRegistry` 里注册物品，但不要在物品类里写属性。

武器或工具构造器里要把原版基础攻击属性抵消掉，避免和数据包属性叠加。例如：

```java
public ExampleSword(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
    super(tier, (int) -tier.getAttackDamageBonus(), 0.0F, properties);
}
```

然后在 `EquipmentStatsDefaults#createDefaults()` 里加默认数据：

```java
putWeapon(defaults, "example_sword", 2500, 12.0D, -2.4D);
```

如果武器还需要铁魔法属性，就用带 `attributes` 的写法：

```java
putWeapon(defaults, "example_sword", 2500, 12.0D, -2.4D, List.of(
        attr(SPELL_POWER, 0.10D, AttributeModifier.Operation.MULTIPLY_BASE),
        attr(COOLDOWN_REDUCTION, 0.08D, AttributeModifier.Operation.MULTIPLY_BASE)
));
```

注意：不要再重写 `getAttributeModifiers` 去塞攻击伤害、攻速或铁魔法属性。

## 新增法杖

法杖类里只保留物品本体和行为，`StaffTier` 写成零属性：

```java
public ExampleStaff() {
    super(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
            new StaffTier(0, 0));
}
```

然后在 `EquipmentStatsDefaults` 里写数据：

```java
putWeapon(defaults, "example_staff", null, 6.0D, -3.0D, List.of(
        attr(SPELL_POWER, 0.15D, AttributeModifier.Operation.MULTIPLY_BASE),
        attr(COOLDOWN_REDUCTION, 0.20D, AttributeModifier.Operation.MULTIPLY_BASE)
));
```

这里 `durability` 传 `null` 表示不接管耐久。想让法杖有耐久就填具体数字。

## 新增盔甲

盔甲材料类里不要写实际数值。`ModArmorMaterials` 里的护甲值、韧性、击退抗性、耐久、额外属性都应该返回 `0` 或空表，让数据接管。

新增一整套盔甲时，推荐用 `putArmorSet`：

```java
putArmorSet(defaults, "example_spell",
        800, 900, 850, 750,
        4, 9, 7, 4,
        3.0D,
        0.0D,
        List.of(
                attr(MAX_MANA, 200.0D, AttributeModifier.Operation.ADDITION),
                attr(SPELL_POWER, 0.07D, AttributeModifier.Operation.MULTIPLY_BASE)
        ));
```

参数顺序是：

```text
前缀,
头盔耐久, 胸甲耐久, 护腿耐久, 靴子耐久,
头盔护甲, 胸甲护甲, 护腿护甲, 靴子护甲,
护甲韧性,
击退抗性,
额外属性列表
```

它会生成：

```text
example_spell_helmet
example_spell_chestplate
example_spell_leggings
example_spell_boots
```

## 新增饰品

饰品类不要再用 `attributeModifiers.put(...)` 写属性。基础饰品 `ESSCurioItem` 已经返回空属性，运行时会由数据系统注入。

在 `EquipmentStatsDefaults` 里写：

```java
putCurio(defaults, "example_ring", List.of(
        attr(MAX_MANA, 200.0D, AttributeModifier.Operation.ADDITION),
        attr(COOLDOWN_REDUCTION, 0.15D, AttributeModifier.Operation.MULTIPLY_BASE)
));
```

如果只是想让数据包作者以后能覆盖，但默认不加属性，也可以写空列表：

```java
putCurio(defaults, "example_charm", List.of());
```

生成的 JSON 会带 `curio: {}`，表示这个饰品槽位已经交给数据系统接管。

## 只接管耐久

有些物品不需要攻击属性或饰品属性，只想把耐久变成数据控制：

```java
putDurability(defaults, "example_bow", 2009);
```

生成 JSON：

```json
{
  "durability": 2009
}
```

耐久规则：

- `durability > 0`：物品可损坏，最大耐久为这个值。
- `durability == 0`：物品不可损坏。
- 不写 `durability`：不接管这个物品的耐久。

## 运行 datagen

改完 `EquipmentStatsDefaults` 后运行：

```powershell
.\gradlew.bat runData --no-daemon --console=plain
```

常规检查：

```powershell
.\gradlew.bat compileJava --no-daemon --console=plain
```

目前 `runData` 可能在末尾打印 `arcane_cauldron` 引用 `irons_spellbooks:block/alchemist_cauldron` 模型不存在的旧异常。如果 Gradle 最后是 `BUILD SUCCESSFUL`，并且 equipment stats JSON 已经生成，这个异常和装备属性数据无关。

## 数据包覆盖示例

玩家或整合包想改紫晶剑数值，可以放一个数据包文件：

```text
data/iron_spells_genesis/iron_spells_genesis_config/equipment_stats/violet_sword.json
```

内容例如：

```json
{
  "attributes": [
    {
      "amount": 0.25,
      "attribute": "irons_spellbooks:cooldown_reduction",
      "operation": "multiply_total"
    }
  ],
  "durability": 12000,
  "weapon": {
    "attack_damage": 24.0,
    "attack_speed": -2.6
  }
}
```

服务端重载数据包后会重新加载，并同步给玩家。多人游戏以服务端数据为准。

## 提交前检查

提交前建议做三件事：

```powershell
rg -n "attributeModifiers\.put|withSpellbookAttributes|new AttributeContainer|new AttributeModifier" src/main/java/miku/united_as_one/genesis/contents/items src/main/java/miku/united_as_one/genesis/registries/item
.\gradlew.bat compileJava --no-daemon --console=plain
.\gradlew.bat runData --no-daemon --console=plain
```

第一条命令应该没有输出。`EquipmentStatsManager` 里出现 `new AttributeModifier` 是正常的，因为它是统一注入属性的地方。

## 常见坑

- 不要手改 `src/generated/resources` 下的 equipment stats JSON，改 `EquipmentStatsDefaults`。
- 不要在物品类、盔甲材料、饰品类里再塞属性，不然会和数据包属性叠加。
- 武器如果继承 `SwordItem`、`AxeItem`、`PickaxeItem` 等，需要把构造器里的原版攻击属性抵消掉。
- `attributes` 里的 `attribute` 必须是真实存在的属性 ID，写错会被静默跳过。
- 盔甲的 `armor`、`armor_toughness`、`knockback_resistance` 会被限制为不小于 0。
- `curio: {}` 不等于装饰字段，它表示“这个物品的 Curios 属性由数据系统接管”。
