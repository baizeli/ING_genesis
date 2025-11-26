package com.baizeli.eternisstarrysky.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class SummonPigSwarmSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "summon_pig_swarm");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchool.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(5)
        .setCooldownSeconds(60.0F)
        .build();

    public SummonPigSwarmSpell() {
        this.manaCostPerLevel = 100;
        this.baseManaCost = 400;
        this.castTime = 100;
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
        return SpellSchool.CELESTIAL_SOURCE.get();
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable("ui.irons_spellbooks.summon_count", getSummonCount(spellLevel))
        );
    }

    private int getSummonCount(int spellLevel) {
        return 4 * spellLevel;
    }

    @Override
    public int getCastTime(int spellLevel) {
        return Math.max(20, 100 - (spellLevel - 1) * 20);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < getSummonCount(spellLevel); i++) {
                Pig pig = EntityType.PIG.create(serverLevel);

                pig.setPos(
                    entity.getX() + Utils.random.nextGaussian() * 2.0D, 
                    entity.getY(),
                    entity.getZ() + Utils.random.nextGaussian() * 2.0D
                );

                pig.setCustomName(Component.translatable("iron_spells_genesis.summoned_pig_name"));

                serverLevel.addFreshEntity(pig);
            }
        }
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}