package com.baizeli.eternisstarrysky.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Invoker
    void callDropCustomDeathLoot(DamageSource damageSource, int looting, boolean hitByPlayer);

    @Invoker
    void callDropFromLootTable(DamageSource damageSource, boolean hitByPlayer);

    @Invoker
    SoundEvent callGetDeathSound();

    @Invoker
    float callGetSoundVolume();
}
