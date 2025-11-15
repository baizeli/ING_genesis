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
        .setCooldownSeconds(0)
        .build();

    public BloodRitualSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
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
            // 记录当前血量
            float currentHealth = entity.getHealth();
            
            // 将血量设置为1
            entity.setHealth(1.0f);
            
            // 计算扣除的血量
            float healthDiff = currentHealth - 1.0f;
            
            // 获得法力值 = 扣除的血量 * 10
            float manaToAdd = healthDiff * 10.0f;
            
            // 增加法力值
            playerMagicData.addMana(manaToAdd);
            
            // 清除所有buff
            entity.removeAllEffects();
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}