package com.baizeli.eternisstarrysky.event;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Items.curios.EternalRing;
import com.baizeli.eternisstarrysky.Items.curios.rune_plus.BloodRunePlus;
import com.baizeli.eternisstarrysky.util.ModCurios;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import io.redspace.ironsspellbooks.entity.spells.devour_jaw.DevourJaw;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import io.redspace.ironsspellbooks.spells.blood.DevourSpell;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ESSLivingEvent {
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
}
