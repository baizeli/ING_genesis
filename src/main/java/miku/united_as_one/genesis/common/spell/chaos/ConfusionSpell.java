package miku.united_as_one.genesis.common.spell.chaos;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.registry.EffectRegistry;
import miku.united_as_one.genesis.init.registry.spell.SpellSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

@AutoSpellConfig
public class ConfusionSpell extends ChaosBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "confusion");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SpellSchoolRegistry.CHAOS_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(420.0F)
        .build();

    public ConfusionSpell() {
        this.manaCostPerLevel = 0;
        this.baseManaCost = 1000;
        this.castTime = 140;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
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
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.duration",
                Utils.timeFromTicks(getDuration(spellLevel), 1)
            )
        );
    }

    private int getDuration(int spellLevel) {
        return 600;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && level instanceof ServerLevel) {
            AABB boundingBox = entity.getBoundingBox().inflate(7);
            
            level.getEntitiesOfClass(LivingEntity.class, boundingBox).forEach((target) -> {
                if (!(target instanceof Player) && entity.distanceTo(target) <= 7) {
                    target.addEffect(new MobEffectInstance(
                        EffectRegistry.CONFUSION.get(),
                        getDuration(spellLevel),
                        0,
                        false,
                        false,
                        true
                    ));
                }
            });
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}