package miku.united_as_one.genesis.contents.effect.spell.chaos;

import miku.bai_ze_li.genesis.api.nbt.GenesisPersistentData;
import miku.bai_ze_li.genesis.api.nbt.PersistentDataKey;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID)
public class WarpedBarrierEffect extends MobEffect {
    public static final PersistentDataKey SHIELD_AMOUNT = PersistentDataKey.of(Genesis.MOD_ID, "shield_amount");

    public WarpedBarrierEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }

    @Override
    public String getDescriptionId() {
        return "effect." + Genesis.MOD_ID + ".warped_barrier";
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        if (GenesisPersistentData.contains(livingEntity, SHIELD_AMOUNT, CompoundTag.TAG_FLOAT)) {
            livingEntity.setAbsorptionAmount(livingEntity.getAbsorptionAmount()
                    - GenesisPersistentData.getFloat(livingEntity, SHIELD_AMOUNT, 0.0F));
            GenesisPersistentData.remove(livingEntity, SHIELD_AMOUNT);
        }
        super.removeAttributeModifiers(livingEntity, attributeMap, amplifier);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
