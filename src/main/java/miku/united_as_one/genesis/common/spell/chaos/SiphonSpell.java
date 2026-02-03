package miku.united_as_one.genesis.common.common.spell.chaos;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.registry.EffectRegistry;
import miku.united_as_one.genesis.init.registry.spell.SpellSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class SiphonSpell extends ChaosBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "siphon");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchoolRegistry.CHAOS_RESOURCE)
        .setMaxLevel(5)
        .setCooldownSeconds(180.0F)
        .build();

    public SiphonSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 6;
        this.castTime = 20;
        this.baseManaCost = 50;
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
                "ui.iron_spells_genesis.absorption_healing", 
                Utils.stringTruncation(getHealPercent(spellLevel, caster), 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.duration",
                Utils.timeFromTicks(getDuration(spellLevel), 1)
            )
        );
    }

    private float getHealPercent(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    private int getDuration(int spellLevel) {
        return 200 + (spellLevel - 1) * 200;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(
                EffectRegistry.SIPHON.get(),
                getDuration(spellLevel),
                spellLevel - 1,
                false,
                false,
                true
            ));
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}