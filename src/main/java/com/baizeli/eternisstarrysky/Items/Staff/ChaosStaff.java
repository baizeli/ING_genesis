package com.baizeli.eternisstarrysky.items.staff;

import com.baizeli.eternisstarrysky.spell.Attributes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ChaosStaff extends StaffItem {
    public ChaosStaff() {
        super(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
            new StaffTier(6, -3,
                new AttributeContainer(
                    Attributes.CHAOS_SPELL_POWER, 0.15,
                    AttributeModifier.Operation.MULTIPLY_BASE
                ),
                new AttributeContainer(
                    AttributeRegistry.COOLDOWN_REDUCTION, 0.20,
                    AttributeModifier.Operation.MULTIPLY_BASE
                )
            )
        );
    }
}