package miku.united_as_one.genesis.contents.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.effect.EffectRegistry;
import miku.united_as_one.genesis.handlers.spell.celestial_source.UnparalleledEvent;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
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
public class UnparalleledSpell extends CelestialSourceBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "unparalleled");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchoolRegistry.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(600)
        .build();

    public UnparalleledSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 200;
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
            ),
            Component.translatable(
                "ui.genesis_magic.spell_power", 
                Utils.stringTruncation(UnparalleledEvent.SPELL_POWER_BONUS * 100, 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.damage", 
                Utils.stringTruncation(UnparalleledEvent.DAMAGE_BONUS * 100, 1)
            ),
            Component.translatable(
                "ui.genesis_magic.movement_speed", 
                Utils.stringTruncation(UnparalleledEvent.SPEED_BONUS * 100, 1)
            )
        );
    }

    // 持续时间
    private int getDuration(int spellLevel, LivingEntity caster) {
        return (60 + (spellLevel - 1) * 30) * 20 + (int) ((getSpellPower(spellLevel, caster) - 1) * 40);
    }

    @Override
    public int getCastTime(int spellLevel) {
        return 200;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance( 
                EffectRegistry.UNPARALLELED.get(),
                getDuration(spellLevel, entity), 
                0, 
                false, 
                false, 
                true
            ));
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}