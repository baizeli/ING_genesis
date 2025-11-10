package com.baizeli.eternisstarrysky.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Util.EntityData;
import com.baizeli.eternisstarrysky.client.WireBoxRenderer;
import com.baizeli.eternisstarrysky.client.network.WireBoxSyncPacket;
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
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

import static com.baizeli.eternisstarrysky.EternisStarrySky.CHANNEL;

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
                Component.translatable("ui.iron_spells_genesis.percent_force_damage", Utils.stringTruncation(getForceDamage(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(3 * spellLevel, 1)),
                Component.translatable("ui.irons_spellbooks.cooldown", Utils.timeFromTicks(getCooldownInTicks(spellLevel, CastSource.NONE, caster), 1)
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

    @Override
    public int getSpellCooldown() {
        return 0;// 阻止原有冷却
    }

    private double getForceDamage(int spellLevel, LivingEntity caster) {
        return spellLevel * caster.getAttributeValue(AttributeRegistry.SPELL_POWER.get()) * this.getSchoolType().getPowerFor(caster) * 0.5;
    }

    private int getCooldownInTicks(int spellLevel, CastSource castSource, LivingEntity caster) {
        int coolDown;

        double playerCooldownModifier = caster.getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION.get());
        float itemCoolDownModifer = 1.0F;
        if (castSource == CastSource.SWORD) {
            itemCoolDownModifer = ServerConfigs.SWORDS_CD_MULTIPLIER.get().floatValue();
        }

        if (spellLevel <= 3) {          // 1~3 级
            coolDown =  200;
        } else if (spellLevel < 10) {  // 4~9 级
            coolDown =  100 * (spellLevel - 1);   // 4→300，5→400，…，9→800
        } else {
            coolDown = 1000;
        }
        return (int) (coolDown * ((double)2.0F - Utils.softCapFormula(playerCooldownModifier)) * itemCoolDownModifer);
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
        if (serverPlayer.getHealth() > serverPlayer.getMaxHealth() / 2) {// 防止错误增加冷却
            addCooldown(serverPlayer, this, castSource, spellLevel);
        }
    }

    @Override
    public void onServerCastComplete(Level serverLevel, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
        if (entity.getHealth() > entity.getMaxHealth() / 2 && !cancelled) {
            int range = 3 * spellLevel;
            AABB box = new AABB(entity.getX() - range, entity.getY() - range, entity.getZ() - range, entity.getX() + range, entity.getY() + range, entity.getZ() + range);

            for (Entity e : serverLevel.getEntities(entity, box, e -> e instanceof LivingEntity && e != entity)) {
                if (e.isAlive()) {
                    e.hurt(getDamageSource(entity), getSpellPower(spellLevel, entity));
                    if (e instanceof LivingEntity living) {
                        living.setHealth((float) (living.getHealth() - living.getHealth() * getForceDamage(spellLevel, entity) * 0.01));
                    }
                    ((ServerLevel) serverLevel).sendParticles(ParticleHelper.BLOOD,
                            e.getX(), e.getY() + e.getBbHeight() * 0.5, e.getZ(),
                            250,// 数量
                            e.getBbWidth() * 0.6,
                            e.getBbHeight() * 0.4,
                            e.getBbWidth() * 0.6,
                            0.12
                    );
                    long expireAt = serverLevel.getGameTime() + 400;   // 20秒后失效
                    // 网络包同步
                    CHANNEL.send(net.minecraftforge.network.PacketDistributor.ALL.noArg(),
                            new WireBoxSyncPacket(e.getUUID(), true, expireAt));
                    WireBoxRenderer.entitiesForRenderWireBoxRenderer.put(e, new EntityData(expireAt, e.position()));
                }
            }
            entity.setHealth(entity.getHealth() - entity.getMaxHealth() / 2);
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200 + spellLevel * 20, spellLevel - 1, false, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200 + spellLevel * 20, spellLevel - 1, false, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200 + spellLevel * 20, spellLevel - 1, false, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200 + spellLevel * 15, spellLevel - 1, false, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200 + spellLevel * 15, spellLevel - 1, false, true, true));
        } else if (!cancelled) {// 血量不够，取消施法
            if (entity instanceof ServerPlayer sp) {
                sp.sendSystemMessage(
                        Component.translatable("spell.iron_spells_genesis.warped_blood_burst.low_health")
                                .withStyle(ChatFormatting.RED),
                        true
                );
            }
            cancelled = true;
        }
        super.onServerCastComplete(serverLevel, spellLevel, entity, playerMagicData, cancelled);
    }
}
