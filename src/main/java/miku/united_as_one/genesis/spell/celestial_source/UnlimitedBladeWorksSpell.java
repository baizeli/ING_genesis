package miku.united_as_one.genesis.spell.celestial_source;

import miku.united_as_one.genesis.entity.spells.celestial_source.notuse.MagicCircle;
import miku.united_as_one.genesis.entity.ModEntities;
import miku.united_as_one.genesis.entity.spells.celestial_source.notuse.SwordEntity;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.particles.ModParticles;
import miku.united_as_one.genesis.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.network.casting.SyncCooldownPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

@AutoSpellConfig
public class UnlimitedBladeWorksSpell extends CelestialSourceBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "unlimited_blade_works");
    private final DefaultConfig defaultConfig;

    public UnlimitedBladeWorksSpell() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.LEGENDARY)
                .setSchoolResource(SpellSchool.CELESTIAL_SOURCE_RESOURCE)
                .setMaxLevel(1)
                .setCooldownSeconds(0)
                .build();
        this.manaCostPerLevel = 200;
        this.baseSpellPower = 70;
        this.spellPowerPerLevel = 200;
        this.castTime = 0;//200;
        this.baseManaCost = 1000;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.iron_spells_genesis.number_of_swords", getNumberOfSwords(spellLevel), 1),
                Component.translatable("ui.irons_spellbooks.cooldown", Utils.timeFromTicks(getCooldownInTicks(CastSource.NONE, caster), 1)));
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
    public int getSpellCooldown() {
        return 0;// 阻止原有冷却
    }

    private int getNumberOfSwords(int spellLevel) {
        return 200 * spellLevel;
    }

    private int getCooldownInTicks(CastSource castSource, LivingEntity caster) {
        int coolDown = 3600;

        double playerCooldownModifier = caster.getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION.get());
        float itemCoolDownModifer = 1.0F;
        if (castSource == CastSource.SWORD) {
            itemCoolDownModifer = ServerConfigs.SWORDS_CD_MULTIPLIER.get().floatValue();
        }

        return (int) (coolDown * ((double) 2.0F - Utils.softCapFormula(playerCooldownModifier)) * itemCoolDownModifer);
    }

    public void addCooldown(ServerPlayer serverPlayer, AbstractSpell spell, CastSource castSource) {
        int effectiveCooldown = getCooldownInTicks(castSource, serverPlayer);
        SpellCooldownAddedEvent.Pre event = new SpellCooldownAddedEvent.Pre(effectiveCooldown, spell, serverPlayer, castSource);
        boolean pre = MinecraftForge.EVENT_BUS.post(event);
        if (castSource != CastSource.SCROLL && !pre) {
            effectiveCooldown = event.getEffectiveCooldown();
            MagicData.getPlayerMagicData(serverPlayer).getPlayerCooldowns().addCooldown(spell, effectiveCooldown);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncCooldownPacket(spell.getSpellId(), effectiveCooldown));
            MinecraftForge.EVENT_BUS.post(new SpellCooldownAddedEvent.Post(effectiveCooldown, spell, serverPlayer, castSource));
        }
    }

    @Override
    public void castSpell(Level world, int spellLevel, ServerPlayer serverPlayer, CastSource castSource, boolean triggerCooldown) {
        super.castSpell(world, spellLevel, serverPlayer, castSource, triggerCooldown);
        addCooldown(serverPlayer, this, castSource);
    }

    @Override
    public void onServerCastComplete(Level serverLevel, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
//        Vec3 lookVec = entity.getLookAngle();
//        if (entity instanceof Player player) {
//            magicCircle((ServerLevel) serverLevel, player, lookVec);
//        }
        SwordEntity sword = new SwordEntity(ModEntities.SWORD_ENTITY.get(), serverLevel, entity.getX(), entity.getY(), entity.getZ(), entity);
        serverLevel.addFreshEntity(new MagicCircle(ModEntities.MAGIC_CIRCLE.get(), serverLevel, entity.getX(), entity.getY(), entity.getZ()));
        serverLevel.addFreshEntity(sword);
        super.onServerCastComplete(serverLevel, spellLevel, entity, playerMagicData, cancelled);
    }

    private static void magicCircle(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ModParticles.MAGIC_CIRCLE.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0);
    }
}
