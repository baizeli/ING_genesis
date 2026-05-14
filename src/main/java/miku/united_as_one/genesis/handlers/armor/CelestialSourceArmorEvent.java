package miku.united_as_one.genesis.handlers.armor;

import miku.united_as_one.genesis.api.equipment.ArmorSetUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CelestialSourceArmorEvent {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        if (!ArmorSetUtil.hasFullCelestialSourceSet(entity)) return;

        // 单次伤害不超过70%最大生命值
        float maxHealth = entity.getMaxHealth();
        float damage = event.getAmount();
        float maxDamage = maxHealth * 0.7f;
        
        if (damage > maxDamage) {
            event.setAmount(maxDamage);
        }
    }

    /*@SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();

        if (!ArmorSetUtil.hasFullCelestialSourceSet(entity)) return;

        // 免疫负面
        if (!event.getEffectInstance().getEffect().isBeneficial()) {
            event.setResult(Event.Result.DENY);
        }
    }*/
}