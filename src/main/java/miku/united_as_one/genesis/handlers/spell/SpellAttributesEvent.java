package miku.united_as_one.genesis.handlers.spell;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.spell.SpellAttributesRegistry;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpellAttributesEvent {

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(
            entity -> SpellAttributesRegistry
                .getAttributes()
                .getEntries()
                .forEach(attribute -> event.add(entity, attribute.get()))
        );    
    }
}