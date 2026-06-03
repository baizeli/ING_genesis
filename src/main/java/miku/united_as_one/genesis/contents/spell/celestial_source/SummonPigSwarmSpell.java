package miku.united_as_one.genesis.contents.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
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

import java.util.*;

@AutoSpellConfig
public class SummonPigSwarmSpell extends CelestialSourceBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "summon_pig_swarm");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchoolRegistry.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(5)
        .setCooldownSeconds(60)
        .build();

    public SummonPigSwarmSpell() {
        this.manaCostPerLevel = 100;
        this.baseManaCost = 400;
        this.castTime = 100;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 4;
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
            Component.translatable("ui.irons_spellbooks.summon_count", getSummonCount(spellLevel, caster))
        );
    }

    // 召唤laowang237的数量
    private int getSummonCount(int spellLevel, LivingEntity caster) {
        int powerBonus = 0;

        if (caster != null) powerBonus = (int) (getSpellPower(spellLevel, caster) / 10);

        return this.baseSpellPower + (spellLevel - 1) * this.spellPowerPerLevel + powerBonus;
    }

    @Override
    public int getCastTime(int spellLevel) {
        return Math.max(20, 100 - (spellLevel - 1) * 20);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < getSummonCount(spellLevel, entity); i++) {
                Pig pig = EntityType.PIG.create(serverLevel);

                if (pig != null) {
                    pig.setPos(
                        entity.getX() + Utils.random.nextGaussian() * 2d,
                        entity.getY(),
                        entity.getZ() + Utils.random.nextGaussian() * 2d
                    );

                    pig.setCustomName(Component.translatable("genesis_magic.summoned_pig_name"));
                    serverLevel.addFreshEntity(pig);
                }
            }
        }
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}