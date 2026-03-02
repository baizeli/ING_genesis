package miku.united_as_one.genesis.common.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.registry.spell.SpellSchoolRegistry;
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
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "absolute_equality");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SpellSchoolRegistry.CELESTIAL_SOURCE_RESOURCE)
        .setMaxLevel(1)
        .setCooldownSeconds(60)
        .build();

    public AbsoluteEqualitySpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 200;
        this.baseManaCost = 400;
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
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 800, 0.1f, true,
            target -> !entity.getType().is(Tags.EntityTypes.BOSSES)
        );
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
                LivingEntity target = targetData.getTarget((ServerLevel) level);

                if (target != null && !entity.getType().is(Tags.EntityTypes.BOSSES)) {
                    // 先计算施法者/目标的当前血量% 再交换/替换-施法者/目标的血量%
                    player.setHealth(player.getMaxHealth() * target.getHealth() / target.getMaxHealth());
                    target.setHealth(target.getMaxHealth() * player.getHealth() / player.getMaxHealth());
                }
            }
        }
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}