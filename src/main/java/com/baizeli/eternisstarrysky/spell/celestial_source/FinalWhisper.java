package com.baizeli.eternisstarrysky.spell.celestial_source;

import com.baizeli.eternisstarrysky.Entity.CustomArrowEntity;
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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoSpellConfig
public class FinalWhisper extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "final_whisper");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SpellSchool.CELESTIAL_SOURCE_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(420.0F)
            .build();

    public FinalWhisper() {
        this.manaCostPerLevel = 500;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 5;
        this.castTime = 140;
        this.baseManaCost = 2000;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.cooldown", Utils.timeFromTicks(getCooldownInTicks(CastSource.COMMAND, caster), 1)),
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getForceDamage(spellLevel, caster), 1)),
                Component.translatable("ui.iron_spells_genesis.force_damage", Utils.stringTruncation(getForceDamage(spellLevel, caster), 1))
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
    public SchoolType getSchoolType() {
        return SpellSchool.CELESTIAL_SOURCE.get();
    }

    @Override
    public int getSpellCooldown() {
        return 0;// 阻止原有冷却
    }

    private double getForceDamage(int spellLevel, LivingEntity caster) {
        double add = 20 * (ServerConfigs.getSpellConfig(this).powerMultiplier() - 1);
        int baseDamage = this.baseSpellPower + this.spellPowerPerLevel * (spellLevel - 1);
        if (caster != null) {
            add += 20 * (caster.getAttributeValue(AttributeRegistry.SPELL_POWER.get()) + this.getSchoolType().getPowerFor(caster) - 2);
        }
        return baseDamage + add;
    }

    @Override
    public float getSpellPower(int spellLevel, @Nullable Entity sourceEntity) {
        return super.getSpellPower(spellLevel, sourceEntity);
    }

    private int getCooldownInTicks(CastSource castSource, LivingEntity caster) {
        int coolDown = 6000;
        double playerCooldownModifier = 1.0D;

        if (caster != null) {
            playerCooldownModifier = caster.getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION.get());
        }
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
        if (entity.getHealth() > entity.getMaxHealth() * 0.6 && !cancelled) {
            entity.setHealth((float) (entity.getHealth() - entity.getMaxHealth() * 0.6));
            CustomArrowEntity arrowsEntity = new CustomArrowEntity(serverLevel, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
            Vec3 direction = entity.getLookAngle();
            arrowsEntity.shoot(direction.x, direction.y, direction.z, 3.5F, 0.0F);
            arrowsEntity.setFinalWhisperArrow(true);
            arrowsEntity.setBaseDamage(0);
            arrowsEntity.setFinalWhisperArrowDamage(getForceDamage(spellLevel, entity));
            arrowsEntity.setOwner(entity);
            serverLevel.addFreshEntity(arrowsEntity);
        } else if (!cancelled) {// 血量不够，取消施法
            if (entity instanceof ServerPlayer sp) {
                sp.sendSystemMessage(
                        Component.translatable("spell.iron_spells_genesis.final_whisper.low_health")
                                .withStyle(ChatFormatting.DARK_BLUE),
                        true
                );
            }
            cancelled = true;
        }
        super.onServerCastComplete(serverLevel, spellLevel, entity, playerMagicData, cancelled);
    }
}
