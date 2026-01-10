package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.network.casting.SyncCooldownPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AutoSpellConfig
@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReversePlagueSpell extends ChaosBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "reverse_plague");
    private final DefaultConfig defaultConfig;
    public static Map<UUID, UUID> entityMap = new HashMap<>();

    public ReversePlagueSpell() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.LEGENDARY)
                .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
                .setMaxLevel(1)
                .setCooldownSeconds(0)
                .build();
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 10;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.cooldown", Utils.timeFromTicks(getCooldownInTicks(CastSource.NONE, caster), 1)));
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
    public int getSpellCooldown() {
        return 0;// 阻止原有冷却
    }

    private int getCooldownInTicks(CastSource castSource, LivingEntity caster) {
        int coolDown = 4800;

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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 100, 0.35F);
    }

    @Override
    public void castSpell(Level world, int spellLevel, ServerPlayer serverPlayer, CastSource castSource, boolean triggerCooldown) {
        super.castSpell(world, spellLevel, serverPlayer, castSource, triggerCooldown);
        addCooldown(serverPlayer, this, castSource);
    }

    @Override
    public void onServerCastComplete(Level serverLevel, int spellLevel, LivingEntity livingEntity, MagicData playerMagicData, boolean cancelled) {
        Entity entity = serverLevel.getEntities().get(((TargetEntityCastData) playerMagicData.getAdditionalCastData()).getTargetUUID());
        if (entity instanceof LivingEntity living) {
            for (MobEffectInstance effectInstance : living.getActiveEffects().stream().toList()) {
                if (effectInstance.getEffect().getCategory() == MobEffectCategory.BENEFICIAL) {
                    livingEntity.addEffect(effectInstance);
                    living.removeEffect(effectInstance.getEffect());
                }
            }
            for (MobEffectInstance effectInstance : livingEntity.getActiveEffects().stream().toList()) {
                if (effectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    living.addEffect(effectInstance);
                    livingEntity.removeEffect(effectInstance.getEffect());
                }
            }
            living.getPersistentData().putLong("remaining time", serverLevel.getGameTime() + 600);
            entityMap.put(livingEntity.getUUID(), living.getUUID());
        }
        super.onServerCastComplete(serverLevel, spellLevel, livingEntity, playerMagicData, cancelled);
    }
}
