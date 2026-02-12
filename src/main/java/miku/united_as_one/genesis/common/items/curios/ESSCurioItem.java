package miku.united_as_one.genesis.common.items.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

public class ESSCurioItem extends Item implements ICurioItem {
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();

    public ESSCurioItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @Nullable Level level,
            @NotNull List<Component> tooltipComponents,
            @NotNull TooltipFlag isAdvanced
    ) {
        tooltipComponents.add(Component.translatable(getItemTooltipKey(this)));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return attributeModifiers;
    }

    public String getItemTooltipKey(Item item) {
        return item.getDescriptionId() + ".hover";
    }
}
