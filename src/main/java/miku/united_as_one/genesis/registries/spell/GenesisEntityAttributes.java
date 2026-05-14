package miku.united_as_one.genesis.registries.spell;

import miku.united_as_one.genesis.registries.spell.SpellAttributesRegistry;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GenesisEntityAttributes {

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(type -> {
            // 混沌法术抗性/强度
            e.add(type, SpellAttributesRegistry.CHAOS_SPELL_POWER.get());
            e.add(type, SpellAttributesRegistry.CHAOS_MAGIC_RESIST.get());
            
            // 星源法术抗性/强度
            e.add(type, SpellAttributesRegistry.CELESTIAL_SOURCE_SPELL_POWER.get());
            e.add(type, SpellAttributesRegistry.CELESTIAL_SOURCE_MAGIC_RESIST.get());

            // 美食法术抗性/强度
            e.add(type, SpellAttributesRegistry.CULINARY_SPELL_POWER.get());
            e.add(type, SpellAttributesRegistry.CULINARY_MAGIC_RESIST.get());
        });
    }
}