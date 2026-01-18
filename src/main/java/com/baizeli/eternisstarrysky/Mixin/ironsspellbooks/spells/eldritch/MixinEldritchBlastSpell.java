package com.baizeli.eternisstarrysky.Mixin.ironsspellbooks.spells.eldritch;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.eldritch_blast.EldritchBlastVisualEntity;
import io.redspace.ironsspellbooks.spells.eldritch.EldritchBlastSpell;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.UUID;

@Mixin(value = EldritchBlastSpell.class, remap = false)
public abstract class MixinEldritchBlastSpell extends AbstractSpell {
    @Shadow public abstract int getRecastCount(int spellLevel, @Nullable LivingEntity entity);

    @Shadow protected abstract float getDamage(int spellLevel, LivingEntity caster);

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 64, .15f);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetEntityCastData) {
            var recasts = playerMagicData.getPlayerRecasts();
            if (!recasts.hasRecastForSpell(this.getSpellId())) {
                recasts.addRecast(
                        new RecastInstance(
                                getSpellId(),
                                spellLevel,
                                getRecastCount(spellLevel, entity),
                                80,
                                castSource,
                                new MultiTargetEntityCastData(
                                        targetEntityCastData.getTarget((ServerLevel) level)
                                )
                        ), playerMagicData
                );
            } else {
                var instance = recasts.getRecastInstance(getSpellId());
                if (instance != null && instance.getCastData() instanceof MultiTargetEntityCastData targetingData) {
                    targetingData.addTarget(targetEntityCastData.getTargetUUID());
                }
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        if (castDataSerializable instanceof MultiTargetEntityCastData targetingData) {
            List<UUID> targets = targetingData.getTargets();
            int targetSize = targets.size();
            int spellLevel = recastInstance.getSpellLevel();
            int count = getRecastCount(spellLevel, serverPlayer);
            float avg = (float) count / targetSize;
            for (int i = 0; i < targetSize; i++) {
                ServerLevel level = (ServerLevel) serverPlayer.level;
                var target = (LivingEntity) level.getEntity(targets.get(i));
                if (target != null) {
                    for (int j = Mth.ceil(avg * i); j < avg * i; j++) {
                        level.addFreshEntity(new EldritchBlastVisualEntity(level, serverPlayer.getEyePosition().subtract(0.0F, 0.75F, 0.0F), target.position(), serverPlayer));
                        DamageSources.applyDamage(target, getDamage(spellLevel, serverPlayer), getDamageSource(serverPlayer));
                        MagicManager.spawnParticles(level, ParticleHelper.UNSTABLE_ENDER, target.getX(), target.getY(), target.getZ(), 50, 0.0F, 0.0F, 0.0F, 0.3, false);
                    }
                }
            }
        }
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new MultiTargetEntityCastData();
    }
}
