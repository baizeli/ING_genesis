package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.client.WireBoxRenderer;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.network.casting.SyncCooldownPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

@AutoSpellConfig
public class WarpedBloodBurst extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "warped_blood_burst");
    private final DefaultConfig defaultConfig;

    public WarpedBloodBurst() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.LEGENDARY)
                .setSchoolResource(SpellSchool.CHAOS_RESOURCE)
                .setMaxLevel(10)
                .setCooldownSeconds(0)
                .build();
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 16;
        this.spellPowerPerLevel = 6;
        this.castTime = 100;
        this.baseManaCost = 50;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(3 * spellLevel, 1)),
                Component.translatable("ui.irons_spellbooks.cooldown", Utils.timeFromTicks(getCooldownInTicks(spellLevel), 1)
        ));
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
        return SpellSchool.CHAOS.get();
    }

    private int getCooldownInTicks(int spellLevel) {
        if (spellLevel <= 3) {          // 1~3 级
            return 200;
        } else if (spellLevel < 10) {  // 4~9 级
            return 100 * (spellLevel - 1);   // 4→300，5→400，…，9→800
        }
        return 1000;
    }

    public void addCooldown(ServerPlayer serverPlayer, AbstractSpell spell, CastSource castSource, int spellLevel) {
        int effectiveCooldown = getCooldownInTicks(spellLevel);
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
        int range = 3 * spellLevel;
        AABB box = new AABB(entity.getX() - range, entity.getY() - range, entity.getZ() - range, entity.getX() + range, entity.getY() + range, entity.getZ() + range);

        for (Entity e : serverLevel.getEntities(entity, box, e -> e instanceof LivingEntity && e != entity)) {
            if (e.isAlive()) {
                e.hurt(getDamageSource(entity), getSpellPower(spellLevel, entity));
                WireBoxRenderer.entitiesForRenderWireBoxRenderer.put(e, 600);
            }
        }
        super.onServerCastComplete(serverLevel, spellLevel, entity, playerMagicData, cancelled);
    }
}
