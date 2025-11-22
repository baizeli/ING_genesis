package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

@AutoSpellConfig
public class DeadStarDecreeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "dead_star_decree");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(240.0F)
        .build();

    public DeadStarDecreeSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 160;
        this.spellPowerPerLevel = 40;
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
                "ui.irons_spellbooks.damage", 
                Utils.stringTruncation(getDamage(spellLevel, caster), 2)
            ),
            Component.translatable(
                "ui.irons_spellbooks.radius", 
                Utils.stringTruncation(getRadius(spellLevel, caster), 1)
            )
        );
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    private float getRadius(int spellLevel, LivingEntity caster) {
        return 10 + (spellLevel - 1) * 5;
    }

    @Override
    public int getCastTime(int spellLevel) {
        return 200 - (spellLevel - 1) * 20;
    }

    @Override
    public int getManaCost(int spellLevel) {
        return 1000 + (spellLevel - 1) * 100;
    }
}