package miku.united_as_one.genesis.contents.spell.ice;

import io.redspace.ironsspellbooks.util.ParticleHelper;
import miku.united_as_one.genesis.Genesis;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.*;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombEntity;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;

@AutoSpellConfig
public class SnowBurialSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "snow_burial");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SchoolRegistry.ICE_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(240)
        .build();

    public SnowBurialSpell() {
        this.baseManaCost = 600;
        this.castTime = 80;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.damage", 
                Utils.stringTruncation(getDamage(caster), 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.duration", Utils.timeFromTicks(getDuration(), 1)
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
        return CastType.LONG;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.FINISH_ANIMATION;
    }

    private float getDamage(LivingEntity caster) {
        float baseDamage = 60 + (float) (caster.getAttributeValue(AttributeRegistry.MAX_MANA.get()) * 0.1);
        float spellPower = (float) caster.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
        return baseDamage + (baseDamage * spellPower / 100);
    }

    private int getDuration() {
        return 300;
    }

    /*@Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        double maxMana = entity.getAttributeValue(AttributeRegistry.MAX_MANA.get());
        if (maxMana < 600) return false;
        return !(playerMagicData.getMana() < maxMana * 0.9f);
    }*/

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 48, 0.1f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (level.isClientSide()) return;
        
        playerMagicData.setMana(0);

        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
            Entity target = targetData.getTarget((ServerLevel) level);
            if (target instanceof LivingEntity livingTarget) {
                for (Entity nearby : level.getEntities(entity, livingTarget.getBoundingBox().inflate(2))) {
                    if (nearby instanceof LivingEntity livingNearby) {
                        if (entity.isAlliedTo(livingNearby) || nearby instanceof Player) continue;

                        livingNearby.hurt(getDamageSource(entity), getDamage(entity));

                        for (int i = 0; i < 20; i++) {
                            MagicManager.spawnParticles(level, ParticleHelper.SNOWFLAKE,
                                livingNearby.getX() + level.random.nextGaussian() * 0.8,
                                livingNearby.getY() + livingNearby.getBbHeight() + 3 - level.random.nextDouble() * 5,
                                livingNearby.getZ() + level.random.nextGaussian() * 0.8,
                            10, 0, 0, 0, -0.15, false);
                            if (i % 2 == 0) MagicManager.spawnParticles(level, ParticleHelper.SNOW_DUST,
                                livingNearby.getX() + level.random.nextGaussian() * 0.8,
                                livingNearby.getY() + livingNearby.getBbHeight() + 3 - level.random.nextDouble() * 5,
                                livingNearby.getZ() + level.random.nextGaussian() * 0.8,
                            10, 0, 0, 0, -0.1, false);
                        }

                        IceTombEntity iceTomb = new IceTombEntity(level, entity);

                        iceTomb.moveTo(livingNearby.position());
                        iceTomb.setLifetime(getDuration());
                        iceTomb.setEvil();

                        level.addFreshEntity(iceTomb);

                        livingNearby.startRiding(iceTomb, true);
                    }
                }
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}