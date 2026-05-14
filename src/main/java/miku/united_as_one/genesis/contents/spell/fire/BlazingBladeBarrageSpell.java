package miku.united_as_one.genesis.contents.spell.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@AutoSpellConfig
public class BlazingBladeBarrageSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "blazing_blade_barrage");
    private final DefaultConfig defaultConfig;

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1))
        );
    }

    public BlazingBladeBarrageSpell() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.UNCOMMON)
                .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
                .setMaxLevel(3)
                .setCooldownSeconds(50.0F)
                .build();
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 3;
        this.castTime = 0;
        this.baseManaCost = 100;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 50, 0.1f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData entityCastData) {
            LivingEntity target = entityCastData.getTarget((ServerLevel) level);
            if (target != null) {
                entity.playSound(SoundRegistry.FIRE_CAST.get(), 2.0F, (float) Utils.random.nextIntBetweenInclusive(80, 110) * 0.01F);
                Vec3 pos = entity.position();
                int count = 7;
                int delay = Utils.random.nextIntBetweenInclusive(30, 70);
                float yAngle = -Utils.getAngle(target.getX(), target.getZ(), entity.getX(), entity.getZ()) + ((float) Math.PI / 2F);

                for(int i = 0; i < count; ++i) {
                    Vec3 offset = new Vec3((double) 1.5F * (double) entity.getScale(), 0.0, 0.0).zRot(Mth.lerp((float) i / ((float) count - 1.0F), 0.0F, -(float) Math.PI)).yRot(yAngle).add(0.0, entity.getEyeHeight(), 0.0);
                    FieryDaggerEntity dagger = new FieryDaggerEntity(target.level);
                    dagger.setOwner(entity);
                    dagger.ownerTrack = offset;
                    dagger.setTarget(target);
                    dagger.setPos(pos.add(offset.yRot(entity.getYRot())));
                    dagger.delay = delay + i * 2;
                    dagger.setDamage(getSpellPower(spellLevel, entity));
                    entity.level.addFreshEntity(dagger);
                }
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
