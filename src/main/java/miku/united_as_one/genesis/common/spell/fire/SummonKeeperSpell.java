package miku.united_as_one.genesis.common.spell.fire;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.spell.fire.SummonedKeeperEntity;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
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
                "ui.irons_spellbooks.summon_count", getSummonCount()
            ),
            Component.translatable(
                "ui.irons_spellbooks.hp", Utils.stringTruncation(getSummonHealth(spellLevel, (float) caster.getAttributeValue(Attributes.ATTACK_DAMAGE)), 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.damage", Utils.stringTruncation(getSummonDamage(spellLevel), 1)
            ),
            Component.translatable(
                "ui.iron_spells_genesis.exist_time", getLifetime((float) caster.getAttributeValue(Attributes.ATTACK_DAMAGE)) / 20
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

    private int getSummonCount() {
        return 2;
    }

    private float getSummonHealth(int spellLevel, float spellPower) {
        return 10 + (spellLevel - 1) * 10 * (1 + spellPower / 100);
    }

    private float getSummonDamage(int spellLevel) {
        return 5 + (spellLevel - 1) * 5;
    }

    private int getLifetime(float spellPower) {
        return (int) ((20 * 90) * (1 + spellPower / 100));
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        ServerLevel serverLevel = (ServerLevel) level;

        for (int i = 0; i < getSummonCount(); i++) {
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

            float nh = getSummonHealth(spellLevel, (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE));
            keeper.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(nh);
            keeper.setHealth(nh);
            keeper.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getSummonDamage(spellLevel));

            keeper.setSummoner(entity);
            keeper.setIsSummoned();

            level.addFreshEntity(keeper);

            SummonManager.initSummon(
                entity,
                keeper,
                getLifetime((float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE)), 
                new SummonedEntitiesCastData()
            );

            keeper.triggerRise();
            /*keeper.riseAnimTick = 50;*/

            MagicManager.spawnParticles(serverLevel, ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(),
                entity.getX() + oX, entity.getY(), entity.getZ() + oZ, 30, 0.3, 0.3, 0.3, 0.05, false
            );
        }

        serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
            SoundRegistry.SOULCALLER_TOLL_SUCCESS.get(), SoundSource.PLAYERS, 6, 1
        );
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}