package miku.united_as_one.genesis.contents.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.effect.EffectRegistry;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class StellarSoulControlSpell extends CelestialSourceBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "stellar_soul_control");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchoolRegistry.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(90)
        .build();

    public StellarSoulControlSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 60;
        this.spellPowerPerLevel = 5;
        this.castTime = 20;
        this.baseManaCost = 200;
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
                Utils.timeFromTicks(getBuffDuration(spellLevel, caster), 1)
            )
        );
    }

    // 持续时间
    private int getBuffDuration(int spellLevel, LivingEntity caster) {
        int[] durations = {15, 20, 25};
        
        return durations[Math.min(spellLevel - 1, durations.length - 1)] * 20 + 
            (int) ((getSpellPower(spellLevel, caster) - 1) * 20);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            int duration = getBuffDuration(spellLevel, entity);

            player.addEffect(new MobEffectInstance(
                EffectRegistry.STELLAR_SOUL_CONTROL.get(),
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