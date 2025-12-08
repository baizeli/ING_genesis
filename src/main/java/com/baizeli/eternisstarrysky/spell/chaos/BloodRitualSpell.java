package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class BloodRitualSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "blood_ritual");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(120.0F)
        .build();

    public BloodRitualSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 0;
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

    @Override
    public SchoolType getSchoolType() {
        return SpellSchool.CHAOS.get();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide) {
            float currentHealth = entity.getHealth();

            entity.setHealth(1.0f);

            float healthDiff = currentHealth - 1.0f;

            // 1混沌/通用法术强度=(+)1点法力值+0.5倍转化
            float spellPower = getSpellPower(spellLevel, entity);
            float conversionRate = 10.0f + (spellPower - 1.0f) * 0.5f;
            float manaToAdd = healthDiff * conversionRate;

            playerMagicData.addMana(manaToAdd);

            entity.removeAllEffects();
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}