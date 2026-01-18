package com.baizeli.eternisstarrysky.Items.curios;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Set;

public class EternalRing extends ESSCurioItem {
    private static final Set<MobEffect> immuneEffects = Set.of(
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.HARM,
            MobEffects.CONFUSION,
            MobEffects.BLINDNESS,
            MobEffects.HUNGER,
            MobEffects.WEAKNESS,
            MobEffects.POISON,
            MobEffects.WITHER,
            MobEffects.LEVITATION,
            MobEffects.UNLUCK,
            MobEffects.DARKNESS
    );

//    private static final Set<MobEffect> test = new HashSet<>();
//    static {
//        Class<MobEffects> effectClass = MobEffects.class;
//        Field[] fields = effectClass.getDeclaredFields();
//
//        for (Field field : fields) {
//            if(field.getType() == MobEffect.class) {
//                MobEffect mobEffect;
//                try {
//                    field.setAccessible(true);
//                    mobEffect = (MobEffect) field.get(null);
//                } catch (IllegalAccessException ignored) {continue;}
//                if(mobEffect.getCategory() == MobEffectCategory.HARMFUL) {
//                    if(mobEffect != MobEffects.BAD_OMEN) {
//                        test.add(mobEffect);
//                    }
//                }
//            }
//        }
//        List<String> list = Arrays.stream(fields).filter(field1 -> {
//            try {
//                return test.contains((MobEffect) field1.get(null));
//            } catch (IllegalAccessException e) {
//                return false;
//            }
//        }).map(Field::getName).toList();
//        System.out.println("immuneEffectList : " + list);
//    }

    public EternalRing() {
        attributeModifiers.put(AttributeRegistry.CAST_TIME_REDUCTION.get(), new AttributeModifier(
                "Eternal Ring Cast Time Reduction", 0.2, AttributeModifier.Operation.MULTIPLY_BASE
        ));
        attributeModifiers.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(
                "Eternal Ring Max Mana", 200, AttributeModifier.Operation.ADDITION
        ));
        attributeModifiers.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                "Eternal Ring Spell Power", 0.15, AttributeModifier.Operation.MULTIPLY_BASE
        ));
        attributeModifiers.put(Attributes.ARMOR, new AttributeModifier(
                "Eternal Ring Armor", 6, AttributeModifier.Operation.ADDITION
        ));
        attributeModifiers.put(Attributes.LUCK, new AttributeModifier(
                "Eternal Ring Luck", 4, AttributeModifier.Operation.ADDITION
        ));
        attributeModifiers.put(AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(
                "Eternal Ring Cooldown Reduction", 0.2, AttributeModifier.Operation.MULTIPLY_BASE
        ));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if(entity.getTicksFrozen() > 0) entity.setTicksFrozen(0);
        if(entity.isOnFire()) entity.clearFire();
    }

    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof EternalRing;
    }

    public static boolean immuneEffect(MobEffectInstance effectInstance) {
        return immuneEffects.contains(effectInstance.getEffect());
    }
}
