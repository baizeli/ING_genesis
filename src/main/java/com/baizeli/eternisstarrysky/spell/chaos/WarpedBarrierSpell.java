package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import com.baizeli.eternisstarrysky.spell.SpellUtils;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.config.IronConfigParameters;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class WarpedBarrierSpell extends ChaosBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "warped_barrier");
    private final DefaultConfig defaultConfig;

    public WarpedBarrierSpell() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.COMMON)
                .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
                .setMaxLevel(3)
                .setCooldownSeconds(120F)
                .build();
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 150;
        this.spellPowerPerLevel = 10;
        this.castTime = 0;
        this.baseManaCost = 150;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.iron_spells_genesis.health_cost_percent", 90, 1),
                Component.translatable("ui.iron_spells_genesis.health_conversion_efficiency", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getEffectDuration(spellLevel, caster), 1), 1)
        );
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    private double getConvertPercent(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) / 100;
    }

    private long getEffectDuration(int spellLevel, LivingEntity caster) {
        int baseDuration = 40 + (spellLevel - 1) * 10;

        double entitySpellPowerModifier = 1.0D;
        double entitySchoolPowerModifier= 1.0D;
        double configPowerModifier = 1.0D;

        if (caster != null) {
            entitySpellPowerModifier = caster.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
            entitySchoolPowerModifier = this.getSchoolType().getPowerFor(caster);
            configPowerModifier = SpellConfigManager.getSpellConfigValue(this, IronConfigParameters.POWER_MULTIPLIER).floatValue();
        }
        return (baseDuration + Math.round((entitySpellPowerModifier + entitySchoolPowerModifier + configPowerModifier - 3) * 100)) * 20;
    }


    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float damage = entity.getHealth() * 0.9f;
        entity.setHealth(entity.getHealth() - damage);

        // 先移除之前的效果，防止nbt在entity.addEffect里被移除导致无法正常扣除伤害吸收血量
        if (entity.getEffect(ModEffect.WARPED_BARRIER.get()) != null) {
            entity.removeEffect(ModEffect.WARPED_BARRIER.get());
        }

        float convertedShield = (float) (damage * getConvertPercent(spellLevel, entity));
        entity.getPersistentData().putFloat(EternisStarrySky.MOD_ID + ":shield_amount", convertedShield);

        entity.addEffect(new MobEffectInstance(
                ModEffect.WARPED_BARRIER.get(),
                (int)getEffectDuration(spellLevel, entity),
                0,
                false,
                false,
                false
        ));
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + convertedShield);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
