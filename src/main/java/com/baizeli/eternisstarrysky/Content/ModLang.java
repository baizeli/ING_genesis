package com.baizeli.eternisstarrysky.Content;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Items.ModItems;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModLang {
	public record LangEntity<T>(T key, String zhCn, String enUs) {}
	public static final List<LangEntity<?>> langList = new ArrayList<>();
	private static final String hoverText = ".hover";
	public enum TranslatableMessage{
		CREATIVE_TAB_NAME(new LangEntity<>(
				"item_group." + EternisStarrySky.MODID + "." + EternisStarrySky.MODID,
				"铁之魔法书：创世纪",
				"Iron's Spells 'n Spellbooks: New genesis"
		)),

		;
		private final LangEntity<String> langEntity;
		TranslatableMessage(LangEntity<String> lang){
			this.langEntity = lang;
		}

		public String getKey(){
			return langEntity.key;
		}
	}

	/**
	 * 在此处定义翻译键值对
	 */
	public static void initLang(){
		langList.clear();

		langList.add(new LangEntity<>(ModItems.ETERNAL_RING.get(), "§d恒久之戒", "§dEternal Ring"));
		langList.add(new LangEntity<>(ModItems.LIGHTNING_RUNE_PLUS.get(), "§b强化雷霆符文", "§bLightning Rune Plus"));
		langList.add(new LangEntity<>(ModItems.NATURE_RUNE_PLUS.get(), "§b强化自然符文", "§bNature Rune Plus"));
		langList.add(new LangEntity<>(ModItems.ENDER_RUNE_PLUS.get(), "§b强化末影符文", "§bEnder Rune Plus"));
		langList.add(new LangEntity<>(ModItems.HOLY_RUNE_PLUS.get(), "§b强化神圣符文", "§bHoly Rune Plus"));
		langList.add(new LangEntity<>(ModItems.ICE_RUNE_PLUS.get(), "§b强化冰霜符文", "§bIce Rune Plus"));
		langList.add(new LangEntity<>(ModItems.BLOOD_RUNE_PLUS.get(), "§b强化猩红符文", "§bBlood Rune Plus"));
		langList.add(new LangEntity<>(ModItems.FIRE_RUNE_PLUS.get(), "§b强化炽焰符文", "§bFire Rune Plus"));
		langList.add(new LangEntity<>(ModItems.ELDRITCH_RUNE_PLUS.get(), "§b强化邪术符文", "§bEldritch Rune Plus"));

		langList.add(new LangEntity<>(
				getItemTooltipKey(ModItems.ETERNAL_RING.get()),
				"""
						§b§l所有法术实际释放等级+1
						§b§l免疫大多数原版负面效果
						§b§l免疫原版 §r§6火焰，冰冻，闪电 §b§l伤害
						§b§l免疫原版燃烧与冻结的效果
						§e 比钻石更久远""",
				"""
						......"""
		));
		langList.add(new LangEntity<>(
				getItemTooltipKey(ModItems.LIGHTNING_RUNE_PLUS.get()),
				"""
						雷暴法术会立即释放无需短暂瞄准
						雷鸣长枪会额外释放一道伤害为长枪面板伤害*0.75的冲击波""",
				"""
						......"""
		));
		langList.add(new LangEntity<>(
				getItemTooltipKey(ModItems.NATURE_RUNE_PLUS.get()),
				"""
						毒箭射击和毒液飞溅产生的毒雾伤害*2，范围*2，且无视护甲
						腐蚀喷吐影响范围*1.5，释放后回复一半消耗的法力值""",
				"""
						......"""
		));
		langList.add(new LangEntity<>(
				getItemTooltipKey(ModItems.ENDER_RUNE_PLUS.get()),
				"""
						星海落瀑召唤的彗星爆炸范围*4，伤害*2
						魔法箭命中目标后延迟半秒会在目标处释放同等伤害的回响打击""",
				"""
						......"""
		));
		langList.add(new LangEntity<>(
				getItemTooltipKey(ModItems.HOLY_RUNE_PLUS.get()),
				"""
						治愈之环的影响半径*3，治疗频率*2
						圣灵会给目标施加曳光弹的debuff效果，持续时间与距离相等
						圣灵伤害*1.5""",
				"""
						......"""
		));
		langList.add(new LangEntity<>(
				getItemTooltipKey(ModItems.ICE_RUNE_PLUS.get()),
				"""
						霜降召唤的冰块会极快下落并具有双重判定
						霜降还会对目标造成§c缓慢V(00:08)§r
						冰霜箭速度*4，伤害施加施法者护甲值的25%""",
				"""
						......"""
		));
		langList.add(new LangEntity<>(
				getItemTooltipKey(ModItems.BLOOD_RUNE_PLUS.get()),
				"""
						血步后的下次攻击将附加一次同等级但伤害为该次攻击50%的嗜血啃咬
						猩红斩击会回复与造成伤害相等的魔法值""",
				"""
						......"""
		));
		langList.add(new LangEntity<>(
				getItemTooltipKey(ModItems.FIRE_RUNE_PLUS.get()),
				"""
						由灼烧/岩浆炸弹产生的灼焰场域伤害*1.5，半径+2，持续时间*2
						炽焰追踪弹幕只能存在2秒，但消失后重新生成一次且伤害*1.8""",
				"""
						......"""
		));
		langList.add(new LangEntity<>(
				getItemTooltipKey(ModItems.ELDRITCH_RUNE_PLUS.get()),
				"""
						邪术冲击波改为多重锁定施法""",
				"""
						......"""
		));

		initLangMessage();
	}

	private static void initLangMessage() {
		for (TranslatableMessage value : TranslatableMessage.values()) {
			langList.add(value.langEntity);
		}
	}

	public static String getItemTooltipKey(Item item) {
		return item.getDescriptionId() + hoverText;
	}
}
