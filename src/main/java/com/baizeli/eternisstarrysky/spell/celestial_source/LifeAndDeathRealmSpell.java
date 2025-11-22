package com.baizeli.eternisstarrysky.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class LifeAndDeathRealmSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "life_and_death_realm");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchool.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(600.0F)
        .build();

    public LifeAndDeathRealmSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 5;
        this.castTime = 20;
        this.baseManaCost = 100;
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
        return SpellSchool.CELESTIAL_SOURCE.get();
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        int duration = getBuffDuration(spellLevel);
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.effect_length", 
                Utils.timeFromTicks(duration, 1)
            )
        );
    }

    private int getBuffDuration(int spellLevel) {
        return 100 + (spellLevel - 1) * 100;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            int duration = getBuffDuration(spellLevel);
            
            player.addEffect(new MobEffectInstance(
                ModEffect.LIFE_AND_DEATH_REALM.get(),
                duration,
                0,
                false,
                false,
                true
            ));
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}