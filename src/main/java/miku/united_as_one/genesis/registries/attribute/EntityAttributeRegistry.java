package miku.united_as_one.genesis.registries.attribute;

import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import miku.united_as_one.genesis.contents.entity.WardenSpellcaster;
import miku.united_as_one.genesis.contents.entity.boss.BloodBoss;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.BoxEntity;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.blade_works.MagicCircle;
import miku.united_as_one.genesis.contents.entity.spell.celestial_source.blade_works.SwordEntity;
import miku.united_as_one.genesis.contents.entity.spell.eldritch.SummonedWardenEntity;
import miku.united_as_one.genesis.contents.entity.spell.fire.SummonedKeeperEntity;
import miku.united_as_one.genesis.contents.entity.test.BaiZeLiEntity;
import miku.united_as_one.genesis.registries.entity.EntityRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class EntityAttributeRegistry {
    private EntityAttributeRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(EntityAttributeRegistry::onAttributeCreate);
    }

    private static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.WARDEN_SPELLCASTER.get(), WardenSpellcaster.createAttributes().build());
        event.put(EntityRegistry.MAGIC_CIRCLE.get(), MagicCircle.createAttributes().build());
        event.put(EntityRegistry.BOX_ENTITY.get(), BoxEntity.createAttributes().build());
        event.put(EntityRegistry.SWORD_ENTITY.get(), SwordEntity.createAttributes().build());
        event.put(EntityRegistry.BLOOD_BOSS.get(), BloodBoss.setAttributes().build());
        event.put(EntityRegistry.BLOOD_TENTACLE.get(), VoidTentacle.createLivingAttributes().build());
        event.put(EntityRegistry.SUMMONED_KEEPER.get(), SummonedKeeperEntity.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_WARDEN.get(), SummonedWardenEntity.createAttributes().build());
        event.put(EntityRegistry.BAI_ZE_LI.get(), BaiZeLiEntity.createAttributes().build());
    }
}
