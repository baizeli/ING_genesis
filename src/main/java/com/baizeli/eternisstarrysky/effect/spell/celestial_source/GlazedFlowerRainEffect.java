package com.baizeli.eternisstarrysky.effect.spell.celestial_source;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GlazedFlowerRainEffect extends MobEffect {
    public GlazedFlowerRainEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }
    
    @Override
    public String getDescriptionId() {
        return "effect." + EternisStarrySky.MOD_ID + ".glazed_flower_rain";
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && entity instanceof Player player) {
            if (entity.tickCount % 20 == 0) {
                spawnGlazedFlowerRain(player, amplifier);
            }
        }

        super.applyEffectTick(entity, amplifier);
    }
    
    private void spawnGlazedFlowerRain(Player player, int amplifier) {
        Level level = player.level();
        BlockPos playerPos = player.blockPosition();

        // aabb计算半径15格内...
        int radius = 15;
        AABB area = new AABB(
            playerPos.getX() - radius, playerPos.getY() - 5, playerPos.getZ() - radius,
            playerPos.getX() + radius, playerPos.getY() + 10, playerPos.getZ() + radius
        );

        List<Entity> entities = level.getEntities(null, area);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity && entity.isAlive()) {
                double distance = entity.distanceToSqr(player);

                if (distance <= radius * radius) {
                    // 友方[1s回复8点生命值]
                    if (isFriendly(livingEntity)) {
                        livingEntity.heal(8.0F);
                    }
                    // 亡灵生物[1s12点伤害]
                    else if (livingEntity.getMobType() == MobType.UNDEAD) {
                        livingEntity.hurt(level.damageSources().magic(), 12.0F);
                    } 
                    // 非亡灵生物
                    else if (isEnemy(livingEntity)) {
                        // 缓慢
                        livingEntity.addEffect(
                            new MobEffectInstance(
                                MobEffects.MOVEMENT_SLOWDOWN, 
                                80, 
                                3
                            )
                        );

                        // 虚弱
                        livingEntity.addEffect(
                            new MobEffectInstance(
                                MobEffects.WEAKNESS, 
                                80, 
                                3
                            )
                        );

                        // 挖掘疲劳
                        livingEntity.addEffect(
                            new MobEffectInstance(
                                MobEffects.DIG_SLOWDOWN, 
                                80, 
                                3
                            )
                        );
                    }
                }
            }
        }
        
        // 施法者自身[1s回复8点生命值]
        player.heal(8.0F);
    }
    
    private boolean isFriendly(LivingEntity target) {
        // 所有玩家/友方
        return target instanceof Player || target.getType().getCategory().isFriendly();
    }
    
    private boolean isEnemy(LivingEntity target) {
        // 已处理的亡灵/怪物
        return target.getMobType() != MobType.UNDEAD && target.getType().getCategory() == MobCategory.MONSTER;
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}