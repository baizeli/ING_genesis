package miku.united_as_one.genesis.contents.items.curios;

import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class GenesisCurseItem extends Item implements ICurioItem, IPresetSpellContainer {
    public GenesisCurseItem() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }

    public boolean isEquippedBy(@Nullable LivingEntity entity) {
        return entity != null && CuriosApi.getCuriosInventory(entity).map((inv) -> inv.findFirstCurio(this).isPresent()).orElse(false);
    }

    public static boolean isEquippedBy(@Nullable LivingEntity entity, Item item) {
        return entity != null && CuriosApi.getCuriosInventory(entity).map((inv) -> inv.findFirstCurio(item).isPresent()).orElse(false);
    }

    @NotNull
    public ICurio.@NotNull SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (!ISpellContainer.isSpellContainer(itemStack)) {
            ISpellContainer container = ISpellContainer.create(100, true, true);
            ISpellContainer.set(itemStack, container);
        }
    }
}
