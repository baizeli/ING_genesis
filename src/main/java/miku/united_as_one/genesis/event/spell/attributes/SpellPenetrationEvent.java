package miku.united_as_one.genesis.event.spell.attributes;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import miku.united_as_one.genesis.registry.spell.SpellAttributesRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SpellPenetrationEvent {

    @SubscribeEvent
    public static void onSpellDamage(SpellDamageEvent event) {
        var spellDamageSource = event.getSpellDamageSource();
        
        if (spellDamageSource.spell().getSchoolType() == SchoolRegistry.FIRE.get()) {
            if (spellDamageSource.getEntity() instanceof LivingEntity livingAttacker) {
                double flamePenetration = livingAttacker.getAttributeValue(SpellAttributesRegistry.FLAME_SPELL_PENETRATION.get());

                if (flamePenetration > 0) {
                    event.setAmount(event.getAmount() * (1 + (float) flamePenetration));
                }
            }
        }
    }
}