package miku.united_as_one.genesis.common.spell.celestial_source;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class IFlySpell extends CelestialSourceBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "i_fly");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchoolRegistry.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(360.0F)
        .build();

    public IFlySpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 180;
        this.spellPowerPerLevel = 120;
        this.castTime = 100;
        this.baseManaCost = 900;
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
                Utils.timeFromTicks(getDuration(spellLevel, caster), 1)
            )
        );
    }

    // 持续时间
    private int getDuration(int spellLevel, LivingEntity caster) {
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
        return Math.max(20, 100 - (spellLevel - 1) * 20);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(
                EffectRegistry.I_FLY.get(),
                getDuration(spellLevel, entity), 
                0, 
                false, 
                false, 
                true
            ));
        } 
    }
}