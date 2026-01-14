package com.baizeli.eternisstarrysky.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class GlazedFlowerRainSpell extends CelestialSourceBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "glazed_flower_rain");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SpellSchool.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(600.0F)
        .build();

    public GlazedFlowerRainSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 200;
        this.baseManaCost = 1000;
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
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.effect_length", 
                Utils.timeFromTicks(getDurationTicks(spellLevel, caster), 1)
            ),
            Component.translatable("ui.irons_spellbooks.radius", 15)
        );
    }

    // 持续时间
    private int getDurationTicks(int spellLevel, LivingEntity caster) {
        int baseDuration = (180 + (spellLevel - 1) * 60) * 20;
        if (caster == null) {
            return baseDuration;
        }
        
        float spellPower = getSpellPower(spellLevel, caster);
        int additionalDuration = (int) ((spellPower - 1.0f) * 40);
        return baseDuration + additionalDuration;
    }

    @Override
    public int getCastTime(int spellLevel) {
        return Math.max(20, 200 - (spellLevel - 1) * 20);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(
                ModEffect.GLAZED_FLOWER_RAIN.get(),
                getDurationTicks(spellLevel, entity),
                spellLevel - 1,
                false,
                true,
                true
            ));
        }
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}