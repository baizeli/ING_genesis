package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

@AutoSpellConfig
public class WarpedBarrierSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "warped_barrier");
    private final DefaultConfig defaultConfig;

    public WarpedBarrierSpell() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.COMMON)
                .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
                .setMaxLevel(3)
                .setCooldownSeconds(0)
                .build();
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 150;
        this.spellPowerPerLevel = 10;
        this.castTime = 0;
        this.baseManaCost = 15;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.cooldown", Utils.timeFromTicks(getCooldownInTicks(spellLevel, CastSource.COMMAND, caster), 1)),
                Component.translatable("ui.iron_spells_genesis.health_cost_percent", 90, 1),
                Component.translatable("ui.iron_spells_genesis.health_conversion_efficiency", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1))
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
    public SchoolType getSchoolType() {
        return SpellSchool.CHAOS.get();
    }

    @Override
    public int getSpellCooldown() {
        return 0;// 阻止原有冷却
    }

    private double getConvertPercent(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) / 100;
    }

    private int getCooldownInTicks(int spellLevel, CastSource castSource, LivingEntity caster) {
        int coolDown;
        double playerCooldownModifier = 1.0D;

        if (caster != null) {
            playerCooldownModifier = caster.getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION.get());
        }
        float itemCoolDownModifer = 1.0F;
        if (castSource == CastSource.SWORD) {
            itemCoolDownModifer = ServerConfigs.SWORDS_CD_MULTIPLIER.get().floatValue();
        }
        coolDown = spellLevel * 50;
        return (int) (coolDown * ((double) 2.0F - Utils.softCapFormula(playerCooldownModifier)) * itemCoolDownModifer);
    }

    public void addCooldown(ServerPlayer serverPlayer, AbstractSpell spell, CastSource castSource, int spellLevel) {
        int effectiveCooldown = getCooldownInTicks(spellLevel, castSource, serverPlayer);
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
        addCooldown(serverPlayer, this, castSource, spellLevel);
    }

    @Override
    public void onServerCastComplete(Level serverLevel, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
        float damage = entity.getHealth() * 0.9f;
        entity.setHealth(entity.getHealth() - damage);
        entity.setAbsorptionAmount((float) (entity.getAbsorptionAmount() + damage * getConvertPercent(spellLevel, entity)));
        super.onServerCastComplete(serverLevel, spellLevel, entity, playerMagicData, cancelled);
    }
}
