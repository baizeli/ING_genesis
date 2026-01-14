package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import com.baizeli.eternisstarrysky.event.spell.chaos.BloodWarEvent;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import com.baizeli.eternisstarrysky.spell.SpellUtils;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class BloodWarSpell extends ChaosBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "blood_war");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(0F)
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
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        int duration = getBuffDuration(spellLevel);
        return List.of(
            Component.translatable("ui.irons_spellbooks.cooldown",
                Utils.timeFromTicks(getCooldownInTicks(spellLevel, CastSource.COMMAND, caster), 1)
            ),
            Component.translatable("ui.irons_spellbooks.effect_length",
                Utils.timeFromTicks(duration, 1)
            ) ,
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
            )
        );
    }

    private int getCooldownInTicks(int spellLevel, CastSource castSource, LivingEntity caster) {
        int coolDown = 2400 + (spellLevel - 1) * 1200;

        double playerCooldownModifier = 1.0D;
        float itemCoolDownModifer = 1.0F;

        if (caster != null) {
            playerCooldownModifier = caster.getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION.get());
        }

        if (castSource == CastSource.SWORD) {
            itemCoolDownModifer = ServerConfigs.SWORDS_CD_MULTIPLIER.get().floatValue();
        }

        return (int) (coolDown * ((double) 2.0F - Utils.softCapFormula(playerCooldownModifier)) * itemCoolDownModifer);
    }

    private int getBuffDuration(int spellLevel) {
        return (60 * spellLevel) * 20;
    }

    @Override
    public void castSpell(Level world, int spellLevel, ServerPlayer serverPlayer, CastSource castSource, boolean triggerCooldown) {
        super.castSpell(world, spellLevel, serverPlayer, castSource, triggerCooldown);
        if (!MagicData.getPlayerMagicData(serverPlayer).getPlayerRecasts().hasRecastForSpell(this.getSpellId()) && triggerCooldown && (!serverPlayer.isCreative() || ServerConfigs.CREATIVE_COOLDOWN.get())) {
            SpellUtils.addCooldown(serverPlayer, this, castSource, getCooldownInTicks(spellLevel, castSource, serverPlayer));
        }
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