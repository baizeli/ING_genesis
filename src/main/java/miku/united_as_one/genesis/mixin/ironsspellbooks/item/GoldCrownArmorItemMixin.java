package miku.united_as_one.genesis.mixin.ironsspellbooks.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.GoldCrownArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

@Mixin(GoldCrownArmorItem.class)
public class GoldCrownArmorItemMixin implements ICurioItem {
    @Unique
    private static final String eternisStarrySky$SLOT = "crown";
    @Unique
    private static final AttributeContainer[] eternisStarrySky$ATTRIBUTES = {
            new AttributeContainer(AttributeRegistry.MAX_MANA, 10000.0F, AttributeModifier.Operation.ADDITION),
            new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.75, AttributeModifier.Operation.MULTIPLY_TOTAL),
            new AttributeContainer(AttributeRegistry.SPELL_POWER, 1.0F, AttributeModifier.Operation.MULTIPLY_TOTAL)
    };

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (!slotContext.identifier().equals(eternisStarrySky$SLOT)) {
            return ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack);
        }

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        String id = String.format("%s_%s", eternisStarrySky$SLOT, slotContext.index());
        for (AttributeContainer holder : eternisStarrySky$ATTRIBUTES) {
            builder.put(holder.attribute().get(), holder.createModifier(id));
        }
        return builder.build();
    }

    @Override
    public ICurio.@NotNull SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }
}