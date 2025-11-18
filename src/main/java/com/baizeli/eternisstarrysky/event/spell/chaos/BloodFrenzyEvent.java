package com.baizeli.eternisstarrysky.event.spell.chaos;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.effect.spell.ModEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MODID)
public class BloodFrenzyEvent {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(ModEffect.BLOOD_FRENZY.get())) {
            if (entity instanceof Player player) {
                // 获取施法者攻击范围.
                double reach = player.getEntityReach();
                AABB searchBox = player.getBoundingBox().inflate(reach);
                List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                    LivingEntity.class,
                    searchBox,
                    e -> e != player && e.isAlive() && player.canReach(e, 0)
                );

                // 如果附近有实体/找到最近的实体.
                if (!nearbyEntities.isEmpty()) {
                    LivingEntity nearestEntity = null;
                    double nearestDistance = Double.MAX_VALUE;

                    for (LivingEntity nearbyEntity : nearbyEntities) {
                        double distance = player.distanceToSqr(nearbyEntity);
                        if (distance < nearestDistance) {
                            nearestDistance = distance;
                            nearestEntity = nearbyEntity;
                        }
                    }

                    // 将施法者推向最近的实体.
                    if (nearestEntity != null) {
                        double dx = nearestEntity.getX() - player.getX();
                        double dz = nearestEntity.getZ() - player.getZ();
                        double distance = Math.sqrt(dx * dx + dz * dz);

                        if (distance > 0) {
                            double force = 0.1D;
                            player.push(dx / distance * force, 0, dz / distance * force);
                        }
                    }

                    // 自动攻击...
                    if (player.level().isClientSide) {
                        Minecraft mc = Minecraft.getInstance();

                        if (player.getAttackStrengthScale(0.0F) >= 1.0F) {
                            for (LivingEntity target : nearbyEntities) {
                                if (player.canReach(target, 0)) {
                                    if (mc.gameMode != null) {
                                        mc.gameMode.attack(player, target);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}