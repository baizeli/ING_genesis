package com.baizeli.eternisstarrysky.spell;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GenesisEntityAttributes {

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(type -> {
            e.add(type, Attributes.CHAOS_SPELL_POWER.get());
            e.add(type, Attributes.CHAOS_MAGIC_RESIST.get());
            e.add(type, Attributes.CELESTIAL_SOURCE_SPELL_POWER.get());
            e.add(type, Attributes.CELESTIAL_SOURCE_MAGIC_RESIST.get());
        });
    }
}