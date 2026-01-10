package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import com.baizeli.eternisstarrysky.spell.SpellUtils;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
                .setCooldownSeconds(0)
                .build();
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 150;
        this.spellPowerPerLevel = 10;
        this.castTime = 0;
        this.baseManaCost = 15;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.cooldown", Utils.timeFromTicks(getCooldownInTicks(spellLevel, CastSource.COMMAND, caster), 1)),
                Component.translatable("ui.iron_spells_genesis.health_cost_percent", 90, 1),
                Component.translatable("ui.iron_spells_genesis.health_conversion_efficiency", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1))
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

    @Override
    public int getSpellCooldown() {
        return 0;// 阻止原有冷却
    }

    private double getConvertPercent(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) / 100;
    }

    private int getCooldownInTicks(int spellLevel, CastSource castSource, LivingEntity caster) {
        int coolDown;
        double playerCooldownModifier = 1.0D;

        if (caster != null) {
            playerCooldownModifier = caster.getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION.get());
        }
        float itemCoolDownModifer = 1.0F;
        if (castSource == CastSource.SWORD) {
            itemCoolDownModifer = ServerConfigs.SWORDS_CD_MULTIPLIER.get().floatValue();
        }
        coolDown = spellLevel * 50;
        return (int) (coolDown * ((double) 2.0F - Utils.softCapFormula(playerCooldownModifier)) * itemCoolDownModifer);
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
        float damage = entity.getHealth() * 0.9f;
        entity.setHealth(entity.getHealth() - damage);
        entity.setAbsorptionAmount((float) (entity.getAbsorptionAmount() + damage * getConvertPercent(spellLevel, entity)));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
