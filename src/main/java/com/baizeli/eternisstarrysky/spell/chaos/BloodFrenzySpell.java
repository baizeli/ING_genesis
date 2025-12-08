package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class BloodFrenzySpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "blood_frenzy");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(420.0F)
        .build();

    public BloodFrenzySpell() {
        this.manaCostPerLevel = 100;
        this.baseManaCost = 500;
        this.castTime = 100;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
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
        return CastType.LONG;
    }

    @Override
    public SchoolType getSchoolType() {
        return SpellSchool.CHAOS.get();
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.iron_spells_genesis.damage_multiplier", 
                Utils.stringTruncation(getDamageMultiplier(spellLevel, caster), 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.duration",
                Utils.timeFromTicks(getDuration(spellLevel, caster), 1)
            )
        );
    }

    // 伤害倍数
    private float getDamageMultiplier(int spellLevel, LivingEntity caster) {
        float spellPower = getSpellPower(spellLevel, caster);
        return 200.0f + (spellPower - 1.0f) * 1.0f;
    }

    // 持续时间
    private int getDuration(int spellLevel, LivingEntity caster) {
        int baseDuration = 600 * spellLevel;
        float spellPower = getSpellPower(spellLevel, caster);
        int additionalDuration = (int) ((spellPower - 1.0f) * 10.0f);
        return baseDuration + additionalDuration;
    }

    @Override
    public int getCastTime(int spellLevel) {
        return Math.max(20, 100 - (spellLevel - 1) * 20);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(
                ModEffect.BLOOD_FRENZY.get(),
                getDuration(spellLevel, entity),
                spellLevel - 1,
                false,
                false,
                true
            ));
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}