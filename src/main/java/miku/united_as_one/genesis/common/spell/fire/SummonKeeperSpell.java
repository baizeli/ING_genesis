package miku.united_as_one.genesis.common.spell.fire;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.spell.fire.SummonedKeeperEntity;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import io.redspace.ironsspellbooks.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;
import javax.annotation.Nullable;

@AutoSpellConfig
public class SummonKeeperSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "summon_keeper");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
        .setMaxLevel(6)
        .setCooldownSeconds(180)
        .build();

    public SummonKeeperSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 10;
        this.castTime = 60;
        this.baseManaCost = 50;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.summon_count", getRecastCount(spellLevel, caster)
            ),
            Component.translatable(
                "ui.irons_spellbooks.hp", Utils.stringTruncation(getSummonHealth(spellLevel), 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.damage", Utils.stringTruncation(getSummonDamage(spellLevel), 1)
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
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable)) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    private float getSummonHealth(int spellLevel) {
        return 10 + (spellLevel - 1) * 10;
    }

    private float getSummonDamage(int spellLevel) {
        return 5 + (spellLevel - 1) * 5;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PlayerRecasts rs = playerMagicData.getPlayerRecasts();
        if (!rs.hasRecastForSpell(this)) {
            SummonedEntitiesCastData SD = new SummonedEntitiesCastData();
            int ST = 20 * 90;

            for (int i = 0; i < getRecastCount(spellLevel, entity); i++) {
                SummonedKeeperEntity keeper = new SummonedKeeperEntity(level);

                double oX = -entity.getLookAngle().z * 2 * (i == 0 ? 1 : -1);
                double oZ = entity.getLookAngle().x * 2 * (i == 0 ? 1 : -1);
                
                keeper.setPos(entity.getX() + oX, entity.getY(), entity.getZ() + oZ);

                keeper.setYRot(entity.getYRot());
                keeper.setXRot(entity.getXRot());

                if (spellLevel >= 7) {
                    keeper.setIsRestored();
                    keeper.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.LEGIONNAIRE_FLAMBERGE.get()));
                } else keeper.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.KEEPER_FLAMBERGE.get()));

                Objects.requireNonNull(keeper.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(getSummonDamage(spellLevel));
                Objects.requireNonNull(keeper.getAttributes().getInstance(Attributes.MAX_HEALTH)).setBaseValue(getSummonHealth(spellLevel));
                keeper.setHealth(keeper.getMaxHealth());

                keeper.triggerRise();
                keeper.setSummoner(entity);
                keeper.setIsSummoned();

                level.addFreshEntity(keeper);
                SummonManager.initSummon(entity, keeper, ST, SD);

                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundRegistry.SOULCALLER_TOLL_SUCCESS.get(), SoundSource.PLAYERS, 6, 1
                );

                MagicManager.spawnParticles(level, ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(),
                    entity.getX() + oX, entity.getY(), entity.getZ() + oZ, 30, 0.3, 0.3, 0.3, 0.05, false
                );
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