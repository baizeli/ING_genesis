package miku.united_as_one.genesis.common.event.spell.attributes;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import miku.united_as_one.genesis.init.registry.spell.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unchecked")
@Mod.EventBusSubscriber
public class SpellPenetrationEvent {

    @SubscribeEvent
    public static void onSpellDamage(SpellDamageEvent event) {
        var spellDamageSource = event.getSpellDamageSource();
        
        if (!(spellDamageSource.getEntity() instanceof LivingEntity livingAttacker)) return;

        event.setAmount((float) (event.getAmount() * livingAttacker.getAttributeValue(SpellAttributesRegistry.SPELL_DAMAGE_PERCENT.get())));

        for (Object[] data : new Object[][] {
            {SchoolRegistry.FIRE.get(), SpellAttributesRegistry.FLAME_SPELL_PENETRATION},
            {SchoolRegistry.HOLY.get(), SpellAttributesRegistry.HOLY_SPELL_PENETRATION},
            {SchoolRegistry.ICE.get(), SpellAttributesRegistry.FROST_SPELL_PENETRATION},
            {SchoolRegistry.BLOOD.get(), SpellAttributesRegistry.SCARLET_SPELL_PENETRATION},
            {SchoolRegistry.ENDER.get(), SpellAttributesRegistry.ENDER_SPELL_PENETRATION},
            {SchoolRegistry.LIGHTNING.get(), SpellAttributesRegistry.THUNDER_SPELL_PENETRATION},
            {SchoolRegistry.NATURE.get(), SpellAttributesRegistry.NATURE_SPELL_PENETRATION},
            {SchoolRegistry.ELDRITCH.get(), SpellAttributesRegistry.WARLOCK_SPELL_PENETRATION},
            {SpellSchoolRegistry.CHAOS.get(), SpellAttributesRegistry.CHAOS_SPELL_PENETRATION},
            {SpellSchoolRegistry.CELESTIAL_SOURCE.get(), SpellAttributesRegistry.CELESTIAL_SOURCE_SPELL_PENETRATION}
        }) {
            if (spellDamageSource.spell().getSchoolType() == data[0]) {
                double penetration = livingAttacker.getAttributeValue(((RegistryObject<Attribute>) data[1]).get());
                if (penetration > 0) {
                    event.setAmount((float) (event.getAmount() * penetration));
                }
                break;
            }
        }
    }
}