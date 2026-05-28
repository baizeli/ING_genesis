package miku.united_as_one.genesis.data.equipment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

public final class EquipmentStatsManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static volatile Map<ResourceLocation, EquipmentStats> stats = Map.of();
    private static volatile Map<ResourceLocation, EquipmentStats> previousStats = Map.of();

    private EquipmentStatsManager() {
    }

    public static void warn(String message, Object... args) {
        LOGGER.warn(message, args);
    }

    public static Map<ResourceLocation, EquipmentStats> currentStats() {
        return stats;
    }

    public static EquipmentStats get(ResourceLocation itemId) {
        return stats.get(itemId);
    }

    public static void rebuildFromConfig() {
        Map<ResourceLocation, EquipmentStats> loaded = new LinkedHashMap<>();
        for (EquipmentStatsConfig config : ModEquipmentStatsConfigs.EQUIPMENT_STATS.getAll()) {
            ResourceLocation id = config.getID();
            if (id == null) {
                continue;
            }
            try {
                loaded.put(id, config.toStats());
            } catch (RuntimeException exception) {
                LOGGER.warn("Skipping invalid equipment stats {}", id, exception);
            }
        }
        previousStats = stats;
        stats = Map.copyOf(loaded);
        LOGGER.info("Loaded {} equipment stat entries from L2Library config", stats.size());
    }

    public static void refreshPlayers(Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.isEmpty()) {
                    continue;
                }
                ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
                if (itemId == null) {
                    continue;
                }
                removeGeneratedEquipmentModifiers(player, itemId, stack, slot);
                Multimap<Attribute, AttributeModifier> modifiers = buildEquipmentModifiers(stack, slot);
                if (!modifiers.isEmpty()) {
                    player.getAttributes().addTransientAttributeModifiers(modifiers);
                }
            }
            refreshCurios(player);
        }
    }

    public static OptionalInt getConfiguredDurability(ItemStack stack) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        EquipmentStats itemStats = itemId == null ? null : get(itemId);
        return itemStats != null && itemStats.durability() != null
                ? OptionalInt.of(itemStats.durability())
                : OptionalInt.empty();
    }

    public static Multimap<Attribute, AttributeModifier> buildEquipmentModifiers(ItemStack stack, EquipmentSlot slot) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            return HashMultimap.create();
        }
        EquipmentStats itemStats = get(itemId);
        if (itemStats == null) {
            return HashMultimap.create();
        }

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        if (slot == EquipmentSlot.MAINHAND && itemStats.weapon() != null) {
            addModifier(modifiers, itemId, slot.getName(), Attributes.ATTACK_DAMAGE, EquipmentStatsDefaults.ATTACK_DAMAGE,
                    itemStats.weapon().attackDamage(), AttributeModifier.Operation.ADDITION);
            addModifier(modifiers, itemId, slot.getName(), Attributes.ATTACK_SPEED, EquipmentStatsDefaults.ATTACK_SPEED,
                    itemStats.weapon().attackSpeed(), AttributeModifier.Operation.ADDITION);
        }

        if (stack.getItem() instanceof ArmorItem armorItem
                && armorItem.getEquipmentSlot() == slot
                && itemStats.armor() != null) {
            addModifier(modifiers, itemId, slot.getName(), Attributes.ARMOR, EquipmentStatsDefaults.ARMOR,
                    itemStats.armor().armor(), AttributeModifier.Operation.ADDITION);
            addModifier(modifiers, itemId, slot.getName(), Attributes.ARMOR_TOUGHNESS, EquipmentStatsDefaults.ARMOR_TOUGHNESS,
                    itemStats.armor().armorToughness(), AttributeModifier.Operation.ADDITION);
            if (itemStats.armor().knockbackResistance() != 0.0D) {
                addModifier(modifiers, itemId, slot.getName(), Attributes.KNOCKBACK_RESISTANCE, EquipmentStatsDefaults.KNOCKBACK_RESISTANCE,
                        itemStats.armor().knockbackResistance(), AttributeModifier.Operation.ADDITION);
            }
            addConfiguredAttributes(modifiers, itemId, slot.getName(), itemStats, true);
        }
        return modifiers;
    }

    public static Multimap<Attribute, AttributeModifier> buildCurioModifiers(ItemStack stack, SlotContext slotContext) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            return HashMultimap.create();
        }
        EquipmentStats itemStats = get(itemId);
        if (itemStats == null || itemStats.curio() == null) {
            return HashMultimap.create();
        }

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        addConfiguredAttributes(modifiers, itemId, curioSlotKey(slotContext), itemStats, false);
        return modifiers;
    }

    public static boolean controlsCurio(ItemStack stack) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        EquipmentStats itemStats = itemId == null ? null : get(itemId);
        return itemStats != null && itemStats.curio() != null;
    }

    public static Set<Attribute> controlledEquipmentAttributes(ItemStack stack, EquipmentSlot slot) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            return Set.of();
        }
        EquipmentStats itemStats = get(itemId);
        Set<ResourceLocation> ids = new HashSet<>();

        if (slot == EquipmentSlot.MAINHAND && hasWeapon(itemStats)) {
            ids.add(EquipmentStatsDefaults.ATTACK_DAMAGE);
            ids.add(EquipmentStatsDefaults.ATTACK_SPEED);
        }

        if (stack.getItem() instanceof ArmorItem armorItem
                && armorItem.getEquipmentSlot() == slot
                && hasArmor(itemStats)) {
            ids.add(EquipmentStatsDefaults.ARMOR);
            ids.add(EquipmentStatsDefaults.ARMOR_TOUGHNESS);
            ids.add(EquipmentStatsDefaults.KNOCKBACK_RESISTANCE);
            addAttributeIds(ids, itemStats);
        }

        return resolveAttributes(ids);
    }

    public static double getWeaponAttackDamage(ItemStack stack, double fallback) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        EquipmentStats itemStats = itemId == null ? null : get(itemId);
        return itemStats != null && itemStats.weapon() != null ? itemStats.weapon().attackDamage() : fallback;
    }

    private static void refreshCurios(ServerPlayer player) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            for (ICurioStacksHandler stacksHandler : handler.getCurios().values()) {
                for (int index = 0; index < stacksHandler.getStacks().getSlots(); index++) {
                    ItemStack stack = stacksHandler.getStacks().getStackInSlot(index);
                    if (stack.isEmpty()) {
                        continue;
                    }
                    ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
                    if (itemId == null) {
                        continue;
                    }
                    SlotContext slotContext = new SlotContext(stacksHandler.getIdentifier(), player, index, false, true);
                    removeGeneratedCurioModifiers(player, itemId, slotContext);
                    Multimap<Attribute, AttributeModifier> modifiers = buildCurioModifiers(stack, slotContext);
                    if (!modifiers.isEmpty()) {
                        player.getAttributes().addTransientAttributeModifiers(modifiers);
                    }
                }
            }
        });
    }

    private static boolean hasWeapon(EquipmentStats itemStats) {
        return itemStats != null && itemStats.weapon() != null;
    }

    private static boolean hasArmor(EquipmentStats itemStats) {
        return itemStats != null && itemStats.armor() != null;
    }

    private static boolean hasCurio(EquipmentStats itemStats) {
        return itemStats != null && itemStats.curio() != null;
    }

    private static void addConfiguredAttributes(
            Multimap<Attribute, AttributeModifier> modifiers,
            ResourceLocation itemId,
            String slotKey,
            EquipmentStats itemStats,
            boolean skipArmorAttributes
    ) {
        for (EquipmentStats.AttributeStat attributeStat : itemStats.attributes()) {
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeStat.attribute());
            if (attribute == null) {
                LOGGER.warn("Skipping missing attribute {} for equipment {}", attributeStat.attribute(), itemId);
                continue;
            }
            if (skipArmorAttributes && (attribute == Attributes.ARMOR || attribute == Attributes.ARMOR_TOUGHNESS)) {
                continue;
            }
            addModifier(modifiers, itemId, slotKey, attribute, attributeStat.attribute(),
                    attributeStat.amount(), attributeStat.operation());
        }
    }

    private static void addAttributeIds(Set<ResourceLocation> ids, EquipmentStats itemStats) {
        if (itemStats != null) {
            itemStats.attributes().forEach(attribute -> ids.add(attribute.attribute()));
        }
    }

    private static Set<Attribute> resolveAttributes(Set<ResourceLocation> ids) {
        Set<Attribute> result = new HashSet<>();
        for (ResourceLocation id : ids) {
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(id);
            if (attribute != null) {
                result.add(attribute);
            }
        }
        return result;
    }

    private static void removeGeneratedEquipmentModifiers(ServerPlayer player, ResourceLocation itemId, ItemStack stack, EquipmentSlot slot) {
        Set<ResourceLocation> attributeIds = new HashSet<>();
        EquipmentStats current = get(itemId);
        EquipmentStats previous = previousStats.get(itemId);

        if (hasWeapon(current) || hasWeapon(previous)) {
            attributeIds.add(EquipmentStatsDefaults.ATTACK_DAMAGE);
            attributeIds.add(EquipmentStatsDefaults.ATTACK_SPEED);
        }
        if (hasArmor(current) || hasArmor(previous)) {
            attributeIds.add(EquipmentStatsDefaults.ARMOR);
            attributeIds.add(EquipmentStatsDefaults.ARMOR_TOUGHNESS);
            attributeIds.add(EquipmentStatsDefaults.KNOCKBACK_RESISTANCE);
            addAttributeIds(attributeIds, current);
            addAttributeIds(attributeIds, previous);
        }

        removeModifiers(player, itemId, slot.getName(), attributeIds);
        removeOriginalModifiers(player, stack, slot, resolveAttributes(attributeIds));
    }

    private static void removeGeneratedCurioModifiers(ServerPlayer player, ResourceLocation itemId, SlotContext slotContext) {
        Set<ResourceLocation> attributeIds = new HashSet<>();
        EquipmentStats current = get(itemId);
        EquipmentStats previous = previousStats.get(itemId);

        if (hasCurio(current) || hasCurio(previous)) {
            addAttributeIds(attributeIds, current);
            addAttributeIds(attributeIds, previous);
        }

        removeModifiers(player, itemId, curioSlotKey(slotContext), attributeIds);
    }

    private static void removeModifiers(ServerPlayer player, ResourceLocation itemId, String slotKey, Set<ResourceLocation> attributeIds) {
        for (ResourceLocation attributeId : attributeIds) {
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeId);
            if (attribute == null) {
                continue;
            }
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance != null) {
                instance.removeModifier(uuidFor(itemId, slotKey, attributeId));
            }
        }
    }

    private static void removeOriginalModifiers(ServerPlayer player, ItemStack stack, EquipmentSlot slot, Set<Attribute> attributes) {
        if (attributes.isEmpty()) {
            return;
        }
        stack.getItem().getDefaultAttributeModifiers(slot).forEach((attribute, modifier) -> {
            if (!attributes.contains(attribute)) {
                return;
            }
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance != null) {
                instance.removeModifier(modifier.getId());
            }
        });
    }

    private static void addModifier(
            Multimap<Attribute, AttributeModifier> modifiers,
            ResourceLocation itemId,
            String slotKey,
            Attribute attribute,
            ResourceLocation attributeId,
            double amount,
            AttributeModifier.Operation operation
    ) {
        modifiers.put(attribute, new AttributeModifier(
                uuidFor(itemId, slotKey, attributeId),
                Genesis.MOD_ID + "." + attributeId,
                amount,
                operation
        ));
    }

    private static String curioSlotKey(SlotContext slotContext) {
        return "curio:" + slotContext.identifier() + ":" + slotContext.index();
    }

    private static UUID uuidFor(ResourceLocation itemId, String slotKey, ResourceLocation attributeId) {
        String key = Genesis.MOD_ID + "|" + itemId + "|" + slotKey + "|" + attributeId;
        return UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8));
    }
}
