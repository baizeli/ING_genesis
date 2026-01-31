package miku.united_as_one.genesis.event.spell;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registry.spell.SpellAttributesRegistry;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpellAttributesEvent {

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(
            entity -> SpellAttributesRegistry
                .getAttributes()
                .getEntries()
                .forEach(attribute -> e.add(entity, attribute.get()))
        );
    }
}