package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.List;

@AutoSpellConfig
public class AmenofuwariSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "amenofuwari");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(12.0F)
        .build();

    public AmenofuwariSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 25;
        this.spellPowerPerLevel = 25;
        this.castTime = 40;
        this.baseManaCost = 40;
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
                "ui.irons_spellbooks.distance", Utils.stringTruncation(getDistance(spellLevel), 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.cooldown", Utils.timeFromTicks(getCooldownInTicks(spellLevel), 1)
            )
        );
    }

    private float getDistance(int spellLevel) {
        switch (spellLevel) {
            case 1: return 25.0f;
            case 2: return 50.0f;
            case 3: return 75.0f;
            default: return 25.0f;
        }
    }

    private int getCooldownInTicks(int spellLevel) {
        switch (spellLevel) {
            case 1: return 240;
            case 2: return 140;
            case 3: return 100;
            default: return 240;
        }
    }

    @Override
    public int getCastTime(int spellLevel) {
        switch (spellLevel) {
            case 1: return 40;
            case 2: return 28;
            case 3: return 20;
            default: return 40;
        }
    }

    @Override
    public int getManaCost(int spellLevel) {
        switch (spellLevel) {
            case 1: return 40;
            case 2: return 45;
            case 3: return 50;
            default: return 40;
        }
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide) {
            float maxDistance = getDistance(spellLevel);
            HitResult hitResult = Utils.raycastForEntity(level, entity, 64.0F, true);

            if (hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult) hitResult;

                if (entityHitResult.getEntity() instanceof LivingEntity targetEntity) {
                    double distance = entity.distanceTo(targetEntity);
                    if (distance <= maxDistance) {
                        Vec3 casterPos = entity.position();
                        Vec3 targetPos = targetEntity.position();
                        
                        entity.teleportTo(targetPos.x, targetPos.y, targetPos.z);
                        targetEntity.teleportTo(casterPos.x, casterPos.y, casterPos.z);
                    }
                }
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}