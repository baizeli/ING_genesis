package miku.united_as_one.genesis.contents.entity.boss.damage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BloodBossDamageSource extends DamageSource {
    public BloodBossDamageSource(@NotNull Entity directEntity, @NotNull Entity causingEntity, @Nullable Vec3 damageSourcePosition) {
        super(directEntity.level.damageSources.damageTypes.getHolderOrThrow(DamageTypes.MOB_ATTACK), directEntity, causingEntity, damageSourcePosition);
    }

    public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity livingEntity) {
        Component component = this.causingEntity == null ? Objects.requireNonNull(this.directEntity).getDisplayName() : this.causingEntity.getDisplayName();
        return Component.translatable("death.attack.iron_spells_genesis.blood_boss", livingEntity.getDisplayName(), component);
    }
}