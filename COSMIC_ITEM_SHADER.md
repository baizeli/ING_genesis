# 物品模型星空 Shader 使用说明

GenesisLib 的物品模型星空效果由两部分组成：

- 物品模型使用 `genesis_magic:cosmic` loader，并提供 mask 贴图。
- 通过数据配置或注解给物品注册 `GenesisItemShaderEffect`。

只有第二步没有用，模型必须先接入 cosmic loader，否则 shader 不会参与渲染。

## 模型写法

物品模型示例：

```json
{
  "parent": "item/generated",
  "textures": {
    "layer0": "genesis_magic:item/your_item"
  },
  "loader": "genesis_magic:cosmic",
  "cosmic": {
    "mask": "genesis_magic:item/mask/your_item_mask"
  }
}
```

`layer0` 是普通物品贴图。`mask` 是星空显示区域，白色区域显示星空，黑色或透明区域不显示。

星空 shader、`cosmic_0` 到 `cosmic_9` 星点贴图在 `genesis_api` 里；mask 仍然放本体资源域。

## 数据配置

数据配置放在本体配置数据目录：

```text
src/main/resources/data/genesis_magic/genesis_magic_config/item_shader_effects/
```

文件名对应物品 id。比如：

```text
infinity_sword.json -> genesis_magic:infinity_sword
violet_galaxy_ingot.json -> genesis_magic:violet_galaxy_ingot
```

配置格式：

```json
{
  "use_type": 10,
  "scale": 0.6,
  "red": 0.0,
  "green": 0.02,
  "blue": 0.03,
  "alpha": 1.0
}
```

字段说明：

- `use_type`：星空预设类型，范围 `0` 到 `15`。
- `scale`：星空缩放。通常 `0.6` 即可。
- `red` / `green` / `blue`：传给 shader 的颜色参数。
- `alpha`：传给 shader 的第四颜色参数。部分预设会用它作为特殊模式开关。

## 注解方式

物品类可以直接加注解：

```java
@GenesisCosmicItemEffect(
        useType = 10,
        scale = 0.6F,
        red = 0.0F,
        green = 0.02F,
        blue = 0.03F,
        alpha = 1.0F
)
public class YourItem extends Item {
    public YourItem(Properties properties) {
        super(properties);
    }
}
```

注册后调用：

```java
GenesisAnnotations.registerCosmicItemEffect(ItemRegistry.YOUR_ITEM.get());
```

如果注解写在独立模板类上：

```java
GenesisAnnotations.registerCosmicItemEffect(ItemRegistry.YOUR_ITEM.get(), YourAnnotatedClass.class);
```

## 16 种 use_type

`use_type` 当前是数字预设，不是 enum。

| use_type | 效果说明 |
| --- | --- |
| `0` | 基础深色星空。`alpha = 2.33` 时偏粉紫，否则可用 `red/green/blue` 染色。 |
| `1` | 纯颜色/彩虹模式。`alpha = 1.33` 时走彩虹，否则直接使用 `red/green/blue`。 |
| `2` | 灰黑随机彩点星空，整体偏暗。 |
| `3` | 紫色水晶/折射感星空。 |
| `4` | 紫色星云与流动尘埃。 |
| `5` | 深蓝闪烁星点，带脉冲。 |
| `6` | 青黑底彩色星点。 |
| `7` | 多彩幻彩星云。 |
| `8` | 柔和梦幻渐变星云。 |
| `9` | 蓝色雾状星空。当前 `CELESTIAL_SOURCE` tooltip 背景使用这个。 |
| `10` | 极光/冷蓝星空。当前 `infinity_sword` 使用这个。 |
| `11` | 旋涡/核心发光型星空。 |
| `12` | 黑底白星密集星空。 |
| `13` | 粉紫星云，适合紫极、幻彩类物品。 |
| `14` | 红黑混沌星空。当前 `CHAOS` tooltip 背景使用这个。 |
| `15` | 云雾 raymarch 星云。当前 `avaritia_infinity_sword` 使用这个。 |

## 常用模板

紫极/粉紫星云：

```json
{
  "use_type": 13,
  "scale": 0.6,
  "red": 0.0,
  "green": 0.02,
  "blue": 0.03,
  "alpha": 1.0
}
```

无限/创造类云雾星云：

```json
{
  "use_type": 15,
  "scale": 0.6,
  "red": 0.1,
  "green": 0.1,
  "blue": 0.1,
  "alpha": 1.0
}
```

冷蓝极光：

```json
{
  "use_type": 10,
  "scale": 0.6,
  "red": 0.0,
  "green": 0.02,
  "blue": 0.03,
  "alpha": 1.0
}
```

彩虹：

```json
{
  "use_type": 1,
  "scale": 0.6,
  "red": 1.0,
  "green": 1.0,
  "blue": 1.0,
  "alpha": 1.33
}
```

红黑混沌：

```json
{
  "use_type": 14,
  "scale": 0.6,
  "red": 0.08,
  "green": 0.0,
  "blue": 0.0,
  "alpha": 1.0
}
```

## 排查清单

如果物品没有星空效果，按顺序检查：

1. 物品模型是否有 `"loader": "genesis_magic:cosmic"`。
2. `cosmic.mask` 指向的 mask 贴图是否存在。
3. 数据配置文件名是否和物品 id path 一致。
4. 数据配置是否在 `data/genesis_magic/genesis_magic_config/item_shader_effects/`。
5. 注解方式是否调用了 `GenesisAnnotations.registerCosmicItemEffect(...)`。
6. 客户端日志里是否成功注册了 `genesis_api:cosmic` shader。

