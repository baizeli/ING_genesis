package miku.united_as_one.genesis.contents.spell.ice;

import miku.united_as_one.genesis.Genesis;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.*;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.ice_spike.IceSpikeEntity;
import io.redspace.ironsspellbooks.entity.spells.ray_of_frost.RayOfFrostVisualEntity;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.*;

@AutoSpellConfig
public class FrostThrustArraySpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "frost_thrust_array");
    private final DefaultConfig defaultConfig = new DefaultConfig()
        .setMinRarity(SpellRarity.LEGENDARY)
        .setSchoolResource(SchoolRegistry.ICE_RESOURCE)
        .setMaxLevel(3)
        .setCooldownSeconds(60)
        .build();

    public FrostThrustArraySpell() {
        this.manaCostPerLevel = 25;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 2;
        this.castTime = 20*3;
        this.baseManaCost = 125;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
            Component.translatable(
                "ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1)
            ),
            Component.translatable(
                "ui.irons_spellbooks.radius", Utils.stringTruncation(getRadius(), 1)
            )
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
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.OVERHEAD_MELEE_SWING_ANIMATION;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    private int getRadius() {
        return 7;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!level.isClientSide()) {
            entity.playSound(SoundRegistry.ICE_CAST.get(), 1, 1);

            int radius = getRadius();
            AABB searchArea = new AABB(
                entity.getX() - radius, entity.getY() - 5, entity.getZ() - radius,
                entity.getX() + radius, entity.getY() + 10, entity.getZ() + radius
            );

            TargetedAreaEntity targetArea = TargetedAreaEntity.createTargetAreaEntity(level, entity.position(), radius, 0x0000ff);
            targetArea.setDuration(20*3);
            /*targetArea.setOwner(entity);*/

            for (Entity targetEntity : level.getEntities(entity, searchArea)) {
                if (targetEntity instanceof LivingEntity livingTarget &&
                   targetEntity.isAlive() && !targetEntity.isSpectator()) {
                    if (targetEntity.distanceToSqr(entity.position()) <= radius * radius) {
                        IceSpikeEntity iceSpike = new IceSpikeEntity(level, entity);
                        
                        iceSpike.setPos(targetEntity.getX() + 0.5, targetEntity.getY(), targetEntity.getZ() + 0.5);

                        iceSpike.setSpikeSize(3);
                        iceSpike.setWaitTime(0);

                        iceSpike.setDamage(getDamage(spellLevel, entity));
                        level.addFreshEntity(iceSpike);

                        Vec3 startPos = targetEntity.position().add(0, 2.5, 0);
                        Vec3 endPos = startPos.add(0, -10, 0).add(targetEntity.getLookAngle().scale(3));

                        RayOfFrostVisualEntity ray = new RayOfFrostVisualEntity(level, startPos, endPos, livingTarget);
                        level.addFreshEntity(ray);

                        DamageSources.applyDamage(livingTarget, getSpellPower(spellLevel, entity) * 0.8f,
                            this.getDamageSource(entity).setFreezeTicks(livingTarget.getTicksRequiredToFreeze()));
                    }
                }
            }
        }
        
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}