package miku.united_as_one.genesis.spell.celestial_source;

import miku.united_as_one.genesis.Entity.CustomArrowEntity;
import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.*;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class MyriadArrowsSpell extends CelestialSourceBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "myriad_arrows");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchool.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(180.0F)
        .build();

    public MyriadArrowsSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
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
                "ui.irons_spellbooks.damage", 
                Utils.stringTruncation(getDamage(spellLevel, caster), 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.projectile_count", 
                getArrowCount(spellLevel, caster)
            ),
            Component.translatable(
                "ui.irons_spellbooks.duration", 
                Utils.timeFromTicks(getDuration(spellLevel, caster), 1)
            )
        );
    }

    // 箭矢发射的数量
    private int getArrowCount(int spellLevel, LivingEntity caster) {
        int baseCount = 300 + (spellLevel - 1) * 50;
        if (caster == null) {
            return baseCount;
        }
        
        float spellPower = getSpellPower(spellLevel, caster);
        int additionalCount = (int) (spellPower - 1.0f);
        return baseCount + additionalCount;
    }

    // 每支箭矢造成的伤害
    private float getDamage(int spellLevel, LivingEntity caster) {
        if (caster == null) {
            return 16.0f;
        }
        
        float spellPower = getSpellPower(spellLevel, caster);
        return 16.0f + (spellPower - 1.0f) * 0.1f;
    }

    // 持续发射持续的时间
    private int getDuration(int spellLevel, LivingEntity caster) {
        int baseDuration = 5 * 20;
        if (caster == null) {
            return baseDuration;
        }
        
        float spellPower = getSpellPower(spellLevel, caster);
        int reduction = (int) ((spellPower - 1.0f) * 100);
        return Math.max(20, baseDuration - reduction);
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.ARROW_VOLLEY_PREPARE.get());
    }

    /*@Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.EVOKER_CAST_SPELL);
    }*/

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 16, 0.1f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            LivingEntity target = null;

            if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
                target = castTargetingData.getTarget(serverLevel);
            }

            if (target != null) {
                CustomArrowEntity arrowsEntity = new CustomArrowEntity(
                    serverLevel, 
                    target.getX(), 
                    target.getY(), 
                    target.getZ()
                );

                arrowsEntity.setMyriadArrows(
                    true,
                    target, 
                    getArrowCount(spellLevel, entity),
                    getDuration(spellLevel, entity),
                    getDamage(spellLevel, entity)
                );
                
                arrowsEntity.setOwner(entity);

                serverLevel.addFreshEntity(arrowsEntity);
            }
        }
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}