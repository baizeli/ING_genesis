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
            // 混沌法术抗性/强度
            e.add(type, Attributes.CHAOS_SPELL_POWER.get());
            e.add(type, Attributes.CHAOS_MAGIC_RESIST.get());
            
            // 星源法术抗性/强度
            e.add(type, Attributes.CELESTIAL_SOURCE_SPELL_POWER.get());
            e.add(type, Attributes.CELESTIAL_SOURCE_MAGIC_RESIST.get());
        });
    }
}