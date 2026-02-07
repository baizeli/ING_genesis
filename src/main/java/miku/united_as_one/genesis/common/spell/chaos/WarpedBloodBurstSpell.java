package miku.united_as_one.genesis.common.spell.chaos;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.renderer.spell.chaos.WireBoxRenderer;
import miku.united_as_one.genesis.common.network.WireBoxSyncPacket;
import miku.united_as_one.genesis.init.registry.spell.SpellSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static miku.united_as_one.genesis.Genesis.CHANNEL;

@AutoSpellConfig
public class WarpedBloodBurstSpell extends ChaosBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "warped_blood_burst");
    private final DefaultConfig defaultConfig;

    public WarpedBloodBurstSpell() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.COMMON)
                .setSchoolResource(SpellSchoolRegistry.CHAOS_RESOURCE)
                .setMaxLevel(10)
                .setCooldownSeconds(60F)
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
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(10, 1))
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

    private double getForceDamage(int spellLevel, LivingEntity caster) {
        double entitySpellPowerModifier = 1.0F;
        double entitySchoolPowerModifier = 1.0F;
        float configPowerModifier = (float)ServerConfigs.getSpellConfig(this).powerMultiplier();
        if (caster != null) {
            entitySpellPowerModifier = caster.getAttributeValue(AttributeRegistry.SPELL_POWER.get());
            entitySchoolPowerModifier = this.getSchoolType().getPowerFor(caster);
        }
        return spellLevel * entitySpellPowerModifier * entitySchoolPowerModifier * configPowerModifier * 0.5;
    }

    @Override
    public void castSpell(Level world, int spellLevel, ServerPlayer serverPlayer, CastSource castSource, boolean triggerCooldown) {
        super.castSpell(world, spellLevel, serverPlayer, castSource, triggerCooldown);
    }

    @Override
    public void onServerCastComplete(Level serverLevel, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
        if (entity.getHealth() > entity.getMaxHealth() / 2 && !cancelled) {
            int range = 10;
            AABB box = new AABB(entity.getX() - range, entity.getY() - range, entity.getZ() - range, entity.getX() + range, entity.getY() + range, entity.getZ() + range);

            for (Entity e : serverLevel.getEntities(entity, box, e -> e instanceof LivingEntity && e != entity)) {
                if (e.isAlive()) {
                    e.hurt(getDamageSource(entity), getSpellPower(spellLevel, entity));
                    if (e instanceof LivingEntity living) {
                        // 强制伤害
                        living.setHealth((float) (living.getHealth() - living.getMaxHealth() * getForceDamage(spellLevel, entity) * 0.01));
                    }
                    ((ServerLevel) serverLevel).sendParticles(ParticleHelper.BLOOD,
                            e.getX(), e.getY() + e.getBbHeight() * 0.5, e.getZ(),
                            250,// 数量
                            e.getBbWidth() * 0.6,
                            e.getBbHeight() * 0.4,
                            e.getBbWidth() * 0.6,
                            0.12
                    );
                    long expireAt = serverLevel.getGameTime() + 100;   // 5秒
                    // 网络包同步
                    CHANNEL.send(net.minecraftforge.network.PacketDistributor.ALL.noArg(),
                            new WireBoxSyncPacket(e.getUUID(), true, expireAt));
                    WireBoxRenderer.entitiesForRenderWireBoxRenderer.put(e.getUUID(), expireAt);
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
                                .withStyle(ChatFormatting.DARK_RED),
                        true
                );
            }
            cancelled = true;
        }
        super.onServerCastComplete(serverLevel, spellLevel, entity, playerMagicData, cancelled);
    }
}
