package miku.united_as_one.genesis.content;

import miku.united_as_one.genesis.util.ModCurios;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Map;

public class ModTags {
	private static final Map<ResourceLocation, TagKey<Item>> itemTags = new HashMap<>();
	public static final TagKey<Item> RING = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, ModCurios.RING.getName()));
	public static final TagKey<Item> HEAD = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, ModCurios.HEAD.getName()));
	public static final TagKey<Item> HANDS = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, ModCurios.HANDS.getName()));
	public static final TagKey<Item> CROWN = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, ModCurios.CROWN.getName()));
	public static final TagKey<Item> CHARM = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, ModCurios.CHARM.getName()));
	public static final TagKey<Item> BRACELET = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, ModCurios.BRACELET.getName()));
	public static final TagKey<Item> BODY = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, ModCurios.BODY.getName()));
	public static final TagKey<Item> BELT = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, ModCurios.BELT.getName()));
	public static final TagKey<Item> BACK = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, ModCurios.BACK.getName()));
	public static final TagKey<Item> SPELLBOOK = createItem(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "spellbook"));

	private static TagKey<Item> createItem(final ResourceLocation name) {
		TagKey<Item> itemTagKey = TagKey.create(Registries.ITEM, name);
		itemTags.put(name, itemTagKey);
		return itemTagKey;
	}

	public static TagKey<Item> getTagKeyByResourceLocation(ResourceLocation resourceLocation) {
		return itemTags.get(resourceLocation);
	}
}
