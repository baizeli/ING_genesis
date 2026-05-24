package miku.united_as_one.genesis.handlers.spell.attributes;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.api.mixin.DamageSourceInterface;
import miku.united_as_one.genesis.api.mixin.LivingEventEC;
import miku.united_as_one.genesis.data.damage.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpellDamageBypassEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        applyAttackOrHurtFlags(event.getSource(), (LivingEventEC) event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        applyAttackOrHurtFlags(event.getSource(), (LivingEventEC) event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!isCelestialSource(event.getSource())) {
            return;
        }

        LivingEventEC ec = (LivingEventEC) event;
        ec.ironSpellGenesis$hackedUnCancelable(true);
        ec.ironSpellGenesis$hackedOnlyAmountUp(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeathHighest(LivingDeathEvent event) {
        applyCelestialDeathFlags(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDeathLowest(LivingDeathEvent event) {
        applyCelestialDeathFlags(event);
    }

    private static void applyAttackOrHurtFlags(DamageSource source, LivingEventEC event) {
        if (isCelestialSource(source)) {
            ((DamageSourceInterface) source).ironSpellGenesis$setBypassAll(true);
            event.ironSpellGenesis$hackedUnCancelable(true);
            event.ironSpellGenesis$hackedOnlyAmountUp(true);
        } else if (isChaos(source)) {
            ((DamageSourceInterface) source).ironSpellGenesis$setBypassArmor(true);
        }
    }

    private static void applyCelestialDeathFlags(LivingDeathEvent event) {
        if (!isCelestialSource(event.getSource())) {
            return;
        }

        LivingEventEC ec = (LivingEventEC) event;
        ec.ironSpellGenesis$hackedUnCancelable(true);
        event.getEntity().setHealth(0F);
    }

    private static boolean isCelestialSource(DamageSource source) {
        return source.is(DamageTypes.CELESTIAL_SOURCE_MAGIC);
    }

    private static boolean isChaos(DamageSource source) {
        return source.is(DamageTypes.CHAOS_MAGIC);
    }
}
