package miku.united_as_one.genesis.contents.items.curios;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
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

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        clearElementalState(slotContext.entity());
    }

    public static boolean test(ItemStack stack) {
        return stack.getItem() instanceof EternalRing;
    }

    public static boolean immuneEffect(MobEffectInstance effectInstance) {
        MobEffect effect = effectInstance.getEffect();
        return immuneEffects.contains(effect)
                || (effect.getCategory() == MobEffectCategory.HARMFUL && effect != MobEffects.BAD_OMEN);
    }

    public static void clearElementalState(LivingEntity entity) {
        if(entity.getTicksFrozen() > 0) entity.setTicksFrozen(0);
        if(entity.isOnFire()) entity.clearFire();
    }
}
