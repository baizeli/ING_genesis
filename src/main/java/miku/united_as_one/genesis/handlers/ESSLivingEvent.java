package miku.united_as_one.genesis.handlers;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.renderer.entity.spell.chaos.WireBoxRenderer;
import miku.united_as_one.genesis.contents.entity.gungnir.GungnirDaggerEntity;
import miku.united_as_one.genesis.contents.items.GoodCake;
import miku.united_as_one.genesis.contents.items.curios.EternalRing;
import miku.united_as_one.genesis.contents.items.curios.rune_plus.BloodRunePlus;
import miku.united_as_one.genesis.contents.items.weapon.sword.Gungnir;
import miku.united_as_one.genesis.registries.item.ItemRegistry;
import miku.united_as_one.genesis.registries.spell.SpellAttributesRegistry;
import miku.united_as_one.genesis.api.curios.ModCurios;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import io.redspace.ironsspellbooks.entity.spells.devour_jaw.DevourJaw;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.spells.blood.DevourSpell;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Genesis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ESSLivingEvent {
    private static final UUID UUID = Mth.createInsecureUUID(RandomSource.createNewThreadLocalInstance());

    @SubscribeEvent
    public static void immuneDamage(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        Entity attacker = source.getEntity();
        float damageAmount = event.getAmount();
        if(ModCurios.hasCurios(target, EternalRing::test)) {
            if(source.is(DamageTypeTags.IS_LIGHTNING)
                || source.is(DamageTypeTags.IS_FREEZING)
                || source.is(DamageTypeTags.IS_FIRE)){
                event.setCanceled(true);
            }
        }
        if(attacker instanceof ServerPlayer player) {
            if(ESSIronSpellEvent.bloodStepUsedMap.containsKey(player.getUUID())) {
                Integer spellLevel = ESSIronSpellEvent.bloodStepUsedMap.get(player.getUUID());
                DevourJaw devourJaw = new DevourJaw(player.level, player, target);
                devourJaw.vigorLevel = new DevourSpell().getHpBonus(spellLevel, player);
                devourJaw.setPos(target.position());
                devourJaw.setYRot(target.getYRot());
                devourJaw.setDamage(damageAmount * 0.5f);
                player.level.addFreshEntity(devourJaw);
                ESSIronSpellEvent.bloodStepUsedMap.remove(player.getUUID());
            }
            if(ModCurios.hasCurios(player, BloodRunePlus::test)) {
                if(source.getDirectEntity() instanceof BloodSlashProjectile) {
                    MagicData data = MagicData.getPlayerMagicData(player);
                    AttributeInstance attribute = player.getAttribute(AttributeRegistry.MAX_MANA.get());
                    if(attribute != null) {
                        float maxMana = (float) attribute.getValue();
                        data.addMana(damageAmount);
                        if(data.getMana() > maxMana) {
                            data.setMana(maxMana);
                        }
                        PacketDistributor.sendToPlayer(player, new SyncManaPacket(data));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEffectAdd(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if(ModCurios.hasCurios(entity, EternalRing::test)) {
            if(EternalRing.immuneEffect(event.getEffectInstance())) {
                entity.removeEffect(event.getEffectInstance().getEffect());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if (WireBoxRenderer.entitiesForRenderWireBoxRenderer.containsKey(event.getEntity().getUUID())) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();
        if (!living.level.isClientSide()) {
            AttributeInstance maxMana = living.getAttribute(AttributeRegistry.MAX_MANA.get());
            if (maxMana != null) {
                double percent = living.getAttributeValue(SpellAttributesRegistry.MAX_MANA_PERCENT.get());
                AttributeModifier mod = new AttributeModifier(UUID, Genesis.MOD_ID + ":max_mana_percent",
                        percent - 1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
                maxMana.removeModifier(mod);
                maxMana.addTransientModifier(mod);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void modWeaponHurtEvent1(LivingHurtEvent event) {
        if (event.getSource().directEntity instanceof GungnirDaggerEntity) {
            ItemRegistry.GUNGNIR.get().onHurt(null, event);
        } else if (event.getSource().getEntity() instanceof LivingEntity living) {
            if (living.getMainHandItem().getItem() instanceof Gungnir gungnir)
                gungnir.onHurt(living.getMainHandItem(), event);
            else if (living.getMainHandItem().getItem() instanceof GoodCake goodCake)
                goodCake.onHurt(living.getMainHandItem(), event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void modWeaponHurtEvent2(LivingAttackEvent event) {
        if (event.getSource().directEntity instanceof GungnirDaggerEntity) {
            ItemRegistry.GUNGNIR.get().onAttack(null, event);
        } else if (event.getSource().getEntity() instanceof LivingEntity living) {
            if (living.getMainHandItem().getItem() instanceof Gungnir gungnir)
                gungnir.onAttack(living.getMainHandItem(), event);
            else if (living.getMainHandItem().getItem() instanceof GoodCake goodCake)
                goodCake.onAttack(living.getMainHandItem(), event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void modWeaponHurtEvent3(LivingDamageEvent event) {
        if (event.getSource().directEntity instanceof GungnirDaggerEntity) {
            ItemRegistry.GUNGNIR.get().onDamage(null, event);
        } else if (event.getSource().getEntity() instanceof LivingEntity living) {
            if (living.getMainHandItem().getItem() instanceof Gungnir gungnir)
                gungnir.onDamage(living.getMainHandItem(), event);
            else if (living.getMainHandItem().getItem() instanceof GoodCake goodCake)
                goodCake.onDamage(living.getMainHandItem(), event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void modWeaponHurtEvent5(LivingDeathEvent event) {
        if (event.getSource().directEntity instanceof GungnirDaggerEntity) {
            ItemRegistry.GUNGNIR.get().onDeath(null, event,  EventPriority.HIGHEST);
        } else if (event.getSource().getEntity() instanceof LivingEntity living) {
            if (living.getMainHandItem().getItem() instanceof Gungnir gungnir)
                gungnir.onDeath(living.getMainHandItem(), event, EventPriority.HIGHEST);
            else if (living.getMainHandItem().getItem() instanceof GoodCake goodCake)
                goodCake.onDeath(living.getMainHandItem(), event, EventPriority.HIGHEST);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void modWeaponHurtEvent(LivingDeathEvent event) {
        if (event.getSource().directEntity instanceof GungnirDaggerEntity) {
            ItemRegistry.GUNGNIR.get().onDeath(null, event,  EventPriority.LOWEST);
        } else if (event.getSource().getEntity() instanceof LivingEntity living) {
            if (living.getMainHandItem().getItem() instanceof Gungnir gungnir)
                gungnir.onDeath(living.getMainHandItem(), event, EventPriority.LOWEST);
            else if (living.getMainHandItem().getItem() instanceof GoodCake goodCake)
                goodCake.onDeath(living.getMainHandItem(), event, EventPriority.LOWEST);
        }
    }
}
