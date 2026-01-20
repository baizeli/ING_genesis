package miku.united_as_one.genesis.spell.celestial_source;

import miku.united_as_one.genesis.Entity.ModEntities;
import miku.united_as_one.genesis.Entity.spells.celestial_source.DeadStarDecreeComet;
import miku.united_as_one.genesis.EternisStarrySky;
import miku.united_as_one.genesis.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.*;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.FogParticleOptions;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

@AutoSpellConfig
public class DeadStarDecreeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "dead_star_decree");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SpellSchool.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(240.0F)
        .build();

    public DeadStarDecreeSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 25;
        this.spellPowerPerLevel = 25;
        this.castTime = 60;
        this.baseManaCost = 500;
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
        return CastType.CONTINUOUS;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.damage", 
                Utils.stringTruncation(getLargeCometDamage(spellLevel, caster), 2)
            ),
            Component.translatable(
                "ui.irons_spellbooks.radius", 
                Utils.stringTruncation(getRadius(spellLevel, caster), 1)
            )
        );
    }

    // 陨石的伤害
    private float getDamage(int spellLevel, LivingEntity caster) {
        float baseDamage = 10 * spellLevel;
        if (caster == null) {
            return baseDamage;
        }
        
        float spellPower = getSpellPower(spellLevel, caster);
        float additionalDamage = spellPower - 1.0f;
        return baseDamage + additionalDamage;
    }
    
    // 大陨石的伤害
    private float getLargeCometDamage(int spellLevel, LivingEntity caster) {
        float baseDamage = 100 + (spellLevel - 1) * 10;
        if (caster == null) {
            return baseDamage;
        }
        
        float spellPower = getSpellPower(spellLevel, caster);
        float additionalDamage = spellPower - 1.0f;
        return baseDamage + additionalDamage;
    }

    private float getRadius(int spellLevel, LivingEntity caster) {
        return 15;
    }

    @Override
    public int getCastTime(int spellLevel) {
        return 60;
    }

    @Override
    public int getManaCost(int spellLevel) {
        return 500 + (spellLevel - 1) * 50;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.ENDER_CAST.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!(playerMagicData.getAdditionalCastData() instanceof DeadStarDecreeCastData)) {
            Vec3 targetArea = Utils.moveToRelativeGroundLevel(world, Utils.raycastForEntity(world, entity, 40, true).getLocation(), 12);
            playerMagicData.setAdditionalCastData(new DeadStarDecreeCastData(targetArea));
        }
        
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        if (playerMagicData == null || !(playerMagicData.getAdditionalCastData() instanceof DeadStarDecreeCastData castData)) {
            return;
        }

        float radius = getRadius(spellLevel, entity);
        int tick = playerMagicData.getCastDurationRemaining() - 1;

        if (tick == 20) {
            Vec3 center = castData.center;
            Vec3 largeCometSpawn = new Vec3(center.x, center.y + 50, center.z);
            var trajectory = new Vec3(0, -1, 0);

            shootLargeComet(level, spellLevel, entity, largeCometSpawn, trajectory);

            ParticleOptions largeCometFog = new FogParticleOptions(new Vector3f(1f, 1f, 0f), 4.0f);
            MagicManager.spawnParticles(level, largeCometFog, largeCometSpawn.x, largeCometSpawn.y, largeCometSpawn.z, 1, 1, 1, 1, 1, false);
            MagicManager.spawnParticles(level, largeCometFog, largeCometSpawn.x, largeCometSpawn.y, largeCometSpawn.z, 1, 1, 1, 1, 1, true);
        }
        
        if (tick % 20 == 0) {
            castData.updateTrackedEntities(level.getEntities(entity, AABB.ofSize(castData.center, radius * 3, radius, radius * 3), e -> e instanceof LivingEntity && !DamageSources.isFriendlyFireBetween(entity, e)));
        }

        if (tick % 4 == 0)
            for (int i = 0; i < 8; i++) {
                Vec3 center = castData.center;
                Vec3 spawnTarget = Utils.moveToRelativeGroundLevel(level, center.add(new Vec3(0, 0, entity.getRandom().nextFloat() * radius).yRot(entity.getRandom().nextInt(360) * Mth.DEG_TO_RAD)), 3).add(0, 0.5, 0);
                var trajectory = new Vec3(.15f, -.85f, 0).normalize();
                Vec3 spawn = Utils.raycastForBlock(level, spawnTarget, spawnTarget.add(trajectory.scale(-12)), ClipContext.Fluid.NONE).getLocation().add(trajectory);
                
                shootComet(level, spellLevel, entity, spawn, trajectory, radius);
                
                ParticleOptions cometFog = new FogParticleOptions(new Vector3f(1f, 1f, 0f), 0.75f);
                MagicManager.spawnParticles(level, cometFog, spawn.x, spawn.y, spawn.z, 1, 1, 1, 1, 1, false);
                MagicManager.spawnParticles(level, cometFog, spawn.x, spawn.y, spawn.z, 1, 1, 1, 1, 1, true);
            }
    }

    public void shootComet(Level world, int spellLevel, LivingEntity entity, Vec3 spawn, Vec3 trajectory, float explosionRadius) {
        DeadStarDecreeComet fireball = new DeadStarDecreeComet(ModEntities.DEAD_STAR_DECREE_COMET.get(), world);
        fireball.setPos(spawn.add(-1, 0, 0));
        fireball.shoot(trajectory, .075f);
        fireball.setDamage(getDamage(spellLevel, entity));
        fireball.setExplosionRadius(explosionRadius);
        fireball.setOwner(entity);
        world.addFreshEntity(fireball);
        world.playSound(null, spawn.x, spawn.y, spawn.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0f, 0.7f + Utils.random.nextFloat() * .3f);
    }

    public void shootLargeComet(Level world, int spellLevel, LivingEntity entity, Vec3 spawn, Vec3 trajectory) {
        DeadStarDecreeComet fireball = new DeadStarDecreeComet(ModEntities.DEAD_STAR_DECREE_LARGE_COMET.get(), world);
        fireball.setPos(spawn);
        fireball.shoot(trajectory, 0);
        fireball.setDamage(getLargeCometDamage(spellLevel, entity));
        fireball.setExplosionRadius(10.0f);
        fireball.setOwner(entity);
        world.addFreshEntity(fireball);
        world.playSound(null, spawn.x, spawn.y, spawn.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0f, 0.7f + Utils.random.nextFloat() * .3f);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_CONTINUOUS_OVERHEAD;
    }

    public static class DeadStarDecreeCastData implements ICastData {
        Vec3 center;
        final List<Entity> trackedEntities = new ArrayList<>();

        public DeadStarDecreeCastData(Vec3 center) {
            this.center = center;
        }

        @Override
        public void reset() {
            trackedEntities.clear();
        }

        public void updateTrackedEntities(List<Entity> entities) {
            trackedEntities.clear();
            trackedEntities.addAll(entities);
        }
    }
}