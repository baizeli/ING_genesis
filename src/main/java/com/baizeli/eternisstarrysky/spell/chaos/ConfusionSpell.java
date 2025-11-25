package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

@AutoSpellConfig
public class ConfusionSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "confusion");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(60.0F)
        .build();

    public ConfusionSpell() {
        this.manaCostPerLevel = 0;
        this.baseManaCost = 1000;
        this.castTime = 140;
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
        return SpellSchool.CHAOS.get();
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.duration",
                Utils.timeFromTicks(getDuration(spellLevel), 1)
            )
        );
    }

    private int getDuration(int spellLevel) {
        return 600;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && level instanceof ServerLevel) {
            AABB boundingBox = entity.getBoundingBox().inflate(7);
            
            level.getEntitiesOfClass(LivingEntity.class, boundingBox).forEach((target) -> {
                if (!(target instanceof Player) && entity.distanceTo(target) <= 7) {
                    target.addEffect(new MobEffectInstance(
                        ModEffect.CONFUSION.get(),
                        getDuration(spellLevel),
                        0,
                        false,
                        false,
                        true
                    ));
                }
            });
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}