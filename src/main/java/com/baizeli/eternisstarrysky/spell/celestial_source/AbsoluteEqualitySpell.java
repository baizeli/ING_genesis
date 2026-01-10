package com.baizeli.eternisstarrysky.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

@AutoSpellConfig
public class AbsoluteEqualitySpell extends CelestialSourceBaseSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "absolute_equality");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SpellSchool.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(1200.0F)
        .build();

    public AbsoluteEqualitySpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 200;
        this.baseManaCost = 1500;
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(
            level, entity, playerMagicData, this, 800, 0.1f, true,
            target -> !isBossEntity(target)
        );
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
                LivingEntity target = targetData.getTarget((ServerLevel) level);

                if (target != null && !isBossEntity(target)) {
                    // 先计算施法者/目标的当前血量%
                    float playerHealthPercent = player.getHealth() / player.getMaxHealth();
                    float targetHealthPercent = target.getHealth() / target.getMaxHealth();

                    // 再交换/替换-施法者/目标的血量%
                    player.setHealth(player.getMaxHealth() * targetHealthPercent);
                    target.setHealth(target.getMaxHealth() * playerHealthPercent);
                }
            }
        }
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private boolean isBossEntity(LivingEntity entity) {
        // 你是不是boss呀??
        if (entity.getType().is(Tags.EntityTypes.BOSSES)) {
            return true;
        }

        // 不是boss你可以通过了
        return false;
    }
}