package com.baizeli.eternisstarrysky.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Util.spell.celestial_source.FateWedgeUtil;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class FateWedgeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "fate_wedge");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.EPIC)
        .setSchoolResource(SpellSchool.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(120.0F)
        .build();

    public FateWedgeSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 60;
        this.spellPowerPerLevel = 0;
        this.castTime = 20;
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
    public SchoolType getSchoolType() {
        return SpellSchool.CELESTIAL_SOURCE.get();
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable("ui.iron_spells_genesis.max_damage_bonus", 50),
            Component.translatable(
                "ui.irons_spellbooks.effect_length",
                Utils.timeFromTicks(getDurationInTicks(spellLevel, caster), 1)
            )
        );
    }

    public int getDurationInTicks(int spellLevel, LivingEntity caster) {
        return (int) (getSpellPower(spellLevel, caster) * 20);
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 16, 0.1f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
                LivingEntity target = targetData.getTarget((ServerLevel) level);

                if (target != null) {
                    int duration = getDurationInTicks(spellLevel, entity);
                    FateWedgeUtil.applyFateWedgeEffect(player, target, 0, duration);
                }
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}