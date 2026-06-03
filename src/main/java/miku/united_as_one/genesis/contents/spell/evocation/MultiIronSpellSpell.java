package miku.united_as_one.genesis.contents.spell.evocation;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.contents.entity.projectile.ThrownIron;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.*;

@AutoSpellConfig
public class MultiIronSpellSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "multi_iron_spell");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.COMMON)
        .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
        .setMaxLevel(10)
        .setCooldownSeconds(20)
        .build();

    public MultiIronSpellSpell() {
        this.manaCostPerLevel = 16;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 80;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel), 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.projectile_count", getRecastCount(spellLevel, caster)
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

    @Override
    public int getRecastCount(int spellLevel, LivingEntity entity) {
        return 6;
    }

    private float getDamage(int spellLevel) {
        return 6 + (spellLevel - 1) * 0.5f;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 10, 0.1f);
    }

    @SuppressWarnings("removal")
    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new MultiTargetEntityCastData();
    }

    @SuppressWarnings("removal")
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetEntityCastData) {
            var recasts = playerMagicData.getPlayerRecasts();

            if (!recasts.hasRecastForSpell(getSpellId())) {
                recasts.addRecast(new RecastInstance(
                    getSpellId(),
                    spellLevel,
                    getRecastCount(spellLevel, entity),
                    3 * 20,
                    castSource,
                    new MultiTargetEntityCastData(targetEntityCastData.getTarget((ServerLevel) level))
                ), playerMagicData);
            } else {
                var instance = recasts.getRecastInstance(this.getSpellId());
                if (instance != null && instance.getCastData() instanceof MultiTargetEntityCastData targetingData)
                    targetingData.addTarget(targetEntityCastData.getTargetUUID());
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        Level level = serverPlayer.level;

        if (castDataSerializable instanceof MultiTargetEntityCastData targetingData) {
            for (UUID uuid : targetingData.getTargets()) {
                var target = (LivingEntity) ((ServerLevel) serverPlayer.level).getEntity(uuid);

                if (target == null) continue;

                ThrownIron thrownIron = new ThrownIron(level, serverPlayer);
                thrownIron.setDamage(getDamage(recastInstance.getSpellLevel()));
                thrownIron.setLifeTime(10 * 20);

                var vec = target.getBoundingBox().getCenter().subtract(serverPlayer.getEyePosition()).normalize();
                thrownIron.shoot(vec.x, vec.y, vec.z, 1.0f, 0);

                level.addFreshEntity(thrownIron);
            }
        }
    }
}
