package com.baizeli.eternisstarrysky.spell.chaos;

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
public class BloodWarSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "blood_war");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(240.0F)
        .build();

    public BloodWarSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 60;
        this.spellPowerPerLevel = 60;
        this.castTime = 20;
        this.baseManaCost = 150;
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
        int duration = getBuffDuration(spellLevel);
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.effect_length", 
                Utils.timeFromTicks(duration, 1)
            )/* ,
            Component.translatable(
                "ui.iron_spells_genesis.spell_power", 
                Utils.stringTruncation(BloodWarEvent.SPELL_POWER_BONUS_PER_THRESHOLD * 100, 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.damage", 
                Utils.stringTruncation(BloodWarEvent.DAMAGE_BONUS_PER_THRESHOLD * 100, 1)
            ),
            Component.translatable(
                "ui.iron_spells_genesis.movement_speed", 
                Utils.stringTruncation(BloodWarEvent.SPEED_BONUS_PER_THRESHOLD * 100, 1)
            ) */
        );
    }

    private int getBuffDuration(int spellLevel) {
        return (int) ((60 * spellLevel) * 20);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            int duration = getBuffDuration(spellLevel);

            player.addEffect(new MobEffectInstance(
                ModEffect.BLOOD_WAR.get(),
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