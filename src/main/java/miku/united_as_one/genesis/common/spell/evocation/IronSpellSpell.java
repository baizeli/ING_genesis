package miku.united_as_one.genesis.common.spell.evocation;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.projectile.ThrownIron;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class IronSpellSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "iron_spell");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
        .setMaxLevel(10)
        .setCooldownSeconds(2)
        .build();

    public IronSpellSpell() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 10;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel), 1)
            )
        );
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
        return CastType.INSTANT;
    }

    private float getDamage(int spellLevel) {
        return 6 + (spellLevel - 1) * 0.5f;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide()) {
            ThrownIron thrownIron = new ThrownIron(level, entity);

            thrownIron.setDamage(getDamage(spellLevel));
            thrownIron.setLifeTime(10*20);
            thrownIron.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, 0.5f, 0);

            level.addFreshEntity(thrownIron);
        }
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}