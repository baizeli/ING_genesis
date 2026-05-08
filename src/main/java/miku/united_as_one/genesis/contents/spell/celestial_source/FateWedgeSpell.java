package miku.united_as_one.genesis.contents.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.api.spell.celestial_source.FateWedgeUtil;
import miku.united_as_one.genesis.registries.EffectRegistry;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;

@AutoSpellConfig
public class FateWedgeSpell extends CelestialSourceBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "fate_wedge");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SpellSchoolRegistry.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(240)
        .build();

    public FateWedgeSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 60;
        this.spellPowerPerLevel = 0;
        this.castTime = 20;
        this.baseManaCost = 170;
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
            Component.translatable("ui.iron_spells_genesis.max_damage_bonus", 50)
        );
    }

    // 持续时间
    private int getDuration(int spellLevel, LivingEntity caster) {
        return 180 * 20 + (int) ((getSpellPower(spellLevel, caster) - 1.0f) * 40);
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
                    FateWedgeUtil.setInitialHealth(player, target);
                    int duration = getDuration(spellLevel, entity);

                    // 刻命之楔[施法者本身]
                    player.addEffect(new MobEffectInstance(
                        EffectRegistry.FATE_WEDGE.get(),
                        duration, 
                        0, 
                        false, 
                        false, 
                        true
                    ));
                    
                    // 发光[目标]
                    target.addEffect(new MobEffectInstance(
                        MobEffects.GLOWING, 
                        duration, 
                        0, 
                        false, 
                        false, 
                        true
                    ));
                }
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}