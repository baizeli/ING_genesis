package miku.united_as_one.genesis.common.spell.eldritch;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.spell.eldritch.SummonedWardenEntity;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.*;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.*;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;

import java.util.*;
import javax.annotation.Nullable;

@AutoSpellConfig
public class SummonWardenSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "summon_warden");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(240)
        .build();

    public SummonWardenSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 10;
        this.castTime = 60;
        this.baseManaCost = 300;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable("ui.irons_spellbooks.summon_count", getRecastCount(spellLevel, caster)),
            Component.translatable("ui.irons_spellbooks.hp", Utils.stringTruncation(getSummonHealth(spellLevel), 1)),
            Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSummonDamage(spellLevel), 1)),
            Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks(getSummonDuration(), 1))
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
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable))
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    private float getSummonHealth(int spellLevel) {
        return 100 + (spellLevel - 1) * 20;
    }

    private float getSummonDamage(int spellLevel) {
        return 20 + (spellLevel - 1) * 5;
    }

    private int getSummonDuration() {
        return 20 * 60 * 2;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PlayerRecasts rs = playerMagicData.getPlayerRecasts();
        if (!rs.hasRecastForSpell(this)) {
            SummonedEntitiesCastData SD = new SummonedEntitiesCastData();
            int ST = getSummonDuration();

            for (int i = 0; i < getRecastCount(spellLevel, entity); i++) {
                SummonedWardenEntity warden = new SummonedWardenEntity(level);

                double oX = -entity.getLookAngle().z * 2 * (i == 0 ? 1 : -1);
                double oZ = entity.getLookAngle().x * 2 * (i == 0 ? 1 : -1);

                warden.setPos(entity.getX() + oX, entity.getY(), entity.getZ() + oZ);
                warden.setYRot(entity.getYRot());
                warden.setXRot(entity.getXRot());

                Objects.requireNonNull(warden.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(getSummonDamage(spellLevel));
                Objects.requireNonNull(warden.getAttributes().getInstance(Attributes.MAX_HEALTH)).setBaseValue(getSummonHealth(spellLevel));
                warden.setHealth(warden.getMaxHealth());

                warden.getBrain().setMemoryWithExpiry(MemoryModuleType.IS_EMERGING, Unit.INSTANCE, 134);
                warden.getBrain().setMemoryWithExpiry(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, Long.MAX_VALUE);
                warden.setSummoner(entity);
                warden.setIsSummoned();

                level.addFreshEntity(warden);
                SummonManager.initSummon(entity, warden, ST, SD);
            }

            rs.addRecast(new RecastInstance(this.getSpellId(),
                spellLevel,
                getRecastCount(spellLevel, entity),
                ST,
                castSource,
                SD
            ), playerMagicData);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}