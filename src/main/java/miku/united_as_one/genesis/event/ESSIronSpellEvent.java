package miku.united_as_one.genesis.event;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.items.curios.EternalRing;
import miku.united_as_one.genesis.items.curios.rune_plus.BloodRunePlus;
import miku.united_as_one.genesis.items.curios.rune_plus.NatureRunePlus;
import miku.united_as_one.genesis.util.ModCurios;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Genesis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ESSIronSpellEvent {

    public static final Map<UUID, Integer> bloodStepUsedMap = new HashMap<>();

    @SubscribeEvent
    public static void spellOnCast(SpellOnCastEvent event) {
        Player entity = event.getEntity();
        if(ModCurios.hasCurios(entity, EternalRing::test)) {
            event.setSpellLevel(event.getSpellLevel() + 1);
        }

        if(ModCurios.hasCurios(entity, NatureRunePlus::test)) {
            if(SpellRegistry.ACID_ORB_SPELL.get().getSpellId().equals(event.getSpellId())) {
                event.setManaCost(Mth.ceil((float) event.getManaCost() / 2));
            }
        }

        if(ModCurios.hasCurios(entity, BloodRunePlus::test)) {
            if(SpellRegistry.BLOOD_STEP_SPELL.get().getSpellId().equals(event.getSpellId())) {
                bloodStepUsedMap.put(entity.getUUID(), event.getSpellLevel());
            }
        }
    }
}
